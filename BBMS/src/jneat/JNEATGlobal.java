package jneat;

public class JNEATGlobal {
	
	// NOTE: Due to constructors, there may be some gaps, i.e. 
	// a new ID number is guaranteed to NOT duplicate an existing one,
	// but it is not necessarily sequential.
	static int numGenes = 0;
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
	
	/** Returns the ID of the next available gene ID.  Synomyous with innovation number.  Increments global counter. */
	public static int NewGeneID() {
		return numGenes++;
	}
	
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
   
   /** Duplicates a trait and returns it, or returns a newly generated Trait if a null pointer is passed.
    */
   public static Trait derive_trait(Trait t) {
	   Trait ret = new Trait();
	   	   
	   if (t != null) {
		   for (int i = 0; i < JNEATGlobal.numTraitParams; i++) {
				ret.params[i] = t.params[i];
		   }
	   }
	   
	   return ret;
   }

}
