package org.stockmarket;

import java.lang.reflect.Field;

import org.cellular.cell.Cell;
import org.cellular.twodimensional.SimpleTwoDimensionalGrid;
import org.cellular.twodimensional.XYCoordinates;
import org.junit.Assert;
import org.junit.Test;

public class StockMarketAutomatonRuleTest {
    
    @Test
    public void getNumberOfNeighborsTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        StockMarketAutomatonRule rule = new StockMarketAutomatonRule();
        StockMarketWorld world = new StockMarketWorld(3, 3, rule);
        
        Cell<Action>[][] worldGrid = new MarketParticipant[3][3];
        
        worldGrid[0][0] = createNewMarketParticipant(world, rule, Action.SELL, 0, 0);
        worldGrid[0][1] = createNewMarketParticipant(world, rule, Action.SELL, 0, 1);
        worldGrid[0][2] = createNewMarketParticipant(world, rule, Action.BUY, 0, 2);
        worldGrid[1][0] = createNewMarketParticipant(world, rule, Action.BUY, 1, 0);
        worldGrid[1][1] = createNewMarketParticipant(world, rule, Action.NEUTRAL, 1, 1);
        worldGrid[1][2] = createNewMarketParticipant(world, rule, Action.BUY, 1, 2);
        worldGrid[2][0] = createNewMarketParticipant(world, rule, Action.SELL, 2, 0);
        worldGrid[2][1] = createNewMarketParticipant(world, rule, Action.NEUTRAL, 2, 1);
        worldGrid[2][2] = createNewMarketParticipant(world, rule, Action.BUY, 2, 2);
        
        Field worldGridField = SimpleTwoDimensionalGrid.class.getDeclaredField("worldGrid");
        worldGridField.setAccessible(true);
        
        worldGridField.set(world, worldGrid);
        
        Assert.assertEquals(4, rule.getNumberOfBulishNeighbors(world, new XYCoordinates(1, 1)));
        Assert.assertEquals(3, rule.getNumberOfBearishNeighbors(world, new XYCoordinates(1, 1)));
    }

    private MarketParticipant createNewMarketParticipant(StockMarketWorld world, StockMarketAutomatonRule rule, Action action, int x, int y) {
        return new MarketParticipant(action, rule, new XYCoordinates(x, y), world);
    }
}
