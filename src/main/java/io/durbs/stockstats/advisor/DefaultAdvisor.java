package io.durbs.stockstats.advisor;

import io.durbs.stockstats.Constants;
import io.durbs.stockstats.domain.Price;
import io.durbs.stockstats.domain.Advise;
import io.durbs.stockstats.provider.StockProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultAdvisor implements Advisor {

    @Autowired
    private StockProvider stockProvider;

    @Override
    public Advise calculateMostRecentAdvise(String symbol) {

        val stock = stockProvider.lookupStockBySymbol(symbol);
        final Advise advise;

        if (stock.isPresent()) {

            val prices = stockProvider.getPricesForStock(stock.get());

            if (prices.size() < Constants.MIN_NUMBER_OF_STOCK_PRICES_TO_ADVISE) {
                throw new IllegalArgumentException("Getting a profit requires at least " + Constants.MIN_NUMBER_OF_STOCK_PRICES_TO_ADVISE + " prices");
            }

            Price minPrice = prices.get(0);
            Price maxPrice = prices.get(1);

            Double maxProfit = prices.get(1).getPrice() - prices.get(0).getPrice();

            for (Integer index = 1; index < prices.size(); index++) {

                val currentPrice = prices.get(index);
                val potentialProfit = currentPrice.getPrice() - minPrice.getPrice();

                if (potentialProfit > maxProfit) {
                    maxProfit = potentialProfit;
                    maxPrice = currentPrice;
                }

                if (currentPrice.getPrice() < minPrice.getPrice()) {
                    minPrice = currentPrice;
                }
            }

            advise = new Advise(minPrice.getDate(), minPrice.getPrice(), maxPrice.getDate(), maxPrice.getPrice());

        } else {
            advise = null;
        }

        return advise;
    }
}
