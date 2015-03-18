package jneat;

public class JNEATGlobal {
	
	static int numGenomes = 0;
	static int numNetworks = 0;
	static int numNodes = 0;
	static int numLinks = 0;
	static int numTraits = 0;
	
	/** Number of parameters in a trait */
	static int numTraitParams = 8;
	
	/** Probability of mutating a single trait parameter*/
	static double traitParamMutProb = 0.05;
	
	/** Severity of trait mutations (by default -1 to +1) */
	static double traitMutationPower = 1.00;
	
	/** Max number of activation cycles a network will go through before declaring a disconnection error */
	static int maxActivationCycles = 30;
	
	/** Returns the ID of the next available genome ID.  Increments the global counter. */
	public static int NewGenomeID() {
		return numGenomes++;
	}	
	
	/** Returns the ID of the next available network ID.  Increments the global counter. */
	public static int NewNetworkID() {
		return numNetworks++;
	}
	
	/** Returns the ID of the next available node ID.  Increments the global counter. */
	public static int NewNodeID() {
		return numNodes++;
	}
	
	/** Returns the ID of the next available link ID.  Increments the global counter. */
	public static int NewLinkID() {
		return numLinks++;
	}
	
	/** Returns the ID of the next available trait ID.  Increments the global counter. */
	public static int NewTraitID() {
		return numTraits++;
	}
	
	/** Sigmoid function for the given parameters. 
	 * 
	 * @param activesum
	 * @param slope
	 * @param constant
	 * @return
	 */
   public static double fsigmoid(double activesum,double slope,double constant) 
    {
		 return (1/(1+(Math.exp(-(slope*activesum))))); //Compressed
    }

}
