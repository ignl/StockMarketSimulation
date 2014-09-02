package org.conwaysgameoflife;
import java.io.IOException;

import org.cellular.grid.booleangrid.SimpleBooleanGridWorld;
import org.cellular.grid.booleangrid.TextBooleanGridWorldGui;
import org.cellular.gui.WorldGui;
import org.cellular.twodimensional.XYCoordinates;
import org.cellular.world.World;
import org.conwaysgameoflife.patterns.GosperGliderGunPattern;

public class Main {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		WorldGui<Boolean> gui = new TextBooleanGridWorldGui();
		World<Boolean> world = new SimpleBooleanGridWorld(30, 100, new ConwaysGameOfLifeRule());
//		world.addPattern(new BlinkerPattern(), new GridCoordinates(5, 5));
//		world.addPattern(new BlockPattern(), new GridCoordinates(10, 10));
//		world.addPattern(new ToadPattern(), new GridCoordinates(15, 15));
		world.addPattern(new GosperGliderGunPattern(), new XYCoordinates(5, 5));
		gui.showWorld(world);
		for (int i = 0; i < 1000; i++) {
			Thread.sleep(100);
			world.nextState();
			gui.showWorld(world);
		}
	}

}
