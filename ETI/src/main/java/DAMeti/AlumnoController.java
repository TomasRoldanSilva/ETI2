package DAMeti;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AlumnoController {

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
	    @FXML
	    private void abrirRegistro(ActionEvent event) throws IOException {
	        // Cargar la vista de registro
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/registro.fxml"));
	        Parent registroView = loader.load();
	        
	        // Crear una nueva escena con la vista de registro
	        Scene registroScene = new Scene(registroView);
	        
	        // Obtener la ventana actual (Stage) y establecer la nueva escena
	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.setScene(registroScene);
	        stage.show();
	    }
	    
	    @FXML
	    private void abrirLogin(ActionEvent event) throws IOException {
	        // Cargar la vista de registro
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/login.fxml"));
	        Parent registroView = loader.load();
	        
	        // Crear una nueva escena con la vista de registro
	        Scene registroScene = new Scene(registroView);
	        
	        // Obtener la ventana actual (Stage) y establecer la nueva escena
	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.setScene(registroScene);
	        stage.show();
	    }

	}