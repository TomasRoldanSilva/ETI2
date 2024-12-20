package DAMeti;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sql.conexion;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

// Implementa Initializable para inicializar el ComboBox
public class RegistroController implements Initializable {

    @FXML
    private TextField nombreCompletoField;

    @FXML
    private ComboBox<Integer> cursoField;  // Cambiado a ComboBox<Integer>

    @FXML
    private TextField nombreMadrePadreField;

    @FXML
    private TextField nombreTutorField;

    @FXML
    private TextField nombreUsuarioField;

    @FXML
    private PasswordField contrasenaField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Agrega valores del 1 al 6 al ComboBox de curso
        cursoField.getItems().addAll(1, 2, 3, 4, 5, 6);
    }

    @FXML
    private void registerAlumno() {
        String nombreCompleto = nombreCompletoField.getText();
        Integer curso = cursoField.getValue();  // Obtiene el valor seleccionado
        String nombreMadrePadre = nombreMadrePadreField.getText();
        String nombreTutor = nombreTutorField.getText();
        String nombreUsuario = nombreUsuarioField.getText();
        String contrasena = contrasenaField.getText();

        // Validación de campos vacíos
        if (nombreCompleto.isEmpty() || curso == null || nombreMadrePadre.isEmpty() || nombreTutor.isEmpty() || nombreUsuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Error", "Por favor, rellena todos los campos obligatorios. Los que tienen un *.");
            return;
        }

        // Validación de letras en campos específicos, permitiendo tildes y ñ
        if (!nombreCompleto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+") || 
            !nombreMadrePadre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+") || 
            !nombreTutor.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            mostrarAlerta("Error", "Los campos de nombres solo deben contener letras, tildes y espacios.");
            return;
        }

        try (Connection connection = conexion.dameConexion()) {
            // Verificar si el nombre ya existe en la base de datos
            String checkNombreSql = "SELECT COUNT(*) AS cantidad FROM alumnos WHERE nombre = ?";
            PreparedStatement checkNombreStmt = connection.prepareStatement(checkNombreSql);
            checkNombreStmt.setString(1, nombreCompleto);

            ResultSet rsNombre = checkNombreStmt.executeQuery();
            if (rsNombre.next() && rsNombre.getInt("cantidad") > 0) {
                mostrarAlerta("Nombre duplicado", 
                              "El nombre ya está registrado. Por favor, utiliza un número o el segundo apellido para diferenciarlo.");
                return; // Detener el flujo de registro
            }

            // Verificar si el nombre de usuario ya existe en la base de datos
            String checkUsuarioSql = "SELECT COUNT(*) AS cantidad FROM alumnos WHERE usuario = ?";
            PreparedStatement checkUsuarioStmt = connection.prepareStatement(checkUsuarioSql);
            checkUsuarioStmt.setString(1, nombreUsuario);

            ResultSet rsUsuario = checkUsuarioStmt.executeQuery();
            if (rsUsuario.next() && rsUsuario.getInt("cantidad") > 0) {
                mostrarAlerta("Usuario duplicado", 
                              "El nombre de usuario ya está registrado. Por favor, elige uno diferente.");
                return; // Detener el flujo de registro
            }

            // Si el nombre y el usuario no existen, proceder con la inserción
            String insertSql = "INSERT INTO alumnos (nombre, curso, nombre_madre_padre, nombre_tutor, usuario, contrasena) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(insertSql);
            stmt.setString(1, nombreCompleto);
            stmt.setInt(2, curso);
            stmt.setString(3, nombreMadrePadre);
            stmt.setString(4, nombreTutor);
            stmt.setString(5, nombreUsuario);
            stmt.setString(6, contrasena);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                mostrarAlerta("Registro correcto", "Alumno registrado correctamente.");

                // Limpiar los campos tras el registro exitoso
                nombreCompletoField.clear();
                cursoField.setValue(null);
                nombreMadrePadreField.clear();
                nombreTutorField.clear();
                nombreUsuarioField.clear();
                contrasenaField.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error en el registro", "Error al registrar el usuario en la base de datos.");
        }
    }

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

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
