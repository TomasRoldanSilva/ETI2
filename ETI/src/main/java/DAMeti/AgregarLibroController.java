package DAMeti;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Modelo.Libro;
import sql.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AgregarLibroController {

    @FXML
    private TextField txtId;         // Nuevo campo de texto para ID
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAsignatura;
    @FXML
    private TextField txtCurso;
    @FXML
    private TextField txtEditorial;
    @FXML
    private TextField txtIsbn;
    @FXML
    private TextField txtNumeroDeCopias;
    @FXML
    private Label lblStatus;
    @FXML
    private Button btnCancelar;

    private GestionarLibroController libroService;
    
    private boolean modificado = false;

    public void setLibroService(GestionarLibroController libroService) {
        this.libroService = libroService;
    }

    @FXML
    private void guardarLibro() {
        // Obtener valores de los campos de texto
        String idText = txtId.getText();  // ID como texto
        String titulo = txtTitulo.getText();
        String asignatura = txtAsignatura.getText();
        String cursoText = txtCurso.getText();
        String editorial = txtEditorial.getText();
        String isbn = txtIsbn.getText();
        String numCopiasText = txtNumeroDeCopias.getText();

        // Verificar que todos los campos estén llenos
        if (idText.isEmpty() || titulo.isEmpty() || asignatura.isEmpty() || cursoText.isEmpty() || editorial.isEmpty() || isbn.isEmpty() || numCopiasText.isEmpty()) {
            lblStatus.setText("Todos los campos son obligatorios.");
            return;
        }

        // Validar que ID, Curso y Número de Copias sean numéricos
        try {
            // Convertir el ID, Curso y Número de Copias a enteros
            int id = Integer.parseInt(idText); // Convertir ID a int
            int curso = Integer.parseInt(cursoText); // Convertir Curso a int
            int numCopias = Integer.parseInt(numCopiasText); // Convertir Número de Copias a int

            // Guardar en la base de datos
            try (Connection conn = conexion.dameConexion();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO libros (id, titulo, asignatura, curso, editorial, isbn, numero_de_copias) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

                // Asignar los parámetros al PreparedStatement
                stmt.setInt(1, id);                 
                stmt.setString(2, titulo);
                stmt.setString(3, asignatura);
                stmt.setInt(4, curso);
                stmt.setString(5, editorial);
                stmt.setString(6, isbn);
                stmt.setInt(7, numCopias);

                // Ejecutar la consulta de inserción
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    lblStatus.setText("Libro añadido correctamente.");
                    if (libroService != null) {
                        libroService.cargarLibros();  // Recargar los libros si el servicio está disponible
                    }
                } else {
                    lblStatus.setText("Error al añadir libro.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lblStatus.setText("Error de conexión con la base de datos.");
            }
        } catch (NumberFormatException e) {
            lblStatus.setText("ID, Curso y Número de Copias deben ser numéricos.");
        }
    }

    // Métodos adicionales para botones de navegación
    @FXML
    private void handleBackButtonAction() {
        lblStatus.setText("Botón 'Atrás' presionado.");
    }

    @FXML
    private void handleInicioButtonAction() {
        lblStatus.setText("Botón 'Inicio' presionado.");
    }
    
    @FXML
    private void cancelar() {
        modificado = false;  // Si se cancela, no se considera ninguna modificación
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
