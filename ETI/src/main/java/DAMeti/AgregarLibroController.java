package DAMeti;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
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
    private ComboBox<Integer> comboCurso;
    @FXML
    private TextField txtEditorial;
    @FXML
    private TextField txtIsbn;
    @FXML
    private TextField txtNumeroDeCopias;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lblStatus;
    private boolean modificado = false;


    private GestionarLibroController libroService;

    public void setLibroService(GestionarLibroController libroService) {
        this.libroService = libroService;
    }

    @FXML
    private void initialize() {
        comboCurso.getItems().addAll(1, 2, 3, 4, 5, 6);
    }

    @FXML
    private void guardarLibro() {
        String idText = txtId.getText();
        String titulo = txtTitulo.getText();
        String asignatura = txtAsignatura.getText();
        Integer curso = comboCurso.getValue();
        String editorial = txtEditorial.getText();
        String isbn = txtIsbn.getText();
        String numCopiasText = txtNumeroDeCopias.getText();

        if (idText.isEmpty() || titulo.isEmpty() || asignatura.isEmpty() || curso == null || editorial.isEmpty() || isbn.isEmpty() || numCopiasText.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Todos los campos son obligatorios.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            int numCopias = Integer.parseInt(numCopiasText);

            try (Connection conn = conexion.dameConexion();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO libros (id, titulo, asignatura, curso, editorial, isbn, numero_de_copias) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

                stmt.setInt(1, id);
                stmt.setString(2, titulo);
                stmt.setString(3, asignatura);
                stmt.setInt(4, curso);
                stmt.setString(5, editorial);
                stmt.setString(6, isbn);
                stmt.setInt(7, numCopias);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Libro añadido correctamente.");
                    if (libroService != null) {
                        libroService.cargarLibros(); // Actualizar libros en la ventana principal
                    }
                    cerrarVentana();
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al añadir libro.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error de conexión", "Error de conexión con la base de datos.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos Inválidos", "ID y Número de Copias deben ser numéricos.");
        }
    }


    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void handleBackButtonAction() {
        lblStatus.setText("Botón 'Atrás' presionado.");
    }

    @FXML
    private void handleInicioButtonAction() {
        lblStatus.setText("Botón 'Inicio' presionado.");
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }
}
