package org.cellular.grid.booleangrid;

import org.cellular.cell.Cell;
import org.cellular.cell.SimpleCell;
import org.cellular.cell.rules.CellularAutomatonRule;
import org.cellular.twodimensional.XYCoordinates;
import org.cellular.twodimensional.SimpleTwoDimensionalGrid;

/**
 * Simple and probably most common representation of 2d grid world.
 * 
 * @author Ignas
 *
 */
public class SimpleBooleanGridWorld extends SimpleTwoDimensionalGrid<Boolean> {
	
	/**
	 * Contructor to create new grid world with fixed height and length.
	 */
	public SimpleBooleanGridWorld(int height, int length, CellularAutomatonRule<Boolean> rule) {
		super(height, length, rule);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Cell<Boolean> createNewCell(XYCoordinates coordinates, CellularAutomatonRule<Boolean> rule) {
		return new SimpleCell<Boolean>(false, rule, coordinates, this);
	}

}
