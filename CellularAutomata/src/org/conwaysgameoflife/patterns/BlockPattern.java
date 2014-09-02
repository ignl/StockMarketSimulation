package org.conwaysgameoflife.patterns;

import org.cellular.twodimensional.GridPattern;

/**
 * Block.
 * 
 * @author Ignas
 *
 */
public class BlockPattern extends GridPattern<Boolean> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean[][] get() {
		return new Boolean[][] {{true, true}, {true, true}};
	}

}
