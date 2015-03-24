package testbed;

import java.util.Iterator;
import java.util.Vector;

import bbms.GlobalFuncs;
import jneat.*;

public class JNEATtest {
	
	public static void test1() {
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
	
	public static void test2() {
		double gT = 99.0;
		double eP = gT * 0.8;
		
		System.out.println("Answer: " + eP);
	}
	
	public static void test3() {
		Genome x = new Genome();
		NNode node1 = new NNode(NodeTypeEnum.SENSOR);
		NNode node2 = new NNode(NodeTypeEnum.NEURON);
		Trait t = new Trait();
		Trait t2 = new Trait();
		t2.setTraitParam(3, 1.5);
		Gene l1 = new Gene(node1, node2, 0.3);
		
		x.nodes.add(node1);
		x.nodes.add(node2);
		x.traits.add(t);
		x.traits.add(t2);
		x.genes.add(l1);
		
		Genome x2 = x.duplicate();
		x.traits.get(1).setTraitParam(1, -5);
		
		System.out.println(x.PrintGenome());
		System.out.println(x2.PrintGenome());
		
		
	}
	
	public static void main(String[] args) {
		testX();
	} 
	
	public static void testX() {
		
		for (int i = 0; i < 10; i++) {
			if (i > 2) {
				System.out.println("Break");
				break;
			}
		}
		
		return;
	}
	

}
