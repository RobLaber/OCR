/**
 * A DigitTrainingPoint is a simple class to represent a single 'training point' from the 
 * Kaggle Digit data set.  It essentially consists of a 28x28 grid of grayscale pixel 
 * intensities, together with a label, which is the digit that it represents.
 */
public class DigitTrainingPoint extends DigitTestPoint implements GriddyTrainingPoint {

	private final int label;

	public DigitTrainingPoint(Grid grid, int label) {
		super(grid);
		this.label = label;
	}
	
	public int getLabel() {
		return this.label;
	}
}