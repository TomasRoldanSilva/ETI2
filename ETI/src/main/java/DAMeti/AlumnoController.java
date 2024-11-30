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
	//clase inicial de alumno con los tres botones para elegir qué quiere hacer: registro, login o preguntas frecuentes. 

	@FXML
	
	private void abrirRegistro(ActionEvent event) throws IOException {
		// Cargar la vista de registro
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/registro.fxml"));
		Parent registroView = loader.load();

		// Crear una nueva escena con la vista de registro
		Scene registroScene = new Scene(registroView);

		// Obtener la ventana actual  y establecer la nueva escena
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

	@FXML
	private void mostrarFaq(ActionEvent event) throws IOException {
		try {
			// Cargar la vista de preguntas frecuentes
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/Faq.fxml"));
			Parent faqView = loader.load();
			// Crear una nueva escena con la vista FAQ
			Scene faqScene = new Scene(faqView);
			// Obtener el escenario actual y establecer la nueva escena
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(faqScene);
			stage.setTitle("Preguntas Frecuentes");
			stage.show();
		} catch (IOException e) {
			System.err.println("Error al cargar la vista de preguntas frecuentes: " + e.getMessage());
			e.printStackTrace();
		}
	}

}