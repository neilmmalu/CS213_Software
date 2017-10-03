package application;

import java.util.Comparator;

public class Song {
	
	String name = "";
	String artist = "";
	String album = "";
	int year = 0;
	public boolean exists = false;
	public Song next = null;
	
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
	
//	public static class Compare implements Comparator<Song>{
//
//		@Override
//		public int compare(Song o1, Song o2) {
//			// TODO Auto-generated method stub
//			return o1.getName().compareToIgnoreCase(o2.getName());
//		}
//		
//		public boolean equals(Song o1, Song o2) {
//			return o1.getName().toLowerCase().equals(o2.getName().toLowerCase()) 
//					&& o1.getArtist().toLowerCase().equals(o2.getArtist().toLowerCase());
//		}
//		
//	}
}
