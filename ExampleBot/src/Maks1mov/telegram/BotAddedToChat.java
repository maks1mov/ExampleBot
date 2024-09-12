package Maks1mov.telegram;

import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;

import Maks1mov.Main;
import Maks1mov.logger.RecordLevel;
import Maks1mov.telegram.utils.BotUtils;

public class BotAddedToChat {
	
	public BotAddedToChat(Update update) {
		
		try {
			onCallbackReceived(update);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onCallbackReceived(Update update) {
		
		ChatMemberUpdated myChatMember = update.getMyChatMember();

		String userID = myChatMember.getFrom().getId().toString();
		String username = myChatMember.getFrom().getFirstName();

		if (myChatMember.getNewChatMember().getStatus().equals("member")) {

			// Получаем ID чата
			String groupID = myChatMember.getChat().getId().toString();
			
			// логируем
			Main.getLogger().log(RecordLevel.TELEGRAM, "Bot was added to Group. groupID: " + groupID + " by userID: " + userID + " from user " + username, true);

			// Отправляем сообщение
			BotUtils.sendTextMessage(groupID, "Привет, <b>" + username + "</b>\n");
			
			
			
			
		}
	}
}