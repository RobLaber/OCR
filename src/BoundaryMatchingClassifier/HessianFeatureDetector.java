/**
 * This class explores the use of the discrete Hessian for feature detection.  This is suitable
 * for any type of data with a griddy layout.
 */

import java.util.*;
import java.io.*;

public class HessianFeatureDetector {

	private static final int DEFAULT_NUM_POINTS = 5;

	public static void main(String[] args) throws FileNotFoundException{
	
		int numPoints = DEFAULT_NUM_POINTS;
		
		if (args.length > 0) {
			try {
				numPoints = Integer.parseInt(args[0]);
			}
			catch(NumberFormatException e){
				numPoints = DEFAULT_NUM_POINTS;
			}
		}
		
		List<DigitTrainingPoint> rawData = new ArrayList<>();
	
		rawData = IOUtils.getFirstNTrainingPoints(numPoints);
		
		for (DigitTrainingPoint point : rawData) {
			Grid grid = point.getGrid();
			grid.print();
			// Compute the Grid of Hessians and scale it down.
			Grid rescaledHessian = Hessian.computeHessians(grid).rescale(99);
			rescaledHessian.print();
			
			// Filter the grid by eliminating small values.
			extractFeatures(20, rescaledHessian).print();
		}	
	}
	
	/**
	 * Given a grid, this method will return a new Grid where the only nonzero entries
	 * are the entries of the passed Grid with absolute value at least threshold.
	 */
	private static Grid extractFeatures(int threshold, Grid grid) {	
		return grid.filterSmallValues(threshold);	
	}
}