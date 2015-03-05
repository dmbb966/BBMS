package jneat;

import java.util.*;

/** The genetic codification of a gene */
public class Gene {
	
	/** Reference to object for identifying input/ouptut nodes and features*/
	Link lnk;
	
	int innovation_num;
	int mutation_num;

	/** TRUE if the gene is enabled, FALSE otherwise */
	boolean enabled;
	
	public Gene(Gene g, Trait tp, NNode inode, NNode onode) {
		lnk = new Link(tp, g.lnk.weight, inode, onode, g.lnk.recurrent);
		innovation_num = g.innovation_num;
		mutation_num = g.mutation_num;
		enabled = g.enabled;
	}

}
