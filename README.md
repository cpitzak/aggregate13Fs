# Aggregate 13Fs

## About
This aggregates [13F Forms](http://en.wikipedia.org/wiki/Form_13F) to gain insights about what the guru money managers and investors
are buying and selling each quarter.

3:58 http://www.youtube.com/watch?v=YmmIbrKDYbw&feature=youtu.be


Decisions I've made based on this tool (2013):

- CBI  (2nd Quarter)
- AAPL (2nd Quarter)
- SU   (3rd Quarter)

Keep in mind that while this tool gives me ideas I do a fair amount of research on my own to see which equities I will ultimately buy or sell. The above are decisions I have
made with this tool.

Also note there are a lot of improvements that I would like to make to this tool. The addition of a database was a late night to get a database up and running so that
I could quickly filter on stock symbols against gurus without having to have the tool re-download all the 13Fs (a large amount of data).

CBI was found from Warren Buffetts 13F the night it was release. It was a new company, I hurried on my research and placed a buy that night to be executed at market open.

I bought AAPL at the end of Jan 2013. Because of large drops over the summer I became worried and was close to selling once it reached a bit above my original purchase price. As a result of
my concern I added to this tool the ability to check a given stock quote against all gurus to see what they are doing. In the sample section you can see the run I performed "AAPL Trends"
which convinced me not to sell but to buy more. 

**Disclaimer: I take no responsibility for the correctness or use of this tool. Use it at your own risk.**

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
