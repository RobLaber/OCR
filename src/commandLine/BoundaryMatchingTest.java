
/**
 * Front end for the BoundaryMatching classifier.
 */


import java.util.*;
import java.io.*;

public class BoundaryMatchingTest {

	public static void main(String[] args) {
	
		int numPoints = 1000;
		
		if (args.length > 0) {
			try {
				numPoints = Integer.parseInt(args[0]);
			}
			catch(NumberFormatException e) {
				throw new RuntimeException("Illegal argument for numPoints.");
			}
		}
		
		List<DigitTrainingPoint> data = new ArrayList<>();
		Classifier classifier = BoundaryMatchingClassifier.CLASSIFIER();
		
		try {
			data = IOUtils.getFirstNTrainingPoints(numPoints);
		}
		catch(Exception e) {}
		
		System.out.println("Score: " + CrossValidation.crossValidate(classifier, data)); 
	}

}