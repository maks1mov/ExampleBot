package Maks1mov.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	private File log;

	public Logger() {
		
		log = new File("core.log");
		
		if (!log.exists()) {
			
			try {
				log.createNewFile();
			} catch (IOException var2) {
				System.out.println("Can not create core.log: " + var2.getMessage());
			}
		}
	}

	public String getLogFormat(RecordLevel level, String s) {
		
		SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
		Date now = new Date();
		
		return "[" + date.format(now) + "]" + " [" + level.toString() + "] " + s;
	}

	public void log(RecordLevel level, String s) {
		
		String str = getLogFormat(level, s);
		System.out.println(str);
		
		try {
			addRecord(log, level, s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void log(RecordLevel level, String s, boolean command) {
		
		String str = getLogFormat(level, s);
		System.out.println(str);

		if (command) {
			System.out.print(">");
		}

		try {
			addRecord(log, level, s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addRecord(File log, RecordLevel level, String line) throws IOException {
		
		BufferedWriter writer = null;

		writer = new BufferedWriter(new FileWriter(log, true));

		if (level != RecordLevel.RAW) {
			writer.write(getLogFormat(level, line));
		} else {
			writer.write(line);
		}

		writer.newLine();
		writer.close();
	}
}