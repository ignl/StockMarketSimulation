package org.cellular.twodimensional;

import org.cellular.pattern.Pattern;

/**
 * Pattern in 2D grid abstract class.
 * 
 * @author Ignas
 *
 * @param <T> Cell value type.
 */
public abstract class GridPattern<T> implements Pattern {
	
	/**
	 * {@inheritDoc}
	 */
	public abstract T[][] get();

}
