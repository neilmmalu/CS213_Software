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
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import View.Controller;


public class Songlib extends Application {
	
	public static Stage primaryStage;
	public static Song first;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Songlib.primaryStage = primaryStage;
			Parent root = FXMLLoader.load(getClass().getResource("/View/Scene.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Song Library");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Launches the program and starts with the initialize() method in Controller
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {		
		launch(args);
		save_session();
	}
	
	/*
	 * Deletes the existing JSON file, creates a new one and stores the songs as JSONObjects in a JSONArray.
	 * Uses FileWriter to convert the array into a JSON string and writes the output to songs.json
	 * */
	@SuppressWarnings("unchecked")
	private static void save_session() throws IOException {
		
		File file = new File("songs.json");
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
		
		first = Controller.head;
		Song temp = first;
		JSONArray library = new JSONArray();
		
		//Traversing through the linked list
		while(temp != null) {
			
			//new JSON object for each song
			JSONObject song = new JSONObject();
			song.put("name", temp.getName());
			song.put("artist", temp.getArtist());
			song.put("album", temp.getAlbum());
			song.put("year", temp.getYear());
			
			//adding the object to the JSONArray
			library.add((JSONObject)song);
			temp = temp.next;
		}
		FileWriter out = new FileWriter(file);
		//Writing the array as a JSONString to the output file
		JSONArray.writeJSONString(library, out);
		out.close();
	}
}

