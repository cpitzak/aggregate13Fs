package guru.aggregator;

import guru.aggregator.aggregators.GuruNameAggregator;
import guru.aggregator.aggregators.GuruDataAggregator;
import guru.aggregator.database.Database;
import guru.aggregator.model.Client;
import guru.aggregator.model.Guru;
import guru.aggregator.model.Money;
import guru.aggregator.model.Ticker;
import guru.aggregator.view.PrintResults;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {
		String hr = Client.hr;
		Database database = new Database();
		Money.init(Currency.getInstance("USD"), RoundingMode.HALF_EVEN);
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.println("Welcome to the 13F Aggregator!\n");
		if (args.length > 0 && args[0].equals("-update")) {
			System.out
					.println("Please enter your gurufocus.com username and password to update the database.\n");
			System.out.print("Enter your username: ");
			String username = bufferReader.readLine();
			System.out.print("Enter your password: ");
			String password = bufferReader.readLine();

			Client client = new Client();
			client.login(username, password);

			GuruNameAggregator namesAggregator = new GuruNameAggregator(client);
			List<String> guruNames = namesAggregator.getGuruNames();
			GuruDataAggregator dataAggregator = new GuruDataAggregator(client);
			for (int i = 0; i < guruNames.size(); i++) {
				Guru guru = dataAggregator.getGuru(guruNames.get(i));
				for (Ticker ticker : guru.getTickers()) {
					ticker.getTransactionHistory().adjustTxShares();
				}
				PrintResults.print(guru);
				database.save(guru);
			}
			System.out.println("Finished.");
			client.logout();
		} else {
			//TODO: database query for only guru names
			List<Guru> gurus = database.getGurus();
			System.out.println();
			for (int i = 0; i < gurus.size(); i++) {
				System.out.println((i + 1) + ". " + gurus.get(i).getName());
			}
			System.out.print("\nGuru to aggregate[number or all]: ");
			String guruSelection = bufferReader.readLine();

			if (guruSelection.equalsIgnoreCase("all")) {
				System.out.print("Filter on symbol[symbol or no]: ");
				String symbolFilterSelection = bufferReader.readLine();
				if (symbolFilterSelection.equalsIgnoreCase("no")) {
					System.out.println();
					//TODO: database query for all gurus
					for (Guru guru : gurus) {
						System.out.println(hr + guru.getName() + hr);
						PrintResults.print(guru);
					}
				} else {
					for (Guru guru : gurus) {
						//TODO: database query for guru with the given ticker symbol holdings
						Guru guruWith = guru.getGuruWith(symbolFilterSelection);
						if (guruWith != null) {
							System.out.println(hr + guru.getName() + hr);
							PrintResults.print(guruWith);
						}
					}
				}
			} else {
				Integer index = Integer.parseInt(guruSelection);
				Guru guru = gurus.get(index - 1);
				System.out.print("Filter on symbol[symbol or no]: ");
				String symbolFilterSelection = bufferReader.readLine();
				if (symbolFilterSelection.equalsIgnoreCase("no")) {
					//TODO: database query for all gurus
					System.out.println(hr + guru.getName() + hr);
					PrintResults.print(guru);
				} else {
					//TODO: database query for guru with the given ticker symbol holdings
					Guru guruWith = guru.getGuruWith(symbolFilterSelection);
					if (guruWith != null) {
						System.out.println(hr + guru.getName() + hr);
						PrintResults.print(guruWith);
					}
				}
			}
		}
		System.out.println("Finished.");
	}

}
