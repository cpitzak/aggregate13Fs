package guru.aggregator;

import guru.aggregator.aggregators.GuruNameAggregator;
import guru.aggregator.aggregators.GuruDataAggregator;
import guru.aggregator.model.Client;
import guru.aggregator.model.Filters;
import guru.aggregator.model.Guru;
import guru.aggregator.view.PrintResults;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.println("Welcome to the 13F Aggregator!\n");
		System.out
				.println("This aggregator requires an account from gurufocus.com\n");
		System.out.print("Enter your username: ");
		String username = bufferReader.readLine();
		System.out.print("Enter your password: ");
		String password = bufferReader.readLine();

		Client client = new Client();
		client.login(username, password);

		GuruNameAggregator namesAggregator = new GuruNameAggregator(client);
		List<String> guruNames = namesAggregator.getGuruNames();

		System.out.println();
		for (int i = 0; i < guruNames.size(); i++) {
			System.out.println((i + 1) + ". " + guruNames.get(i));
		}

		System.out.print("\nGuru to aggregate[number or all]: ");
		String guruSelection = bufferReader.readLine();

		GuruDataAggregator dataAggregator = new GuruDataAggregator(client);
		if (guruSelection.equalsIgnoreCase("all")) {
			System.out.print("Filter on symbol[symbol or no]: ");
			String symbolFilterSelection = bufferReader.readLine();
			if (symbolFilterSelection.equalsIgnoreCase("no")) {
				System.out.println();
				for (int i = 0; i < guruNames.size(); i++) {
					Guru guru = dataAggregator.getGuru(guruNames.get(i));
					PrintResults.print(guru);
				}
			} else {
				System.out.println();
				Filters filters = new Filters();
				filters.addTickerSymbol(symbolFilterSelection);
				for (int i = 0; i < guruNames.size(); i++) {
					Guru guru = dataAggregator.getGuruWithFilters(
							guruNames.get(i), filters);
					PrintResults.print(guru);
				}
			}
		} else {
			try {
				Integer index = Integer.parseInt(guruSelection);
				System.out.print("Filter on symbol[symbol or no]: ");
				String symbolFilterSelection = bufferReader.readLine();
				if (symbolFilterSelection.equalsIgnoreCase("no")) {
					System.out.println();
					Guru guru = dataAggregator
							.getGuru(guruNames.get(index - 1));
					PrintResults.print(guru);
				} else {
					System.out.println();
					Filters filters = new Filters();
					filters.addTickerSymbol(symbolFilterSelection);
					Guru guru = dataAggregator.getGuruWithFilters(
							guruNames.get(index - 1), filters);
					PrintResults.print(guru);
				}
			} catch (NumberFormatException e) {
				System.out.println();
			}
		}
		System.out.println("Finished.");
		client.logout();
	}

}
