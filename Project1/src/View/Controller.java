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
	
	//List of FXML Components
	@FXML ListView<String> SongList;
	@FXML TextArea SongDetails;
	@FXML Button AddSong;
	@FXML Button DeleteSong;
	@FXML Button EditSong;
	@FXML TextField SongNameAdd;
	@FXML TextField SongArtistAdd;
	@FXML TextField SongAlbumAdd;
	@FXML TextField SongYearAdd;
	@FXML Button EditConfirm;
	@FXML Button EditCancel;
	
	//Observable list to be added to SongList
	public ObservableList<String> list_view;
	
	//Head of the linked list - our main data structure
	public static Song head;
	public boolean read = false;
	
	/*
	 * Initialize method is called by default when main runs. Checks if a file exists and loads songs from the file if it does. 
	 * If the file does not exist, it creates a new file.
	 * Loads songs from the linked list into the observable list so they can be displayed, with corresponding details
	 * */
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
			//parse through the JSONArray in the file
			JSONArray a = (JSONArray) parser.parse(new FileReader(file));
			for(Object obj: a) {
				//For each object in the array, the key: value pair of the object is added to a linked list
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
		
		//Adding contents of the linked list to the observable arraylist
		while(temp != null) {
			list_view.add(temp.getName() + " - "+temp.getArtist());
			temp = temp.next;
		}
		
		SongList.setItems(list_view);
		
		//Set initial selection to first item in the list
		SongList.getSelectionModel().select(0);
		
		//Display details of the first item in the list
		displaySongs(primaryStage);
		
		/*
		 * Additional event handler for key presses. Shows the details of the song corresponding to the selector in the ListViews
		 * */
		SongList.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				
				//If the UP arrow key is pressed
				if(event.getCode() == KeyCode.UP) {
					int index = SongList.getSelectionModel().getSelectedIndex();
					setDetails(index - 1);
				}
				
				//If the DOWN arrow key is pressed
				if(event.getCode() == KeyCode.DOWN) {
					int index = SongList.getSelectionModel().getSelectedIndex();
					setDetails(index + 1);
				}
			}
			
		});
		
		/*
		 * Event handler for mouse clicking
		 * */
		SongList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent event) {
				 displaySongs(primaryStage);
            }
		});
	}
	
	/*
	 * Selects a particular index and calls setDetails to get the details at that index
	 * */
	public void displaySongs(Stage primaryStage) {
		int index = SongList.getSelectionModel().getSelectedIndex();
		if(index == -1) {
			index = 0;
		}
		SongList.getSelectionModel().clearAndSelect(index);
		setDetails(index);
	}
	
	/*
	 * Takes in the index, traverses through the linked list and displays details of the object on the TextArea
	 * @param	index	The index of the song in the observable list
	 * */
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
	
	/*
	 * Event handler when the Add Button is clicked. 
	 * */
	public void add_song(ActionEvent event) {
		
		//Checks if the Song Name and artist fields are blank
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
		
		//Tries parsing the text for an integer and pops an error if the year is not an integer
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
		
		//Confirmation pop-up/alert
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Add");
		alert.setContentText("Are you sure you want to add this song?");
		Optional<ButtonType> result = alert.showAndWait();
		
		/*If OK is clicked, adds song, checks for duplicates and selects that particular song and displays its 
		 * Details if the add is successful
		 * */
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
	
	/*
	 * Event handler when the Delete button is clicked
	 * */
	public void delete_song(ActionEvent event) {
		int index = SongList.getSelectionModel().getSelectedIndex();
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Delete");
		alert.setContentText("Are you sure you want to delete this song?");
		Optional<ButtonType> result = alert.showAndWait();
		
		//If the OK Button on the pop-up is clicked
		if(result.get() == ButtonType.OK) {
			Song temp = head;
			Song prev = temp;
			
			//Traversing through the linked list for the index
			for(int i = 0; i < index; i++) {
				prev = temp;
				if(temp.next != null) {
					temp = temp.next;
				}
			}
			
			//If no more songs are left in the list, clears the SongList
			if(temp == head) {
				if(head == null) {
					SongDetails.setText("");
					update_list();
					return;
				}
				head = head.next;
				SongList.getSelectionModel().select(index + 1);
				setDetails(index);
				update_list();
				return;
			}
			
			//Delete temp
			prev.next = temp.next;
			update_list();
			
			//If its the last song in the list, chooses the previous song and displays those details
			if(temp.next == null) {
				SongList.getSelectionModel().select(index - 1);
				setDetails(index);
				return;
			}
			
			//Chooses the next song after deletion
			SongList.getSelectionModel().select(index);
			setDetails(index);
		}
		
	}
	
	/*
	 * Event handler if the Edit button is clicked 
	 * */
	public void edit_song(ActionEvent event) {
		
		//Gets the index of the song selected and traverses through the linked list to get the song
		int index = SongList.getSelectionModel().getSelectedIndex();
		Song temp = head;
		for(int i = 0; i < index; i++) {
			if(temp != null) {
				temp = temp.next;
			}
		}
		String song_name = temp.getName();
		String song_artist = temp.getArtist();
		String song_album = temp.getAlbum();
		String song_year = Integer.toString(temp.getYear());
		
		//Sets the fields to the song information
		SongNameAdd.setText(song_name);
		SongArtistAdd.setText(song_artist);
		SongAlbumAdd.setText(song_album);
		SongYearAdd.setText(song_year);
		
		
		//Disables the add and delete buttons, shows the invisible Confirm and Cancel buttons
		EditConfirm.setVisible(true);
		EditCancel.setVisible(true);
		AddSong.setDisable(true);
		DeleteSong.setDisable(true);
	}
	
	/*
	 * Event handler if the Confirm button is clicked
	 * */
	public void edit_confirmed(ActionEvent event) {
		String new_name = SongNameAdd.getText();
		String new_artist = SongArtistAdd.getText();
		String new_album = SongAlbumAdd.getText();
		int new_year = 0;
		if(!SongYearAdd.getText().equals("")) {
			new_year = 0;
		}
		try {
			new_year = Integer.parseInt(SongYearAdd.getText());
		}
		catch(NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid input");
			alert.setContentText("Year needs to be an integer. Please try again");
			alert.showAndWait();
			return;
		}
		
		//Checks if the song is a duplicate after editing
		boolean check = check_duplicate(new_name, new_artist);
		if(!check) {
			int index = SongList.getSelectionModel().getSelectedIndex();
			Song temp = head;
			Song prev = temp;
			for(int i = 0; i < index; i++) {
				prev = temp;
				if(temp != null) {
					temp = temp.next;
				}
			}
			if(temp == head) {
				head = head.next;
			}
			
			//deletes the previous song
			temp.editName(new_name);
			temp.editArtist(new_artist);
			temp.editAlbum(new_album);
			temp.editYear(new_year);
			prev.next = temp.next;
			
			//Creates a new song with the new information
			int newIndex = add_song(new_name, new_artist, new_album, new_year);
			
			//Enables Add and Delete buttons and hides Confirm and Cancel buttons. Clears the fields
			AddSong.setDisable(false);
			DeleteSong.setDisable(false);
			SongNameAdd.setText("");
			SongArtistAdd.setText("");
			SongAlbumAdd.setText("");
			SongYearAdd.setText("");
			EditConfirm.setVisible(false);
			EditCancel.setVisible(false);
			update_list();
			SongList.getSelectionModel().select(newIndex);
			setDetails(newIndex);
		}
		else {
			//Error for duplicate songs
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Duplicate Song");
			alert.setContentText("Song already exists. Please try again");
			alert.showAndWait();
			AddSong.setDisable(false);
			DeleteSong.setDisable(false);
			SongNameAdd.setText("");
			SongArtistAdd.setText("");
			SongAlbumAdd.setText("");
			SongYearAdd.setText("");
			return;
		}
		
	}
	
	/*
	 * Event handler if the Cancel button is clicked for edit. Basically restores the app to its normal state
	 * */
	public void edit_cancelled(ActionEvent event) {
		AddSong.setDisable(false);
		DeleteSong.setDisable(false);
		SongNameAdd.setText("");
		SongArtistAdd.setText("");
		SongAlbumAdd.setText("");
		SongYearAdd.setText("");
		EditConfirm.setVisible(false);
		EditCancel.setVisible(false);
	}

	
	/*
	 * Creates a new song and adds it to its position in a linked list
	 * @param	song_name
	 * @param	song_artist
	 * @param	song_album
	 * @param	song_year
	 * @return	index of the position of the song, so it can be selected and details can be displayed 
	 * */
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
			if(curr.next == null && compare(curr,song) < 0) {
				//insert song after current, at end of list
				curr.next = song;
				index++;
				song.next = null;
			}else if(compare(curr,song) > 0) {
				if(curr == head) {
					//insert song at front of list
					song.next = curr;
					head = song;
				}else {
					//insert song before current, in middle of list
					prev.next = song;
					song.next = curr;
				}
				
			}
			
			/*if (curr == head) {
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
			}*/
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
	
	/*
	 * Traverses through the linked list and checks if the song already exists or not
	 * @param	song_name
	 * @param	song_artist
	 * @return	True if the song exists, false if it doesn't
	 * */
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
	
	/*
	 * Custom compare method that takes 2 song objects as parameter and returns -1, 0, 1 based on the position of the 2 songs
	 * @param	song1
	 * @param	song2
	 * @return	-1		if song1 comes before song2
	 * @return	0		if both songs are the same		//Should never happen!
	 * @return	1		if song2 comes before song1
	 * */
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
	
	/*
	 * Helper method to keep track of the main linked list and debug
	 * 
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
	*/
	
	/*
	 * Updates the list once add, edit or delete is called. Deletes the old observable list and creates a new one
	 * Sets the selected indexes to the correct index even after addition/deletion and shows details of the selected song
	 * */
	public void update_list() {
		Song temp = head;
		if(head == null) {
			SongList.getItems().removeAll();
		}
		list_view = FXCollections.observableArrayList();
		while(temp != null) {
			if(temp != null) {
				list_view.add(temp.getName() + " - "+temp.getArtist());
			}
			temp = temp.next;
		}
		SongList.setItems(list_view);
		if(list_view.isEmpty()) {
			SongDetails.setText("");
		}
	}
	
}
