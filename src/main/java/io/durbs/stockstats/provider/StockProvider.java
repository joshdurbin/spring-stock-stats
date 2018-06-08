package io.durbs.stockstats.provider;

import io.durbs.stockstats.domain.Price;
import io.durbs.stockstats.domain.Stock;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface StockProvider {

    @Cacheable("stocks")
    Optional<Stock> lookupStockBySymbol(String symbol);

    /**
     *
     * Require proper @link Stock object so that existence is validated.
     *
     * @param stock
     * @return Stock prices sorted by date in ascending order.
     */
    @Cacheable("stockPrices")
    List<Price> getPricesForStock(Stock stock);

}
