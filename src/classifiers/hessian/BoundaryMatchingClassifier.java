/**
 * This classifier works by trying to match the boundaries of the test object to the
 * training object.  The effective boundary is computed using a hessian feature detector.
 * We then 'overlay' this feature vector onto a training point to see how well the boundaries 
 * coincide.
 *
 * This doesn't seem to score particularly well, usually scoring in the 40-50% correct range.
 * Various modifications boost performance towards the high end of that range.  It seems to 
 * be good at detecting 8's and 0's, but it almost never predicts a label of 1.
 *
 */
import java.util.*;
 
public class BoundaryMatchingClassifier implements Classifier {

	// The filter for feature detection. 
	private static int featureThreshold = 1;
	
	// The threshold at or above which a score is considered a near perfect match.  For 
	// example, a value of 0.98 means that 98% of the detected boundary points match.
	private static double HIGH_SCORE_THRESH = 1.0;

	/** This class is stateless. */
	private BoundaryMatchingClassifier() {}
	
	/** 
	 * This is a singleton class.  It might be prudent in the future to make multiple 
	 * instances, but for now this works.
	 */
	public static BoundaryMatchingClassifier CLASSIFIER() {
		return new BoundaryMatchingClassifier();
	}

	public int classify(TestPoint testPoint, Iterable<TrainingPoint> trainingSet) {
		
		// cache the hessians info for the test point.
		List<Grid.Index> testFeatureIndices = 
			Hessian.computeHessians(((GriddyTestPoint) testPoint).getGrid()).extractFeatureIndices(featureThreshold);
		
		// For each point in the training set, store the resulting score as a function of 
		// the label.
		Map<Integer, List<Double>> labelToScoreMap = new HashMap<>();
		
		// Keep track of the labels that have at least one very high score.
		Set<Integer> labelsWithHighScores = new HashSet<>();
		
		// Maintain the single best score.
		double highScore = 0;
		int labelWithHighestScore = -1;
		
		for (TrainingPoint trainingPoint : trainingSet) {
		
			if (trainingPoint instanceof GriddyTrainingPoint) {
				
				double score = score(testFeatureIndices, (GriddyTrainingPoint) trainingPoint);
				
				if (labelToScoreMap.containsKey(trainingPoint.getLabel())) {
					labelToScoreMap.get(trainingPoint.getLabel()).add(score);
				}
				else {
					List<Double> vals = new ArrayList<>();
					vals.add(score);
					labelToScoreMap.put(trainingPoint.getLabel(), vals);
				}
				if (score >= HIGH_SCORE_THRESH) {
					labelsWithHighScores.add(trainingPoint.getLabel());
				}	
				if (score >= highScore) {
					highScore = score;
					labelWithHighestScore = trainingPoint.getLabel();
				}			
			}
		}
		
		
		Set<Integer> bestCandidates;
		
		if (labelsWithHighScores.isEmpty()) {
			bestCandidates = labelToScoreMap.keySet();
		}
		else {
			bestCandidates = labelsWithHighScores;
		}

		// Find the candidate with highest mean score.
		double maxMean = 0;
		int bestLabel = -1;
		for (Integer label : bestCandidates) {
			double meanScore = mean(labelToScoreMap.get(label));
			if (meanScore > maxMean) {
				bestLabel = label;
				maxMean = meanScore;
			}
		}
		if (bestLabel != labelWithHighestScore) {
			System.out.println("best label does not have the highest score");
		}
	//	((GriddyTestPoint) testPoint).getGrid().print();
	//	System.out.println(labelToScoreMap);
		System.out.printf("Classified as %d.  \n", bestLabel);
	//	System.out.printf("HighScore: %f \n", highScore);
		
		return bestLabel;
	}
	
	
	
	/**
	 * Quantify how well the boundary of the test point lines up with the boundary of the 
	 * training point.  This will return a double between 0 and 1, inclusive.
	 */
	private static double score(List<Grid.Index> testFeatureIndices, GriddyTrainingPoint trainPoint) {

		List<Grid.Index> trainFeatureIndices = Hessian.computeHessians(trainPoint.getGrid()).extractFeatureIndices(featureThreshold);
	
		int numMatchingFeatures = 0;
		
		for (Grid.Index index : testFeatureIndices) {
			// for each testFeatureIndex, test whether there are any trainFeatures nearby
			if (containsNeighbor(trainFeatureIndices, index)) {
				numMatchingFeatures++;
			}
		}
		
		// Return the fraction of testfeatures that match the trainFeatures.
		return 1.0 * numMatchingFeatures / testFeatureIndices.size();
	}
	
	/**
	 * Given an index in a grid and a list of grid indices, determine whether the list contains
	 * any indices that are nearby to the specified index.  In this case, nearby means that
	 * delta i and delta j are both less than or equal to closenessThreshold.
	 */
	private static boolean containsNeighbor(List<Grid.Index> indices, Grid.Index index) {
	
		// This value is a magic number that seems to produce the highest scores.
		int closenessThreshold = 1;
	
		for (Grid.Index compareIndex : indices) {
			if (index.neighborOf(compareIndex, closenessThreshold)) {
				return true;
			}
		}
		return false;
	} 
	
	
	/**
	 * Compute the mean of a list.
	 */
	private static double mean(Collection<Double> vals) {
		double sum = 0;
		for (Double val : vals) {
			sum += val;
		}
		return sum / vals.size();
	}	
} 