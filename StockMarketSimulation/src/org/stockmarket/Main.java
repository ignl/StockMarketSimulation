package org.stockmarket;

import java.io.IOException;

import org.cellular.gui.WorldGui;
import org.cellular.world.World;

public class Main {

    public static void main(final String[] args) throws IOException, InterruptedException {
        final WorldGui<Action> gui = new SwingStockMarketGui();
        final World<Action> world = new StockMarketWorld(10, 10, new StockMarketAutomatonRule());
        gui.showWorld(world);
        for (int i = 0; i < 100; i++) {
            Thread.sleep(1000);
            world.nextState();
            gui.showWorld(world);
            System.out.println("Iteration " + i);
        }
    }

}
