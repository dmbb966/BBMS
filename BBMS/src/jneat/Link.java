package jneat;


/** The connection from one node to another with an associated weight.
 *  Can be marked as recurrent.
 */
public class Link {
	int id;
	
	double weight;
	
	/** Weight added to the adjusted weight */
	double bias;
	
	NNode in_node;
	NNode out_node;
	
	/** Points to the trait of parameters for genetic creation.  */
	Trait linkTrait;
	
	boolean recurrent;
	boolean time_delay;
	
	public Link(double w, NNode inode, NNode onode, boolean recur) {
		this(null, w, inode, onode, recur);
	}
	
	public Link (Trait t, double w, NNode inode, NNode onode, boolean recur) {
		weight = w;
		in_node = inode;
		out_node = onode;
		recurrent = recur;
		time_delay = false;
		id = JNEATGlobal.NewLinkID();
		linkTrait = t;
	}
	
	public String PrintLink() {
		String ret = "LINK #" + id + " is from Node #" + in_node.id + " to Node #" + out_node.id + "\n" + 
				"with weight " + weight + " and bias " + bias + ".";
		if (recurrent) ret = ret + " RECURRENT";
		if (time_delay) ret = ret + " DELAYED";
		
		return ret;
	}
}
