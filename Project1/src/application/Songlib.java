package application;
	
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Songlib extends Application {
	
	static ArrayList<Song> masterList;
	AnchorPane root;
	@Override
	public void start(Stage primaryStage) {
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
	
	public static void main(String[] args) {
		masterList = new ArrayList<Song>();
		
		launch(args);
	}
}

