package View;

import java.util.ArrayList;

import application.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller {
	
	@FXML ListView<String> SongList;
	@FXML TextArea SongDetails;
	@FXML Button AddSong;
	@FXML Button DeleteSong;
	@FXML Button EditSong;
	@FXML TextField SongNameAdd;
	@FXML TextField SongArtistAdd;
	@FXML TextField SongAlbumAdd;
	@FXML TextField SongYearAdd;
	
	private ObservableList<String> list_view;
	private ArrayList<Song> songs;
	
	public void start(Stage primaryStage, ArrayList<Song> masterList) {
		if(masterList == null) {
			return;
		}
		list_view = FXCollections.observableArrayList();
		this.songs = masterList;
		for(int i = 0; i < masterList.size(); i++) {
			if(masterList.isEmpty()) {
				break;
			}
			list_view.add(masterList.get(i).getName());
		}
		
		SongList.setItems(list_view);
		SongList.getSelectionModel().select(0);
		SongList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent event) {
				 displaySongs(primaryStage);
            }
		});

	}
	
	public void displaySongs(Stage primaryStage) {
		int index = SongList.getSelectionModel().getSelectedIndex();
		if(index == -1) {
			for(int i = 0; i < songs.size(); i++) {
				if(songs.get(i).exists) {
					index = i;
					break;
				}
			}
		}
		if(index == -1) {
			index = 0;
		}
		SongList.getSelectionModel().clearAndSelect(index);
		setDetails(index);
	}
	
	public void setDetails(int index) {
		String song_name = songs.get(index).getName();
		String song_artist = songs.get(index).getArtist();
		String song_album = songs.get(index).getAlbum();
		String song_year = Integer.toString(songs.get(index).getYear());
		SongDetails.setText("Name: "+song_name+ "\nArtist: "+song_artist+"\nAlbum: "+song_album+"\nYear: "+song_year);
	}
	
	public void add_song(ActionEvent event) {
		if(SongNameAdd.getText().equals("") || SongArtistAdd.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Input");
			alert.setContentText("Song name and artist required. Please try again");
			alert.showAndWait();
		}
		
		String song_name = SongNameAdd.getText();
		String song_artist = SongArtistAdd.getText();
		String song_album = "";
		if(!SongAlbumAdd.getText().equals("")) {
			song_album = SongAlbumAdd.getText();
		}
		int song_year = 0;
		try {
			song_year = Integer.parseInt(SongYearAdd.getText());
		}
		catch(NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Input");
			alert.setContentText("Song year should be an integer. Please try again");
			alert.showAndWait();
		}
		
//		add_song(song_name, song_artist, song_album, song_year);
	}
	
	public void delete_song(ActionEvent event) {
		
	}
	
	public void edit_song(ActionEvent event) {
		
	}
	
/*	private void add_song(String song_name, String song_artist, String song_album, int song_year) {
		boolean check = check_duplicate(song_name, song_artist);
		
		if(!check) {
			Song song = new Song(song_name, song_artist, song_album, song_year);
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Duplicate Song");
			alert.setContentText("Song already exists. Please try again");
			alert.showAndWait();
			return;
		}
	}*/
	
}
