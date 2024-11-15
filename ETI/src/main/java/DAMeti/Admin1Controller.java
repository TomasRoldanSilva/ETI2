package DAMeti;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;  
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class Admin1Controller {

    @FXML
    private TextField nombreField;

    @FXML
    private PasswordField claveField;

    @FXML
    private Label mensajeLabel;

    @FXML
    private Button entrarButton; 

    // Nombre y clave del administrador
    private final String ADMIN_NOMBRE = "admin";
    private final String ADMIN_CLAVE = "1234";

    @FXML
    private void adminLogin(ActionEvent event) {
        String nombre = nombreField.getText();
        String clave = claveField.getText();

        if (nombre.equals(ADMIN_NOMBRE) && clave.equals(ADMIN_CLAVE)) {
            mensajeLabel.setText("Ingreso exitoso");
            
            // Redirige a la vista admin2.fxml
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/admin2.fxml"));
                Parent admin2View = loader.load();
                Scene admin2Scene = new Scene(admin2View);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(admin2Scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                mensajeLabel.setText("Error al cargar la vista de administrador.");
            }
        } else {
            mensajeLabel.setText("Nombre o clave incorrectos");
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
    	App.changeScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/inicio.fxml");

    }


    @FXML
    private void handleInicioButtonAction(ActionEvent event) throws IOException {
        // Cargar y mostrar la vista inicio.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/inicio.fxml"));
        Parent inicioView = loader.load();
        Scene inicioScene = new Scene(inicioView);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(inicioScene);
        stage.show();
    }

   

}