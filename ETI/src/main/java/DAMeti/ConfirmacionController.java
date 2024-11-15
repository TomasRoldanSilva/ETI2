package DAMeti;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

public class ConfirmacionController {

    @FXML
    private Label mensajeConfirmacion;

    // Método para configurar el mensaje de confirmación
    public void setMensajeConfirmacion(String mensaje) {
        mensajeConfirmacion.setText(mensaje);
    }

    // Acción al hacer clic en "Aceptar"
    public void aceptarAction(ActionEvent event) {
       
           /* // Cerrar la ventana de confirmación
            Stage stage = (Stage) mensajeConfirmacion.getScene().getWindow();
            stage.close();
            
            // Cargar la ventana principal (pantalla de inicio)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/inicio.fxml")); 
            Parent root = loader.load();
            
            // Mostrar la ventana de inicio
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Pantalla de Inicio");
            primaryStage.setScene(new Scene(root, 800, 600)); // Ajusta el tamaño según sea necesario
            primaryStage.show();*/
                // Obtiene el stage (ventana actual)
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // Cierra la ventana
                stage.close();
        
        }
    }
