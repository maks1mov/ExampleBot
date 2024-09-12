package Maks1mov.utm;

import java.util.ArrayList;

import Maks1mov.utm.objects.UtmTag;
import Maks1mov.utm.utils.UtmDatabaseManager;
import Maks1mov.utm.utils.UtmTelegramNotifer;
import Maks1mov.utm.utils.UtmTimer;
import lombok.Getter;

public class UtmStorage {

	@Getter private ArrayList<UtmTag> allUtmTags = new ArrayList<UtmTag>();
	@Getter private UtmTimer utmTimer;
	@Getter private UtmDatabaseManager utmDatabaseManager;
	@Getter private UtmTelegramNotifer utmTelegramNotifer;

	public UtmStorage() {

		utmDatabaseManager = new UtmDatabaseManager(this);
		utmTimer = new UtmTimer(this);
		utmTelegramNotifer = new UtmTelegramNotifer(this);
	}

	 /**
	 *  Пример ссылки - https://t.me/maks1movtest_bot?start=id=1-traffic=ads. 
	 *  Вставлять код в обработчике телеграм бота, когда кто-то пишет в него! 
	 * 
	 *  boolean возвращает true тогда, когда метка у ссылки была обнаружена, 
	 *  а данные о переходе были добавлены в таблицу mysql
	 * 
	 */
	public boolean handleBotStartUtmQuery(String inputText) {

		if (inputText.contains("start") && inputText.contains("traffic")) {

			String utm = inputText.replace("/start ", "");
			String traffic = utm.split("-")[1].replace("traffic=", "");
			int id = Integer.parseInt(utm.split("-")[0].replace("id=", ""));

			addNewJoinToUTM(id, traffic);
			return true;
		}
		
		else return false;
	}
	
	public void addNewJoinToUTM(int id, String traffic) {
		utmDatabaseManager.addNewJoinToUTM(id, traffic);
	}

	public void deleteUtmTagById(int id) {
		utmDatabaseManager.deleteUtmTagById(id);
	}
}