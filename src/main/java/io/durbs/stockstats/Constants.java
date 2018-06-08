package io.durbs.stockstats;

import java.math.RoundingMode;

public class Constants {

    private Constants() {
    }

    public static final Integer DEFAULT_PROFIT_BIG_DECIMAL_PRECISION = 2;
    public static final RoundingMode DEFAULT_PROFIT_ROUNDING_MODE = RoundingMode.HALF_DOWN;
    public static final Integer MIN_NUMBER_OF_STOCK_PRICES_TO_ADVISE = 2;
}
