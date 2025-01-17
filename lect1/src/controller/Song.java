package controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Song {
    private String songName;
    private String artist;
    private int totalPlays;
    // Stores date -> number of plays on that date
    private Map<LocalDate, Integer> playHistoryByDate;

    public Song(String songName, String artist) {
        this.songName = songName;
        this.artist = artist;
        this.totalPlays = 0;
        this.playHistoryByDate = new HashMap<>();
    }

    public String getSongName() {
        return songName;
    }

    public String getArtist() {
        return artist;
    }

    public int getTotalPlays() {
        return totalPlays;
    }

    public Map<LocalDate, Integer> getPlayHistoryByDate() {
        return playHistoryByDate;
    }

    // Called whenever song is played
    public void incrementPlayCount(LocalDate date) {
        this.totalPlays++;
        this.playHistoryByDate.put(date,
                this.playHistoryByDate.getOrDefault(date, 0) + 1
        );
    }
}