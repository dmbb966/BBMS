package jneat;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import bbms.GlobalFuncs;

public class Species {
	int id;
	int age;
	
	double avg_fitness;
	double max_fitness;
	double max_fitness_ever;
	
	/** Length of time since hte last update; if too old3, species will go extinct */
	int age_lastimprovement;
	
	/** Number of expected children */
	int expected_offspring;
	
	boolean newSpecies;
	boolean beenChecked;
	
	/** All organisms in the species */
	Vector<Organism> organisms;
	
	public Species(boolean novel) {
		id = JNEATGlobal.NewSpeciesID();
		age = 1;
		avg_fitness = 0.0;
		expected_offspring = 0;
		newSpecies = novel;
		age_lastimprovement = 0;
		max_fitness = 0.0;
		max_fitness_ever = 0.0;
	}

	public Species() {
		this(false);
	}
	
	public int TimeSinceImprovement() {
		return (age - age_lastimprovement);
	}
	
	/** Change the fitness of the organisms in the species to higher values for very new species
	 * (to protect them from premature pruning).  */
	public void AdjustFitness() {
		int age_debt = (TimeSinceImprovement() + 1) - JNEATGlobal.p_dropoff_age;
		
		for (int i = 0; i < organisms.size(); i++) {
			Organism _organism = organisms.elementAt(i);
		
			
			// Multiply fitness by size of species - will be normalized later
			
			// Fitness decreases after a stagnation point (dropoff_age)
			if (age_debt >= 1) _organism.fitness *= JNEATGlobal.p_dropoff_coeff;
			
			// Fitness boost given to young children
			if (age <= JNEATGlobal.p_age_youngOrganism) _organism.fitness *= JNEATGlobal.p_age_significance;
			
			// Fitness cannot be negative
			if (_organism.fitness < 0.0) _organism.fitness = 0.0001;
			
			// Shares fitness within the species <DISABLED since organism fitness doesn't represent the species fitness
			// _organism.fitness /= organisms.size();
			
		}
		
		// Sorts the population and marks for death those after the survival_thresh * pop_size		
		Collections.sort(organisms, new OrganismComparator());
		
		// Update age of last improvement if applicable
		if (organisms.firstElement().orig_fitness > max_fitness_ever) {
			age_lastimprovement = age;
			max_fitness_ever = organisms.firstElement().orig_fitness;
		}
		
		// Determine the number that get to reproduce depending on the survival threshold
		// Checks to ensure at least one parent will survive
		int num_parents = (int) Math.floor(JNEATGlobal.p_survival_threshold * (double)organisms.size());
		if (num_parents <= 0) num_parents = 1;
		
		// Mark for death those ranked too low to be parents, as well as mark the champion
		organisms.firstElement().champion = true;
		
		int count = 1;
		Iterator<Organism> itr_organism = organisms.iterator();
		
		while (itr_organism.hasNext() && count <= num_parents) {
			Organism _organism = itr_organism.next();
			count++;
		}
		
		// At this point, all remaining organisms can be eliminated
		while (itr_organism.hasNext()) {
			itr_organism.next().eliminate = true;
		}
	}
	
	/** Determines the average fitness of the species and updates its attributes accordingly */
	public void ComputeAvgFitness() {
		Iterator<Organism> itr_organism = organisms.iterator();
		double total = 0.0;
		
		while (itr_organism.hasNext()) {
			total += itr_organism.next().fitness;
		}
		
		avg_fitness = total / (double) organisms.size();
	}
	
	/** Updates the maximum fitness attribute for this species */
	public void ComputeMaxFitness() {
		Iterator<Organism> itr_organism = organisms.iterator();
		
		while (itr_organism.hasNext()) {
			Organism _organism = itr_organism.next();
			if (_organism.fitness > max_fitness) max_fitness = _organism.fitness;
		}
	}
	
	/** Computes the collective offspring of the entire species (sum of all organisms' offspring). */
	public double CountOffspring(double skim) {
		Iterator<Organism> itr_organism = organisms.iterator();		
		
		double x1 = 0.0;
		double y1 = 1.0;
		double r1 = 0.0;
		double r2 = skim;
		int n1 = 0;
		int n2 = 0;
		
		while (itr_organism.hasNext()) {			
			Organism _organism = itr_organism.next();		
			x1 = _organism.expected_offspring;
			 
			n1 = (int) (x1 / y1);
			r1 = x1 - ((int) (x1 / y1) * y1);
			n2 = n2 + n1;
			r2 = r2 + r1;
		 
			if (r2 >= 1.0) 
			{
			   n2 = n2 + 1;
			   r2 = r2 - 1.0;
			}
		}
		
		expected_offspring = n2;
		return r2;
	}
	
	public boolean reproduce(int generation, Population pop, Vector<Species> sorted_species) {
		
		if (expected_offspring > 0 && organisms.size() == 0) {
			System.out.println("ERROR!  Attempted to reproduce an empty species!");
			return false;
		}
		
		Organism thechamp = organisms.firstElement();
		Organism mother = null;
		Organism baby = null;
		Genome newGenome = null;
		Network net_analogue = null;
		
		boolean champ_done = false;
		boolean mut_struct_baby = false;	// Indicates if we change the structure of the baby
		
		// Create the designated number of offspring for the species one by one
		for (int i = 0; i < expected_offspring; i++) {
			
			if (expected_offspring > JNEATGlobal.p_pop_size) System.out.println("ALERT: Expected offspring exceeds parameters");
			
			// If there is a super champion (population champion) finish off some special clones
			if (thechamp.super_champ_offspring > 0) {
				mother = thechamp;
				baby = new Organism(0.0, newGenome, generation);
				newGenome = mother.genome.duplicate(i);
				
				if (thechamp.super_champ_offspring > 1) {
					if (GlobalFuncs.randFloat() < 0.8 || JNEATGlobal.p_mutate_add_link_prob == 0.0) {
						newGenome.MutateLinkWeight(JNEATGlobal.p_mutate_weight_power, 1.0, MutationTypeEnum.GAUSSIAN);						
					} else {
						// Occasionally add a link to a superchamp
						net_analogue = newGenome.Genesis(generation);
						newGenome.MutateAddLink(pop, JNEATGlobal.p_newlink_tries);
						mut_struct_baby = true;
					}
				} 
				else if (thechamp.super_champ_offspring == 1) {
					if (thechamp.pop_champ){
						baby.pop_champ_child = true;
						baby.high_fit = mother.orig_fitness;
					}
				}
				
				thechamp.super_champ_offspring--;				
			}
			
			// If we have a Species champion, just clone it
			else if (!champ_done && expected_offspring > 5) {
				mother = thechamp;
				newGenome = mother.genome.duplicate(i);		
				baby = new Organism(0.0, newGenome, generation);	// The baby takes after its mother
				champ_done = true;
			}
			
			else if (GlobalFuncs.randFloat() < JNEATGlobal.p_mutate_only_prob || (organisms.size() - 1) == 1) {
				// Choose the random parent
				mother = organisms.elementAt(GlobalFuncs.randRange(0,  organisms.size() - 1));
				newGenome = mother.genome.duplicate(i);
				
				// Mutate according to probabilities
				if (GlobalFuncs.randFloat() < JNEATGlobal.p_mutate_add_node_prob) {
					newGenome.
				}
				
			}
			
		}
	
		
		return false;
	}
	
	public void RemoveOrganism(Organism org) {
		boolean rOrg= organisms.removeElement(org);
		if (!rOrg) System.out.println("ALERT: Attempted to remove a nonexistant Organism from a Species");
	}
	
	public String PrintSpecies() {
		String ret = "";
		
		ret += "SPECIES # " + id + " with age " + age + "| avg_fit=" + avg_fitness + ", max_fit = " + max_fitness;
		ret += ", max_fit_ever = " + max_fitness_ever + ", eOffspring = " + expected_offspring;
		ret += ", last improved " + age_lastimprovement + "\n";
		ret += "  This species has " + organisms.size() + " organisms."; 
		
		return ret;
	}
	
}
