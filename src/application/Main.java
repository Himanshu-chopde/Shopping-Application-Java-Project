package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("HomePage.fxml"));
			Scene scene = new Scene(root);						//@../../../seller/SellerLoginPage.fxml
			scene.getStylesheets().add(getClass().getResource("/styles/ButtonStyle.css").toExternalForm());
			primaryStage.setScene(scene);						
			primaryStage.getIcons().add(new Image(("/icons/ShoppingAppLogo.gif")));
			primaryStage.setResizable(false);
			primaryStage.setFullScreen(true);
			primaryStage.show();
			primaryStage.setFullScreen(false);
			//primaryStage.setMaximized(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
