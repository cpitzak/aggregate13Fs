# 13F Aggregator

## About
This aggregates [13F Forms](http://en.wikipedia.org/wiki/Form_13F) to gain insights about what the guru money managers and investors
are buying and selling each quarter.

## Prerequisites
You need to have the following installed and running:

- [MongoDB](http://www.mongodb.org)

## Run
If you are running for the first time or want to update your database [you must be online]:

   	$ java -jar build/13FAggregator.jar -update

For subsequent runs, use your database for quick results [you can be offline]:

	$ java -jar build/13FAggregator.jar

## Sample Runs

  * [David Einhorn](sample_runs/david_einhorn.txt)
  * [AAPL Trends](sample_runs/appl_trends.txt)

## More Information

This is a fork of the [13F Aggregator] (https://github.com/nileshbits/13FAggregator)
