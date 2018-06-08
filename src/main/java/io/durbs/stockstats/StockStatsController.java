package io.durbs.stockstats;

import io.durbs.stockstats.advisor.Advisor;
import io.durbs.stockstats.domain.Advise;
import io.durbs.stockstats.domain.Price;
import io.durbs.stockstats.domain.Stock;
import io.durbs.stockstats.provider.StockProvider;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StockStatsController {

    @Autowired
    private StockProvider stockProvider;

    @Autowired
    private Advisor advisor;

    @GetMapping("/stocks/v0/{symbol}")
    public ResponseEntity<Stock> getStockBySymbol(@PathVariable("symbol") final String symbol) {

        final ResponseEntity responseEntity;

        val stock = stockProvider.lookupStockBySymbol(symbol);

        if (stock.isPresent()) {
            responseEntity = ResponseEntity.ok(stock.get());
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @GetMapping("/stocks/v0/{symbol}/prices")
    public ResponseEntity<List<Price>> getStockPricesBySymbol(@PathVariable("symbol") final String symbol) {

        final ResponseEntity responseEntity;

        val stock = stockProvider.lookupStockBySymbol(symbol);

        if (stock.isPresent()) {
            responseEntity = ResponseEntity.ok(stockProvider.getPricesForStock(stock.get()));
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @GetMapping("/stocks/v0/{symbol}/singleMaxProfit")
    public ResponseEntity<Advise> calculateMostRecentAdvise(@PathVariable("symbol") final String symbol) {

        return ResponseEntity.ok(advisor.calculateMostRecentAdvise(symbol));
    }

}
