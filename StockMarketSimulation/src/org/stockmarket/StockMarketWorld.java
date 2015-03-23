package org.stockmarket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import org.cellular.cell.Cell;
import org.cellular.cell.rules.CellularAutomatonRule;
import org.cellular.twodimensional.SimpleTwoDimensionalGrid;
import org.cellular.twodimensional.XYCoordinates;

/**
 * A {@link World} class that represents stock market exchange.
 * 
 * @author Ignas
 *
 */
public class StockMarketWorld extends SimpleTwoDimensionalGrid<Action> {
    
    private static final int INITIAL_PRICE = 100;

    private final StockExchangeLevel2Book level2;

    private List<Candle> candles = new ArrayList<>(Arrays.asList(new Candle(INITIAL_PRICE, INITIAL_PRICE, INITIAL_PRICE, INITIAL_PRICE)));
    
    private Integer currentOpen = INITIAL_PRICE;

    private Integer currentHigh = INITIAL_PRICE;

    private Integer currentLow = INITIAL_PRICE;

    private Integer currentClose = INITIAL_PRICE;

    public StockMarketWorld(final int height, final int length, final CellularAutomatonRule<Action> rule) {
        super(height, length, rule);
        level2 = new StockExchangeLevel2Book();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Cell<Action> createNewCell(final XYCoordinates coordinates, final CellularAutomatonRule<Action> rule) {
        final Random random = new Random();
        // 33% chance to buy, sell, or neutral initial state
        int randomInt = random.nextInt(3);
        final Action action = randomInt == 0 ? Action.NEUTRAL : randomInt == 1 ? Action.BUY : Action.SELL;
        return new MarketParticipant(action, rule, coordinates, this);
    }

    /**
     * During each iteration each market participant performs its actions - buy sell or neutral.
     * 
     * {@inheritDoc}
     */
    @Override
    public void nextState() {
        currentOpen = getLastCandle().getClose();
        currentHigh = currentOpen;
        currentLow = currentOpen;
        currentClose = currentOpen;
        
        List<MarketParticipant> participants = new ArrayList<>();
        
        for (final Cell<Action>[] cellArray : worldGrid) {
            for (final Cell<Action> cell : cellArray) {
                final MarketParticipant participant = (MarketParticipant) cell;
                participants.add(participant);
            }
        }
        
        // make participants to act in random order during each iteration
        Collections.shuffle(participants);
        boolean useLimitOrders = true;
        int i = 0;
        for (MarketParticipant participant : participants) {
            participant.act(useLimitOrders);
            if (useLimitOrders && (i++ > participants.size() / 5)) { // 20% participants use limit orders
                useLimitOrders = false;
            }
        }
        
        candles.add(new Candle(currentOpen, currentHigh, currentLow, currentClose));
        
        // CellularAutomatonRule sets new values for next round
        super.nextState();
    }

    /**
     * @return Last transaction price. For last close use getLastCandle() method.
     */
    public int getLastPrice() {
        return level2.getLastPrice();
    }

    /**
     * Places market buy order on the exchange.
     */
    public void buyMarket(final MarketParticipant buyer, final int numberOfShares) {
        int sharesFilled = 0;
        while (sharesFilled < numberOfShares) {
            final Order order = level2.getOffers().poll();
            if (order == null) {
                break; // if no more orders remaining order is not filled
            }
            final MarketParticipant seller = order.getParticipant();
            final int transactionSum = order.getNumberOfShares() * order.getPrice();
            
            buyer.executeBuy(order.getNumberOfShares(), transactionSum);
            seller.executeSell(order.getNumberOfShares(), transactionSum);
            
            level2.setLastPrice(order.getPrice());
            sharesFilled = sharesFilled + order.getNumberOfShares();

            System.out.println("Bought " + order.getNumberOfShares() + " @" + order.getPrice());

            if (currentHigh < level2.getLastPrice()) {
                currentHigh = level2.getLastPrice();
            }
            currentClose = level2.getLastPrice();
        }
    }

    /**
     * Places market sell order on the exchange.
     */
    public void sellMarket(final MarketParticipant seller, final int numberOfShares) {
        int sharesFilled = 0;
        while (sharesFilled < numberOfShares) {
            final Order order = level2.getBids().poll();
            if (order == null) {
                break; // if no more orders remaining order is not filled
            }
            final MarketParticipant buyer = order.getParticipant();
            final int transactionSum = order.getNumberOfShares() * order.getPrice();
            seller.executeSell(order.getNumberOfShares(), transactionSum);
            buyer.executeBuy(order.getNumberOfShares(), transactionSum);
            level2.setLastPrice(order.getPrice());
            sharesFilled = sharesFilled + order.getNumberOfShares();

            System.out.println("Sold " + order.getNumberOfShares() + " @" + order.getPrice());

            if (currentLow > level2.getLastPrice()) {
                currentLow = level2.getLastPrice();
            }
            currentClose = level2.getLastPrice();
        }
    }

    /**
     * Places limit buy order on the exchange.
     */
    public void placeBid(final MarketParticipant buyer, final int numberOfShares, final int price) {
        // decompose order into minimum lot orders for simplicity of filling them in sellMarket() method 
        for (int i = 0; i < numberOfShares / 100; i++) {
            level2.getBids().add(new Order(buyer, price, 100)); // minimum 100 shares lots
        }
    }

    /**
     * Places limit sell order on the exchange.
     */
    public void placeOffer(final MarketParticipant seller, final int numberOfShares, final int price) {
        // decompose order into minimum lot orders for simplicity of filling them in buyMarket() method 
        for (int i = 0; i < numberOfShares / 100; i++) {
            level2.getOffers().add(new Order(seller, price, 100)); // minimum 100 shares lots
        }
    }

    /**
     * @return Previous last candle.
     */
    public Candle getLastCandle() {
        return candles.get(candles.size() - 1);
    }
    
    /**
     * Market candles.
     */
    public List<Candle> getCandles() {
        return candles;
    }

    /**
     * Example of Level2 order book looks like this in real world:
     *
     * price bids offers 
     * 103          5 
     * 102          4 
     * 101          3 
     * 100 
     * 99       2 
     * 98       3 
     * 97       5
     *
     * So in this example if you want to buy there are 3 shares for 101$, 4 shares for 102$ and 5 shares for 103$. And
     * if you want to sell, someone buys 2 shares for 99$ from you, 3 shares for 98$ and 5 shares for 97$.
     *
     * @author Ignas
     *
     */
    public static class StockExchangeLevel2Book {
        private final PriorityQueue<Order> bids = new PriorityQueue<Order>(10, (o1, o2) -> o1.getPrice() < o2.getPrice() ? 1 : -1);
        private final PriorityQueue<Order> offers = new PriorityQueue<Order>(10, (o1, o2) -> o1.getPrice() > o2.getPrice() ? 1 : -1);
        private int lastPrice = INITIAL_PRICE;

        public PriorityQueue<Order> getBids() {
            return bids;
        }

        public PriorityQueue<Order> getOffers() {
            return offers;
        }

        public int getLastPrice() {
            return lastPrice;
        }

        public void setLastPrice(final int lastPrice) {
            this.lastPrice = lastPrice;
        }

    }

    /**
     * Order to buy or sell shares.
     *
     * @author Ignas
     *
     */
    public static class Order {
        private final MarketParticipant participant;
        private final int price;
        private final int numberOfShares;

        public Order(final MarketParticipant participant, final int price, final int numberOfShares) {
            super();
            this.participant = participant;
            this.price = price;
            this.numberOfShares = numberOfShares;
        }

        public MarketParticipant getParticipant() {
            return participant;
        }

        public int getPrice() {
            return price;
        }

        public int getNumberOfShares() {
            return numberOfShares;
        }
    }

    /**
     * Represents daily (or one iteration) candle bar in chart.
     * 
     * @author Ignas
     *
     */
    public static class Candle {
        private final int open;
        private final int high;
        private final int low;
        private final int close;

        public Candle(final int open, final int high, final int low, final int close) {
            super();
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
        }

        public int getOpen() {
            return open;
        }

        public int getHigh() {
            return high;
        }

        public int getLow() {
            return low;
        }

        public int getClose() {
            return close;
        }
    }
}
