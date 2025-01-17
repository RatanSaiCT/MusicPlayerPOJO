import controller.MusicPlayer;
import controller.Song;
import exceptions.DuplicateSongException;
import exceptions.SongNotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

//class Song {
//    String name;
//    String artist;
//    int playCount;
//    Date lastPlayed;
//
//    public Song(String name, String artist) {
//        this.name = name;
//        this.artist = artist;
//        this.playCount = 0;
//        this.lastPlayed = null;
//    }
//
//    public void play() {
//        playCount++;
//        lastPlayed = new Date();
//    }
//
//    public String toString() {
//        return "[" + name + ", " + artist + "]";
//    }
//}

//class MusicPlayer {
//    private Map<String, List<Song>> artistSongs;
//    private List<Song> songList;
//
//    public MusicPlayer() {
//        artistSongs = new HashMap<>();
//        songList = new ArrayList<>();
//    }
//
//    // 1. Add Song
//    public void addSong(String name, String artist) {
//        Song newSong = new Song(name, artist);
//        songList.add(newSong);
//        artistSongs.computeIfAbsent(artist, k -> new ArrayList<>()).add(newSong);
//    }
//
//    // 2. Play Song
//    public void playSong(String name, String artist) {
//        for (Song song : songList) {
//            if (song.name.equals(name) && song.artist.equals(artist)) {
//                song.play();
//                return;
//            }
//        }
//        throw new IllegalArgumentException("Song not found: " + name + " by " + artist);
//    }
//
//    // 3. Retrieve Songs by Artist
//    public List<Song> retrieveSongs(String artist) {
//        return artistSongs.getOrDefault(artist, Collections.emptyList());
//    }
//
//    // 4. Top Songs by Limit
//    public List<Song> topSongs(int limit) {
//        return songList.stream()
//                .sorted((a, b) -> Integer.compare(b.playCount, a.playCount))
//                .limit(limit)
//                .collect(Collectors.toList());
//    }
//
//    // 5. Top Songs by Artist
//    public List<Song> topSongs(String artist) {
//        return retrieveSongs(artist).stream()
//                .sorted((a, b) -> Integer.compare(b.playCount, a.playCount))
//                .collect(Collectors.toList());
//    }
//
//    // 6. Top Songs by Date
//    public List<Song> topSongsByDate() {
//        return songList.stream()
//                .filter(song -> song.lastPlayed != null)
//                .sorted((a, b) -> b.lastPlayed.compareTo(a.lastPlayed))
//                .collect(Collectors.toList());
//    }
//
//    // 7. Songs Played Less Than For Specified Days
//    public List<Song> songsPlayedLessThanFor(int days) {
//        Date thresholdDate = new Date(System.currentTimeMillis() - (days * 24L * 60 * 60 * 1000));
//        return songList.stream()
//                .filter(song -> song.lastPlayed == null || song.lastPlayed.before(thresholdDate))
//                .collect(Collectors.toList());
//    }
//}

//public class MusicPlayerApp {
//    public static void main(String[] args) {
//
//        MusicPlayer player = new MusicPlayer();
//
//        // Adding songs
//        player.addSong("Song1", "Artist1");
//        player.addSong("Song2", "Artist1");
//        player.addSong("Song3", "Artist2");
//
//        // Playing songs
//        player.playSong("Song1", "Artist1");
//        player.playSong("Song1", "Artist1");
//        player.playSong("Song3", "Artist2");
//
//        // Retrieve Songs by Artist
//        System.out.println("Songs by Artist1: " + player.retrieveSongs("Artist1"));
//
//        // Top Songs by Limit
//        System.out.println("Top 2 Songs: " + player.topSongs(2));
//
//        // Top Songs by Artist
//        System.out.println("Top Songs by Artist1: " + player.topSongs("Artist1"));
//
//        // Top Songs by Date
//        System.out.println("Top Songs by Date: " + player.topSongsByDate());
//
//        // Songs Played Less Than For 1 Day
//        System.out.println("Songs played less than 1 day ago: " + player.songsPlayedLessThanFor(1));
//
//    }
//}



public class Main {
    public static void main(String[] args) {
        MusicPlayer player = new MusicPlayer();

        try {
            // 1. Add songs
            player.addSong("Song A", "Artist X");
            player.addSong("Song B", "Artist X");
            player.addSong("Song C", "Artist Y");

            // 2. Play songs
            player.playSong("Song A", "Artist X");
            player.playSong("Song A", "Artist X");
            player.playSong("Song B", "Artist X");
            player.playSong("Song C", "Artist Y");

            // 3. Retrieve songs by artist
            System.out.println("Songs of Artist X: " + player.retrieveSongs("Artist X"));

            // 4. Top Songs by limit
            System.out.println("Top 2 Songs overall:");
            for (Song s : player.topSongs(2)) {
                System.out.println(s.getSongName() + " - " + s.getArtist()
                        + " (Total Plays: " + s.getTotalPlays() + ")");
            }

            // 5. Top songs of an artist (names only)
            System.out.println("Top Songs of Artist X: " + player.topSongsByArtist("Artist X"));


            Scanner sc = new Scanner(System.in);
            int num = sc.nextInt();

            // 6. Top songs by a specific date
            LocalDate today = LocalDate.now();
            System.out.println("Top Songs played today: ");
            for (Song s : player.topSongsByDate(today)) {
                System.out.println(s.getSongName() + " - " + s.getArtist()
                        + " (Plays today: "
                        + s.getPlayHistoryByDate().get(today) + ")");
            }

            // 7. Songs not played in the last 2 days
            System.out.println("Songs not played in the last 2 days: ");
            for (Song s : player.songsPlayedLessThanFor(2)) {
                System.out.println(s.getSongName() + " by " + s.getArtist());
            }

        } catch (DuplicateSongException | SongNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}