package DAMeti;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Modelo.Libro;
import sql.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AgregarLibroController {

    @FXML
    private TextField txtId;         
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAsignatura;
    @FXML
    private ComboBox<Integer> comboCurso; // Cambio a ComboBox para el curso
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
    private void initialize() {
        // Inicializar ComboBox con valores del 1 al 6
        comboCurso.getItems().addAll(1, 2, 3, 4, 5, 6);
    }

    @FXML
    private void guardarLibro() {
        // Obtener valores de los campos de texto
        String idText = txtId.getText();  
        String titulo = txtTitulo.getText();
        String asignatura = txtAsignatura.getText();
        Integer curso = comboCurso.getValue(); // Obtener el valor seleccionado del ComboBox
        String editorial = txtEditorial.getText();
        String isbn = txtIsbn.getText();
        String numCopiasText = txtNumeroDeCopias.getText();

        // Verificar que todos los campos estén llenos
        if (idText.isEmpty() || titulo.isEmpty() || asignatura.isEmpty() || curso == null || editorial.isEmpty() || isbn.isEmpty() || numCopiasText.isEmpty()) {
            lblStatus.setText("Todos los campos son obligatorios.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            int numCopias = Integer.parseInt(numCopiasText);

            try (Connection conn = conexion.dameConexion();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO libros (id, titulo, asignatura, curso, editorial, isbn, numero_de_copias) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

                stmt.setInt(1, id);
                stmt.setString(2, titulo);
                stmt.setString(3, asignatura);
                stmt.setInt(4, curso); // Usar el valor seleccionado del ComboBox
                stmt.setString(5, editorial);
                stmt.setString(6, isbn);
                stmt.setInt(7, numCopias);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    lblStatus.setText("Libro añadido correctamente.");
                    if (libroService != null) {
                        libroService.cargarLibros();
                    }
                } else {
                    lblStatus.setText("Error al añadir libro.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lblStatus.setText("Error de conexión con la base de datos.");
            }
        } catch (NumberFormatException e) {
            lblStatus.setText("ID y Número de Copias deben ser numéricos.");
        }
    }

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
        modificado = false;
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
