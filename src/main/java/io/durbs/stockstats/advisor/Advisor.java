package io.durbs.stockstats.advisor;

import io.durbs.stockstats.domain.Advise;

public interface Advisor {

    /**
     *
     * @param symbol
     * @return
     */
    Advise calculateMostRecentAdvise(String symbol);
}
