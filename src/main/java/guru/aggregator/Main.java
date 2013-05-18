package guru.aggregator;

import guru.aggregator.model.Client;
import guru.aggregator.model.Guru;
import guru.aggregator.view.PrintResults;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

public class Main {

	public static void main(String[] args) throws Exception {
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Welcome to the 13F Aggregator!\n");
		System.out.println("This aggregator requires an account from gurufocus.com\n");
		System.out.print("Enter your username: ");
		String username = bufferReader.readLine();
		System.out.print("Enter your password: ");
		String password = bufferReader.readLine();

		Client client = new Client();
		try {
			client.login(username, password);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		GuruHTMLScrapper htmlScrapper = new GuruHTMLScrapper(client);
		List<String> gurus = htmlScrapper.getGuruNames();

		for (int i = 0; i < gurus.size(); i++) {
			System.out.println((i + 1) + ". " + gurus.get(i));
		}

		System.out.print("Guru to aggregate[number or all]: ");
		String selection = bufferReader.readLine();

		StockAggregator stockAggregator = new StockAggregator(client);
		if (selection.equalsIgnoreCase("all")) {
			for (int i = 0; i < gurus.size(); i++) {
				Guru guru = stockAggregator.getGuru(gurus.get(i));
				PrintResults.print(guru);
			}
		} else {
			try {
				Integer index = Integer.parseInt(selection);
				Guru guru = stockAggregator.getGuru(gurus.get(index - 1));
				PrintResults.print(guru);
			} catch (NumberFormatException e) {
				System.out.println();
			}
		}

		client.logout();
	}

}
