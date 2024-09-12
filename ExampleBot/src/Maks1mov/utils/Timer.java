package Maks1mov.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Maks1mov.Main;
import Maks1mov.logger.RecordLevel;
import Maks1mov.telegram.utils.BotUtils;

public class Timer {

    private int count = 0;
    private final ScheduledExecutorService scheduler;
    private final Random random;

    public Timer() {
    	
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.random = new Random();
        
        scheduleDailyTasks();
        
        Main.getLogger().log(RecordLevel.INFO, "Timer was started!", false);
    }

    private void scheduleDailyTasks() {
    	
        scheduler.scheduleAtFixedRate(() -> {
        	
            for (int i = 0; i < 3; i++) {
            	
                long delay = getRandomDelayInSeconds();
                scheduler.schedule(this::executeTask, delay, TimeUnit.SECONDS);
            }
            
        }, getInitialDelayUntilNextDay(), 24, TimeUnit.HOURS);
    }

    private void executeTask() {
    	
        if (count == 11) {
            count = 0;
        }
        
        count += 1;
        
        BotUtils.sendTextMessage("1745680928", "Рассылка началась!");
        Main.getLogger().log(RecordLevel.INFO, "Spam was started!", true);
        
        Main.getDatabase().startTgSpam(count);
    }

    private long getRandomDelayInSeconds() {
        return random.nextInt(24 * 60 * 60);
    }

    private long getInitialDelayUntilNextDay() {
    	
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDay = now.toLocalDate().plusDays(1).atStartOfDay();
        Duration duration = Duration.between(now, nextDay);
        return duration.getSeconds();
    }

    public void stop() {
        scheduler.shutdown();
    }
}