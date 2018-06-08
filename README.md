# Stock Stats

This Java 8 / Spring-based app integrates with a free stock provider, pulls and caches historical stock
data, and runs analysis to determine the best, single, buy/sell dates for a stock. Buy events must occur
before sale events. The API will return the profit per share and the dates the purchase and sell of stock
should occur on.

[![CircleCI](https://circleci.com/gh/joshdurbin/spring-stock-stats.svg?style=svg)](https://circleci.com/gh/joshdurbin/spring-stock-stats)

## Setup

1. Install the Oracle Java 8 JDK
2. Install gradle
3. (optional) If you're viewing the code in Intelij, be sure into install the Lombok plugin *and* enable Annotation processing for the project.

## Run

1. Run `gradle bootRun`

## Access stock data

1. Stock data is available via a GET to `http://localhost:8080/stocks/v0/$symbol` ex: `http://localhost:8080/stocks/v0/aapl`
1. Stock price data is available via a GET to `http://localhost:8080/stocks/v0/$symbol/prices` ex: `http://localhost:8080/stocks/v0/aapl/prices`

## Advise data

1. Stock data is available via a GET to `http://localhost:8080/stocks/v0/{symbol}/singleMaxProfit`.

## Notes

- Service should check data provider for any query to ensure there's enough history to calculate profit, with proper error handling
- Service should probably at some point allow for multiple buy/sale calculation windoww with a configurable duration
- The service impl assumes that shorting is not allowed (selling prior to buying)
- First tried stab at this with the naive approach, or even sorting the list of price/dates by their price, then running from the outside in, comparing lowest price and highest, next highest etc... looking for those where the low as before the high (a function on the LocalDate object of Price [see impl]). That flopped pretty hard and I ran out of time this afternoon.
- It seemed like the higher performance, linear solution was that referenced on interview cake's stock-price project. The solution seems straight forward, though in certain large data test scenarios (with actual data in my running solution), that sell dates fall prior to buys with respect to time which violates things. The tests that do exist account for this in smaller data sizes, but still there's an issue in my implementation or the solution.
