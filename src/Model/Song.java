package Model;

/**
 * Song Model Class Represents a single song entity
 */
public class Song {

    private String songId;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private int duration; // in seconds

    // Constructor
    public Song(String songId, String title, String artist, String album,
            String genre, int year, int duration) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.duration = duration;
    }

    // Getters
    public String getSongId() {
        return songId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public int getDuration() {
        return duration;
    }

    // Setters
    public void setSongId(String songId) {
        this.songId = songId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // Convert duration to mm:ss format
    public String getFormattedDuration() {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return title + " - " + artist + " (" + year + ")";
    }

    // For table display
    public Object[] toArray() {
        return new Object[]{songId, title, artist, album, genre, year, duration};
    }
}
