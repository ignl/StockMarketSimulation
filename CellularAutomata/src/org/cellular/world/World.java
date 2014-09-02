package org.cellular.world;

import org.cellular.cell.Cell;
import org.cellular.cell.coordinates.Coordinates;
import org.cellular.pattern.Pattern;

/**
 * Root interface that represents all types of cell grids/worlds. Usually its 2d grid of cells, but can be any dimension.
 * World contains cells and is able to iterate its states. World state changes depending on {@link CellularAutomatonRule}s that world cells contains.
 * 
 * @author Ignas
 * 
 * @param <T> Cell value type.
 *
 */
public interface World<T> {
	
	/**
	 * Iterate world to its next state in time.
	 */
	public void nextState();
	
	/**
	 * Add a pattern to the world by hand.
	 * 
	 * @param pattern Pattern to add.
	 * @param coordinates Coordinates where pattern must be added.
	 */
	public void addPattern(Pattern pattern, Coordinates coordinates);
	
	/**
	 * Get cell by its coordinates.
	 */
	public Cell<T> getCell(Coordinates coordinates);

}
