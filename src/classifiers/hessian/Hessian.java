
/**
 *  Class to compute the discrete Hessian of an integral grid.
 */
 
public final class Hessian {

	/** Compute the Hessian at each point, and return the resulting grid. */
	public static Grid computeHessians(Grid original) {
	
		int[][] hessians = new int[original.width()][original.height()];
		for (int i = 0; i < original.width(); i++) {
			for (int j = 0; j < original.height(); j++) {
				hessians[i][j] = hessianAtPoint(original, i, j);
			}
		}
		return new Grid(hessians);
	}
	
	
	/** Compute the Hessian of a grid at a pair of indices i, j.  Returns 0 for boundary points. */
	private static int hessianAtPoint(Grid grid, int i, int j) {
	
		if (grid.boundaryContains(i, j)) {
			return 0;
		}
		
		int dxdx = dxdx(grid, i, j);
		int dydy = dydy(grid, i, j); 
		int dydx = dydx(grid, i, j);
		
		return dxdx * dydy - (dydx * dydx);
	
	}
	
	/** Compute the second order partial wrt i. This assumes that i, j is an interior point on grid. */
	private static int dxdx(Grid grid, int i, int j) {
	  return grid.valueAt(i + 1, j) + grid.valueAt(i - 1, j) - 2 * grid.valueAt(i, j);
	}
	
	/** Compute the second order partial wrt j. This assumes that i, j is an interior point on grid. */
	private static int dydy(Grid grid, int i, int j) {
		return grid.valueAt(i, j + 1) + grid.valueAt(i, j - 1) - 2 * grid.valueAt(i, j); 
	}
	
	/** Compute the first order partial wrt i.  This assumes that i, j is an interior point on grid. */
	private static int dx(Grid grid, int i, int j) {
		return (grid.valueAt(i + 1, j) - grid.valueAt(i - 1, j)) / 2;
	}

	/** Compute the mixed second order partial.  This assumes that i, j is an interior point on grid. */
	private static int dydx(Grid grid, int i, int j) {
		return (dx(grid, i, j + 1) - dx(grid, i, j - 1)) / 2;
	}

}