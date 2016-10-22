

/**
 * Class that can cross validate a model.  The percentage of points in the training set 
 * that will be used for model validation is a fixed percentage of the total number of 
 * points in the training set. 
 */
 
import java.util.*;
 
public final class CrossValidation {

	/** The percentage of the data points which will be tested. */
	private static final double TEST_PERCENTAGE = 0.2;

	/** No need to construct a Utils class. */
	private CrossValidation() {}

	/** 
	 * Compute the effectiveness of the Classifier, and return the percentage
	 * of correct classifications. 
	 *
	 *  TODO:  This should probably work with generic label types.
	 */
	public static double crossValidate(Classifier classifier, List<? extends TrainingPoint> data) {
	
		Set<Integer> indicesToTest = generateRandomIndices(data.size());
		Set<TrainingPoint> validationSet = getValidationSet(data, indicesToTest);
		
		int correct = 0; 
		
		for(Integer index : indicesToTest) {
		
			int predictedValue = classifier.classify(data.get(index), validationSet);
			
			if (predictedValue == data.get(index).getLabel()) {
				correct++;
				System.out.println("Correct!\n");
			}	
		}
		
		// Don't forget to convert to double!
		return correct * 1.0 / indicesToTest.size();
	}
	
	/**
	 * Get the set of TrainingPoints that will be used to validate the model.  This method returns
	 * a set of TrainingPoints with corresponding set of indices equal to the complement of 
	 * indicesToTest.  In other words, the set of indices in data is equal to the disjoint union
	 * of indicesToTest and the indices of the set of TrainingPoints that this method returns.  
	 */
	private static Set<TrainingPoint> getValidationSet(List<? extends TrainingPoint> data, Set<Integer> indicesToTest) {
	
		Set<TrainingPoint> validationSet = new HashSet<>();
		
		// Validation points are all data points except those with index in indicesToTest.
		for (int i = 0; i < data.size(); i++) {
			if (!indicesToTest.contains(i)) {
				validationSet.add(data.get(i));
			}
		}
		
		return validationSet;
	}

	/**
	 * Return a list of randomly chosen indices from the passed List.  The returned List
	 * will contain TEST_PERCENTAGE percent of the indices from the passed List.
	 */
	private static Set<Integer> generateRandomIndices(int maxValue) {
	
		// Choose a percentage of the possible indices.
		int numToChoose = new Double(maxValue * TEST_PERCENTAGE).intValue();
		
		Set<Integer> chosenIndices = new HashSet<>();
		
		Random random = new Random();
		
		// Probably not the most efficient way to do this.  Will need to change for large
		// values of TEST_PERCENTAGE.
		while(chosenIndices.size() < numToChoose) {
			chosenIndices.add(random.nextInt(maxValue));
		}
		
		return chosenIndices;
	} 
} 