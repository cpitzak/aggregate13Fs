package guru.aggregator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
		Aggregator aggregator = new Aggregator();
		aggregator.connect(username, password);
		aggregator.aggregate(10.0f);
		aggregator.close();
	}

}
