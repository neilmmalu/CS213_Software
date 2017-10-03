package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {		
		
		launch(args);
//		save_session();
	}
	
	private static void save_session() throws IOException {
		
		File file = new File("songs.json");
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
		
		first = Controller.head;
		Song temp = first;
		JSONArray library = new JSONArray();
		while(temp != null) {
			JSONObject song = new JSONObject();
			song.put("name", temp.getName());
			song.put("artist", temp.getArtist());
			song.put("album", temp.getAlbum());
			song.put("year", temp.getYear());
			library.add((JSONObject)song);
			temp = temp.next;
		}
		FileWriter out = new FileWriter(file);
		library.writeJSONString(library, out);
		out.close();
	}
}

