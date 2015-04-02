package unit;

public enum FitnessTypeEnum {
	/** Fitness is based on the number of times it spots enemy units throughout the scenario. */
	SIMPLE_GREEDY,
	/** As SIMPLE_GREEDY, except if an enemy is spotted by multiple friendlies the reward is shared among them */
	SHARED_SPOTTING,
	/** Fitness averaged among all organisms of the same species for a given scenario */
	SOVIET_COMMUNISM,
	/** Fitness averaged among all organisms that took part in the scenario */
	FULL_COMMUNISM
}
