package Maks1mov.logger;

@SuppressWarnings("all")
public enum RecordLevel {

	WARNING("WARNING", 0), INFO("INFO", 1), ERROR("ERROR", 2), RAW("RAW", 3), TELEGRAM("TELEGRAM", 4);

	private static final RecordLevel[] $VALUES = new RecordLevel[] { WARNING, INFO, ERROR, RAW, TELEGRAM };

	private RecordLevel(String var1, int var2) {}
}