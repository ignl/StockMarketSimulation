package org.conwaysgameoflife.patterns;

import org.cellular.twodimensional.GridPattern;

/**
 * Toad.
 * 
 * @author Ignas
 *
 */
public class ToadPattern extends GridPattern<Boolean> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean[][] get() {
		return new Boolean[][] {{false, true, true, true}, {true, true, true, false}};
	}

}
