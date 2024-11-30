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
import sql.conexion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Modelo.Alumno;

public class BienvenidoController {
	//clase donde se recibe al alumno identificado y se le dan las opciones disponibles: consultar datos o pedir libros. 

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
    //metodo que verifica si tiene prestamos vencidos para que no pueda pedir nuevos libros 
    private boolean tienePrestamosVencidos(String nombreAlumno) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = conexion.dameConexion(); // Usar la conexión proporcionada
            String sql = "SELECT COUNT(*) AS cantidad FROM prestamos WHERE nombre_alumno = ? AND fecha_devolucion < CURDATE()";
            stmt = conn.prepareStatement(sql); // Crear el PreparedStatement
            stmt.setString(1, nombreAlumno); // Establecer el parámetro

            rs = stmt.executeQuery(); // Ejecutar la consulta
            if (rs.next()) {
                int cantidad = rs.getInt("cantidad");
                if (cantidad > 0) { // Si hay préstamos vencidos
                    mostrarError("Préstamos vencidos", 
                                 "No puedes solicitar un nuevo libro hasta que devuelvas los libros pendientes.");
                    return true; // Préstamos vencidos encontrados
                }
            }
        } catch (SQLException e) {
            mostrarError("Error de conexión a la base de datos", "No se pudo verificar los préstamos vencidos.");
            e.printStackTrace();
        } finally {
            // Liberar los recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false; // No hay préstamos vencidos permite pedir libros 
    }

//el alumno si no tiene libros vencidos pasará a la siguiente escena que es pedir libros
    @FXML
    private void handlePedirLibroAction(ActionEvent event) {
        // Verificar si el alumno tiene préstamos vencidos
        if (tienePrestamosVencidos(alumno.getNombre())) {
            // Redirigir al inicio
            cambiarEscena((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/inicio.fxml");
            return; // Bloquear el flujo
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/pedirLibro.fxml"));
            Parent root = loader.load();

            // Pasar el objeto Alumno al siguiente controlador
            PedirLibroController pedirLibroController = loader.getController();
            pedirLibroController.setAlumno(alumno); 

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
