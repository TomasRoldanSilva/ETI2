package DAMeti;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import java.io.IOException;

public class InicioController {

    @FXML
    private void handleAlumnoButtonAction(ActionEvent event) throws IOException {
        
        Scene currentScene = ((Node) event.getSource()).getScene();
        App.pushScene(currentScene);

        // Cargar la nueva vista de Alumno
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/alumno1.fxml"));
        Parent alumnoView = loader.load();
        Scene alumnoScene = new Scene(alumnoView);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(alumnoScene);
        stage.show();
    }

    @FXML
    private void handleAdminButtonAction(ActionEvent event) throws IOException {
        // Obtener la escena actual y almacenarla en la pila
        Scene currentScene = ((Node) event.getSource()).getScene();
        App.pushScene(currentScene);

        // Cargar la nueva vista de Admin
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/admin1.fxml"));
        Parent adminView = loader.load();
        Scene adminScene = new Scene(adminView);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(adminScene);
        stage.show();
    }
    
   
}