
/**
  * Basic class to represent an integral grid.  This also provides some additional 
  * functionality for filtering values.
  */
import java.util.*;  
import java.lang.*;
  
public class Grid {

	private int[][] values;
	private int height;
	private int width;
	
	// Hang onto the min/max values in the grid for easy rescaling.
	private int minValueInGrid;
	private int maxValueInGrid;  
	
	/** Constructor. */
	public Grid(int[][] values) {
		this.values = values;
		this.height = values[0].length;
		this.width = values.length;
		
		int maxValue = Integer.MIN_VALUE;
		int minValue = Integer.MAX_VALUE;
		
		// Get the max and min values.
		for (int i = 0 ; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {		
				if (values[i][j] > maxValue) {
					maxValue = values[i][j];
				}
				if (values[i][j] < minValue) {
					minValue = values[i][j];
				}
			}
		}
		this.maxValueInGrid = maxValue;
		this.minValueInGrid = minValue;
	}
	
	/** 
	 * Constructor that specifies a height, width, and is given a List of numeric values.
	 * The length of the list must equal the product of the hight and width.  The grid is populated
	 * in a left to right, top to bottom fashion.
	 */
	public Grid(int height, int width, List<Integer> values) {
		
		assert(height * width == values.size());
		
		int[][] grid = new int[width][height];
		
		// The current index as we run through the list.
		int currentIndex = 0;
		
		// Compute the min/max values in the grid as we go.
		int maxValue = Integer.MIN_VALUE;
		int minValue = Integer.MAX_VALUE;
		
		// Populate the grid and find the extreme values.
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int currentValue = values.get(currentIndex);
				grid[i][j] = currentValue;
				if (currentValue > maxValue) {
					maxValue = currentValue;
				}
				if (currentValue < minValue) {
					minValue = currentValue;
				}		
				currentIndex++;
			}
		}
		
		this.height = height;
		this.width = width;
		this.maxValueInGrid = maxValue;
		this.minValueInGrid = minValue;
		this.values = grid;
	} 
	 
	/** Getters. */
	public int width() {
		return this.width;
	}
	
	public int height() {
		return this.height;
	}
	
	public int valueAt(int i, int j) {
		assert(i >= 0  && i <width());
		assert(j >= 0 && j < height());
		
		return values[i][j];
	}
	
	public int valueAt(Index index) {
		return valueAt(index.i, index.j);
	}
	
	/** Check if the indicies i, j represent an 'interior' point in a Grid. */
	public boolean interiorContains(int i, int j) {
		return i > 0 && j > 0 && i < width() - 1 && j < height() - 1;
	}
	
	/** Check if the indices i, j represent a 'boundary' point in a Grid. */
	public boolean boundaryContains(int i, int j) {
		return !interiorContains(i, j);
	}
	
	/** 
	 * This method will return a new grid that is a scaled version of the original.  The 
	 * scaled grid will be such that the absolute values of all of its entries will be 
	 * less than or equal to maxValue.
	 *
	 * @throws IllegalArgumentException If maxValue is negative.
	 */
	public Grid rescale(int maxValue) {
		
		if (maxValue <= 0) {
			throw new IllegalArgumentException("maxValue must be positive.");
		}
		
		// The idea is to scale each entry by maxValue / denominator.  That means denominator must be
		// equal to the max of the absolute values of min/maxValueInGrid.
		int denominator = Math.max(Math.abs(minValueInGrid), Math.abs(maxValueInGrid));
		
		int[][] scaledValues = new int[width()][height()];
		
		for (int i = 0; i < scaledValues.length; i++) {
			for (int j = 0; j < scaledValues[0].length; j++) {
				scaledValues[i][j] = (valueAt(i, j) * maxValue) / denominator;
			}
		}
		return new Grid(scaledValues);	
	}
	
	/**
	 * Filter this grid by setting all values to 0 unless they have absolute value at 
	 * least threshold.  This returns a new Grid.
	 */
	public Grid filterSmallValues(int threshold) {
	
		assert(threshold > 0);
		
		int[][] newGrid = new int[width()][height()];
		
		for (int j = 0; j < height(); j++) {
			for (int i = 0; i < width(); i++) {
				if (Math.abs(valueAt(i, j)) < threshold) {
					newGrid[i][j] = 0;
				}
				else {
					newGrid[i][j] = valueAt(i, j);
				}
			}
		}
		return new Grid(newGrid);
	} 
	
	/**
	 * Print a Grid to the screen.
	 */
	public void print() {
		
		for (int j = 0; j < height(); j++) {
			for (int i = 0; i < width(); i++) {
				System.out.printf("%3d ", valueAt(i, j));
			}
			System.out.printf("\n");
		}
		System.out.printf("\n");	
	}
	
	
	/**
	 * Extract the indices of the values in the grid whose absolute value is at least
	 * threshold.
	 */
	public List<Grid.Index> extractFeatureIndices(int threshold) {
	
		List<Grid.Index> indices = new ArrayList<>();
		
		for (int i = 0 ; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				if (Math.abs(valueAt(i, j)) >= threshold) {
					indices.add(new Grid.Index(i, j));
				}
			}
		}
		return indices;
	}
	
	/**
	 * Inner class to represent an 'index' in a grid.  It is annoying to have to always pass around
	 * two values when specifying locations in a grid, and this class helps make that less
	 * unpleasant.
	 */
	 public static final class Index {
	 
	 	private int i;
	 	private int j;
	 	
	 	public Index(int i, int j) {
	 		this.i = i;
	 		this.j = j;
	 	}
	 	
	 	/**
	 	 * Is this index a within 'dist' of the other index?  More precisely, this 
	 	 * method will return true if both other.i and other.j are no more that dist away
	 	 * from this.i and this.j, respectively.
	 	 */
	 	public boolean neighborOf(Index other, int dist) {
	 		
	 		assert(dist >= 0);
	 	
	 		return Math.abs(other.i - this.i) <= dist && Math.abs(other.j - this.j) <= dist;
	 	} 
	 	
	 	/**
	 	 * It is useful to print these out sometimes.
	 	 */
	 	public String toString() {
	 		return "(" + i + ", " + j + ")";
	 	}
	 }
}