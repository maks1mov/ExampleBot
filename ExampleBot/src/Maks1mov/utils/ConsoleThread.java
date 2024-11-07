package Maks1mov.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleThread {

	public ConsoleThread() {

		new Thread(() -> {

			try {
				Thread.sleep(200);
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

					while (true) {
						print(">");
						onCommand(reader.readLine());
					}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}).start();
	}

	public void onCommand(String arg) {

		final String[] args = arg.split(" ");
		final String command = args[0];

		switch (command) {

		case "clear":

			print("\u001b[2J");
			break;
		}
	}

	private void print(String p) {
		System.out.print(p);
	}
}