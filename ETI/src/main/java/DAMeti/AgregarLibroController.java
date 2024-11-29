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
        String idText = txtId.getText().trim(); // Permite entradas vacías para id
        String titulo = txtTitulo.getText().trim();
        String asignatura = txtAsignatura.getText().trim();
        Integer curso = comboCurso.getValue();
        String editorial = txtEditorial.getText().trim();
        String isbn = txtIsbn.getText().trim();
        String numCopiasText = txtNumeroDeCopias.getText().trim();

        // Verifica campos obligatorios (excepto id)
        if (titulo.isEmpty() || asignatura.isEmpty() || curso == null || editorial.isEmpty() || isbn.isEmpty() || numCopiasText.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Todos los campos (excepto ID) son obligatorios.");
            return;
        }

        // Validación adicional para ISBN
        if (isbn.length() != 13 || !isbn.matches("\\d+")) {
            mostrarAlerta(Alert.AlertType.WARNING, "ISBN Inválido", "El ISBN debe contener exactamente 13 dígitos numéricos.");
            return;
        }

        try {
            int numCopias = Integer.parseInt(numCopiasText); // Valida que sea numérico

            String sql;
            boolean incluyeId = !idText.isEmpty();
            // Si se incluye el ID o no, cambia la consulta SQL
            if (incluyeId) {
                sql = "INSERT INTO libros (id, titulo, asignatura, curso, editorial, isbn, numero_de_copias) VALUES (?, ?, ?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO libros (titulo, asignatura, curso, editorial, isbn, numero_de_copias) VALUES (?, ?, ?, ?, ?, ?)";
            }

            try (Connection conn = conexion.dameConexion();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                int paramIndex = 1; // Índice del parámetro en la consulta

                // Asignar ID solo si el usuario lo ingresó
                if (incluyeId) {
                    int id = Integer.parseInt(idText); // Valida que sea numérico
                    stmt.setInt(paramIndex++, id);
                }

                // Asigna los demás valores
                stmt.setString(paramIndex++, titulo);
                stmt.setString(paramIndex++, asignatura);
                stmt.setInt(paramIndex++, curso);
                stmt.setString(paramIndex++, editorial);
                stmt.setString(paramIndex++, isbn);
                stmt.setInt(paramIndex++, numCopias);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Libro añadido", "Libro añadido correctamente.");
                    if (libroService != null) {
                        libroService.cargarLibros(); // Actualizar libros en la ventana principal
                    }
                    cerrarVentana(); // Cerrar ventana tras éxito
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al añadir libro.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error de conexión", "Error de conexión con la base de datos.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos Inválidos", "ID (si se introduce) y Número de Copias deben ser numéricos.");
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
