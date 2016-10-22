/**
 * Interface for objects that can classify a data point given a set of training points.
 *
 * Currently all labels are ints, but eventually we can make this use some generics so that
 * labels can be any type.
 */
 
import java.util.*;
 
public interface Classifier {

	/** Given a set of TrainingPoints and a TestPoint, classify the test point. */
	public int classify(TestPoint pointToTest, Iterable<TrainingPoint> trainingSet);

}