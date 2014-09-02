package org.conwaysgameoflife.patterns;

import org.cellular.twodimensional.GridPattern;

/**
 * Blinker.
 * 
 * @author Ignas
 *
 */
public class BlinkerPattern extends GridPattern<Boolean> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean[][] get() {
		return new Boolean[][] {{false, false, false}, {true, true, true}, {false, false, false}};
	}

}
