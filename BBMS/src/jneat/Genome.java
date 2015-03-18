package jneat;

import java.util.Vector;

public class Genome {
	
	/** Reference from this genotype to its phenotype, that is, from its genetic roots to its observable characteristics  */
	Network phenotype;
	
	int genome_id;
	
	/** 
	 * Each Gene has a market telling when it arose historically.
	 * Therefore, these Genes can be used to speciate the population and
	 * provides an evolutionary history of innovation and link-building.
	 */
	Vector<Gene> genes;
	Vector<Trait> traits;
	Vector<NNode> nodes;
	
	

}
