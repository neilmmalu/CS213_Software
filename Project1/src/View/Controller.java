package View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import application.Song;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import application.Song;
import application.Songlib;

public class Controller implements Initializable{
	
	@FXML ListView<String> SongList;
	@FXML TextArea SongDetails;
	@FXML Button AddSong;
	@FXML Button DeleteSong;
	@FXML Button EditSong;
	@FXML TextField SongNameAdd;
	@FXML TextField SongArtistAdd;
	@FXML TextField SongAlbumAdd;
	@FXML TextField SongYearAdd;
	
	public ObservableList<String> list_view;
	public static Song head;
	public boolean read = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		head = Songlib.first;
		Stage primaryStage = Songlib.primaryStage;
		File file = new File("songs.json");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JSONParser parser = new JSONParser();
		try {
			JSONArray a = (JSONArray) parser.parse(new FileReader(file));
			for(Object obj: a) {
				JSONObject Song = (JSONObject)obj;
				String song_name = String.valueOf(Song.get("name"));
				String song_artist = String.valueOf(Song.get("artist"));
				String song_album = String.valueOf(Song.get("album"));
				int song_year = Integer.parseInt(String.valueOf(Song.get("year")));
//				System.out.println(song_name);
				add_song(song_name, song_artist, song_album, song_year);
			}
		}
		catch(ParseException e) {
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		read = true;
		list_view = FXCollections.observableArrayList();
		Song temp = head;
		while(temp != null) {
			list_view.add(temp.getName());
			temp = temp.next;
		}
		
		SongList.setItems(list_view);
		SongList.getSelectionModel().select(0);
		displaySongs(primaryStage);
		SongList.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getCode() == KeyCode.UP) {
					int index = SongList.getSelectionModel().getSelectedIndex();
					setDetails(index - 1);
				}
				if(event.getCode() == KeyCode.DOWN) {
					int index = SongList.getSelectionModel().getSelectedIndex();
					setDetails(index + 1);
				}
			}
			
		});
		
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
			index = 0;
		}
		SongList.getSelectionModel().clearAndSelect(index);
		setDetails(index);
	}
	
	public void setDetails(int index) {
		Song temp = head;
		for(int i = 0; i < index; i++) {
			if(temp.next != null) {
				temp = temp.next;
			}
			else {
				break;
			}
		}
		if(temp == null) {
			return;
		}
		String song_name = temp.getName();
		String song_artist = temp.getArtist();
		String song_album = temp.getAlbum();
		String song_year = Integer.toString(temp.getYear());
		if(temp.getYear() == 0) {
			song_year = "";
		}
		SongDetails.setText("Name: "+song_name+ "\nArtist: "+song_artist+"\nAlbum: "+song_album+"\nYear: "+song_year);
	}
	
	public void add_song(ActionEvent event) {
		if(SongNameAdd.getText().equals("") || SongArtistAdd.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Input");
			alert.setContentText("Song name and artist required. Please try again");
			alert.showAndWait();
			return;
		}
		
		String song_name = SongNameAdd.getText();
		String song_artist = SongArtistAdd.getText();
		String song_album = "";
		if(!SongAlbumAdd.getText().equals("")) {
			song_album = SongAlbumAdd.getText();
		}
		int song_year = 0;
		if(!SongYearAdd.getText().equals("")) {
			try {
				song_year = Integer.parseInt(SongYearAdd.getText());
			}
			catch(NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Input");
				alert.setContentText("Song year should be an integer. Please try again");
				alert.showAndWait();
				return;
			}
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Add");
		alert.setContentText("Are you sure you want to add this song?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			int index = add_song(song_name, song_artist, song_album, song_year);
			update_list();
			SongList.getSelectionModel().select(index);
			displaySongs(Songlib.primaryStage);
			setDetails(index);
			SongNameAdd.setText("");
			SongArtistAdd.setText("");
			SongAlbumAdd.setText("");
			SongYearAdd.setText("");
		}
		else {
			return;
		}
	}
	
	public void delete_song(ActionEvent event) {
		int index = SongList.getSelectionModel().getSelectedIndex();
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Delete");
		alert.setContentText("Are you sure you want to delete this song?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			Song temp = head;
			Song prev = temp;
			for(int i = 0; i < index; i++) {
				prev = temp;
				if(temp.next != null) {
					temp = temp.next;
				}
			}
			if(temp == head) {
				if(head == null) {
					update_list();
					return;
				}
				head = head.next;
				SongList.getSelectionModel().select(index + 1);
				setDetails(index);
				update_list();
				return;
			}
			prev.next = temp.next;
			update_list();
			if(temp.next == null) {
				SongList.getSelectionModel().select(index - 1);
				setDetails(index);
				return;
			}
			SongList.getSelectionModel().select(index);
			setDetails(index);
		}
		
	}
	
	public void edit_song(ActionEvent event) {
		
	}

	
	
	public int add_song(String song_name, String song_artist, String song_album, int song_year) {
		boolean check = check_duplicate(song_name, song_artist);
		int index = 0;
		if (!check) {
			Song song = new Song(song_name, song_artist, song_album, song_year);
			Song curr = head;
			Song prev = null;
			if (curr == null) {
				head = song;
				return 0;
			}
			while(curr.next != null && compare(curr, song) < 0) {
				index++;
				prev = curr;
				curr = curr.next;
			}
			if (curr == head) {
				song.next = curr;
				head = song;
			}else if(compare(curr,song) > 0) {
				prev.next = song;
				song.next = curr;
			}else if(curr.next == null) {
				curr.next = song;
				index++;
				song.next = null;
			}else{
				//compare(curr, song) returns > 0
				song.next = curr.next;
				curr.next = song;
				index++;
			}
		}	
		else if(read){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Duplicate Song");
			alert.setContentText("Song already exists. Please try again");
			alert.showAndWait();
			return -1;
		}
		return index;
		
	}
	
	public Boolean check_duplicate(String song_name, String song_artist) {
		Song curr = head;
		while (curr != null) {
			if(curr.getName().equalsIgnoreCase(song_name)  && curr.getArtist().equalsIgnoreCase(song_artist)) {
				return true;
			}
			curr = curr.next;
		}
		return false;
	}
	
	public int compare(Song song1, Song song2) {
		try {
			int result = song1.getName().compareToIgnoreCase(song2.getName());
			if (result == 0) {
				result = song1.getArtist().compareToIgnoreCase(song2.getArtist());
			}
			return result;
		}
		catch(NullPointerException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	private void print(Song song) {
		if(song == null) {
			return;
		}
		Song temp = song;
		while(temp != null) {
			if(temp != null) {
				System.out.println(temp.getName());
			}
			temp = temp.next;
		}
	}
	
	public void update_list() {
		Song temp = head;
		if(head == null) {
			SongList.getItems().removeAll();
		}
		list_view = FXCollections.observableArrayList();
		while(temp != null) {
			if(temp != null) {
				list_view.add(temp.getName());
			}
			temp = temp.next;
		}
		SongList.setItems(list_view);
	}
	
}
