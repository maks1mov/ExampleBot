package Maks1mov;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import Maks1mov.db.Database;
import Maks1mov.db.Storage;
import Maks1mov.logger.Logger;
import Maks1mov.logger.RecordLevel;
import Maks1mov.telegram.Bot;
import Maks1mov.utils.ConsoleThread;
import Maks1mov.utils.Timer;
import Maks1mov.utm.UtmStorage;
import lombok.Getter;

public class Main { 

	@Getter private static Logger logger;
	@Getter private static Timer timer;
	
	@Getter private static Database database;
	@Getter private static Storage storage;
	
	@Getter private static UtmStorage utmStorage;
	 
	public static void main(String[] args)  {

		logger = new Logger();
		
		logger.log(RecordLevel.INFO, "Starting...", false);
		logger.log(RecordLevel.INFO, "Loading properties...", false);
		
		database = new Database();
		storage = new Storage();
		utmStorage = new UtmStorage();
		timer = new Timer();
		
		loadTelegramBot();
		
		logger.log(RecordLevel.INFO, "Running Bot", false);
		
		new ConsoleThread();
	}
	
	private static void loadTelegramBot() {

		try {
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(new Bot());
			
		} catch (TelegramApiException ex) {
			ex.printStackTrace();
		}
	}
}