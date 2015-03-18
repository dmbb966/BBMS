package jneat;

import java.util.Iterator;
import java.util.Vector;

import bbms.GlobalFuncs;

public class Genome {
	
	/** Reference from this genotype to its phenotype, that is, from its genetic roots to its observable characteristics  */
	Network phenotype;
	
	public int genome_id;
	
	/** 
	 * Each Gene has a market telling when it arose historically.
	 * Therefore, these Genes can be used to speciate the population and
	 * provides an evolutionary history of innovation and link-building.
	 */
	public Vector<Gene> genes;
	public Vector<Trait> traits;
	public Vector<NNode> nodes;
	
	/** Duplicates and returns the existing Genome with the next ID in sequence */
	public Genome duplicate() {
		
		// Duplicate traits		
		Iterator<Trait> itr_trait = traits.iterator();
		Vector<Trait> traits_dup = new Vector<Trait>();
		
		while (itr_trait.hasNext()) {
			Trait _trait = (Trait)itr_trait.next();
			Trait newTrait = new Trait(_trait);
			traits_dup.add(newTrait);
		}
		
		// Duplicate Nodes
		Iterator<NNode> itr_node = nodes.iterator();
		Vector<NNode> nodes_dup = new Vector<NNode>();
		
		while (itr_node.hasNext()) {
			NNode _node = (NNode)itr_node.next();
			Trait assoc_trait = null;
			
			if (_node.nodeTrait != null) {
				itr_trait = traits_dup.iterator();
				
				while (itr_trait.hasNext()) {
					Trait _trait = (Trait)itr_trait.next();
					if (_trait.id == _node.nodeTrait.id) {
						assoc_trait = _trait;
						break;
					}
				}
			}
			
			NNode newNode = new NNode(_node, assoc_trait);
			_node.dup = newNode;
			nodes_dup.add(newNode);
		}
		
		// Duplicate Genes
		Iterator<Gene> itr_gene = genes.iterator();
		Vector<Gene> genes_dup = new Vector<Gene>();
		
		while (itr_gene.hasNext()) {
			Gene _gene = (Gene)itr_gene.next();
			
			// Point to the new nodes created in the previous step
			NNode iNode = _gene.lnk.in_node.dup;
			NNode oNode = _gene.lnk.out_node.dup;
			Trait traitPtr = _gene.lnk.linkTrait;
			
			Trait assoc_trait = null;
			
			if (traitPtr != null) {
				itr_trait = traits_dup.iterator();
				while (itr_trait.hasNext()) {
					Trait _trait = (Trait) itr_trait.next();
					if (_trait.id == traitPtr.id) {
						assoc_trait = _trait;
						break;
					}
				}
			}
			
			// Creates a new gene with pointer to the new node
			Gene newGene = new Gene(_gene, assoc_trait, iNode, oNode);
			genes_dup.add(newGene);
		}
		
		// Now generates the new genome
		return new Genome(genes_dup, traits_dup, nodes_dup);						
	}
	
	
	public void Mutate_LinkWeight(double power, double rate, MutationTypeEnum mutation_type) {
		int num = 0;					// Counts gene placement
		int gene_total = genes.size();
		double powerMod = 1.0;			// (Supposedly) Modified power by gene number
		
		// The power of mutation will increase the farther you go into the genome.
		// This is based on the theory that older genes are more fit since they have remained the longest
		
		double randNum;
		double randChoice;						// Determines which kind of mutation to do on a gene
		double endPart = gene_total * 0.8;		// The last part of the genome
		double gaussPoint;
		double coldGaussPoint;
		
		boolean severe;		// Occasionally makes a significant mutation with 50% probability
		if (GlobalFuncs.randFloat() > 0.5) severe = true;
		else severe = false;
		
		Iterator<Gene> itr_gene = genes.iterator();
		
		while (itr_gene.hasNext()) {
			
			// Adjust mutation severity
			Gene _gene = (Gene)itr_gene.next();
			if (severe) {
				gaussPoint = 0.3;
				coldGaussPoint = 0.1;
			} else {
				if (gene_total >= 10 && num > endPart) {
					gaussPoint = 0.5;
					coldGaussPoint = 0.3;
				} else {
					gaussPoint = 1.0 - rate;
					if (GlobalFuncs.randFloat() > 0.5) coldGaussPoint = 1.0 - rate - 0.1;
					else coldGaussPoint = 1.0 - rate;					
				}
			}
			
			// Returns a random number from [-1, +1]
			randNum = GlobalFuncs.randPosNeg() * GlobalFuncs.randFloat() * power * powerMod;
			
			if (mutation_type == MutationTypeEnum.GAUSSIAN){
				randChoice = GlobalFuncs.randFloat();
				if (randChoice > gaussPoint) _gene.lnk.weight += randNum;
				else if (randChoice > coldGaussPoint) _gene.lnk.weight = randNum;
			} else if (mutation_type == MutationTypeEnum.COLD_GAUSSIAN) {
				_gene.lnk.weight = randNum;
			}
			
			_gene.mutation_num = _gene.lnk.weight;
			num += 1;
		}
	}
	
	
	/** 
	 * Generates and returns a new Network based on this genome.
	 */
	public Network Genesis() {
		Network newNet = new Network();
		Iterator<NNode> itr_node = nodes.iterator();
		
		while (itr_node.hasNext()) {
			NNode _node = itr_node.next();
			
			// Copies the gene node for the phenotype
			NNode newNode = new NNode(_node, null);
			
			// Derive the link's parameters from the node's trait
			Trait curTrait = _node.nodeTrait;
			newNode.nodeTrait = JNEATGlobal.derive_trait(curTrait);
			
			if (_node.gNodeLabel == GeneLabelEnum.BIAS || _node.gNodeLabel == GeneLabelEnum.INPUT) {
				newNet.attachInput(_node);
			} else if (_node.gNodeLabel == GeneLabelEnum.OUTPUT) {
				newNet.attachOutput(_node);
			} else {
				newNet.attachHidden(_node);
			}
			
			_node.analogue = newNode;	
		}
		
		if (genes.size() == 0) {
			System.out.println("ALERT!  There are no genes for the network generated from genome #" + genome_id);
		}
		
		if (newNet.outputs.size() == 0) {
			System.out.println("ALERT!  There are no outputs for the network generated from genome #" + genome_id);
		}
		
		Iterator<Gene> itr_gene = genes.iterator();
		
		while (itr_gene.hasNext()) {
			Gene _gene = itr_gene.next();
			
			// Only creates the link if the gene is enabled
			if (_gene.enabled){
				Link curLink = _gene.lnk;
				NNode iNode = curLink.in_node.analogue;
				NNode oNode = curLink.out_node.analogue;
				
				// NOTE: This line could be run through a recurrency check if desired.
				// There is no need to do this with the current implementation of NEAT.
				
				Link newLink = new Link(curLink.weight, iNode, oNode, curLink.recurrent);
				oNode.incoming.add(newLink);
				iNode.outgoing.add(newLink);
				
				// Derive the link's parameters from its trait pointer
				Trait curTrait = curLink.linkTrait;
				curLink.linkTrait = JNEATGlobal.derive_trait(curTrait);
			}
		}
		
		// Attaches the genotype and phenotype
		newNet.genotype = this;
		phenotype = newNet;
				
		return newNet;
	}
	
	
	public String PrintGenome() {
		String ret = "---GENOME #" + genome_id + " START---\n";
		ret = ret + "Has " + genes.size() + " genes, " +  nodes.size() + " nodes, and " + traits.size() + " traits.\n";
		
		ret = ret + "\n || Node Info ||\n\n";
		
		Iterator<NNode> itr_node = nodes.iterator();
		while (itr_node.hasNext()) {
			NNode _node = itr_node.next();
			ret = ret + _node.PrintNode() + "\n\n";
		}
		
		ret = ret + "\n || Gene Info ||\n\n";
		
		Iterator<Gene> itr_gene = genes.iterator();
		while (itr_gene.hasNext()) {
			Gene _gene = itr_gene.next();
			ret = ret + _gene.PrintGene() + "\n\n";
		}
		
		ret = ret + "\n || Trait Info ||\n\n";
		
		Iterator<Trait> itr_trait = traits.iterator();
		while (itr_trait.hasNext()) {
			Trait _trait = itr_trait.next();
			ret = ret + _trait.PrintTrait() + "\n";
		}
		
		ret = ret + "\n ---GENOME END---";
		
		return ret;
	}
	
	public Genome(Vector<Gene> g, Vector<Trait> t, Vector<NNode> n) {
		genome_id = JNEATGlobal.NewGenomeID();
		traits = t;
		nodes = n;
		genes = g;
		phenotype = null;		
	}
	
	public Genome() {
		this(new Vector<Gene>(), new Vector<Trait>(), new Vector<NNode>());		
	}

}
