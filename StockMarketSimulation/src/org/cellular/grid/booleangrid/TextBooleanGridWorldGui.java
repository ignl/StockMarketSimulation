package org.cellular.grid.booleangrid;

import org.cellular.gui.WorldGui;
import org.cellular.twodimensional.XYCoordinates;
import org.cellular.world.World;

/**
 * Text GUI for {@link SimpleBooleanGridWorld}
 * 
 * @author Ignas
 *
 */
public class TextBooleanGridWorldGui implements WorldGui<Boolean> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showWorld(World<Boolean> world) {
		SimpleBooleanGridWorld booleanGridWorld = (SimpleBooleanGridWorld)world;
		for (int y = 0; y < booleanGridWorld.getHeight(); y++) {
			for (int x = 0; x < booleanGridWorld.getLenght(); x++) {
				System.out.print(booleanGridWorld.getCell(new XYCoordinates(x, y)).getValue() ? "@" : "-");
			}
			System.out.println();
		}
		for (int y = 0; y < booleanGridWorld.getHeight(); y++) { // print empty lines to separate different states (if its large enough it will create animation for changing states)
			System.out.println();
		}
	}

}
