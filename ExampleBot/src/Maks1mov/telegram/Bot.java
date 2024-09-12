package Maks1mov.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@SuppressWarnings("deprecation")
public class Bot extends TelegramLongPollingBot {

	private static Bot bot;

	public static Bot getBot() {
		return bot;
	}

	public String getBotUsername() {
		return "oceni_anonimno_bot";
	}

	public String getBotToken() {
		return "7484005112:AAH0SRNfgIkI81ssJBFE5B3Rj4YPyCZRz4g";
	}

	public void onUpdateReceived(Update update) {

		bot = this;

		if (update.hasMyChatMember()) {
			
			new BotAddedToChat(update);
			return;
		}

		if (update.hasCallbackQuery())
			new BotCallback(update);

		else
			new BotMessage(update);
	}
}