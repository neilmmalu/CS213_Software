/*
 * Name: Neil Malu
 * NetID: nmm182
 * Section: 04
 * 
 * Name: Christopher Iverson
 * Net ID: cji16
 * Section: 02
 * 
 * Software Methodology Project 1
 * 
 * Song Library
 * */

package application;

public class Song {
	
	String name = "";
	String artist = "";
	String album = "";
	int year = 0;
	public boolean exists = false;
	public Song next = null;
	
	//Constructor for creating a new song abject
	public Song(String name, String artist, String album, int year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	
	/*
	 * Returns private variables in a non static way
	 * */
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
