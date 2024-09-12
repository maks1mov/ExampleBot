package Maks1mov.telegram.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

public class BotSpam {
	
	public static void sendTelegramSpamMessage(int idMsg, String chatId) {
		
		SendPhoto photo = new SendPhoto();
		photo.setChatId(chatId);
		photo.setParseMode("HTML");
		photo.setPhoto(new InputFile("https://trash.maks1mov.ru/images/anonbot/quizFinish.png"));
		
		switch (idMsg) {
		
		case 0:
			photo.setCaption(
				 "<b>Hello</b> \n"
					);
			break;
		}
	}
}