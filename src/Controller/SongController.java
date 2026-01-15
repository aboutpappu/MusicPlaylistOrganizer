package Controller;

import Model.Song;
import Model.SongStack;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.table.DefaultTableModel;

/**
 * Controller for Song operations
 * Handles CRUD, Search, and Sort operations
 */
public class SongController {
    private ArrayList<Song> songList;
    private SongStack deletedSongsStack;
    private ArrayList<Song> recentSongs;
    private static final int MAX_RECENT = 5;

    public SongController() {
        songList = new ArrayList<>();
        deletedSongsStack = new SongStack();
        recentSongs = new ArrayList<>();
        initializeSampleData();
    }

    // Initialize with sample data for testing
    private void initializeSampleData() {
        addSong(new Song("S001", "Bohemian Rhapsody", "Queen", "A Night at the Opera", "Rock", 1975, 354));
        addSong(new Song("S002", "Imagine", "John Lennon", "Imagine", "Pop", 1971, 183));
        addSong(new Song("S003", "Billie Jean", "Michael Jackson", "Thriller", "Pop", 1983, 294));
        addSong(new Song("S004", "Stairway to Heaven", "Led Zeppelin", "Led Zeppelin IV", "Rock", 1971, 482));
        addSong(new Song("S005", "Hotel California", "Eagles", "Hotel California", "Rock", 1977, 391));
    }

    // CRUD Operations
    public boolean addSong(Song song) {
        if (findSongById(song.getSongId()) != null) {
            return false; // Song ID already exists
        }
        songList.add(song);
        addToRecent(song);
        return true;
    }

    public boolean updateSong(Song updatedSong) {
        for (int i = 0; i < songList.size(); i++) {
            if (songList.get(i).getSongId().equals(updatedSong.getSongId())) {
                songList.set(i, updatedSong);
                return true;
            }
        }
        return false;
    }

    public boolean deleteSong(String songId) {
        Song song = findSongById(songId);
        if (song != null) {
            deletedSongsStack.push(song);
            songList.remove(song);
            return true;
        }
        return false;
    }

    public Song undoDelete() {
        Song song = deletedSongsStack.pop();
        if (song != null) {
            songList.add(song);
        }
        return song;
    }

    // Find operations
    public Song findSongById(String songId) {
        for (Song song : songList) {
            if (song.getSongId().equals(songId)) {
                return song;
            }
        }
        return null;
    }

    // Linear Search - searches through all songs
    public ArrayList<Song> linearSearch(String keyword, String searchBy) {
        ArrayList<Song> results = new ArrayList<>();
        keyword = keyword.toLowerCase();

        for (Song song : songList) {
            boolean match = false;
            switch (searchBy) {
                case "Title":
                    match = song.getTitle().toLowerCase().contains(keyword);
                    break;
                case "Artist":
                    match = song.getArtist().toLowerCase().contains(keyword);
                    break;
                case "Album":
                    match = song.getAlbum().toLowerCase().contains(keyword);
                    break;
                case "Genre":
                    match = song.getGenre().toLowerCase().contains(keyword);
                    break;
                case "Year":
                    match = String.valueOf(song.getYear()).contains(keyword);
                    break;
            }
            if (match) {
                results.add(song);
            }
        }
        return results;
    }

    // Binary Search - requires sorted list (by title)
    public Song binarySearch(String title) {
        // First, create a sorted copy
        ArrayList<Song> sorted = new ArrayList<>(songList);
        sorted.sort(Comparator.comparing(Song::getTitle, String.CASE_INSENSITIVE_ORDER));

        int left = 0;
        int right = sorted.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = sorted.get(mid).getTitle().compareToIgnoreCase(title);

            if (comparison == 0) {
                return sorted.get(mid);
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }

    // Sorting operations
    public ArrayList<Song> sortSongs(String sortBy, boolean ascending) {
        ArrayList<Song> sorted = new ArrayList<>(songList);
        
        Comparator<Song> comparator = switch (sortBy) {
            case "Title" -> Comparator.comparing(Song::getTitle, String.CASE_INSENSITIVE_ORDER);
            case "Artist" -> Comparator.comparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER);
            case "Album" -> Comparator.comparing(Song::getAlbum, String.CASE_INSENSITIVE_ORDER);
            case "Genre" -> Comparator.comparing(Song::getGenre, String.CASE_INSENSITIVE_ORDER);
            case "Year" -> Comparator.comparingInt(Song::getYear);
            case "Duration" -> Comparator.comparingInt(Song::getDuration);
            default -> Comparator.comparing(Song::getTitle, String.CASE_INSENSITIVE_ORDER);
        };

        if (!ascending) {
            comparator = comparator.reversed();
        }

        sorted.sort(comparator);
        return sorted;
    }

    // Recent songs management
    private void addToRecent(Song song) {
        recentSongs.remove(song); // Remove if already exists
        recentSongs.add(0, song); // Add to beginning
        if (recentSongs.size() > MAX_RECENT) {
            recentSongs.remove(recentSongs.size() - 1);
        }
    }

    public ArrayList<Song> getRecentSongs() {
        return new ArrayList<>(recentSongs);
    }

    // Get all songs
    public ArrayList<Song> getAllSongs() {
        return new ArrayList<>(songList);
    }

    // Statistics
    public int getTotalSongs() {
        return songList.size();
    }

    public String getSummaryStatistics() {
        if (songList.isEmpty()) {
            return "No songs in the playlist yet.";
        }

        int totalDuration = songList.stream().mapToInt(Song::getDuration).sum();
        int hours = totalDuration / 3600;
        int minutes = (totalDuration % 3600) / 60;

        StringBuilder summary = new StringBuilder();
        summary.append("Total Songs: ").append(songList.size()).append("\n\n");
        summary.append("Total Duration: ").append(hours).append("h ").append(minutes).append("m\n\n");
        
        // Count by genre
        summary.append("Songs by Genre:\n");
        songList.stream()
            .map(Song::getGenre)
            .distinct()
            .forEach(genre -> {
                long count = songList.stream()
                    .filter(s -> s.getGenre().equals(genre))
                    .count();
                summary.append("  • ").append(genre).append(": ").append(count).append("\n");
            });

        return summary.toString();
    }

    // Generate next song ID
    public String generateNextId() {
        int maxId = 0;
        for (Song song : songList) {
            try {
                int id = Integer.parseInt(song.getSongId().substring(1));
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Skip invalid IDs
            }
        }
        return String.format("S%03d", maxId + 1);
    }

    // Table model population
    public void populateTable(DefaultTableModel model, ArrayList<Song> songs) {
        model.setRowCount(0);
        for (Song song : songs) {
            model.addRow(song.toArray());
        }
    }
}