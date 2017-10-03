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
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Songlib extends Application {
	
	static ArrayList<Song> masterList;
	Stage primaryStage;
	AnchorPane root;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Song Library");
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/Scene.fxml"));
			
			root = (AnchorPane) loader.load();
			Controller listController = loader.getController();
			listController.start(primaryStage, masterList);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		masterList = new ArrayList<Song>();
		File file = new File("songs.json");
		if(file.exists()) {
			load_songs(file);
		}
		else {
			System.out.println("not exists");
			file.createNewFile();
		}
		
//		launch(args);
		save_session();
	}
	
	private static void load_songs(File file) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		try {
			JSONArray a = (JSONArray) parser.parse(new FileReader(file));
			for(Object obj: a) {
				JSONObject Song = (JSONObject)obj;
				String song_name = String.valueOf(Song.get("name"));
				String song_artist = String.valueOf(Song.get("artist"));
				String song_album = String.valueOf(Song.get("album"));
				int song_year = Integer.parseInt(String.valueOf(Song.get("year")));
				Song song = new Song(song_name, song_artist, song_album, song_year);
				masterList.add(song);
			}
		}
		catch(ParseException e) {
			
		}
	}
	
	private static void save_session() throws IOException {
		
		File file = new File("songs.json");
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
		
		JSONArray library = new JSONArray();
		for(int i = 0; i < masterList.size(); i++) {
			JSONObject song = new JSONObject();
			song.put("name", masterList.get(i).getName());
			song.put("artist", masterList.get(i).getArtist());
			song.put("album", masterList.get(i).getAlbum());
			song.put("year", masterList.get(i).getYear());
			library.add((JSONObject)song);
			
		}
		FileWriter out = new FileWriter(file);
		library.writeJSONString(library, out);
		out.close();
	}
}

