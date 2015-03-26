package jneat;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import bbms.GlobalFuncs;

/** A Population is a group of Organisms including their species */
public class Population {
	
	public Vector<Organism> organisms = new Vector<Organism>();
	public Vector<Species> species = new Vector<Species>();
	public Vector<Innovation> innovations = new Vector<Innovation>();
	
	/** Current label number available for nodes*/
	private int cur_node_id;
	/** Current innovation number available for genes */
	private int cur_innov_num;
	
	/** Highest species number */
	int last_species;
	
	/** Last generation played */
	int final_gen;
	
	// Fitness statistics
	/** Average fitness of the current epoch */
	double mean_fitness;
	double variance;
	double standard_deviation;
	
	/** An integer that, when above zero, tells the epoch when the first winner appeared */
	int winnergen;
	
	/** Maximum fitness, used for delta code and stagnation detection */
	double highest_fitness;
	
	/** If too high, leads to delta coding process*/
	int highest_last_changed;
	
	public Population(Genome g, int size) {
		winnergen = 0;
		highest_fitness = 0.0;
		highest_last_changed = 0;
		spawn(g, size);
	}
	
	/** Creates a population of random topologies.
	 * 
	 * @param popSize - number of organisms
	 * @param numInputs
	 * @param numOutputs
	 * @param maxNodes - maximum number of hidden nodes in the initially generated topology
	 * @param recur
	 * @param linkprob
	 */
	public Population (int popSize, int numInputs, int numOutputs, int maxNodes, boolean recur, double linkprob) {
		for (int i = 0; i < popSize; i++) {
			// System.out.println("Creating organism #" + i);
			
			Genome newGenome = new Genome(i, numInputs, numOutputs, GlobalFuncs.randRange(0,  maxNodes), maxNodes, recur, linkprob);
			organisms.add(new Organism(0, newGenome, 1));			
		}
		
		cur_node_id = numInputs + numOutputs + maxNodes + 1;
		cur_innov_num = (numInputs + numOutputs + maxNodes) * (numInputs + numOutputs + maxNodes) + 1;
		
		// System.out.println("\n\n" + this.PrintPopulation());
		
		speciate();
	}
	
	public void spawn(Genome g, int size) {
		organisms = new Vector<Organism>();
		Genome newGenome = null;
		
		for (int i = 1; i <= size; i++) {
			newGenome = g.duplicate(i);
			newGenome.MutateLinkWeight(1.0, 1.0, MutationTypeEnum.GAUSSIAN);
			organisms.add(new Organism(0.0, newGenome, 1));
		}
		
		// Keep track of the innovation and node number we're on
		cur_node_id = newGenome.get_next_nodeID();
		cur_innov_num = newGenome.get_next_gene_innovnum();
		
		// Speciate the new population into a species
		speciate();
	}
	
	/** Epoch turns over a population to the next generation based on fitness */
	public void epoch() {
		// Use species' age to modify the objective fitness of organisms
		// That is, give younger species an advantage so they can take hold
		// Also penalizes stagnant species
		// Adjust the fitness using species size to share fitness within a species
		// Finally, if any are below the survival threshold, mark for death
		Species bestSpecies = null;
		int generation = final_gen + 1;
		
		Iterator<Species> itr_species = species.iterator();
		while (itr_species.hasNext()) {
			itr_species.next().AdjustFitness();
		}
		
		// Compute average fitness over all organisms
		Iterator<Organism> itr_organism = organisms.iterator();
		double totalFitness = 0.0;
		while (itr_organism.hasNext()) {
			totalFitness += itr_organism.next().fitness;
		}
		
		double overall_average = totalFitness / organisms.size();
		
		System.out.println(">>> Average fitness for this population: " + overall_average);
		
		// Compute the number of expected offspring for each individual organism
		itr_organism = organisms.iterator();
		while (itr_organism.hasNext()) {
			Organism _organism = itr_organism.next();
			_organism.expected_offspring = _organism.fitness / overall_average;  
			
			System.out.println(">>> Organism " + _organism.genome.genome_id + " is allotted " + _organism.expected_offspring + " babies.");
		}
		
		// Add offspring up within each species to get the number of offspring per species
		itr_species = species.iterator();
		double skim = 0.0;					// Used to track partial expected offspring
		int total_expected = 0;				// Total number of offspring expected
		while (itr_species.hasNext()) {
			Species _species = itr_species.next();
			skim = _species.CountOffspring(skim);
			total_expected += _species.expected_offspring;
		}
		
		int final_expected = 0;
		// Due to lost floating point precision in offspring assignment,
		// we will give an extra baby to the best species.  Babies everywhere!
		if (total_expected < organisms.size()) {
			// Find the species with the most number of expected offspring
			
			itr_species = species.iterator();
			int max_expected = 0;			
			
			
			while (itr_species.hasNext()) {
				Species _species = itr_species.next();
				if (_species.expected_offspring >= max_expected) {
					max_expected = _species.expected_offspring;
					bestSpecies = _species;
				}
				final_expected += _species.expected_offspring;
			}
			
			// Give the extra offspring to the best species
			bestSpecies.expected_offspring++;
			final_expected++;		
		
			// If we still aren't at the total, then there's a problem.
			// This happens if a stagnant species dominates the population and then gets killed off by age
			// The whole population plummets in fitness.  Thus, we repopulate from the best existing one.
			if (final_expected < organisms.size()) {
				System.out.println("WARNING: Population has died.  Repopulating with the best offspring \n\n");
				
				itr_species = species.iterator();
				
				while (itr_species.hasNext()) {
					itr_species.next().expected_offspring = 0;
				}
				bestSpecies.expected_offspring = organisms.size();
			}
		}
		
		// Copy the Species pointers into a new Species list for sorting
		Vector<Species> sorted_species = new Vector<Species>();
		itr_species = species.iterator();
		while (itr_species.hasNext()) {
			sorted_species.add(itr_species.next());						
		}
		
		// Sort the population and mark for death
		Comparator<Species> cmp = new SpeciesComparator();
		Collections.sort(sorted_species, cmp);
		
		StringBuffer report1 = new StringBuffer("");
		itr_species = sorted_species.iterator();
		while (itr_species.hasNext()) {
			Species _species = itr_species.next();
			report1.append("\n  Original fitness of Species #" + _species.id);
			report1.append(" (Size " + _species.organisms.size() + "): ");
			report1.append(" has fitness " + _species.organisms.firstElement().orig_fitness);
			report1.append(" last improved " + (_species.age - _species.age_lastimprovement));
			report1.append(" with # offspring: " + _species.expected_offspring + "\n");
		}
		System.out.println(report1.toString());
		report1 = new StringBuffer("");
		
		Species curSpecies = sorted_species.firstElement();
		int best_species_num = curSpecies.id;
		// Check for population-level stagnation
		curSpecies.organisms.firstElement().pop_champ = true;		
		
		if (curSpecies.organisms.firstElement().orig_fitness > highest_fitness) {
			highest_fitness = curSpecies.organisms.firstElement().orig_fitness;
			highest_last_changed = 0;
			System.out.println("\n Population has reached a new record fitness of: " + highest_fitness);
		} else {
			++highest_last_changed;			
		}
		
		
		// Check for stagnation - if so, perform delta-coding
		if (highest_last_changed >= JNEATGlobal.p_dropoff_age + 5) {
			highest_last_changed = 0;
			int half_pop =JNEATGlobal.p_pop_size / 2;
			
			itr_species = sorted_species.iterator();
			Species _species = itr_species.next();
			// The first organism of the first species can have offspring = 1/2 population size
			_species.organisms.firstElement().super_champ_offspring = half_pop;
			_species.expected_offspring = half_pop;
			_species.age_lastimprovement = _species.age;
			
			if (itr_species.hasNext()) {
				_species = itr_species.next();
				_species.organisms.firstElement().super_champ_offspring = half_pop;
				// The second species will have the remaining half of the population
				_species.expected_offspring = half_pop;
				_species.age_lastimprovement= _species.age;
				
				// The remaining species will not have any offspring
				while (itr_species.hasNext()) {
					itr_species.next().expected_offspring = 0;
				}
			}
			else {
				_species.organisms.firstElement().super_champ_offspring += JNEATGlobal.p_pop_size - half_pop;
				_species.expected_offspring += JNEATGlobal.p_pop_size - half_pop;
			}
		}
		
		// If there is no stagnation, performs baby stealing
		else {
			if (JNEATGlobal.p_babies_stolen > 0) {
				// Take away a constant number of expected offspring from the worst species
				int stolen_babies = 0;
				for (int j = sorted_species.size() - 1; (j >= 0) && (stolen_babies < JNEATGlobal.p_babies_stolen); j--) {
					Species _species = sorted_species.elementAt(j);
					if (_species.age > 5 && _species.expected_offspring > 2) {
						int numtoSteal = JNEATGlobal.p_babies_stolen - stolen_babies;
						
						// If enough babies exist to be stolen, will steal it all from one species
						if (_species.expected_offspring - 1 >= JNEATGlobal.p_babies_stolen) {
							_species.expected_offspring -= numtoSteal;
							stolen_babies = JNEATGlobal.p_babies_stolen;
						}
						// If it can't satisfy its hunger from one species, it will take all its can and move to the next
						else {
							stolen_babies += _species.expected_offspring - 1;
							_species.expected_offspring = 1;
						}
					}
				}
				
				// Mark the best champions of the top species to be the super champions
				// who will take on the extra offspring for cloning or mutant cloning.
				// Determine the exact number that will be given to the top three
				// which is, in order, 1/5, 1/5, and 1/10 of the stolen babies.  
				// Remaining species will get three until stolen babies run out.
				int tb_four[] = new int[3];
				tb_four[0] = JNEATGlobal.p_babies_stolen / 5;
				tb_four[1] = tb_four[0];
				tb_four[2] = JNEATGlobal.p_babies_stolen / 10;
								
				itr_species = sorted_species.iterator();
				int iBlock = 0;
				
				while (stolen_babies != 0 && itr_species.hasNext()) {
					Species _species = itr_species.next();
					if (_species.TimeSinceImprovement() <= JNEATGlobal.p_dropoff_age) {
						if (iBlock < 3) {
							if (stolen_babies >= tb_four[iBlock]) {
								_species.organisms.firstElement().super_champ_offspring = tb_four[iBlock];
								_species.expected_offspring += tb_four[iBlock];
								stolen_babies -= tb_four[iBlock];								
							}
							iBlock++;
						}
						
						else {
							if (GlobalFuncs.randFloat() > 0.1) {
								if (stolen_babies > 3) {
									_species.organisms.firstElement().super_champ_offspring = 3;
									_species.expected_offspring += 3;
									stolen_babies -= 3;
								} else {
									_species.organisms.firstElement().super_champ_offspring = stolen_babies;
									_species.expected_offspring += stolen_babies;
									stolen_babies = 0;
								}
							}												
						}
					}
				}
				
				if (stolen_babies > 0) {					
					Species _species = sorted_species.firstElement();
					_species.organisms.firstElement().super_champ_offspring += stolen_babies;
					_species.expected_offspring += stolen_babies;
					
					System.out.println("Not all stolen babies were distributed.  Giving to the best species (#" + _species.id + ")");
					stolen_babies = 0;					
				}
				
			}
		}
		
		System.out.println(">>><<<\n" + this.PrintPopulation());
		
		// Eliminating organisms flagged for elimination
		itr_organism = organisms.iterator();
		Vector<Organism> vDel = new Vector<Organism>();
		
		while (itr_organism.hasNext()) {
			Organism _organism = itr_organism.next();
			if (_organism.eliminate) {
				// Remove organism from species
				_organism.species.RemoveOrganism(_organism);
				vDel.add(_organism);
			}
		}
		
		// Eliminate organisms from the master list
		for (int i = 0; i < vDel.size(); i++) {
			organisms.removeElement(vDel.elementAt(i));
		}
		vDel.clear();
		
		
		// Start the reproduction of species
		itr_species = sorted_species.iterator();
		while (itr_species.hasNext()) {
			Species _species = itr_species.next();
			_species.reproduce(generation, this, sorted_species);
		}
		
		itr_organism = organisms.iterator();
		while (itr_organism.hasNext()) {
			Organism _organism = itr_organism.next();
			
			// Remove the organism from its species
			Species _species = _organism.species;
			_species.RemoveOrganism(_organism);
		}
		organisms.clear();
		
		
		// Remove all empty species and age those that survive
		// While doing this, create the master organism list for the new generation
		itr_species = species.iterator();
		Vector<Species> sDel = new Vector<Species>();
		int orgcount = 0;
		
		while (itr_species.hasNext()) {
			Species _species = itr_species.next();
			int sSize = _species.organisms.size();
			if (sSize == 0) sDel.add(_species);
			else {
				// Age any species not newly created
				if (_species.newSpecies) _species.newSpecies = false;
				else _species.age++;
				
				// Reconstruct the master list of organisms from the current species
				for (int j = 0; j < sSize; j++) {
					Organism _organism = _species.organisms.elementAt(j);
					_organism.genome.genome_id = orgcount++;
					_organism.genome.phenotype.net_id = _organism.genome.genome_id;
					
					organisms.add(_organism);
				}
			}			
		}
		
		// Eliminate species marked for deletion
		for (int i = 0; i < sDel.size(); i++) {
			System.out.println("--- Removing species " + sDel.elementAt(i).id);
			
			species.removeElement(sDel.elementAt(i));
		}
		
		// Remove innovations of the current generation
		innovations.clear();
		
		// Check to see if the best species died.  That would be bad.
		boolean best_OK = false;
		itr_species = species.iterator();
		while (itr_species.hasNext()) {
			Species _species = itr_species.next();
			if (_species.id == best_species_num) {
				best_OK = true;
				break;
			}
		}
		
		if (!best_OK) System.out.println("ERROR!  The best species died!");					
	}
	
	public void speciate() {
		int counter = 0;	// For number of species
		Species newSpecies = null;
		
		Iterator<Organism> itr_organism = organisms.iterator();
		while (itr_organism.hasNext()) {
			Organism _organism = itr_organism.next();
			// If the list species is empty, create the first
			if (species.isEmpty()) {
				newSpecies = new Species(++counter);				
				newSpecies.organisms.add(_organism);
				
				species.add(newSpecies);
				_organism.species = newSpecies;		
			}
			else {
				Iterator<Species> itr_species = species.iterator();
				boolean done = false;
				
				while (!done && itr_species.hasNext()) {
					Species _species = itr_species.next();
					Organism compare_org = _species.organisms.firstElement();
					double curr_compat = _organism.genome.Compatibility(compare_org.genome);
					
					if (curr_compat < JNEATGlobal.p_compat_threshold) {
						// Found compatible species - add this organism
						_species.organisms.add(_organism);
						_organism.species = _species;
						done = true;
					}
				}
				
				// If no compatible species found, create a new one
				if (!done) {
					newSpecies = new Species(++counter);
					species.add(newSpecies);
					newSpecies.organisms.add(_organism);
					_organism.species = newSpecies;
				}
			}
		}
		
		last_species = counter; // Keep track of the highest species
	}
	
	/** Adds a new species to this population along with the first organism in this new species*/
	public void AddSpecies(Species newSpecies, Organism baby) {
		last_species++;
		species.add(newSpecies);
		newSpecies.organisms.add(baby);
		baby.species = newSpecies;
	}
	
	public void Verify() {
		Iterator<Organism> itr_organism = organisms.iterator();
		while (itr_organism.hasNext()) {
			itr_organism.next().genome.verify();
		}
	}
	
	/** Returns the current node ID and increments it.*/
	public int getCurNodeID_Inc() {
		return cur_node_id++;
	}
	
	
	/** Returns the current innovation number and increments it.*/
	public int getCurInnov_Inc() {
		return cur_innov_num++;
	}
	
	public String PrintPopulation() {
		String ret = "";
		
		ret += "\n\n\n\t\t >>>POPULATION<<<";
		ret += "\n\n\t This population has " + organisms.size() + " organisms, ";
		ret += species.size() + " species :\n";
		
		Iterator<Organism> itr_organism = organisms.iterator();
		while (itr_organism.hasNext()) {
			ret += itr_organism.next().PrintOrganism() + "\n";
		}
		
		Iterator<Species> itr_species = species.iterator();
		while (itr_species.hasNext()) {
			ret += itr_species.next().PrintSpecies() + "\n";
		}
		
		return ret;
	}
}
