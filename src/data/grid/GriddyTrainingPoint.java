/**
 * Interface for TrainingPoints whose underlying data is some kind of integral grid.  Certain 
 * classifiers only make sense for this kind of data.
 */
public interface GriddyTrainingPoint extends TrainingPoint {

	/** Return the underlying grid. */
	public Grid getGrid();
	
}