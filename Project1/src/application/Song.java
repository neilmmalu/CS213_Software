package application;

public class Song {
	
	String name = "";
	String artist = "";
	String album = "";
	int year = 0;
	boolean exists = false;
	
	public Song(String name, String artist, String album, int year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getArtist() {
		return this.artist;
	}
	
	public String getAlbum() {
		return this.album;
	}
	
	public int getYear() {
		return this.year;
	}
	
	public void editName(String newName) {
		this.name = newName;
	}
	
	public void editArtist(String newArtist) {
		this.artist = newArtist;
	}
	
	public void editAlbum(String newAlbum) {
		this.album = newAlbum;
	}
	
	public void editYear(int newYear) {
		this.year = newYear;
	}
	
	public void checkExists(boolean check) {
		this.exists = check;
	}
	
	
}
