package org.cellular.cell.rules;

import org.cellular.cell.coordinates.Coordinates;
import org.cellular.world.World;

/**
 * Core interface of automaton rules. This interface implementation eventually
 * define what kind of cellular automaton it is.
 * 
 * @author Ignas
 *
 * @param <T> Type of Cell value.
 */
public interface CellularAutomatonRule<T> {

	/**
	 * Calculate next iteration individual cell value method.
	 * 
	 * @param currentValue
	 *            Current cell value.
	 * @param grid
	 *            Grid of cells. From it and coordinates Cell neighbors is
	 *            found and rule of new iteration Cell value is applied.
	 * @param coordinates
	 *            Cell coordinates in the grid.
	 * 
	 * @return Cell value of the next iteration.
	 */
	T calculateNewValue(T currentValue, World<T> grid, Coordinates coordinates);

}
