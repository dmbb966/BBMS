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
	
	int id;
	
	int activation_count;
	
	/** Activation value of the node at time t-1.*/
	double last_activation;
	
	/** Activation value of the node at time t-2.*/
	double prior_activation;
	
	/** Links from other nodes to this node. */
	Vector<Link> incoming;
	/** Links from this node to other nodes. */
	Vector<Link> outgoing;
	

	
	public NNode(NodeTypeEnum nType, int nodeID, GeneLabelEnum placement) {
		fType = NodeFuncEnum.SIGMOID;
		this.nType = nType;
		gNodeLabel = GeneLabelEnum.HIDDEN;
		activesum = 0;
		activation = 0;
		active_flag = false;
		id = nodeID;
		activation_count = 0;
		last_activation = 0;
		prior_activation = 0;
		incoming = new Vector<Link>();
		outgoing = new Vector<Link>();
	}
	
	public NNode(NodeTypeEnum nType, int nodeID) {
		this(nType, nodeID, GeneLabelEnum.HIDDEN);
	}
	
}
