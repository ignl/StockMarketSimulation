package org.cellular.cell.coordinates;

/**
 * Cell coordinates interface. Usually its 2d coordinates (x and y) but this
 * interface makes it flexible to use any dimension of cells. 2D coordinates are implemented in {@link 2DCoordinates}
 * 
 * @author Ignas
 *
 */
public interface Coordinates {

	/**
	 * Get Cell coordinates.
	 */
	int[] getCoordinates();
	
}
