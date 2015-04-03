package unit;

import gui.GUI_NB;

import java.util.Vector;

import clock.Clock;
import spotting.SpotRecords;
import spotting.SpotReport;
import bbms.GlobalFuncs;

public enum FitnessTypeEnum {
	/** Fitness is based on the number of times it spots enemy units throughout the scenario. */
	SIMPLE_GREEDY,
	/** As SIMPLE_GREEDY, except if an enemy is spotted by multiple friendlies the reward is shared among them */
	SHARED_SPOTTING,
	/** Fitness averaged among all organisms of the same species for a given scenario */
	SOVIET_COMMUNISM,
	/** Fitness averaged among all organisms that took part in the scenario */
	FULL_COMMUNISM;
	
	public double EvaluateFitness(Unit x) {
		switch(this) {
		case SIMPLE_GREEDY:
			return EvaluateSimpleGreedy(x);
		case SHARED_SPOTTING:
			return 0.7;
		case SOVIET_COMMUNISM:
			return 0.9;
		case FULL_COMMUNISM:
			return 1.1;
		}
		
		return -1.0;
	}
	
	private double EvaluateSimpleGreedy(Unit finger) {
		// Scan spot reports for the current turn to find what this unit has seen
		// Then credit it accordingly
		// Can probably be done more efficiently
		SpotRecords spots = GlobalFuncs.allSpots.getReportsTime(Clock.time);
		
		for (int i = 0; i < spots.records.size(); i++) {
			SpotReport x = spots.records.elementAt(i);
			
			if (x.spotter == finger && GlobalFuncs.scenMap.inReconZone(GlobalFuncs.scenMap.getHex(x.targetLoc))) {
				finger.spotCredits += 1.0;
				GUI_NB.GCO("DEBUG: Unit " + finger.callsign + " credited with spotting " + x.target.callsign + " at time " + x.timeSpotted + " (total credits: " + finger.spotCredits);
			}
		}
		
		// Now calculates the new unit fitness, which is total spots / total number of possible spots
		return finger.spotCredits / GlobalFuncs.maxPossibleSpots;
	}
}
