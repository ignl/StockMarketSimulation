package org.stockmarket;

import org.cellular.twodimensional.XYCoordinates;
import org.cellular.world.World;
import org.junit.Assert;
import org.junit.Test;

public class MarketParticipantTest {
    
    @Test
    public void actWithLimitOrderTest() {
        StockMarketWorld world = new StockMarketWorld(3, 3, new StockMarketAutomatonRule());
        MarketParticipant marketParticipant = createNewMarketParticipant(world);
        
        marketParticipant.act(true);
    }
    
    @Test
    public void executeBuyAndSellTest() {
        StockMarketWorld world = new StockMarketWorld(10, 10, new StockMarketAutomatonRule());
        MarketParticipant marketParticipant = createNewMarketParticipant(world);
        
        marketParticipant.executeBuy(100, 1200);
        Assert.assertEquals(100, marketParticipant.getSharesHold());
        Assert.assertEquals(1200, marketParticipant.getPositionSize());
        Assert.assertEquals(998800, marketParticipant.getTotalCapital());

        marketParticipant.executeBuy(100, 1200);
        Assert.assertEquals(200, marketParticipant.getSharesHold());
        Assert.assertEquals(2400, marketParticipant.getPositionSize());
        Assert.assertEquals(997600, marketParticipant.getTotalCapital());

        marketParticipant.executeBuy(100, 1000);
        Assert.assertEquals(300, marketParticipant.getSharesHold());
        Assert.assertEquals(3400, marketParticipant.getPositionSize());
        Assert.assertEquals(996600, marketParticipant.getTotalCapital());
        
        marketParticipant.executeSell(100, 900);
        Assert.assertEquals(200, marketParticipant.getSharesHold());
        Assert.assertEquals(2500, marketParticipant.getPositionSize());
        Assert.assertEquals(997500, marketParticipant.getTotalCapital());

        marketParticipant.executeSell(200, 2000);
        Assert.assertEquals(0, marketParticipant.getSharesHold());
        Assert.assertEquals(0, marketParticipant.getPositionSize());
        Assert.assertEquals(999500, marketParticipant.getTotalCapital());

        marketParticipant.executeSell(100, 1500);
        Assert.assertEquals(-100, marketParticipant.getSharesHold());
        Assert.assertEquals(-1500, marketParticipant.getPositionSize());
        Assert.assertEquals(1001000, marketParticipant.getTotalCapital());
        
        
    }
    
    private MarketParticipant createNewMarketParticipant(World<Action> world) {
        return new MarketParticipant(Action.NEUTRAL, new StockMarketAutomatonRule(), new XYCoordinates(1, 1), world);
    }

}
