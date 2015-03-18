package jneat;

import java.util.Vector;

/** a NODE is either a NEURON or a SENSOR.
 * As a sensor, it can be loaded with a value for output.
 * As a neuron, it has a list of its incoming input signals.  Use an activation count to avoid flushing.
 */
public class NNode {
	
	/** Activation function type.  SIGMOID is default. */
	NodeFuncEnum fType;
	
	/** Node type.  NEURON or SENSOR. */
	NodeTypeEnum nType;
	
	/** Used for genetic marking of nodes.  Can be INPUT, BIAS, HIDDEN, or OUTPUT*/
	GeneLabelEnum gNodeLabel;
	
	/** Incoming activation sum prior to processing */
	double activesum;
	
	/** Total activation amount entering this node */
	double activation;
	
	boolean active_flag;
	
	boolean is_traversed;
	
	int id;
	
	int activation_count;
	
	int inner_level;
	
	/** Activation value of the node at time t-1.*/
	double last_activation;
	
	/** Activation value of the node at time t-2.*/
	double prior_activation;
	
	/** Links from other nodes to this node. */
	Vector<Link> incoming;
	/** Links from this node to other nodes. */
	Vector<Link> outgoing;

	/** Trait parameters associated with this node */
	Trait nodeTrait;
	
	/** Temporary reference to a node.  Used to generate a new genome in Genome.duplicate()*/
	NNode dup;
	
	/** A reference to a Node.  Used to generate and point from a genetic node (genotype) 
	 * to a real node (phenotype) during the 'genesis' process.  Just don't use protomatter
	 * in the genesis matrix. */
	NNode analogue;

	
	public NNode(NodeTypeEnum nType, GeneLabelEnum placement, Trait t, int id) {
		fType = NodeFuncEnum.SIGMOID;
		this.nType = nType;
		gNodeLabel = GeneLabelEnum.HIDDEN;
		activesum = 0;
		activation = 0;
		active_flag = false;
		this.id = id;
		activation_count = 0;
		last_activation = 0;
		prior_activation = 0;
		incoming = new Vector<Link>();
		outgoing = new Vector<Link>();
		nodeTrait = t;
		is_traversed = false;
		inner_level = 0;
		dup = null;
		analogue = null;
	}
	
	public NNode(NodeTypeEnum nType, GeneLabelEnum placement, Trait t) {
		this(nType, placement, t, JNEATGlobal.NewNodeID());
	}
	
	public NNode(NodeTypeEnum nType, GeneLabelEnum placement)
	{
		this(nType, placement, null);
	}
	
	public NNode(NodeTypeEnum nType) {
		this(nType, GeneLabelEnum.HIDDEN, null);
	}
	
	
	public NNode(NNode n, Trait t) {
		this(n.nType, n.gNodeLabel, t);
		this.id = n.id;	
	}
	
	/** Adds link l to the incoming links of this node. */
	public void AddIncomingLink(Link l) {
		if (l == null) {
			System.out.println("ERROR!  Attempted to link a non-existant node!");
			return;
		}
		
		incoming.addElement(l);
		System.out.println("Added incoming link to Node " + id);
	}
	
	/** Adds link l to the outgoing links of this node. */
	public void AddOutgoingLink(Link l) {
		if (l == null) {
			System.out.println("ERROR!  Attempted to link a non-existant node!");
			return;
		}
		
		outgoing.addElement(l);
		System.out.println("Added outgoing link to Node " + id);
	}
	
	/** Load a sensor neuron with the value that it senses */
	public void SensorInput(double sense) {
		if (nType == NodeTypeEnum.SENSOR) {
			// Time delay memory
			prior_activation = last_activation;
			last_activation=activation;
			
			activation_count++;
			activation = sense;
			
			System.out.println("Sensor " + id + " loaded with value " + sense);
			return;
		}
		
		System.out.println("ERROR!  Neuron " + id + " is not a sensor!");
		return;		
	}
	
	/** Returns the activation from the previous (T-1) step, or 0.0 if there was none */
	public double getTimeDelayActivation() {
		if (activation_count > 1) return last_activation;
		else return 0.0;
	}
	
	/** Returns the activation from the current step, or 0.0 if unactivated */
	public double getActivation() {
		if (activation_count > 0) return activation;
		else return 0.0;
	}

	
	public String PrintNode() {
		String ret = "";
		
		switch (gNodeLabel) {
		case BIAS:
			ret = "Bias ";
			break;
		case INPUT:
			ret = "Input ";
			break;
		case OUTPUT: 
			ret = "Output ";
			break;
		case HIDDEN:
			ret = "Hidden ";
			break;					
		}
		
		ret += "Node " + id + " ";
		if (active_flag) ret += " active ";
		else ret += " inactive ";
		
		ret += "with " + activation_count + " activations.\n";
		ret += "Current: " + activation + " || Past: " + last_activation + " || Prior: " + prior_activation;
		
				
		return ret;
			
	}
	
}
