package Maks1mov.telegram.utils;

import java.util.ArrayList;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import Maks1mov.Main;
import Maks1mov.logger.RecordLevel;
import Maks1mov.telegram.Bot;

public class BotUtils {
	
	private static SendMessage sendMessage = new SendMessage();
	private static Message sentOutMessage = new Message();
	
	public static SendMessage getSendMessage() {
		return sendMessage;
	} 

	public static Message getSentOutMessage() {
		return sentOutMessage;
	}
   
	public static String getUsername(Message msg) {
		User user = msg.getFrom();
		return user.getUserName();
	}
	
	public static String getFirstNameOfUser(Message msg) {
		User user = msg.getFrom();
		return user.getFirstName();
	}
	
	public static void setMainButtons(String chatId) {

		sendMessage = new SendMessage();

		sendMessage.enableHtml(true);
		sendMessage.setText("💬");
		sendMessage.setChatId(String.valueOf(chatId));

		ReplyKeyboardMarkup chooseKeyboard = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(chooseKeyboard);
		
		KeyboardButton financesButton1 = new KeyboardButton();
		financesButton1.setText("1");
		
		KeyboardButton financesButton2 = new KeyboardButton();
		financesButton2.setText("2");
		
		KeyboardButton financesButton3 = new KeyboardButton();
		financesButton3.setText("3");
		
		ArrayList<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

		KeyboardRow financesRow1 = new KeyboardRow();
		KeyboardRow financesRow2 = new KeyboardRow();
		KeyboardRow financesRow3  = new KeyboardRow();
		
		financesRow1.add(financesButton1);
		financesRow2.add(financesButton2);
		financesRow3.add(financesButton3);
		
		keyboard.add(financesRow1);
		keyboard.add(financesRow2);
		keyboard.add(financesRow3);
		
		chooseKeyboard.setKeyboard(keyboard);

		try {
			Bot.getBot().execute(sendMessage);
		} catch (TelegramApiException e) {
			Main.getLogger().log(RecordLevel.ERROR, "Message is not delivered. userID: " + chatId + " from user " 
		 + " with reason: " + e.getMessage().replace("Error executing org.telegram.telegrambots.meta.api.methods.send.SendPhoto query: [403] Forbidden: ", ""), true);
		}
	}

	public static void sendTextMessage(String chatId, String text) {

		sendMessage = new SendMessage();

		sendMessage.enableHtml(true);
		sendMessage.setChatId(chatId);
		sendMessage.setText(text);

		try {
			sentOutMessage = Bot.getBot().execute(sendMessage);
		} catch (TelegramApiException e) {
			Main.getLogger().log(RecordLevel.ERROR, "Message is not delivered. userID: " + chatId + " from user " 
		+ " with reason: " + e.getMessage().replace("Error executing org.telegram.telegrambots.meta.api.methods.send.SendPhoto query: [403] Forbidden: ", ""), true);
		}
	}
}