package Maks1mov.utm.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import Maks1mov.telegram.Bot;
import Maks1mov.utm.UtmStorage;
import Maks1mov.utm.objects.UtmTag;

public class UtmTelegramNotifer {

	private UtmStorage utmStorage;
	
	public UtmTelegramNotifer(UtmStorage utmStorage) {
		this.utmStorage = utmStorage;
	}

	public void sendInfoToTelegramUser(String chatId, Bot bot) {

		String timeStamp = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
		String dayOfWeek = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())).getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru"));
		
		SendMessage message = new SendMessage();

		message.setChatId(chatId);
		message.enableHtml(true);

		message.setText("🛎 Статистика новых переходов за <b>" + timeStamp + " (" +  dayOfWeek + ")</b> \n"
					  + "\n"
					  + " 👥 Новых переходов за сегодня: <b>" + utmStorage.getUtmDatabaseManager().getAllUtmJoinsforToday() + " пользователей </b>\n"
					  + " 👥 Переходов за вчера: <b>" + utmStorage.getUtmDatabaseManager().getAllUtmJoinsforYesterday() + " пользователей </b> \n"
					  + "\n"
					  + " 📆 Переходов за неделю: <b>" + utmStorage.getUtmDatabaseManager().getAllUtmJoinsforWeek() + " пользователей </b> \n"
					  + " 🗓 Переходов за месяц: <b>" + utmStorage.getUtmDatabaseManager().getAllUtmJoinsforMonth() + " пользователей </b> \n"
					  + "\n"
					  + " <b>💸 ОПЛАТ: " + utmStorage.getUtmDatabaseManager().getAllUtmPayments() + " пользователей </b>\n"
					  + "\n"
					  + " 😳 Переходов за все время: <b>" + utmStorage.getUtmDatabaseManager().getAllUtmJoinsTotal() + " пользователей </b>");

		try {
			bot.execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	public void sendInfoToTelegramUserById(String chatId, int id, Bot bot) {

		UtmTag tag = null;
		SendMessage message = new SendMessage();

		message.setChatId(chatId);
		message.enableHtml(true);
		
		// ищем эту метку среди листа
		for(UtmTag tagArray : utmStorage.getAllUtmTags()) {
			
			if(tagArray.getId() == id)
				tag = tagArray;
		}
		
		if(tag == null) {
			
			message.setText("Не удалось найти метку с <b>ID " + id + "</b>!");
			try {
				bot.execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
			return;
		}
		
		message.setText("🛎 Статистика переходов по <b>ID " + id + "</b> \n"
				  + "\n"
				  + " 👥 Новых переходов за сегодня: <b>" + tag.getJoinsForToday() + " пользователей </b>\n"
				  + " 👥 Переходов за вчера: <b>" + tag.getJoinsForYesterday() + " пользователей </b> \n"
				  + "\n"
				  + " 📆 Переходов за неделю: <b>" + tag.getJoinsForWeek() + " пользователей </b> \n"
				  + " 🗓 Переходов за месяц: <b>" + tag.getJoinsForMonth() + " пользователей </b> \n"
				  + "\n"
				  + " <b>💸 ОПЛАТ: " + tag.getPayments() + " пользователей </b>\n"
				  + "\n"
				  + " 😳 Переходов за все время: <b>" + tag.getJoinsForTotal() + " пользователей </b>");

		try {
			bot.execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
