package org.stockmarket;

import java.util.Random;

import org.cellular.cell.SimpleCell;
import org.cellular.cell.coordinates.Coordinates;
import org.cellular.cell.rules.CellularAutomatonRule;
import org.cellular.twodimensional.XYCoordinates;
import org.cellular.world.World;

/**
 * Market participant cell type.
 * 
 * @author Ignas
 *
 */
public class MarketParticipant extends SimpleCell<Action> {

    /** Total capital. Everyone starts with 1M */
    private int totalCapital = 1000000;

    /** Position in number of shares (negative value means short position). */
    private int sharesHold = 0;
    
    /** Position in $. Used to calculate profit/loss (negative value means short position). */
    private int positionSize = 0;
    
    public MarketParticipant(final Action value, final CellularAutomatonRule<Action> rule, final Coordinates cellCoordinates,
            final World<Action> world) {
        super(value, rule, cellCoordinates, world);
    }

    /**
     * Market participant cell method which executes market actions and also updates {@link StockMarketWorld} state.
     */
    public void act(boolean withLimitOrder) {
        final StockMarketWorld stockMarket = (StockMarketWorld) world;
        final int lastPrice = stockMarket.getLastCandle().getClose();
        final Random random = new Random(System.nanoTime());
        
        // buy or sell random amount from 0 to 400 shares
        final int randomNumberOfShares = (random.nextInt(4) + 1) * 100;
        final int numberOfShares = sharesHold != 0 ? Math.abs(sharesHold) : randomNumberOfShares;
        
        if (getValue() == Action.NEUTRAL) {
            int spread = random.nextInt(10) + 4;
            // if market maker undecided he simply bids and offers with big spread
            if (lastPrice > spread) { // if chosen spread is 14 and last price is 15 then we can't put a bid on 0
                stockMarket.placeBid(this, randomNumberOfShares, lastPrice - spread);
            } else {
                stockMarket.placeBid(this, randomNumberOfShares*2, 1); // set bid on minimum price
            }
            stockMarket.placeOffer(this, randomNumberOfShares, lastPrice + spread);
        } else if (withLimitOrder && (getValue() == Action.BUY || getValue() == Action.SELL)) {
            if (getValue() == Action.BUY) {
                int spread = random.nextInt(3) + 1; // first offer @ random spread no more than 4
                System.out.println("Market participant [" + ((XYCoordinates) cellCoordinates).getX() + " "
                        + ((XYCoordinates) cellCoordinates).getY() + "] puts limit buy order for " + numberOfShares + " number of shares");
                stockMarket.placeBid(this, numberOfShares, lastPrice - spread);
            } else if (getValue() == Action.SELL) {
                int spread = random.nextInt(3) + 1; // first offer @ random spread no more than 4
                System.out.println("Market participant [" + ((XYCoordinates) cellCoordinates).getX() + " "
                        + ((XYCoordinates) cellCoordinates).getY() + "] puts limit sell order " + numberOfShares + " number of shares");
                stockMarket.placeOffer(this, numberOfShares, lastPrice + spread);
            }
        } else if (!withLimitOrder && (getValue() == Action.BUY || getValue() == Action.SELL)) {
            // if there is open position then size is of current position size otherwise its random number of shares
            if (getValue() == Action.BUY) {
                System.out.println("Market participant [" + ((XYCoordinates) cellCoordinates).getX() + " "
                        + ((XYCoordinates) cellCoordinates).getY() + "] buys at the market " + numberOfShares + " number of shares");
                stockMarket.buyMarket(this, numberOfShares);
            } else if (getValue() == Action.SELL) {
                System.out.println("Market participant [" + ((XYCoordinates) cellCoordinates).getX() + " "
                        + ((XYCoordinates) cellCoordinates).getY() + "] sells at the market " + numberOfShares + " number of shares");
                stockMarket.sellMarket(this, numberOfShares);
            }
        }
    }

    public void executeBuy(final int nbOfShares, final int transactionSum) {
        totalCapital -= transactionSum;
        sharesHold += nbOfShares;
        if (sharesHold == 0) {
            positionSize = 0;
        } else {
            positionSize += transactionSum; // positionSize positive for buy
        }
    }
    
    public void executeSell(final int nbOfShares, final int transactionSum) {
        totalCapital += transactionSum;
        sharesHold -= nbOfShares;
        if (sharesHold == 0) {
            positionSize = 0;
        } else {
            positionSize -= transactionSum; // positionSize negative for sell
        }
    }
    

    public int getTotalCapital() {
        return totalCapital;
    }

    public int getSharesHold() {
        return sharesHold;
    }

    public int getPositionSize() {
        return positionSize;
    }

}
