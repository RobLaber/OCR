/**
 * Interface for objects that are considered "training points" in the ML sense of the 
 * word.  All training points are expected to have some data that can be classified, 
 * and they also must have a label (aka correct classification).
 */
public interface TrainingPoint extends TestPoint {

	/** Return the label, or correct classification, of this TrainingPoint. */
	public int getLabel();
} 