package DAMeti;

import Modelo.Libro;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import sql.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModificarLibroController {

    @FXML
    private TextField txtId;
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
    private Label lblMensaje;

    private Libro libro;
    private int originalId;  // Variable para almacenar el ID original

    // Método para cargar el libro y su ID original
    public void setLibro(Libro libro) {
        this.libro = libro;
        this.originalId = libro.getId();  // Guardar el ID original del libro
        cargarDatosLibro();
    }

    // Cargar los datos actuales del libro en los campos de texto
    private void cargarDatosLibro() {
        txtId.setText(String.valueOf(libro.getId()));
        txtTitulo.setText(libro.getTitulo());
        txtAsignatura.setText(libro.getAsignatura());
        txtCurso.setText(String.valueOf(libro.getCurso()));
        txtEditorial.setText(libro.getEditorial());
        txtIsbn.setText(libro.getIsbn());
        txtNumeroDeCopias.setText(String.valueOf(libro.getNumeroDeCopias()));
    }

    // Método para guardar cambios en la base de datos
    @FXML
    private void guardarCambios() {
        try {
            libro.setId(Integer.parseInt(txtId.getText()));
            libro.setTitulo(txtTitulo.getText());
            libro.setAsignatura(txtAsignatura.getText());
            libro.setCurso(Integer.parseInt(txtCurso.getText()));
            libro.setEditorial(txtEditorial.getText());
            libro.setIsbn(txtIsbn.getText());
            libro.setNumeroDeCopias(Integer.parseInt(txtNumeroDeCopias.getText()));

            try (Connection conn = conexion.dameConexion();
                 PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE libros SET id = ?, titulo = ?, asignatura = ?, curso = ?, editorial = ?, isbn = ?, numero_de_copias = ? WHERE id = ?")) {

                stmt.setInt(1, libro.getId());               // Nuevo ID
                stmt.setString(2, libro.getTitulo());
                stmt.setString(3, libro.getAsignatura());
                stmt.setInt(4, libro.getCurso());
                stmt.setString(5, libro.getEditorial());
                stmt.setString(6, libro.getIsbn());
                stmt.setInt(7, libro.getNumeroDeCopias());
                stmt.setInt(8, originalId);                 // ID original para la búsqueda

                int filasActualizadas = stmt.executeUpdate();
                if (filasActualizadas > 0) {
                    lblMensaje.setText("Libro modificado correctamente.");
                    lblMensaje.setStyle("-fx-text-fill: green;");
                } else {
                    lblMensaje.setText("No se pudo modificar el libro.");
                    lblMensaje.setStyle("-fx-text-fill: red;");
                }

                // Cerrar la ventana tras la modificación
                Stage stage = (Stage) lblMensaje.getScene().getWindow();
                stage.close();

            } catch (SQLException e) {
                e.printStackTrace();
                lblMensaje.setText("Error al actualizar el libro en la base de datos.");
                lblMensaje.setStyle("-fx-text-fill: red;");
            }
        } catch (NumberFormatException e) {
            lblMensaje.setText("Error: Verifique los campos numéricos.");
            lblMensaje.setStyle("-fx-text-fill: red;");
        }
    }
}

