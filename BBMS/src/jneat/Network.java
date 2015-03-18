package jneat;

import java.util.Vector;

// aka Phenotype
public class Network {
	
	Vector<NNode> inputs;
	Vector<NNode> outputs;
	Vector<NNode> allNodes;
	
	Genome genotype;		// The genotype that created this phenotype
	int net_id;	
	
	public Network(Vector<NNode> in, Vector<NNode> out) {
		if (in != null) inputs = in;
		else inputs = new Vector<NNode>();
		
		if (out != null) outputs = out;
		else outputs = new Vector<NNode>();
		
		allNodes = new Vector<NNode>();
		genotype = null;
		
		net_id = JNEATGlobal.NewNetworkID();		
	}
	
	public Network() {
		this(new Vector<NNode>(), new Vector<NNode>());
	}
	
	/** Attaches a hidden node to this neural network.
	 * 
	 * @param n
	 */
	public void attachHidden(NNode n) {
		allNodes.addElement(n);
		
		System.out.println("Added node " + n.id + " as a hidden node of network " + net_id);
	}
	
	/** Adds node 'n' to this neural network's input nodes
	 * @param n
	 */
	public void attachInput(NNode n) {
		
		inputs.addElement(n);
		allNodes.addElement(n);
		
		System.out.println("Added node " + n.id + " to inputs of network " + net_id);
	}
	
	/** Adds node 'n' to this neural network's output nodes
	 * 
	 * @param n
	 */
	public void attachOutput(NNode n) {
		outputs.addElement(n);
		allNodes.addElement(n);
		
		System.out.println("Added node " + n.id + " to outputs of network " + net_id);
	}
	
	public void linkNodes(NNode parent, NNode child, double weight) {
		if (parent == null || child == null) {
			System.out.println("ERROR!  Attempting to link a non-existant node!");
			return;
		}
		
		Link l = new Link(weight, parent, child, false);
		parent.AddOutgoingLink(l);
		child.AddIncomingLink(l);
		
		System.out.println(l.PrintLink());
	}
	
	
	public boolean ActivateNetwork() {
		int abortCount = 0;		// Counts number of cycles in which the outputs have not all activated
		boolean activateOnce = false;	
		
		while (OutputsOff() || !activateOnce) 
		{
			abortCount++;
			if (abortCount >= JNEATGlobal.maxActivationCycles) {
				System.out.println("ERROR!  Network outputs not active after " + abortCount + " cycles!  Aborting.");
				return false;
			}
		
		
			for (int i = 0; i < allNodes.size(); i++) {
				NNode finger = allNodes.elementAt(i);
			
				if (finger.nType != NodeTypeEnum.SENSOR) {
					finger.activesum = 0.0;
					finger.active_flag = false;
					
					for (int j = 0; j < finger.incoming.size(); j++) {
						Link fingerLink = finger.incoming.elementAt(j);
						
						if (fingerLink.time_delay) {
							finger.activesum += fingerLink.weight * fingerLink.in_node.getTimeDelayActivation();
						} else {
							if (fingerLink.in_node.active_flag || fingerLink.in_node.nType == NodeTypeEnum.SENSOR) finger.active_flag = true;
							
							finger.activesum += fingerLink.weight * fingerLink.in_node.getActivation();
						}
					}			
				}
			}
		
			for (int i = 0; i < allNodes.size(); i++) {
				NNode finger = allNodes.elementAt(i);
				
				if (finger.nType != NodeTypeEnum.SENSOR) {
					if (finger.active_flag) {
						finger.prior_activation = finger.last_activation;
						finger.last_activation = finger.activation;
						
						if (finger.fType == NodeFuncEnum.SIGMOID) {
							finger.activation = JNEATGlobal.fsigmoid(finger.activesum,  4.924273,  2.4621365);
						}
						finger.activation_count += 1.0;
					}
				}
			}
		
			activateOnce = true;
		}
		
		return true;
	}
	
	/** Checks all output neurons of this network.  Returns true if any of these outputs have NOT been activated.
	 * 
	 * @return
	 */
	public boolean OutputsOff() {
		for (int i = 0; i < outputs.size(); i++) {
			if (outputs.elementAt(i).activation_count == 0) return true; 
		}
		
		return false;
	}
}
