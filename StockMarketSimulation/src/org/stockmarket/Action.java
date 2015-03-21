package org.stockmarket;

public enum Action {
    
    /** When market participant is very bullish he buys at the market. */
    BUY_MARKET, 
    /** When market participant is very bearish he sells at the market. */
    SELL_MARKET, 
    /** When market participant is bullish he puts limit buy order. */
    BUY_LIMIT, 
    /** When market participant is bearish he puts limit sell order. */
    SELL_LIMIT, 
    /** When market participant is uncertain. */
    NEUTRAL

}
