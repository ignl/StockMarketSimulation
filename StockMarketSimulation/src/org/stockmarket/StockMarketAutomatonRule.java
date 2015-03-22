package org.stockmarket;

import org.cellular.cell.coordinates.Coordinates;
import org.cellular.cell.rules.CellularAutomatonRule;
import org.cellular.twodimensional.SimpleTwoDimensionalGrid;
import org.cellular.twodimensional.XYCoordinates;
import org.cellular.world.World;

/**
 * If market participant does not have position, then it buys at the market if there are more than 3 bullish neighbors. 
 * If only 1 to 3 bullish neighbors then it uses limit buy order. Same with selling.
 * If he has long position then he will use limit order to exit it if there are more than 5 bearish neighbors. If neighbors are
 * not bearish then it exit position if he has enough profit or too much loss on it. Same goes with short positions.
 * 
 * @author Ignas
 *
 */
public class StockMarketAutomatonRule implements CellularAutomatonRule<Action> {

    private static MarketParticipant EMPTY_CELL = new MarketParticipant(Action.NEUTRAL, null, null, null);

    /**
     * {@inheritDoc}
     */
    @Override
    public Action calculateNewValue(final Action currentValue, final World<Action> world, final Coordinates coordinates) {
        final XYCoordinates gridCoordinates = (XYCoordinates) coordinates;
        final StockMarketWorld marketWorld = (StockMarketWorld) world;

        final MarketParticipant marketParticipant = (MarketParticipant) world.getCell(new XYCoordinates(gridCoordinates.getX(),
                gridCoordinates.getY()));
        
        final int bullishNeighbors = getNumberOfBulishNeighbors(marketWorld, gridCoordinates);
        final int bearishNeighbors = getNumberOfBearishNeighbors(marketWorld, gridCoordinates);


        if (marketParticipant.getSharesHold() == 0) {
            return calculateActionFromNeighbors(marketWorld, gridCoordinates, bullishNeighbors, bearishNeighbors);
        } else {
            int currentPositionValue = marketParticipant.getSharesHold() * marketWorld.getLastCandle().getClose();
            int profitLoss = marketParticipant.getPositionSize() - currentPositionValue;
            double profitLossPrc = (double)(profitLoss * 100)/(double)currentPositionValue;
            if (marketParticipant.getSharesHold() > 0) {  // if already long
                if (marketWorld.getLastCandle().getClose() == marketWorld.getCandles().get(marketWorld.getCandles().size() - 2).getClose()) {
                    return Action.SELL; // if closed same twice in a row - exit position
                } else if (bearishNeighbors >= 4) {
                    return Action.SELL;
                } else if (profitLossPrc > 6) {
                    return Action.SELL;
                } else if (profitLossPrc < -2) {
                    return Action.SELL;
                }
                    
            } else { // if already short
                if (marketWorld.getLastCandle().getClose() == marketWorld.getCandles().get(marketWorld.getCandles().size() - 2).getClose()) {
                    return Action.BUY; // if closed same twice in a row - exit position
                } else if (bullishNeighbors >= 4) {
                    return Action.BUY;
                } else if (profitLossPrc > 6) {
                    return Action.BUY;
                } else if (profitLossPrc < -2) {
                    return Action.BUY;
                }
            }
        }
        
        return Action.NEUTRAL;
    }
    
    private Action calculateActionFromNeighbors(StockMarketWorld marketWorld, XYCoordinates gridCoordinates, int bullishNeighbors, int bearishNeighbors) {
        
        boolean between3and7Bulish = 3 <= bullishNeighbors && bullishNeighbors <= 7;
        boolean between3and7Bearish = 3 <= bearishNeighbors && bearishNeighbors <= 7;
        
        if (between3and7Bearish && between3and7Bulish) {// if have both 4 bullish and 4 bearish neighbors then be neutral
            return Action.NEUTRAL;
        } else if (between3and7Bulish || bearishNeighbors > 7) {// if more than 7 bearish - take contrarian view and buy
            return Action.BUY;
        } else if (between3and7Bearish || bullishNeighbors > 7) {// if more than 7 bullish - take contrarian view and sell
            return Action.SELL;
        } else {
            return Action.NEUTRAL;
        }
    }

    protected int getNumberOfBulishNeighbors(final StockMarketWorld world, final XYCoordinates coordinates) {
        return (isBuying(getUpperNeighbor(world, coordinates)) ? 1 : 0)
                + (isBuying(getLowerNeighbor(world, coordinates)) ? 1 : 0)
                + (isBuying(getLeftNeighbor(world, coordinates)) ? 1 : 0)
                + (isBuying(getRightNeighbor(world, coordinates)) ? 1 : 0)
                + (isBuying(getUpperLeftNeighbor(world, coordinates)) ? 1 : 0)
                + (isBuying(getUpperRightNeighbor(world, coordinates)) ? 1 : 0)
                + (isBuying(getLowerLeftNeigbhor(world, coordinates)) ? 1 : 0)
                + (isBuying(getLowerRightNeigbhor(world, coordinates)) ? 1 : 0);
    }

    protected int getNumberOfBearishNeighbors(final StockMarketWorld world, final XYCoordinates coordinates) {
        return (isSelling(getUpperNeighbor(world, coordinates)) ? 1 : 0)
                + (isSelling(getLowerNeighbor(world, coordinates)) ? 1 : 0)
                + (isSelling(getLeftNeighbor(world, coordinates)) ? 1 : 0)
                + (isSelling(getRightNeighbor(world, coordinates)) ? 1 : 0)
                + (isSelling(getUpperLeftNeighbor(world, coordinates)) ? 1 : 0)
                + (isSelling(getUpperRightNeighbor(world, coordinates)) ? 1 : 0)
                + (isSelling(getLowerLeftNeigbhor(world, coordinates)) ? 1 : 0)
                + (isSelling(getLowerRightNeigbhor(world, coordinates)) ? 1 : 0);
    }
    
    private boolean isBuying(MarketParticipant participant) {
        return participant.getValue() == Action.BUY;
    }

    private boolean isSelling(MarketParticipant participant) {
        return participant.getValue() == Action.SELL;
    }

    private MarketParticipant getUpperNeighbor(final SimpleTwoDimensionalGrid<Action> world, final XYCoordinates coordinates) {
        final int neighborX = coordinates.getX();
        final int neighborY = coordinates.getY() - 1;
        return neighborY >= 0 ? (MarketParticipant) world.getCell(new XYCoordinates(neighborX, neighborY))
                : StockMarketAutomatonRule.EMPTY_CELL;
    }

    private MarketParticipant getLowerNeighbor(final SimpleTwoDimensionalGrid<Action> world, final XYCoordinates coordinates) {
        final int neighborX = coordinates.getX();
        final int neighborY = coordinates.getY() + 1;
        return neighborY < world.getHeight() ? (MarketParticipant) world.getCell(new XYCoordinates(neighborX, neighborY))
                : StockMarketAutomatonRule.EMPTY_CELL;
    }

    private MarketParticipant getLeftNeighbor(final SimpleTwoDimensionalGrid<Action> world, final XYCoordinates coordinates) {
        final int neighborX = coordinates.getX() - 1;
        final int neighborY = coordinates.getY();
        return neighborX >= 0 ? (MarketParticipant) world.getCell(new XYCoordinates(neighborX, neighborY))
                : StockMarketAutomatonRule.EMPTY_CELL;
    }

    private MarketParticipant getRightNeighbor(final SimpleTwoDimensionalGrid<Action> world, final XYCoordinates coordinates) {
        final int neighborX = coordinates.getX() + 1;
        final int neighborY = coordinates.getY();
        return neighborX < world.getLenght() ? (MarketParticipant) world.getCell(new XYCoordinates(neighborX, neighborY))
                : StockMarketAutomatonRule.EMPTY_CELL;
    }

    private MarketParticipant getUpperLeftNeighbor(final SimpleTwoDimensionalGrid<Action> world, final XYCoordinates coordinates) {
        final int neighborX = coordinates.getX() - 1;
        final int neighborY = coordinates.getY() - 1;
        return neighborX >= 0 && neighborY >= 0 ? (MarketParticipant) world.getCell(new XYCoordinates(neighborX, neighborY))
                : StockMarketAutomatonRule.EMPTY_CELL;
    }

    private MarketParticipant getUpperRightNeighbor(final SimpleTwoDimensionalGrid<Action> world, final XYCoordinates coordinates) {
        final int neighborX = coordinates.getX() + 1;
        final int neighborY = coordinates.getY() - 1;
        return neighborX < world.getLenght() && neighborY >= 0 ? (MarketParticipant) world.getCell(new XYCoordinates(neighborX, neighborY))
                : StockMarketAutomatonRule.EMPTY_CELL;
    }

    private MarketParticipant getLowerLeftNeigbhor(final SimpleTwoDimensionalGrid<Action> world, final XYCoordinates coordinates) {
        final int neighborX = coordinates.getX() - 1;
        final int neighborY = coordinates.getY() + 1;
        return neighborX >= 0 && neighborY < world.getHeight() ? (MarketParticipant) world.getCell(new XYCoordinates(neighborX, neighborY))
                : StockMarketAutomatonRule.EMPTY_CELL;
    }

    private MarketParticipant getLowerRightNeigbhor(final SimpleTwoDimensionalGrid<Action> world, final XYCoordinates coordinates) {
        final int neighborX = coordinates.getX() + 1;
        final int neighborY = coordinates.getY() + 1;
        return neighborX < world.getLenght() && neighborY < world.getHeight() ? (MarketParticipant) world.getCell(new XYCoordinates(
                neighborX, neighborY)) : StockMarketAutomatonRule.EMPTY_CELL;
    }

}
