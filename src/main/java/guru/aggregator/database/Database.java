package guru.aggregator.database;

import guru.aggregator.model.Guru;
import guru.aggregator.model.Money;
import guru.aggregator.model.Ticker;
import guru.aggregator.model.Transaction;
import guru.aggregator.model.TransactionHistory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Database {
	private MongoClient client;
	private static final int PORT = 27017;
	private static final String GURUS = "gurus";
	private static final String HOST = "localhost";
	private static final String DATABASE_NAME = "gurudb";
	private static final Logger logger = LoggerFactory.getLogger(Database.class);

	public Database() {
		try {
			client = new MongoClient(HOST, PORT);
		} catch (UnknownHostException e) {
			logger.error("Cannot create Mongo Client with host: " + HOST + " and port: " + PORT);
			e.printStackTrace();
		}
	}

	public void save(Guru guru) {
		DB db = client.getDB(DATABASE_NAME);
		DBCollection collection = db.getCollection(GURUS);
		BasicDBObject databaseObject = guru.getDatabaseObject();
		collection.insert(databaseObject);
	}
	
	public List<Guru> getGurus() {
		List<Guru> gurus = new ArrayList<Guru>();
		DB db = client.getDB(DATABASE_NAME);
		DBCollection collection = db.getCollection(GURUS);
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			Guru guru = getGuru(object);
			gurus.add(guru);
		}
		return gurus;
	}

	private Guru getGuru(DBObject object) {
		String name = (String) object.get(Guru.NAME);
		List<Ticker> tickers = getTickers(object);
		return new Guru(name, tickers);
	}

	@SuppressWarnings("unchecked")
	private List<Ticker> getTickers(DBObject object) {
		List<Ticker> tickers = new ArrayList<Ticker>();
		List<DBObject> tickerObjects = (List<DBObject>) object.get(Guru.TICKERS);
		for (DBObject tickerObject : tickerObjects) {
			Ticker ticker = getTicker(tickerObject);
			tickers.add(ticker);
		}
		return tickers;
	}

	private Ticker getTicker(DBObject tickerObject) {
		String tickerSymbol = (String) tickerObject.get(Ticker.TICKER);
		String percentStr = (String) tickerObject.get(Ticker.PERCENT);
		float percent = Float.parseFloat(percentStr);
		Boolean buyFound = (Boolean) tickerObject.get(Ticker.BUY_FOUND);
		DBObject transactionHistoryObject = (DBObject) tickerObject.get(Ticker.TRANSACTION_HISTORY);
		Long avg = (Long) transactionHistoryObject.get(TransactionHistory.AVG);
		Long min = (Long) transactionHistoryObject.get(TransactionHistory.MIN);
		Integer finalShares = (Integer) transactionHistoryObject.get(TransactionHistory.FINAL_SHARES);
		List<Transaction> transactions = getTransactions(transactionHistoryObject);
		TransactionHistory transactionHistory = new TransactionHistory(transactions);
		transactionHistory.setAvg(Money.fromCents(avg));
		transactionHistory.setMin(Money.fromCents(min));
		transactionHistory.setFinalShares(finalShares);
		Ticker ticker = new Ticker(tickerSymbol, percent, transactionHistory);
		ticker.setBuyFound(buyFound);
		return ticker;
	}

	@SuppressWarnings("unchecked")
	private List<Transaction> getTransactions(DBObject transactionHistoryObject) {
		List<DBObject> transactionObjects = (List<DBObject>) transactionHistoryObject.get(TransactionHistory.TRANSACTIONS);
		List<Transaction> transactions = new ArrayList<Transaction>();
		for (DBObject transactionObject : transactionObjects) {
			Transaction transaction = getTransaction(transactionObject);
			transactions.add(transaction);
		}
		return transactions;
	}

	private Transaction getTransaction(DBObject transactionObject) {
		String date = (String) transactionObject.get(Transaction.DATE);
		String entryType = (String) transactionObject.get(Transaction.ENTRY_TYPE);
		Long averagePrice = (Long) transactionObject.get(Transaction.AVG);
		Long minimumPrice = (Long) transactionObject.get(Transaction.MIN);
		int numberOfShares = (Integer) transactionObject.get(Transaction.NUMBER_OF_SHARES);
		return new Transaction(date, entryType, Money.fromCents(averagePrice), Money.fromCents(minimumPrice),
				numberOfShares);
	}
	
}
