package jneat;

import java.util.*;

/** The genetic codification of a gene */
public class Gene {
	
	/** Reference to object for identifying input/ouptut nodes and features*/
	Link lnk;
	
	int innovation_num;
	double mutation_num;	// Based on pre-mutation link weight

	/** TRUE if the gene is enabled, FALSE otherwise */
	boolean enabled;
	
	public Gene(Gene g, Trait tp, NNode inode, NNode onode) {
		lnk = new Link(tp, g.lnk.weight, inode, onode, g.lnk.recurrent);
		innovation_num = g.innovation_num;
		mutation_num = g.mutation_num;
		enabled = g.enabled;
	}
	
	public Gene(Trait tp, NNode inode, NNode onode, double w, boolean recur, double mnum) {
		lnk = new Link(tp, w, inode, onode, recur);
		innovation_num = JNEATGlobal.NewGeneID();
		mutation_num = mnum;
		enabled = true;
	}
	
	public Gene(Trait tp, double weight, NNode inode, NNode onode, boolean recur, int innov, double mut_num) {
		lnk = new Link(tp, weight, inode, onode, recur);
		innovation_num = innov;
		mutation_num = mut_num;
		enabled = true;
	}
	
	public Gene (NNode inode, NNode onode, double w) {
		this(new Trait(),  inode, onode, w, false, 0.0);
	}
	
	public Gene (Trait tp, NNode inode, NNode onode, double w) {
		this(tp, inode, onode, w, false, 0.0);
	}
	
	public String PrintGene() {
		String ret = "Gene innovation #" + innovation_num + " and mutation #" + mutation_num + ":\n";
		ret = ret + lnk.PrintLink();
		if (!enabled) ret = ret + "\n--- GENE DISABLED ---";
		
		return ret;
	}
	
	public String SaveGeneHeader() {
		StringBuffer buf = new StringBuffer("");
		
		buf.append("# Gene data format follows:\n");
		buf.append("# 'Gene', innovation_num, mutation_num, enabled\n");
		buf.append("#   + Link\n");
						
		return buf.toString();
	}
	
	public String SaveGene() {
		StringBuffer buf = new StringBuffer("");
		
		buf.append("Gene, " + innovation_num + ", ");
		buf.append(mutation_num + ", ");
		buf.append(enabled + ", ");
		buf.append(lnk.SaveLink());
		
		return buf.toString();
	}

}
