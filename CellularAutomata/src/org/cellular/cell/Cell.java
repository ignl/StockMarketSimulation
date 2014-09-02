package org.cellular.cell;


/**
 * Interface of basic building block of Cellular automaton - single cell. Each cell contains value and during each 
 * world time iteration each cell is reevaluated according to rules. For example well known Conway's game of life automaton
 * has cell value 1 (alive) only if 2 or 3 neighbors have 1 otherwise cell value is 0 (die of overcrowding or starvation).
 * 
 * @author Ignas
 *
 * @param <T> Type of Cell value.
 */
public interface Cell<T> {
	
	/**
	 * Cell value getter.
	 * 
	 * @return Cell value.
	 */
	T getValue();
	
	/**
	 * On each time iteration world asks each cell for value reevaluation. Each cell is "smart" and can figure out what its next value should be.
	 */
	void revaluateCell();

	/**
	 * After asking all cells for reevaluation world commits new values for each cell after the iteration. 
	 * After this method is invoked, revalued values are set as new values for Cell.
	 */
	void commitRevaluation();
	
}
