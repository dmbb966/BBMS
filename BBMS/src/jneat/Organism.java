package jneat;

/** Organisms are Genomes and Networks with fitness information (i.e. genotype and phenotype together)
 */
public class Organism {
	int generation;
	double fitness;
	
	/** A DEBUG variable - high fitness of champ */
	double high_fit;			
	
	/** Fitness measure that doesn't change during adjustments */
	double orig_fitness;
	
	/** Used for reporting purposes */
	double error;
	
	boolean winner;
	boolean champion;
	boolean pop_champ;			// Best in the population
	
	/** Indicates if this is a duplicate child of a champion.  Used for tracking purposes. */
	boolean pop_champ_child;	
	
	/** Indicates if there's been a change in the structure of the baby */
	boolean mut_struct_baby;
	
	/** "Has a mating in baby?" */
	boolean mate_baby;
	
	/** Marker for the destruction of inferior Organisms */
	boolean eliminate;	
	
	Network net;		// Phenotype
	Genome genome;		// Genotype
	Species species;
	
	/** Number of expected children.  Fractional because this is alloted based off its proportional fitness in the population. */
	double expected_offspring;
	
	/** Number of reserved offspring for a population leader */
	int super_champ_offspring;
	
	
	public Organism (double xFitness, Genome xGenome, int xGeneration) {
		fitness = xFitness;
		orig_fitness = xFitness;
		genome = xGenome;
		net = genome.Genesis(xGenome.genome_id);
		species = null;
		expected_offspring = 0;
		generation = xGeneration;
		eliminate = false;
		error = 0.0;
		winner = false;
		champion = false;
		super_champ_offspring = 0;
		pop_champ = false;
		pop_champ_child = false;
		high_fit = 0.0;
		mut_struct_baby = false;
		mate_baby = false;
	}
	
	public String PrintOrganism() {
		String ret = "";
		
		ret = ret + "ORGANISM -[Genome ID: " + genome.genome_id + "] ";
		if (champion) ret = ret + "(CHAMPION) ";
		ret = ret + " Fitness: " + fitness + " with offspring = " + expected_offspring + " ";
		if (eliminate) ret = ret + ">ELIMINATE< ";
		
		return ret;
	}
}
