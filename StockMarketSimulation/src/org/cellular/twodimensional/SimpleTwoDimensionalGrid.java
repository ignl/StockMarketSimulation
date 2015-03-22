package org.cellular.twodimensional;

import org.cellular.cell.Cell;
import org.cellular.cell.SimpleCell;
import org.cellular.cell.coordinates.Coordinates;
import org.cellular.cell.rules.CellularAutomatonRule;
import org.cellular.pattern.Pattern;
import org.cellular.world.World;

/**
 * Simple and probably most common representation of 2d grid world.
 * 
 * @author Ignas
 *
 */
public abstract class SimpleTwoDimensionalGrid<T> implements World<T> {

	/** Array of arrays which represents 2D grid of Cells. */
	protected Cell<T>[][] worldGrid = null;
	
	/** Grid height. */
	private int height;

	/** Grid lenght. */
	private int lenght;
	
	private CellularAutomatonRule<T> rule;

	/**
	 * Constructor to create specified size grid world.
	 * 
	 * @param height
	 *            Grid height.
	 * @param length
	 *            Grid length.
	 */
	@SuppressWarnings("unchecked")
	public SimpleTwoDimensionalGrid(int height, int length, CellularAutomatonRule<T> rule) {
		super();
		this.height = height;
		this.lenght = length;
		this.rule = rule;
		this.worldGrid = new Cell[height][lenght];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < lenght; x++) {
				worldGrid[y][x] = createNewCell(new XYCoordinates(x, y), rule);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void nextState() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < lenght; j++) {
				worldGrid[i][j].revaluateCell();
			}
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < lenght; j++) {
				worldGrid[i][j].commitRevaluation();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Cell<T> getCell(Coordinates coordinates) {
		XYCoordinates gridCoordinates = (XYCoordinates) coordinates;
		return worldGrid[gridCoordinates.getY()][gridCoordinates.getX()];
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPattern(Pattern pattern, Coordinates coordinates) {
		@SuppressWarnings("unchecked")
		T[][] patternValues = ((GridPattern<T>)pattern).get();
		XYCoordinates gridCoordinates = ((XYCoordinates)coordinates);
		for (int y = gridCoordinates.getY(); y < getHeight() && y < gridCoordinates.getY() + patternValues.length; y++) {
			for (int x = gridCoordinates.getX(); x < getLenght() && x < gridCoordinates.getX() + patternValues[0].length; x++) {
				worldGrid[y][x] = new SimpleCell<T>(patternValues[y - gridCoordinates.getY()][x - gridCoordinates.getX()], rule, new XYCoordinates(x, y), this);
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public int getLenght() {
		return lenght;
	}
	
	/**
	 * This is template method to create new cell. Each subclass of
	 * {@link SimpleTwoDimensionalGrid} will implement it and will create its own type of
	 * cell. This method is then used in when constructing new world in constructor.
	 */
	protected abstract Cell<T> createNewCell(XYCoordinates coordinates, CellularAutomatonRule<T> rule);
}
