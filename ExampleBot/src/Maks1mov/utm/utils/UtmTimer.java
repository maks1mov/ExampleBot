package Maks1mov.utm.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Maks1mov.utm.UtmStorage;
import Maks1mov.utm.objects.UtmTag;

public class UtmTimer {

	private UtmStorage utmStorage;
	private ScheduledExecutorService scheduler;

	public UtmTimer(UtmStorage utmStorage) {

		this.utmStorage = utmStorage;
		startTimer();
	}

	private void startTimer() {

		scheduler = Executors.newScheduledThreadPool(1);

		Runnable checkTimeTask = () -> {
			
//			System.out.println(getCurrentTime());
			
			if (getCurrentTime().equals("0:0"))
				startDoingSomeShit();
		};

		scheduler.scheduleAtFixedRate(checkTimeTask, 0, 60, TimeUnit.SECONDS);
	}

	private void startDoingSomeShit() {

		utmStorage.getUtmDatabaseManager().setAllUtmJoinsForYesterday(utmStorage.getUtmDatabaseManager().getAllUtmJoinsforToday());
		utmStorage.getUtmDatabaseManager().setAllUtmJoinsForToday(0);
		

		for (UtmTag tag  : utmStorage.getAllUtmTags()) {

			utmStorage.getUtmDatabaseManager().setUtmJoinsForYesterday(tag.getId(), tag.getJoinsForToday());
			utmStorage.getUtmDatabaseManager().setUtmJoinsForToday(tag.getId(), 0);
			
		}

		if (Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow")).get(Calendar.DAY_OF_MONTH) == 1) {

			utmStorage.getUtmDatabaseManager().setAllUtmJoinsForMonth(0);

			for (UtmTag tag  : utmStorage.getAllUtmTags()) {
				utmStorage.getUtmDatabaseManager().setUtmJoinsForMonth(tag.getId(), 0);
			}
		}

		if (getDayOfWeek().equals("понедельник")) {

			utmStorage.getUtmDatabaseManager().setAllUtmJoinsForWeek(0);

			for (UtmTag tag  : utmStorage.getAllUtmTags()) {
				utmStorage.getUtmDatabaseManager().setUtmJoinsForWeek(tag.getId(), 0);
			}
		}
	}

	private String getCurrentTime() {

		LocalDateTime timeAfterFiveMinutes = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H-m");

		return timeAfterFiveMinutes.format(formatter);
	}

	private String getDayOfWeek() {

		LocalDate date = LocalDate.now();

		DayOfWeek dayOfWeek = date.getDayOfWeek();
		Locale localeRu = new Locale("ru", "RU");

		return dayOfWeek.getDisplayName(TextStyle.FULL, localeRu);
	}

	public void stopTimer() {
		scheduler.shutdown();
	}
}