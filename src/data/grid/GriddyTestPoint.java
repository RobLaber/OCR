/**
 * Interface for TestPoints whose underlying data is some kind of integral grid.  Certain 
 * classifiers only make sense for this kind of data.
 */
public interface GriddyTestPoint extends TestPoint {

	/** Return the underlying grid. */
	public Grid getGrid();
	
}