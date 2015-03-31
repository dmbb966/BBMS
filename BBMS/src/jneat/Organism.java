package jneat;

import java.io.BufferedReader;

/** Organisms are Genomes and Networks with fitness information (i.e. genotype and phenotype together)
 */
public class Organism {
	int generation;
	
	/** The adjusted fitness value (adjusted by Species.AdjustFitness())*/
	public double fitness;
	
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
	
	public Network net;		// Phenotype
	public Genome genome;		// Genotype
	public Species species;
	
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
		if (species != null) ret = ret + "Species #" + species.id + " ";
		else ret = ret + "NO SPECIES ASSIGNED ";
		if (pop_champ) ret = ret + "(POP CHAMP) ";
		else if (champion) ret = ret + "(CHAMPION) ";
		ret = ret + " Fitness: " + fitness + " with offspring = " + expected_offspring + " ";
		if (eliminate) ret = ret + ">ELIMINATE< ";
		
		if (net != null) ret += net.PrintNetwork(true); 
		
		return ret;
	}
	
	Organism(BufferedReader reader) {
		
		
	}
	
	public String SaveOrgHeader() {
		StringBuffer buf = new StringBuffer("");
		
		buf.append("# Organism data format follows:\n");
		buf.append("# 'Organism', ID, champion, eliminate, error, expected_offspring, fitness, generation, high_fit, mate_baby, mut_struct_baby, orig_fitness, pop_champ, pop_champ_child, super_champ_offspring, winner\n");		
		buf.append("#    + Genome\n");
		
		
		return buf.toString();
	}
	
	public String SaveOrganism() {
		StringBuffer buf = new StringBuffer("");
		
		buf.append("Organism, " + genome.genome_id + ", ");
		buf.append(champion + ", ");
		buf.append(eliminate + ", ");
		buf.append(error + ", ");
		buf.append(expected_offspring + ", ");
		buf.append(fitness + ", ");
		buf.append(generation + ", ");
		buf.append(high_fit + ", ");
		buf.append(mate_baby + ", ");
		buf.append(mut_struct_baby + ", ");
		buf.append(orig_fitness + ", ");
		buf.append(pop_champ + ", ");
		buf.append(pop_champ_child + ", ");
		buf.append(super_champ_offspring + ", ");
		buf.append(winner + "\n");
		
		// Genome
		buf.append(genome.SaveGenome());
		
		return buf.toString();
	}
}
