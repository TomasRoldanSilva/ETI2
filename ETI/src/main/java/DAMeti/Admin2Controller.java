package DAMeti;


import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.awt.AWTException;

import javafx.event.ActionEvent;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Admin2Controller {

    @FXML
    private Button gestionarUsuarioButton;
    
    @FXML
    private Button gestionarLibroButton;
    
    @FXML
    private Button gestionarPeticionButton;
    
    @FXML
    private Button gestionarPrestamoButton;

    @FXML
    private Button backButton;
    
    @FXML
    private Button inicioButton;

    @FXML
    private void initialize() {
        
    }

    @FXML
    private void handleGestionarUsuarioButtonAction(ActionEvent event) {
        try {
            // Cargar el FXML de AlumnoGestion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/alumnoGestion.fxml")); 
            Parent root = loader.load();

            // Obtener el controlador de AlumnoGestionController
            AlumnoGestionController controller = loader.getController();
            controller.cargarDatos(); 

            // abre la ventana Gestionar Alumnos 
            Stage stage = new Stage();
            stage.setTitle("Gestionar Alumnos");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual
            closeWindow(event);

        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }

    @FXML
    private void handleGestionarLibroButtonAction(ActionEvent event) {
        try {
            // Carga el FXML de gestionarLibros 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/GestionarLibro.fxml"));
            Parent root = loader.load();

            
            GestionarLibroController controller = loader.getController();
            controller.cargarLibros();

          
            Stage stage = new Stage();
            stage.setTitle("Gestionar Libros");
            stage.setScene(new Scene(root));
            stage.show();
//
           
            closeWindow(event);

        } catch (Exception e) {
            e.printStackTrace();
           
        }
    }

//commit
    @FXML
    private void handleGestionarPeticionButtonAction(ActionEvent event) {
        try {
            // Cargar el FXML de la gestión de peticiones
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/Peticiones.fxml"));
            Parent root = loader.load();
            
            // Obtener el controlador de PeticionesController
            PeticionesController controller = loader.getController();
            controller.cargarPeticiones(); 

            // Crear una nueva escena y mostrarla en un nuevo escenario
            Stage stage = new Stage();
            stage.setTitle("Gestionar Peticiones");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual
            closeWindow(event);

        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }


    

    @FXML
    private void handleGestionarPrestamoButtonAction(ActionEvent event) {
    	   try {
               // Cargar el FXML de la gestión de peticiones
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/Prestamo.fxml"));
               Parent root = loader.load();
               
               // Obtener el controlador de PeticionesController
               PrestamoController controller = loader.getController();
               controller.cargarPrestamos(); 

               // Crear una nueva escena y mostrarla en un nuevo escenario
               Stage stage = new Stage();
               stage.setTitle("Gestionar Prestamo");
               stage.setScene(new Scene(root));
               stage.show();

               // Cerrar la ventana actual
               closeWindow(event);

           } catch (Exception e) {
               e.printStackTrace();
               // Manejo de errores
           }
        
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        // Regresa a la ventana anterior
        App.changeScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/admin1.fxml");
    }

    @FXML
    private void handleInicioButtonAction(ActionEvent event) {
        // Regresa a la pantalla de inicio
        App.changeScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/inicio.fxml");
    }

    // Método para cerrar la ventana
    private void closeWindow(ActionEvent event) {
        // Obtiene el stage (ventana actual)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Cierra la ventana
        stage.close();
    }
}
