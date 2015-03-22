package org.cellular.cell.world;

import org.cellular.twodimensional.XYCoordinates;
import org.cellular.world.World;
import org.junit.Assert;
import org.junit.Test;
import org.stockmarket.Action;
import org.stockmarket.MarketParticipant;
import org.stockmarket.StockMarketAutomatonRule;
import org.stockmarket.StockMarketWorld;
import org.stockmarket.StockMarketWorld.Candle;

public class StockMarketWorldTest {
    
    @Test
    public void getCandleTest() {
        StockMarketWorld world = new StockMarketWorld(10, 10, new StockMarketAutomatonRule());
        MarketParticipant marketParticipant = createNewMarketParticipant(world);
        world.placeBid(marketParticipant, 1000, 97);
        world.placeBid(marketParticipant, 900, 96);
        world.placeBid(marketParticipant, 1200, 98);

        world.placeOffer(marketParticipant, 1000, 101);
        world.placeOffer(marketParticipant, 1200, 103);
        world.placeOffer(marketParticipant, 900, 102);
        
        world.buyMarket(marketParticipant, 900);
        world.buyMarket(marketParticipant, 900);
        world.buyMarket(marketParticipant, 900);
        world.sellMarket(marketParticipant, 900);
        world.sellMarket(marketParticipant, 900);
        
        
        Candle lastCandle = world.getLastCandle();
        Assert.assertEquals(lastCandle.getOpen(), 100);
        Assert.assertEquals(lastCandle.getHigh(), 103);
        Assert.assertEquals(lastCandle.getLow(), 97);
        Assert.assertEquals(lastCandle.getClose(), 97);
    }
    
    @Test
    public void placeBidTest() {
        StockMarketWorld world = new StockMarketWorld(10, 10, new StockMarketAutomatonRule());
        MarketParticipant marketParticipant = createNewMarketParticipant(world);
        world.placeBid(marketParticipant, 1000, 100);
        world.placeBid(marketParticipant, 1200, 101);
        world.placeBid(marketParticipant, 900, 99);
        
        world.sellMarket(marketParticipant, 1200);
        
        int lastPrice = world.getLastPrice();
        Assert.assertEquals(101, lastPrice);

        world.sellMarket(marketParticipant, 1000);
        
        lastPrice = world.getLastPrice();
        Assert.assertEquals(100, lastPrice);

        world.sellMarket(marketParticipant, 900);
        
        lastPrice = world.getLastPrice();
        Assert.assertEquals(99, lastPrice);
    }

    @Test
    public void placeOfferTest() {
        StockMarketWorld world = new StockMarketWorld(10, 10, new StockMarketAutomatonRule());
        MarketParticipant marketParticipant = createNewMarketParticipant(world);
        world.placeOffer(marketParticipant, 1000, 100);
        world.placeOffer(marketParticipant, 1200, 101);
        world.placeOffer(marketParticipant, 900, 99);
        
        world.buyMarket(marketParticipant, 900);
        
        int lastPrice = world.getLastPrice();
        Assert.assertEquals(99, lastPrice);

        world.buyMarket(marketParticipant, 1000);
        
        lastPrice = world.getLastPrice();
        Assert.assertEquals(100, lastPrice);

        world.buyMarket(marketParticipant, 200);
        
        lastPrice = world.getLastPrice();
        Assert.assertEquals(101, lastPrice);
        
    }
    
    private MarketParticipant createNewMarketParticipant(World<Action> world) {
        return new MarketParticipant(Action.NEUTRAL, new StockMarketAutomatonRule(), new XYCoordinates(1, 1), world);
    }

}
