package org.conwaysgameoflife;

import org.cellular.cell.Cell;
import org.cellular.cell.SimpleCell;
import org.cellular.cell.coordinates.Coordinates;
import org.cellular.cell.rules.CellularAutomatonRule;
import org.cellular.grid.booleangrid.SimpleBooleanGridWorld;
import org.cellular.twodimensional.XYCoordinates;
import org.cellular.twodimensional.SimpleTwoDimensionalGrid;
import org.cellular.world.World;

/**
 * Conway's game of life rule. If cell's 2 or 3 neighbors is true then cell
 * becomes true also, otherwise cell is false (or alive and dead because of
 * starvation or overcrowding).
 * 
 * @author Ignas
 *
 */
public class ConwaysGameOfLifeRule implements CellularAutomatonRule<Boolean> {
	
	private static Cell<Boolean> EMPTY_CELL = new SimpleCell<Boolean>(false, null, null, null);

	/**
	 * {@inheritDoc}
	 * 
	 * Conway's game of life rules:
	 *  
     * 1. Any live cell with fewer than two live neighbours dies, as if caused by under-population.
     * 2. Any live cell with two or three live neighbours lives on to the next generation.
     * 3. Any live cell with more than three live neighbours dies, as if by overcrowding.
     * 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
	 *
	 */
	@Override
	public Boolean calculateNewValue(Boolean currentValue, World<Boolean> world, Coordinates coordinates) {
		XYCoordinates gridCoordinates = (XYCoordinates)coordinates;
		SimpleTwoDimensionalGrid<Boolean> gridWorld = (SimpleBooleanGridWorld)world;
		int sum = (getUpperNeighbor(gridWorld, gridCoordinates).getValue() ? 1 : 0) + (getLowerNeighbor(gridWorld, gridCoordinates).getValue() ? 1 : 0)
				+ (getLeftNeighbor(gridWorld, gridCoordinates).getValue() ? 1 : 0) + (getRightNeighbor(gridWorld, gridCoordinates).getValue() ? 1 : 0)
				+ (getUpperLeftNeighbor(gridWorld, gridCoordinates).getValue() ? 1 : 0) + (getUpperRightNeighbor(gridWorld, gridCoordinates).getValue() ? 1 : 0)
				+ (getLowerLeftNeigbhor(gridWorld, gridCoordinates).getValue() ? 1 : 0) + (getLowerRightNeigbhor(gridWorld, gridCoordinates).getValue() ? 1 : 0);
		return currentValue ? sum == 2 || sum == 3 : sum == 3;
	}
	
	private Cell<Boolean> getUpperNeighbor(SimpleTwoDimensionalGrid<Boolean> world, XYCoordinates coordinates) {
		int neighborX = coordinates.getX();
		int neighborY = coordinates.getY() - 1;
		return (neighborY >= 0) ? world.getCell(new XYCoordinates(neighborX, neighborY)) : EMPTY_CELL;
	}

	private Cell<Boolean> getLowerNeighbor(SimpleTwoDimensionalGrid<Boolean> world, XYCoordinates coordinates) {
		int neighborX = coordinates.getX();
		int neighborY = coordinates.getY() + 1;
		return (neighborY < world.getHeight()) ? world.getCell(new XYCoordinates(neighborX, neighborY)) : EMPTY_CELL;
	}

	private Cell<Boolean> getLeftNeighbor(SimpleTwoDimensionalGrid<Boolean> world, XYCoordinates coordinates) {
		int neighborX = coordinates.getX() - 1;
		int neighborY = coordinates.getY();
		return (neighborX >= 0) ? world.getCell(new XYCoordinates(neighborX, neighborY)) : EMPTY_CELL;
	}

	private Cell<Boolean> getRightNeighbor(SimpleTwoDimensionalGrid<Boolean> world, XYCoordinates coordinates) {
		int neighborX = coordinates.getX() + 1;
		int neighborY = coordinates.getY();
		return (neighborX < world.getLenght()) ? world.getCell(new XYCoordinates(neighborX, neighborY)) : EMPTY_CELL;
	}

	private Cell<Boolean> getUpperLeftNeighbor(SimpleTwoDimensionalGrid<Boolean> world, XYCoordinates coordinates) {
		int neighborX = coordinates.getX() - 1;
		int neighborY = coordinates.getY() - 1;
		return (neighborX >= 0 && neighborY >= 0) ? world.getCell(new XYCoordinates(neighborX, neighborY)) : EMPTY_CELL;
	}

	private Cell<Boolean> getUpperRightNeighbor(SimpleTwoDimensionalGrid<Boolean> world, XYCoordinates coordinates) {
		int neighborX = coordinates.getX() + 1;
		int neighborY = coordinates.getY() - 1;
		return (neighborX < world.getLenght() && neighborY >= 0) ? world.getCell(new XYCoordinates(neighborX, neighborY)) : EMPTY_CELL;
	}

	private Cell<Boolean> getLowerLeftNeigbhor(SimpleTwoDimensionalGrid<Boolean> world, XYCoordinates coordinates) {
		int neighborX = coordinates.getX() - 1;
		int neighborY = coordinates.getY() + 1;
		return (neighborX >= 0 && neighborY < world.getHeight()) ? world.getCell(new XYCoordinates(neighborX, neighborY)) : EMPTY_CELL;
	}

	private Cell<Boolean> getLowerRightNeigbhor(SimpleTwoDimensionalGrid<Boolean> world, XYCoordinates coordinates) {
		int neighborX = coordinates.getX() + 1;
		int neighborY = coordinates.getY() + 1;
		return (neighborX < world.getLenght() && neighborY < world.getHeight()) ? world.getCell(new XYCoordinates(neighborX, neighborY)) : EMPTY_CELL;
	}

}
