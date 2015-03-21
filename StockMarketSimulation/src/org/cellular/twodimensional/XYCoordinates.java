package org.cellular.twodimensional;

import org.cellular.cell.coordinates.Coordinates;

/**
 * 2D grid coordinates class.
 * 
 * @author Ignas
 *
 */
public class XYCoordinates implements Coordinates {

	private int x;

	private int y;

	/**
	 * Grid coordinates constructor.
	 * 
	 * @param x
	 *            X value in grid.
	 * @param y
	 *            Y value in grid.
	 */
	public XYCoordinates(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] getCoordinates() {
		return new int[] { x, y };
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
