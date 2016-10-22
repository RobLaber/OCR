/**
 * A DigitTestPoint is a simple class to represent a single 'test point' from the Kaggle 
 * Digit data set.  It essentially consists of a 28x28 grid of grayscale pixel intensities.
 */
public class DigitTestPoint implements GriddyTestPoint {

	private final Grid grid;

	public DigitTestPoint(Grid grid) {
		this.grid = grid;
	}
	
	public Grid getGrid() {
		return grid;
	}
}