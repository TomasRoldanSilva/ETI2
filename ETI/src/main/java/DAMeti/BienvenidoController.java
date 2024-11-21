package DAMeti;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;  // Importación corregida
import javafx.stage.Stage;

import java.io.IOException;

import Modelo.Alumno;

public class BienvenidoController {

	@FXML
    private Label lblNombre;

    

    private Alumno alumno;  // Crear una variable Alumno para usar en el controlador

    // Método para recibir datos del alumno
    public void cargarDatosAlumno(int id, String nombre, int curso) {
        this.alumno = new Alumno(id, nombre, curso, nombre, nombre, nombre, nombre);  // Guardamos el alumno
        lblNombre.setText(nombre);
    }
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        cambiarEscena((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/login.fxml");
    }

    @FXML
    private void handleInicioButtonAction(ActionEvent event) {
        cambiarEscena((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/inicio.fxml");
    }  

    @FXML
    private void handleConsultarCuentaAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/Consultarmisdatos.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva escena
            ConsultarDatosController consultarDatosController = loader.getController();
            consultarDatosController.setNombreAlumno(alumno.getNombre());  // Pasar el nombre del alumno

            // Cambiar la escena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Consultar Mis Datos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error de carga", "No se pudo cargar la pantalla de Consultar Mis Datos.");
        }
    }

    @FXML
    private void handlePedirLibroAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/pedirLibro.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva escena y pasar el objeto Alumno
            PedirLibroController pedirLibroController = loader.getController();
            pedirLibroController.setAlumno(alumno);  // Pasar el alumno al siguiente controlador

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarError("Error de carga", "No se pudo cargar la pantalla de Pedir Libro.");
            e.printStackTrace();
        }
    }

    // Método para cambiar la escena
    private void cambiarEscena(Stage stage, String rutaFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarError("Error de carga", "No se pudo cargar la pantalla especificada.");
            e.printStackTrace();
        }
    }

    // Muestra errores críticos
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
