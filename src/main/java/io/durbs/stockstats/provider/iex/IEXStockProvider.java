package io.durbs.stockstats.provider.iex;

import io.durbs.stockstats.domain.Price;
import io.durbs.stockstats.domain.Stock;
import io.durbs.stockstats.provider.StockProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(name = "stockstats.provider", havingValue = "iex", matchIfMissing = true)
public class IEXStockProvider implements StockProvider {

    private static final String BASE_V1_STOCK_INFO_ENDPOINT = "https://api.iextrading.com/1.0/stock/";
    private static final String V1_STOCK_INFO_SUFFIX = "/company";

    private static final String BASE_V1_STOCK_PRICE_ENDPOINT = "https://api.iextrading.com/1.0/stock/";
    private static final String V1_STOCK_PRICE_SUFFIX = "/chart/1y";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${stockstats.pricePointLimit}")
    private Integer pricePointLimit;

    @Override
    public Optional<Stock> lookupStockBySymbol(String symbol) {

        Optional<Stock> stock;

        try {

            stock = Optional.of(restTemplate.getForObject(generateStockLookupURI(symbol), Stock.class));

        } catch (HttpClientErrorException httpClientErrorException) {

            if (httpClientErrorException.getStatusCode() == HttpStatus.NOT_FOUND) {

                log.warn("the symbol '{}' cannot be found", symbol);
                stock = Optional.empty();

            } else {

                throw httpClientErrorException;
            }
        }

        return stock;
    }

    @Override
    public List<Price> getPricesForStock(Stock stock) {

        val pricesPreSort = restTemplate.exchange(
                generatePriceLookupURI(stock.getSymbol()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IEXPrice>>() {}).getBody();

        // no exception handling as we want any failures to prevent caching and bubble
        return pricesPreSort
                .stream()
                .map(n -> new Price(n.getDate(), n.getHigh()))
                .sorted(Comparator.comparing(Price::getDate).reversed())
                .limit(pricePointLimit)
                .collect(Collectors.toList());
    }

    private static String generateStockLookupURI(String symbol) {

        val uriGenerator = new StringBuilder();

        uriGenerator.append(BASE_V1_STOCK_INFO_ENDPOINT);
        uriGenerator.append(symbol);
        uriGenerator.append(V1_STOCK_INFO_SUFFIX);

        return uriGenerator.toString();
    }

    private static String generatePriceLookupURI(String symbol) {

        val uriGenerator = new StringBuilder();

        uriGenerator.append(BASE_V1_STOCK_PRICE_ENDPOINT);
        uriGenerator.append(symbol);
        uriGenerator.append(V1_STOCK_PRICE_SUFFIX);

        return uriGenerator.toString();
    }
}
