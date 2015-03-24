package jneat;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

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
	
	public Species() {
		id = JNEATGlobal.NewSpeciesID();
		age = 1;
		avg_fitness = 0.0;
		expected_offspring = 0;
		newSpecies = false;
		age_lastimprovement = 0;
		max_fitness = 0.0;
		max_fitness_ever = 0.0;
	}
	
	/** Change the fitness of the organisms in the species to higher values for very new species
	 * (to protect them from premature pruning).  */
	public void AdjustFitness() {
		int age_debt = (age - age_lastimprovement + 1) - JNEATGlobal.p_dropoff_age;
		
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
	
	/** Computes the collective offspring of the entire species (sum of all organisms' offspring).
	 * Differs from original JNEAT version in that this does NOT accumulate fractional offspring */
	public void CountOffspring() {
		Iterator<Organism> itr_organism = organisms.iterator();
		int total = 0;
		
		while (itr_organism.hasNext()) {
			Organism _organism = itr_organism.next();		
			
			total += _organism.expected_offspring;					
		}
		
		expected_offspring = total;
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
