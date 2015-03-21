package org.cellular.gui;

import org.cellular.world.World;

/**
 * World gui interface. Its implementations must be able to show world in its
 * current form. How it does it is left for implementation classes - text,
 * swing, html generation etc.
 * 
 * @author Ignas
 *
 * @param <T>
 *            Type of Cell value.
 */
public interface WorldGui<T> {

	/**
	 * Show world in its current state.
	 * 
	 * @param world World.
	 */
	public void showWorld(World<T> world);

}
