package Maks1mov.telegram;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import Maks1mov.Main;
import Maks1mov.logger.RecordLevel;
import Maks1mov.telegram.utils.BotUtils;

public class BotMessage {

    public BotMessage(Update update) {
    	
        try {
            onCallbackReceived(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCallbackReceived(Update update) {
    	
        if (update.getMessage() == null || String.valueOf(update.getMessage().getChatId()).contains("-"))
            return;

        Message msg = update.getMessage();
        String inputText = msg.getText();
        String chatId = String.valueOf(msg.getChatId());

        Main.getLogger().log(RecordLevel.TELEGRAM, "Receive new Message. userID: " + chatId + " from user " + BotUtils.getFirstNameOfUser(msg) + " with text " + inputText, true);

        // GO
        
        
        
    }
}