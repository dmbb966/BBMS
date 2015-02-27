package jneat;

import java.util.Vector;

public class Network {
	
	Vector<NNode> inputs;
	Vector<NNode> outputs;
	Vector<NNode> allNodes;
	
	int net_id;	
	
	public Network(Vector<NNode> in, Vector<NNode> out, int xnet_id) {
		inputs = in;
		outputs = out;
		allNodes = new Vector<NNode>();
		
		allNodes.addAll(in);
		allNodes.addAll(out);
		
		net_id = xnet_id;		
	}
}
