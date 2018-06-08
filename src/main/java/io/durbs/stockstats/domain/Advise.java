package io.durbs.stockstats.domain;

import io.durbs.stockstats.Constants;
import lombok.Data;
import lombok.val;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Advise {

    private final LocalDate purchaseDate;
    private final Double purchaseAmount;
    private final LocalDate sellDate;
    private final Double sellAmount;

    public Double getSingleShareProfit() {

        val profit = new BigDecimal(sellAmount)
                .subtract(new BigDecimal(purchaseAmount))
                .setScale(Constants.DEFAULT_PROFIT_BIG_DECIMAL_PRECISION, Constants.DEFAULT_PROFIT_ROUNDING_MODE);

        return profit.doubleValue();
    }
}
