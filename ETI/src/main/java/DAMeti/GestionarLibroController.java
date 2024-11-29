package DAMeti;

import Modelo.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sql.conexion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class GestionarLibroController {

    @FXML
    private TableView<Libro> tablaLibros;

    @FXML
    private TableColumn<Libro, Integer> txtId;

    @FXML
    private TableColumn<Libro, String> txtTitulo;

    @FXML
    private TableColumn<Libro, String> txtAsignatura;

    @FXML
    private TableColumn<Libro, Integer> txtCurso;

    @FXML
    private TableColumn<Libro, String> txtIsbn;

    @FXML
    private TableColumn<Libro, String> txtEditorial;

    @FXML
    private TableColumn<Libro, Integer> txtNumeroDeCopias;

    @FXML
    private Label lblMensaje;

    @FXML
    private TextField txtBuscarLibroID;

    @FXML
    private Button backButton, inicioButton;

    @FXML
    public void initialize() {
        txtId.setCellValueFactory(new PropertyValueFactory<>("id"));
        txtTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        txtAsignatura.setCellValueFactory(new PropertyValueFactory<>("asignatura"));
        txtCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        txtIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        txtEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        txtNumeroDeCopias.setCellValueFactory(new PropertyValueFactory<>("numeroDeCopias"));

        cargarLibros();
    }

    public void cargarLibros() {
        ObservableList<Libro> listaLibros = FXCollections.observableArrayList();

        try (Connection conn = conexion.dameConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM libros")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String asignatura = rs.getString("asignatura");
                int curso = rs.getInt("curso");
                String editorial = rs.getString("editorial");
                String isbn = rs.getString("isbn");
                int numeroDeCopias = rs.getInt("numero_de_copias");

                Libro libro = new Libro(id, titulo, asignatura, curso, editorial, isbn, numeroDeCopias);
                listaLibros.add(libro);
            }

            tablaLibros.setItems(listaLibros);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaInformacion("Error", "Error al cargar los libros.");
        }
    }

    private Optional<Libro> buscarLibroID(int libroId) {
        try (Connection conn = conexion.dameConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM libros WHERE id = " + libroId)) {

            if (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String asignatura = rs.getString("asignatura");
                int curso = rs.getInt("curso");
                String editorial = rs.getString("editorial");
                String isbn = rs.getString("isbn");
                int numeroDeCopias = rs.getInt("numero_de_copias");

                Libro libro = new Libro(id, titulo, asignatura, curso, editorial, isbn, numeroDeCopias);
                return Optional.of(libro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaInformacion("Error", "Error al buscar el libro.");
        }
        return Optional.empty();
    }

    @FXML
    private void buscarPorID() {
        String idText = txtBuscarLibroID.getText();

        if (idText.isEmpty()) {
            mostrarAlertaInformacion("Error", "Por favor, introduzca un ID.");
            return;
        }

        try {
            int libroId = Integer.parseInt(idText);
            Optional<Libro> libroEncontrado = buscarLibroID(libroId);

            if (libroEncontrado.isPresent()) {
                tablaLibros.getItems().clear();
                tablaLibros.getItems().add(libroEncontrado.get());
                mostrarAlertaInformacion("Libro encontrado", "El libro con ID " + libroId + " fue encontrado.");
            } else {
                mostrarAlertaInformacion("Libro no encontrado", "No se encontró ningún libro con el ID " + libroId + ".");
            }

        } catch (NumberFormatException e) {
            mostrarAlertaInformacion("ID inválido", "El ID debe ser un valor numérico.");
        }
    }

    @FXML
    private void anadirLibro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/AñadirLibro.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Añadir Nuevo Libro");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaInformacion("Error", "Error al abrir la ventana para añadir libro.");
        }
    }

    @FXML
    private void modificarLibro() {
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado == null) {
            mostrarAlertaInformacion("Advertencia", "Seleccione un libro para modificar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/ModificarLibro.fxml"));
            Parent root = loader.load();

            ModificarLibroController controller = loader.getController();
            controller.setLibro(libroSeleccionado);

            Stage stage = new Stage();
            stage.setTitle("Modificar Libro");
            stage.setScene(new Scene(root));
            stage.show();

            stage.setOnHidden(e -> cargarLibros());

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaInformacion("Error", "Error al abrir la ventana de modificación.");
        }
    }

    @FXML
    private void eliminarLibro() {
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado == null) {
            mostrarAlertaInformacion("Advertencia", "Seleccione un libro para eliminar.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmar Eliminación");
        confirmDialog.setContentText("¿Está seguro de que desea eliminar el libro seleccionado?");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = conexion.dameConexion();
                 Statement stmt = conn.createStatement()) {

                String sql = "DELETE FROM libros WHERE id = " + libroSeleccionado.getId();
                int filasEliminadas = stmt.executeUpdate(sql);

                if (filasEliminadas > 0) {
                    tablaLibros.getItems().remove(libroSeleccionado);
                    mostrarAlertaInformacion("Eliminar libro", "El libro fue eliminado correctamente.");
                } else {
                    mostrarAlertaInformacion("Error", "No se pudo eliminar el libro.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlertaInformacion("Error", "Error al eliminar el libro.");
            }
        }
    }

    private void mostrarAlertaInformacion(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/admin2.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaInformacion("Error", "No se pudo volver al menú anterior.");
        }
    }

    @FXML
    private void handleInicioButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/inicio.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaInformacion("Error", "No se pudo cargar el inicio.");
        }
    }
}
