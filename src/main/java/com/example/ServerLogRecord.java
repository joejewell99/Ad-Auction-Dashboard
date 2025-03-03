package com.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogRecord {

    final private static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private LocalDateTime entryDate;
    private String Id;
    private LocalDateTime exitDate;
    private int pagesViewed;
    private boolean conversion;

    public ServerLogRecord(String entryDate,
                           String Id,
                           String exitDate,
                           int pagesViewed,
                           boolean conversion) {
        if (!entryDate.equalsIgnoreCase("n/a")) {
            this.entryDate = LocalDateTime.parse(entryDate, DateTimeFormatter.ofPattern(DATE_PATTERN));
        }
        this.Id = Id;
        if (!exitDate.equalsIgnoreCase("n/a")) {
            this.exitDate = LocalDateTime.parse(exitDate, DateTimeFormatter.ofPattern(DATE_PATTERN));
        }
        this.pagesViewed = pagesViewed;
        this.conversion = conversion;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public String getId() {
        return Id;
    }

    public LocalDateTime getExitDate() {
        return exitDate;
    }

    public int getPagesViewed() {
        return pagesViewed;
    }

    public boolean isConversion() {
        return conversion;
    }

    public long browseTime() {
        if (this.entryDate == null || this.exitDate == null) {
            return 0;
        } else {
            Duration interval = Duration.between(this.entryDate, this.exitDate);
            return interval.toSeconds();
        }
    }

    @Override
    public String toString() {
        return "ServerLogRecord{" +
                "entryDate=" + entryDate +
                ", Id='" + Id + '\'' +
                ", exitDate=" + exitDate +
                ", pagesViewed=" + pagesViewed +
                ", conversion=" + conversion +
                '}';
    }
}
