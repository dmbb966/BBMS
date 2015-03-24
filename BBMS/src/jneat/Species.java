package jneat;

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
	 * (to protect them from premature pruning).  Fitness is divided by the size of the species so 
	 * that it is shared by the species. */
	public void AdjustFitness() {
		int age_debt = (age - age_lastimprovement + 1) - JNEATGlobal.p_dropoff_age;
		
		for (int i = 0; i < organisms.size(); i++) {
			Organism _organism = organisms.elementAt(i);
			
			// Remember the original fitness prior to modification
			
		}
	}
	
	
}
