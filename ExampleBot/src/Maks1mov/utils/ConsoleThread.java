package Maks1mov.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

import Maks1mov.Main;
import Maks1mov.telegram.utils.BotSpam;

public class ConsoleThread {

	private static ConsoleThread consoleThread;
 
	public ConsoleThread() {
 
		new Thread(new Runnable() {

			public void run() {
 
				try {

					Thread.sleep(100);

					BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

					while (true) {

						while (true) {
							System.out.print(">");
							String e = reader.readLine();
							ConsoleThread.this.onCommand(e);
						}
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}).start();
	}

	public void onCommand(String arg) {

		final String[] args = arg.split(" ");
		final String command = args[0];

		if(args.length >= 2) {
			
			if(command.contains("startspam"))
				BotSpam.sendTelegramSpamMessage(new Random().nextInt(11), args[1]);
		}
		
		switch (command) {

		case "clear":

			pr("\u001b[2J");
			break;

		case "startspam":
			Main.getDatabase().startTgSpam(new Random().nextInt(11)); 
			break;
			
		case "theme":

			if (args.length < 2) {
				pr("");
				pr("Использование: theme dark/green/red/purple/yellow");
				pr("");
			}

			if (args[1].contains("dark")) {
				pr("\u001b[37m Тема изменена!");
			}

			if (args[1].contains("red")) {
				pr("\u001b[31m Тема изменена!");
			}

			if (args[1].contains("green")) {
				pr("\u001b[32m Тема изменена!");
			}

			if (args[1].contains("purple")) {
				pr("\u001b[95m Тема изменена!");
			}

			if (args[1].contains("yellow")) {
				pr("\u001b[93m Тема изменена!");
			}

			break;
		}
	}

	private void pr(String p) {
		System.out.println(p);
	}

	public static void execute(String command) {
		consoleThread.onCommand(command);
	}
}