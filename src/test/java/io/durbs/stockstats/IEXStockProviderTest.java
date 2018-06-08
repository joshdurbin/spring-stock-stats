package io.durbs.stockstats;

import io.durbs.stockstats.provider.StockProvider;
import io.durbs.stockstats.provider.iex.IEXStockProvider;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class IEXStockProviderTest {

    @Test
    public void verifyLookupURI() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        StockProvider iex = new IEXStockProvider();

        Method stockLookupURI = IEXStockProvider.class.getDeclaredMethod("generateStockLookupURI", String.class);
        stockLookupURI.setAccessible(true);

        String appleLookupURI = (String) stockLookupURI.invoke(iex, "aapl");

        assertEquals(appleLookupURI, "https://api.iextrading.com/1.0/stock/aapl/company");
    }

    @Test
    public void verifyPriceURI() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        StockProvider iex = new IEXStockProvider();

        Method stockLookupURI = IEXStockProvider.class.getDeclaredMethod("generatePriceLookupURI", String.class);
        stockLookupURI.setAccessible(true);

        String appleLookupURI = (String) stockLookupURI.invoke(iex, "aapl");

        assertEquals(appleLookupURI, "https://api.iextrading.com/1.0/stock/aapl/chart/1y");
    }
}
