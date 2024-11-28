package DAMeti;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

/**
 * JavaFX App
 */
public class App extends Application {
	
	//hola soy tomas

    private static Stack<Scene> sceneHistory = new Stack<>();

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/inicio.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Biblioteca ETI. Disfruta de la lectura. ");
            primaryStage.show();

            // Agregar la escena inicial a la pila
            pushScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pushScene(Scene scene) {
        sceneHistory.push(scene);
    }

    public static Scene popScene() {
        return sceneHistory.isEmpty() ? null : sceneHistory.pop();
    }

    public static int getSceneHistorySize() {
        return sceneHistory.size();
    }

    public static Scene peekScene() {
        return sceneHistory.peek();
    }

    public static void changeScene(Stage stage, String fxmlPath) {
        try {
            // Agregar la escena actual a la pila solo si es diferente de la que ya está en la pila
            if (!sceneHistory.isEmpty() && stage.getScene() != sceneHistory.peek()) {
                pushScene(stage.getScene());
            }

            // Cargar y mostrar la nueva escena
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}