import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FakerAndroidGui extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
//		File file = Jar.getResourceAsFile("/FileChooser.fxml");
//
//		URL uri = file.toURI().toURL();
		Parent root = FXMLLoader.load(getClass().getResource("/FileChooser.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("FakerAndroid GUI");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
