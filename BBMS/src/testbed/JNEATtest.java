package testbed;

import bbms.GlobalFuncs;
import jneat.*;

public class JNEATtest {
	
	public static void main(String[] args) {		
        
		Network testNet = new Network(null, null);
		
		NNode node1 = new NNode(NodeTypeEnum.SENSOR);
		NNode node2 = new NNode(NodeTypeEnum.NEURON);
		
		testNet.attachInput(node1);
		testNet.attachOutput(node2);
		
		testNet.linkNodes(node1, node2, 0.3);
		node1.SensorInput(0.7);
		
		System.out.println("\n" + node1.PrintNode());
		System.out.println("\n" + node2.PrintNode());
		
		testNet.ActivateNetwork();
		
		System.out.println("\n" + node1.PrintNode());
		System.out.println("\n" + node2.PrintNode());
	} 
	

	

}
