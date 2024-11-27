package DAMeti;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class FaqController {
	@FXML
	private void handleBackButtonAction(ActionEvent event) {
		App.changeScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/alumno1.fxml");
	}

	@FXML
	private void handleInicioButtonAction(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/inicio.fxml"));
		Parent inicioView = loader.load();
		Scene inicioScene = new Scene(inicioView);

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(inicioScene);
		stage.show();
	}	
}