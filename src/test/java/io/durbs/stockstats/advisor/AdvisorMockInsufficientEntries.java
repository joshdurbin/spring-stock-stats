package io.durbs.stockstats.advisor;

import io.durbs.stockstats.TestConstants;
import io.durbs.stockstats.domain.Price;
import io.durbs.stockstats.domain.Stock;
import io.durbs.stockstats.provider.StockProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class AdvisorMockInsufficientEntries {

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public Advisor defaultAdvisor() {
            return new DefaultAdvisor();
        }
    }

    @Autowired
    private Advisor advisor;

    @MockBean
    private StockProvider stockProvider;

    @Before
    public void setUp() {

        Stock apple = new Stock(TestConstants.APPLE_SYMBOL, "Apple", "", "");

        Mockito.when(stockProvider.lookupStockBySymbol(TestConstants.APPLE_SYMBOL))
                .thenReturn(Optional.of(apple));

        List<Price> prices = new LinkedList<>();

        prices.add(new Price(LocalDate.now().minusDays(1), 8.29));

        Mockito.when(stockProvider.getPricesForStock(apple)).thenReturn(prices);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calculationCorrect() {

        advisor.calculateMostRecentAdvise(TestConstants.APPLE_SYMBOL);
    }
}
