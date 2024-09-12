package Maks1mov.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

import Maks1mov.Main;
import Maks1mov.logger.RecordLevel;

public class BotCallback {
 
	public BotCallback(Update update) {
   
		try {
			onCallbackReceived(update);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}  
	
	private void onCallbackReceived(Update update) {
		
		String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
		String data = update.getCallbackQuery().getData();
		
		Main.getLogger().log(RecordLevel.TELEGRAM, "Receive new Update. userID: " + chatId + " from user " + " with update " + data, true);
		
		// GO
		
		
		
		
	}
}