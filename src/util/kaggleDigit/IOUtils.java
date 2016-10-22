
/**
 * Class holds on to some common shared functionality for reading and writing files 
 * pertaining to the Kaggle digit test and train csv files.
 */
 
import java.util.*;
import java.io.*;

public final class IOUtils {

	/** 
	  *	For the purposes of this exercise, we can assume that we're always using the 
	  * digits data from Kaggle, which all come on a 28x28 grid.
	  */
	private static final int GRID_WIDTH = 28;
	private static final int GRID_HEIGHT = 28;
	
	private static final int NUM_TRAINING_POINTS = 42000;
	private static final int NUM_TEST_POINTS = 28000;
	
	private static final String PATH_TO_TEST_SET = "../data/test.csv";
	private static final String PATH_TO_TRAIN_SET = "../data/train.csv";
	
	/** 
	 * The getTrainingData() method will read the train.csv file and return a List of
	 * DigitTrainingPoints.
	 * 
	 * @throws FileNotFoundException If there are problems reading the file.
	 */
	public static List<DigitTrainingPoint> getTrainingData() throws FileNotFoundException { 
		return getFirstNTrainingPoints(NUM_TRAINING_POINTS);
	}
	
	/**
	 * Get the first n DigitTrainingPoints in the Kaggle Digit set.
	 * 
	 * @throws FileNotFoundException If there are problems reading the file.
	 */
	public static List<DigitTrainingPoint> getFirstNTrainingPoints(int n) throws FileNotFoundException{
			
		Scanner inputStream = new Scanner(new FileReader(PATH_TO_TRAIN_SET));
		
		// Skip the first line, since it just contains the headers for the columns.
		inputStream.nextLine();
		
		List<DigitTrainingPoint> output = new ArrayList<>();
		
		for (int i  = 0; i < n; i++) {
			List<Integer> pointValues = new ArrayList<>();
			String csvLine = inputStream.nextLine();
			String[] numericValues = csvLine.split(",");
			
			// The first entry in each line is the label.
			int label = Integer.parseInt(numericValues[0]);
			
			for (int j = 1; j < numericValues.length; j++) {
				pointValues.add(Integer.parseInt(numericValues[j]));
			}
			
			output.add(new DigitTrainingPoint(new Grid(GRID_HEIGHT, GRID_WIDTH, pointValues), label));
		}
		return output;
	}
	
	/**
	 * Get the first n DigitTestPoints in the Kaggle Digit data.
	 * 
	 * @throws FileNotFoundException If there are problems reading the file.
	 */
	public static List<DigitTestPoint> getFirstNTestPoints(int n) throws FileNotFoundException {
		
		Scanner inputStream = new Scanner(new FileReader(PATH_TO_TEST_SET));
		
		// Skip the first line, since it just contains the headers for the columns.
		inputStream.nextLine();
		
		List<DigitTestPoint> output = new ArrayList<>();
		
		for (int i  = 0; i < n; i++) {
			List<Integer> pointValues = new ArrayList<>();
			String csvLine = inputStream.nextLine();
			String[] numericValues = csvLine.split(",");
			
			// No label is present for this file.
			for (int j = 0; j < numericValues.length; j++) {
				pointValues.add(Integer.parseInt(numericValues[j]));
			}
			
			output.add(new DigitTestPoint(new Grid(GRID_HEIGHT, GRID_WIDTH, pointValues)));
		}
		return output;
	}

	/** 
	 * The getTestData() method will read the test.csv file and return a List of
	 * DigitTestPoints.
	 * 
	 * @throws FileNotFoundException If there are problems reading the file.
	 */
	public static List<DigitTestPoint> getTestData() throws IOException { 
		return getFirstNTestPoints(NUM_TEST_POINTS);
	}

	/**
	 * The makeFile() method takes the array of integer predictions and writes
	 * them to a .csv file.  This can be used for creating the file to submit to Kaggle 
	 * for grading.
	 */
	public static void makeFile(List<Integer> predictions) {
		try {
			FileWriter writer = new FileWriter("Predictions.csv");
			for (int i = 0; i < predictions.size(); i++) {
				writer.append(predictions.get(i) + "\n");
			}
			writer.flush();
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();			
		}
	}
}	
