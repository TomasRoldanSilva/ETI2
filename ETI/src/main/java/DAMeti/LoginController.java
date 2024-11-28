package DAMeti;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Modelo.Alumno;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContra;
    
    private static final String URL = "jdbc:mysql://localhost:3306/eti"; // Ajusta el nombre de la base de datos
    private static final String USUARIO = "root"; // Ajusta el usuario de la base de datos
    private static final String CONTRASEÑA = ""; // Ajusta la contraseña si es necesario
    
    // Método para establecer la conexión a la base de datos
    public static Connection dameConexion() throws SQLException {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            System.out.println("Conexión establecida.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();
            throw e;
        }
        return conexion;
    }

    // Verifica las credenciales del usuario contra la base de datos
    private boolean verificarCredenciales(String usuario, String contraseña) {
        String sql = "SELECT * FROM alumnos WHERE usuario = ? AND contrasena = ?";
        
        try (Connection connection = dameConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);
            ResultSet resultSet = stmt.executeQuery();
            
            return resultSet.next(); // Si encuentra el usuario y la contraseña, devuelve true
        } catch (SQLException e) {
            mostrarError("Error de conexión a la base de datos", "No se pudo conectar a la base de datos.");
            e.printStackTrace();
            return false;
        }
    }

    // Maneja el botón de inicio de sesión
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String contraseña = txtContra.getText();

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta(AlertType.ERROR, "Campos vacíos", "Por favor, ingrese tanto el usuario como la contraseña.");
        } else {
            boolean autenticado = verificarCredenciales(usuario, contraseña);

            if (autenticado) {
                Alumno alumno = obtenerAlumno(usuario, contraseña); // Obtenemos el objeto Alumno
                
                if (alumno != null) {
                    mostrarAlerta(AlertType.INFORMATION, "Alumno identificado", "Hola, " + alumno.getNombre());
                    
                    // Cargar la vista del BienvenidoController
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/bienvenidoAlumno.fxml"));
                    
                    try {
                        Parent root = loader.load();
                        
                        // Obtener el controlador de BienvenidoController
                        BienvenidoController controller = loader.getController();
                        
                        // Pasar los datos del alumno al controlador de BienvenidoController
                        controller.cargarDatosAlumno(alumno.getId(), alumno.getNombre(), alumno.getCurso());
                        
                        // Cambiar a la pantalla de bienvenida
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        mostrarError("Error al cargar la pantalla", "No se pudo cargar la pantalla de bienvenida.");
                        e.printStackTrace();
                    }
                }
            } else {
                mostrarAlerta(AlertType.ERROR, "Contraseña o usuario incorrectos", "Usuario o contraseña incorrectos.");
            }
        }
    }


    // Método para obtener los datos del alumno desde la base de datos
    public Alumno obtenerAlumno(String usuario, String contrasena) {
        Alumno alumno = null;
        String query = "SELECT id, nombre, curso FROM alumnos WHERE usuario = ? AND contrasena = ?";
        try (Connection connection = dameConexion();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int curso = rs.getInt("curso");
                alumno = new Alumno(id, nombre, curso, "", "", usuario, contrasena);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alumno;
    }

    

  

    // Maneja el evento "Olvidé mi contraseña"
    @FXML
    private void handleOlvidoContrasenaAction(ActionEvent event) {
        String usuario = txtUsuario.getText();

        if (usuario.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Falta el usuario", "Por favor, ingrese su usuario.");
            return;
        }

        // Dialog para verificar el nombre del padre/madre
        TextField txtNombrePadreMadre = new TextField();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Recuperación de contraseña");
        alert.setHeaderText("Introduce el nombre del padre/madre asociado a tu cuenta");
        alert.getDialogPane().setContent(txtNombrePadreMadre);

        alert.showAndWait().ifPresent(response -> {
            String nombrePadreMadre = txtNombrePadreMadre.getText();

            if (verificarPadreMadre(usuario, nombrePadreMadre)) {
                cambiarContrasena(usuario);
            } else {
                mostrarAlerta(AlertType.ERROR, "Error de verificación", "El nombre del padre/madre no coincide con el registrado. Inténtalo de nuevo.");
            }
        });
    }

    // Verifica el nombre del padre/madre en la base de datos
    private boolean verificarPadreMadre(String usuario, String nombrePadreMadre) {
        String sql = "SELECT * FROM alumnos WHERE usuario = ? AND nombre_madre_padre = ?";

        try (Connection connection = dameConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, nombrePadreMadre);
            ResultSet resultSet = stmt.executeQuery();
            
            return resultSet.next();
        } catch (SQLException e) {
            mostrarError("Error de conexión a la base de datos", "No se pudo conectar a la base de datos.");
            e.printStackTrace();
            return false;
        }
    }

    // Cambia la contraseña en la base de datos
    private void cambiarContrasena(String usuario) {
        TextField txtNuevaContrasena = new TextField();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Recuperación de contraseña");
        alert.setHeaderText("Introduce tu nueva contraseña");
        alert.getDialogPane().setContent(txtNuevaContrasena);

        alert.showAndWait().ifPresent(response -> {
            String nuevaContrasena = txtNuevaContrasena.getText();
            if (!nuevaContrasena.isEmpty()) {
                actualizarContrasena(usuario, nuevaContrasena);
                mostrarAlerta(AlertType.INFORMATION, "Contraseña actualizada", "La contraseña se ha actualizado correctamente.");
            }
        });
    }

    // Actualiza la contraseña del usuario en la base de datos
    private void actualizarContrasena(String usuario, String nuevaContrasena) {
        String sql = "UPDATE alumnos SET contrasena = ? WHERE usuario = ?";

        try (Connection connection = dameConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevaContrasena);
            stmt.setString(2, usuario);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                mostrarAlerta(AlertType.ERROR, "Error al actualizar", "No se pudo actualizar la contraseña");
            }
        } catch (SQLException e) {
            mostrarError("Error de conexión a la base de datos", "No se pudo actualizar la contraseña.");
            e.printStackTrace();
        }
    }

 // Método para cambiar la escena
    private void cambiarEscena(Stage stage) {
        try {
            // Ruta a la pantalla de bienvenida del alumno
            Parent root = FXMLLoader.load(getClass().getResource("/DAM/ETI/bienvenidoAlumno.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarError("Error de carga", "No se pudo cargar la pantalla especificada.");
            e.printStackTrace();
        }
    }
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
    	App.changeScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/alumno1.fxml");

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

    // Muestra alertas de información o error
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Muestra mensajes de error específicos
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
