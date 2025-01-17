package controller;

import exceptions.DuplicateSongException;
import exceptions.SongNotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MusicPlayer {

    // Key: songName + "||" + artist, Value: Song object
    private Map<String, Song> songsMap;

    // Key: artist, Value: List of song keys for that artist
    private Map<String, List<String>> artistToSongKeys;

    public MusicPlayer() {
        this.songsMap = new HashMap<>();
        this.artistToSongKeys = new HashMap<>();
    }

    // Helper method to create a unique key for each song
    private String generateKey(String songName, String artist) {
        // A delimiter that won't appear in songName or artist
        return songName + "||" + artist;
    }

    /**
     * 1. Add Song (Song name, Artist)
     */
    public void addSong(String songName, String artist) throws DuplicateSongException {
        String key = generateKey(songName, artist);
        if (songsMap.containsKey(key)) {
            throw new DuplicateSongException("Song [" + songName + "] by [" + artist + "] already exists.");
        }

        // Create and store the new Song
        Song newSong = new Song(songName, artist);
        songsMap.put(key, newSong);

        // Maintain the mapping from artist -> list of song keys
        artistToSongKeys.putIfAbsent(artist, new ArrayList<>());
        artistToSongKeys.get(artist).add(key);
    }

    /**
     * 2. Play Song (Song name, Artist)
     */
    public void playSong(String songName, String artist) throws SongNotFoundException {
        String key = generateKey(songName, artist);
        Song song = songsMap.get(key);
        if (song == null) {
            throw new SongNotFoundException("Song [" + songName + "] by [" + artist + "] not found.");
        }

        // Increment the counters
        song.incrementPlayCount(LocalDate.now());
    }

    /**
     * 3. RetrieveSongs(Artist) ⇒ gives Songs of artist
     */
    public List<String> retrieveSongs(String artist) {
        // Return the list of song names for that artist
        List<String> keys = artistToSongKeys.getOrDefault(artist, new ArrayList<>());
        // Convert key => actual song name
        return keys.stream()
                .map(k -> songsMap.get(k).getSongName())
                .collect(Collectors.toList());
    }

    /**
     * 4. TopSongs(int limit) => gives (Song, Artist)
     *    Return in decreasing order of total plays
     */
    public List<Song> topSongs(int limit) {
        // Sort all songs by total plays descending
        return songsMap.values().stream()
                .sorted((s1, s2) -> Integer.compare(s2.getTotalPlays(), s1.getTotalPlays()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 5. TopSongs(Artist) => gives Song (only the names; sorted by total plays)
     */
    public List<String> topSongsByArtist(String artist) {
        List<String> keys = artistToSongKeys.getOrDefault(artist, new ArrayList<>());
        // Filter songs of the artist, sort by plays desc, then map to song name
        return keys.stream()
                .map(k -> songsMap.get(k))
                .sorted((s1, s2) -> Integer.compare(s2.getTotalPlays(), s1.getTotalPlays()))
                .map(Song::getSongName)
                .collect(Collectors.toList());
    }

    /**
     * 6. TopSongs(By Date) => gives (Song, Artist)
     *    Return the top songs played on that specific date in decreasing frequency order (on that date).
     */
    public List<Song> topSongsByDate(LocalDate date) {
        // Filter only those songs that have a play count on this specific date
        List<Song> playedOnDate = songsMap.values().stream()
                .filter(song -> song.getPlayHistoryByDate().getOrDefault(date, 0) > 0)
                .collect(Collectors.toList());

        // Sort by the frequency on that date desc
        playedOnDate.sort((s1, s2) -> {
            int plays1 = s1.getPlayHistoryByDate().getOrDefault(date, 0);
            int plays2 = s2.getPlayHistoryByDate().getOrDefault(date, 0);
            return Integer.compare(plays2, plays1);
        });

        return playedOnDate;
    }

    /**
     * 7. SongPlayedLessThanFor(Days)
     *    Interpretation: Return all songs that have NOT been played in the last X days.
     */
    public List<Song> songsPlayedLessThanFor(int days) {
        LocalDate today = LocalDate.now();
        // We'll collect songs that have 0 plays for the interval [today - (days-1), today].

        List<Song> result = new ArrayList<>();

        for (Song s : songsMap.values()) {
            boolean playedRecently = false;
            for (int i = 0; i < days; i++) {
                LocalDate checkDate = today.minusDays(i);
                if (s.getPlayHistoryByDate().getOrDefault(checkDate, 0) > 0) {
                    playedRecently = true;
                    break;
                }
            }
            if (!playedRecently) {
                result.add(s);
            }
        }

        return result;
    }
}
