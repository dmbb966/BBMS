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
	
	/** Measures the compatibility between this Genome and the one supplied in the argument.
	 * Measures by computing a linear combination of three characteristics:
	 * - Percent disjoint genes
	 * - Percent excess genes
	 * - Mutational difference within matching genes
	 * 
	 * Formula is: Disjoint_coef * pdg + excess_coef * peg + mutdiff_coef * mdmg
	 * The three coefficients are global parameters
	 */
	public double Compatibility(Genome g) {
		int max_genome_size = Math.max(genes.size(), g.genes.size());
		int j1 = 0;
		int j2 = 0;
		int excess_genes = 0;	
		int matching_genes = 0;	
		int disjoint_genes = 0;	
		
		double mut_diff = 0.0;
		
		for (int j = 0; j < max_genome_size; j++) {
			if (j1 >= genes.size()) {
				excess_genes ++;
				j2 ++;
			} else if (j2 >= g.genes.size()) {
				excess_genes ++;
				j1 ++;
			} else {
				Gene _gene1 = genes.elementAt(j1);
				Gene _gene2 = g.genes.elementAt(j2);
				
				// Extract current innovation numbers
				int p1innov = _gene1.innovation_num;
				int p2innov = _gene2.innovation_num;
				
				if (p1innov == p2innov) {
					matching_genes++;
					mut_diff += Math.abs(_gene1.mutation_num - _gene2.mutation_num);
					j1++;
					j2++;
				} else if (p1innov < p2innov) {
					j1++;
					disjoint_genes++;
				} else if (p2innov < p1innov) {
					j2++;
					disjoint_genes++;
				}
			}
		}
		
		// NOTE: (mut_diff_total / num_matching) gives the AVERAGE difference between mutation_nums for any two matching Genes
		
		return (JNEATGlobal.p_disjoint_coeff * disjoint_genes +
				JNEATGlobal.p_excess_coeff * excess_genes +
				JNEATGlobal.p_mutdiff_coeff * (mut_diff / matching_genes));				
	}
	
	/** For use in mating functions.
	 * Averages the traits from this Genome and the one passed to it.
	 */
	public Vector<Trait> AverageTraits(Genome g) {
		Vector<Trait> newTraits = new Vector<Trait>();
		
		// First, average the traits from the two parents to form the child trait
		// If one trait vector is larger, it will take those additional traits
		// unmodified into the child trait vector.
		for (int j = 0; j < Math.max(traits.size(), g.traits.size()); j++) {
			Trait _trait1 = null; 
			Trait _trait2 = null;
			if (j < traits.size()) _trait1 = traits.elementAt(j);
			if (j < g.traits.size()) _trait2 = g.traits.elementAt(j);
			
			Trait newTrait = new Trait(_trait1, _trait2);
			newTraits.add(newTrait);
		}
		
		return newTraits;		
	}
	
	/** Checks the chosenGene versus the newGene vector and returns if it is duplicate, i.e. should skip
	 * 
	 * @param newGenes
	 * @param chosenGene
	 * @return
	 */
	public boolean CheckGeneConflict(Vector<Gene> newGenes, Gene chosenGene) {
		boolean skipGene = false;
		
		// Check to see if the chosenGene conflicts with one already chosen
		// i.e. do they represent the same link?
		
		Iterator<Gene> itr_newGenes = newGenes.iterator();
		while (itr_newGenes.hasNext()) {
			Gene _curGene = itr_newGenes.next();
			
			if (_curGene.lnk.in_node.id == chosenGene.lnk.in_node.id &&
				_curGene.lnk.out_node.id == chosenGene.lnk.out_node.id &&
				_curGene.lnk.recurrent == chosenGene.lnk.recurrent) {
				skipGene = true;
				break;
			}
			if (_curGene.lnk.in_node.id == chosenGene.lnk.out_node.id &&
				_curGene.lnk.out_node.id == chosenGene.lnk.in_node.id &&
				!_curGene.lnk.recurrent && !chosenGene.lnk.recurrent) {
				skipGene = true;
				break;
			}
		}		
		
		return skipGene;
	}
	
	/** Adds chosenGene and associated nodes and traits 
	 */
	public void AddGene(Vector<NNode> newNodes, Vector<Trait> newTraits, Vector<Gene> newGenes, Gene chosenGene, boolean disableGene) {
		int traitNum = 0;
		
		// Add chosenGene to the child
		if (chosenGene.lnk.linkTrait == null) traitNum = traits.firstElement().id;
		else traitNum = chosenGene.lnk.linkTrait.id - traits.firstElement().id; 
		
		NNode inode = chosenGene.lnk.in_node;
		NNode onode = chosenGene.lnk.out_node;
		NNode newinode = null;
		NNode newonode = null;
		
		boolean foundiNode = false;
		boolean foundoNode = false;
		
		// For ordering, and stuff.
		if (inode.id >= onode.id){
			NNode temp = inode;
			inode = onode;
			onode = temp;
		}
						
		// Search the inode and onode
		for (int i = 0; i < newNodes.size(); i++) {
			NNode curNode = newNodes.elementAt(i);
			if (curNode.id == inode.id){
				foundiNode = true;
				newinode = curNode;							
			}
			if (curNode.id == onode.id) {
				foundoNode = true;
				newonode = curNode;
			}
		}
		
		// Insert inode if needed
		if (!foundiNode) {
			int nodeTraitNum = 0;
			
			if (inode.nodeTrait != null) nodeTraitNum = inode.nodeTrait.id - traits.firstElement().id;						
			
			Trait newTrait = newTraits.elementAt(nodeTraitNum);
			newinode = new NNode(inode, newTrait);
			node_insert(newNodes, newinode);						
		}				
							
		// Insert onode if needed
		if (!foundoNode) {
			int nodeTraitNum = 0;
			
			if (onode.nodeTrait != null) nodeTraitNum = onode.nodeTrait.id - traits.firstElement().id;
			
			Trait newTrait = newTraits.elementAt(nodeTraitNum);
			newonode = new NNode(onode, newTrait);
			node_insert(newNodes, newonode);
		}
		
		
		// Add the gene
		Trait newTrait = newTraits.elementAt(traitNum);
		Gene newGene = new Gene(chosenGene, newTrait, newinode, newonode);
		if (disableGene) {
			newGene.enabled = false;
			disableGene = false;
		}
		
		newGenes.add(newGene);				
	}
	
	public Genome MateMultipoint(Genome g, double fitness1, double fitness2) {
		
		// First, average traits
		Vector<Trait> newTraits = AverageTraits(g);		
		
		// Second, determine which genome is better.
		// The "worse" genome shouldn't be allowed to add extra structural baggage.
		// If they are equally fit, then the smaller one's disjoint and excess genes will be used.

		boolean p1better = false;
		int size1 = genes.size();
		int size2 = g.genes.size();
		
		if (fitness1 > fitness2) p1better = true;
		else if (fitness1 == fitness2 && size1 < size2) p1better = true;
				
		Vector<Gene> newGenes = new Vector<Gene>();
		Vector<NNode> newNodes = new Vector<NNode>();
		
		int j1 = 0;
		int j2 = 0;
		boolean skipGene = false;
		boolean disableGene = false;
		Gene chosenGene = null;
		
		while (j1 < size1 || j2 < size2) {
			skipGene = false;		// Defaults to not skipping a chosen gene
			disableGene = false;	
			if (j1 >= size1) {
				chosenGene = g.genes.elementAt(j2);
				j2++;
				if (p1better) skipGene = true;		// Skip excess from the worse genome
			} else if (j2 >= size2) {
				chosenGene = genes.elementAt(j1);
				j1++;
				if (!p1better) skipGene = true;		// Skip excess from the worse genome
			} else {
				Gene _p1Gene = genes.elementAt(j1);
				Gene _p2Gene = g.genes.elementAt(j2);
				
				if (_p1Gene.innovation_num == _p2Gene.innovation_num) {
					if (GlobalFuncs.randFloat() < 0.5) chosenGene = _p1Gene;
					else chosenGene = _p2Gene;
					
					// If one of the genes is disabled, the corresponding gene in the offspring
					// has a high chance of being disabled as well
					if (!_p1Gene.enabled || !_p2Gene.enabled) {
						if (GlobalFuncs.randFloat() < 0.75) disableGene = true;
					}		
					j1++;
					j2++;	
				} else if (_p1Gene.innovation_num < _p2Gene.innovation_num) {
					chosenGene = _p1Gene;
					j1++;
					if (!p1better) skipGene = true;												
				} else if (_p2Gene.innovation_num < _p1Gene.innovation_num) {
					chosenGene = _p2Gene;
					j2++;
					if (p1better) skipGene = true;
				}
			}
			
			
			if (!skipGene) skipGene = CheckGeneConflict(newGenes, chosenGene);			
			
			// Add gene if not skipped
			if (!skipGene) {
				AddGene(newNodes, newTraits, newGenes, chosenGene, disableGene);
			} 
		} // End while loop
		
		Genome newGenome = new Genome(newGenes, newTraits, newNodes);
		
		boolean outputPresent = false;
		
		// Verify to ensure there are outputs
		for (int i = 0; i < newNodes.size(); i++) {
			NNode curNode = newNodes.elementAt(i);
			if (curNode.gNodeLabel == GeneLabelEnum.OUTPUT) {
				outputPresent = true;
				break;
			}
		}
		
		if (!outputPresent) {
			System.out.println("WARNING!  When conducting MateMultipoint, no output nodes found.");
			System.out.println("Genome A:\n" + this.PrintGenome());
			System.out.println("\nGenome B:\n" + g.PrintGenome());
			System.out.println("\nResulting Genome:\n" + newGenome.PrintGenome());			
		}
		return newGenome;
	}
	
	public Genome MateMultiAverage(Genome g, double fitness1, double fitness2) {
		
		// First, average traits
		Vector<Trait> newTraits = AverageTraits(g);		
		
		// Second, determine which genome is better.
		// The "worse" genome shouldn't be allowed to add extra structural baggage.
		// If they are equally fit, then the smaller one's disjoint and excess genes will be used.

		boolean p1better = false;
		int size1 = genes.size();
		int size2 = g.genes.size();
		
		if (fitness1 > fitness2) p1better = true;
		else if (fitness1 == fitness2 && size1 < size2) p1better = true;
				
		Vector<Gene> newGenes = new Vector<Gene>();
		Vector<NNode> newNodes = new Vector<NNode>();
		
		int j1 = 0;
		int j2 = 0;
		boolean skipGene = false;
		boolean disableGene = false;
		Gene chosenGene = null;
		
		while (j1 < size1 || j2 < size2) {
			skipGene = false;		// Defaults to not skipping a chosen gene
			disableGene = false;	
			if (j1 >= size1) {
				chosenGene = g.genes.elementAt(j2);
				j2++;
				if (p1better) skipGene = true;		// Skip excess from the worse genome
			} else if (j2 >= size2) {
				chosenGene = genes.elementAt(j1);
				j1++;
				if (!p1better) skipGene = true;		// Skip excess from the worse genome
			} else {
				Gene _p1Gene = genes.elementAt(j1);
				Gene _p2Gene = g.genes.elementAt(j2);
				
				if (_p1Gene.innovation_num == _p2Gene.innovation_num) {
					if (GlobalFuncs.randFloat() < 0.5) chosenGene = _p1Gene;
					else chosenGene = _p2Gene;
					
					// NOTE: In original JNEAT, only the link traits are copied in this part to a newly constructed
					// average gene.  
					
					// Average weights
					chosenGene.lnk.weight = (_p1Gene.lnk.weight + _p2Gene.lnk.weight)/ 2.0;
					
					// Randomly takes the in and out nodes from its parent genes
					if (GlobalFuncs.randFloat() > 0.5) chosenGene.lnk.in_node = _p1Gene.lnk.in_node;
					else chosenGene.lnk.in_node = _p2Gene.lnk.in_node;
					
					if (GlobalFuncs.randFloat() > 0.5) chosenGene.lnk.out_node = _p1Gene.lnk.out_node;
					else chosenGene.lnk.out_node = _p2Gene.lnk.out_node;
					
					if (GlobalFuncs.randFloat() > 0.5) chosenGene.lnk.recurrent = _p1Gene.lnk.recurrent;
					else chosenGene.lnk.recurrent = _p2Gene.lnk.recurrent;
					
					chosenGene.innovation_num = _p1Gene.innovation_num;
					chosenGene.mutation_num = (_p1Gene.mutation_num + _p2Gene.mutation_num) / 2.0;
					
					
					// If one of the genes is disabled, the corresponding gene in the offspring
					// has a high chance of being disabled as well
					if (!_p1Gene.enabled || !_p2Gene.enabled) {
						if (GlobalFuncs.randFloat() < 0.75) disableGene = true;
					}		
					j1++;
					j2++;	
				} else if (_p1Gene.innovation_num < _p2Gene.innovation_num) {
					chosenGene = _p1Gene;
					j1++;
					if (!p1better) skipGene = true;												
				} else if (_p2Gene.innovation_num < _p1Gene.innovation_num) {
					chosenGene = _p2Gene;
					j2++;
					if (p1better) skipGene = true;
				}
			}			
			
			if (!skipGene) skipGene = CheckGeneConflict(newGenes, chosenGene);		
			
			// Add gene if not skipped
			if (!skipGene) {
				AddGene(newNodes, newTraits, newGenes, chosenGene, disableGene);
			} 
		} // End while loop
		
		Genome newGenome = new Genome(newGenes, newTraits, newNodes);
		
		boolean outputPresent = false;
		
		// Verify to ensure there are outputs
		for (int i = 0; i < newNodes.size(); i++) {
			NNode curNode = newNodes.elementAt(i);
			if (curNode.gNodeLabel == GeneLabelEnum.OUTPUT) {
				outputPresent = true;
				break;
			}
		}
		
		if (!outputPresent) {
			System.out.println("WARNING!  When conducting MateMultipointAvg, no output nodes found.");
			System.out.println("Genome A:\n" + this.PrintGenome());
			System.out.println("\nGenome B:\n" + g.PrintGenome());
			System.out.println("\nResulting Genome:\n" + newGenome.PrintGenome());			
		}
		return newGenome;
	}
	
	
	/** Inserts a NNode into a NNode vector such that it remains sorted by node ID (ascending)
	 */
	public void node_insert(Vector<NNode> nlist, NNode n) {
		for (int i = 0; i < nlist.size(); i++) {
			if (nlist.elementAt(i).id >= n.id) {
				nlist.insertElementAt(n, i);
				break;
			}
		}
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
