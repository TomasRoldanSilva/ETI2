package DAMeti;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import Modelo.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Modelo.Libro;
import sql.conexion; 

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
    private TableColumn<Libro, String> txtIsbn; // Updated to match ISBN property type

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

    // Initialize the table columns and load data if needed
    @FXML
    public void initialize() {
        // Set cell value factories to match the property names in the Libro model class
        txtId.setCellValueFactory(new PropertyValueFactory<>("id"));
        txtTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        txtAsignatura.setCellValueFactory(new PropertyValueFactory<>("asignatura"));
        txtCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        txtIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn")); // Ensuring property name is correct
        txtEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        txtNumeroDeCopias.setCellValueFactory(new PropertyValueFactory<>("numeroDeCopias")); // Match the property name

        // Load the list of books into the table
        cargarLibros();
    }

    // Other methods remain unchanged



    // Method to load books into the table (can connect to a database or service)
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
                int NumeroDeCopias = rs.getInt("numero_de_copias");

                // Create a Libro object with retrieved data
                Libro libro = new Libro(id, titulo, asignatura, curso, editorial, isbn, NumeroDeCopias);
                listaLibros.add(libro);
            }

            // Set the list to the TableView
            tablaLibros.setItems(listaLibros);

        } catch (SQLException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cargar libros.");
        }
    }


    // Search for a book by ID
 // Dentro de LibroService.java

 // Método para buscar un libro por ID en la base de datos
 private Optional<Libro> buscarLibroID(int libroId) {
     try (Connection conn = conexion.dameConexion();
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery("SELECT * FROM libros WHERE id = " + libroId)) {

         if (rs.next()) {
             // Si se encuentra un resultado, crear un objeto Libro con los datos obtenidos
             int id = rs.getInt("id");
             String titulo = rs.getString("titulo");
             String asignatura = rs.getString("asignatura");
             int curso = rs.getInt("curso");
             String editorial = rs.getString("editorial");
             String isbn = rs.getString("isbn");
             int numeroDeCopias = rs.getInt("numero_de_copias");

             // Crear objeto Libro con los datos
             Libro libro = new Libro(id, titulo, asignatura, curso, editorial, isbn, numeroDeCopias);
             return Optional.of(libro);
         }

     } catch (SQLException e) {
         e.printStackTrace();
         lblMensaje.setText("Error al buscar el libro en la base de datos.");
     }
     return Optional.empty();
 }

 // Método que se ejecuta al hacer clic en el botón de búsqueda
 @FXML
 private void buscarPorID() {
     String idText = txtBuscarLibroID.getText();

     if (idText.isEmpty()) {
         lblMensaje.setText("Por favor, introduzca un ID.");
         return;
     }

     try {
         int libroId = Integer.parseInt(idText);
         Optional<Libro> libroEncontrado = buscarLibroID(libroId);

         if (libroEncontrado.isPresent()) {
             // Si el libro existe, limpiamos la tabla y mostramos solo el libro encontrado
             tablaLibros.getItems().clear();
             tablaLibros.getItems().add(libroEncontrado.get());
             lblMensaje.setText("Libro encontrado.");
         } else {
             lblMensaje.setText("Libro no encontrado.");
         }

     } catch (NumberFormatException e) {
         lblMensaje.setText("ID debe ser numérico.");
     }
 }


    @FXML
    private void anadirLibro() {
        try {
            // Load the FXML file for adding a new book
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/AñadirLibro.fxml"));
            Parent root = loader.load();

            // Get the controller of the new window
            AgregarLibroController controller = loader.getController();
            controller.setLibroService(this); // Pass current instance if needed

            // Show the Add Book form in a new window
            Stage stage = new Stage();
            stage.setTitle("Añadir Nuevo Libro");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al abrir la ventana para añadir libro.");
        }
    }


 // Método para modificar el libro seleccionado en la tabla
    @FXML
    private void modificarLibro() {
        // Verificar que haya un libro seleccionado
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado == null) {
            // Crear una alerta de tipo WARNING
            Alert alerta = new Alert(AlertType.WARNING);
            alerta.setTitle("Selección de libro");
            alerta.setHeaderText(null);  // No hay cabecera
            alerta.setContentText("Seleccione un libro para modificar.");
            
            // Mostrar la alerta
            alerta.showAndWait();
            return;
        }

        try {
            // Cargar la vista de ModificarLibro.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/ModificarLibro.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista de modificación
            ModificarLibroController controller = loader.getController();
            
            // Pasar el libro seleccionado al controlador
            controller.setLibro(libroSeleccionado);

            // Abrir la ventana de modificación en un nuevo Stage
            Stage stage = new Stage();
            stage.setTitle("Modificar Libro");
            stage.setScene(new Scene(root));
            stage.show();

            // Al cerrar la ventana de modificación, recargar la lista de libros
            stage.setOnHidden(e -> cargarLibros());

        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al abrir la ventana de modificación.");
        }
    }

    @FXML
    private void eliminarLibro() {
        // Obtiene el libro seleccionado de la tabla
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado == null) {
            // Crear una alerta de tipo WARNING
            Alert alerta = new Alert(AlertType.WARNING);
            alerta.setTitle("Selección de libro");
            alerta.setHeaderText(null);  // No hay cabecera
            alerta.setContentText("Seleccione un libro para eliminarlo.");
            
            // Mostrar la alerta
            alerta.showAndWait();
            return;
        }

        // Crear un cuadro de diálogo de confirmación
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmar Eliminación");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("¿Está seguro de que desea eliminar el libro seleccionado?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Intenta eliminar el libro de la base de datos
            try (Connection conn = conexion.dameConexion();
                 Statement stmt = conn.createStatement()) {

                // Ejecuta una consulta SQL para eliminar el libro por su ID
                String sql = "DELETE FROM libros WHERE id = " + libroSeleccionado.getId();
                int filasEliminadas = stmt.executeUpdate(sql);

                if (filasEliminadas > 0) {
                    // Elimina el libro de la lista observable y muestra un mensaje
                    tablaLibros.getItems().remove(libroSeleccionado);
                    lblMensaje.setText("Libro eliminado correctamente.");
                } else {
                    lblMensaje.setText("No se pudo eliminar el libro de la base de datos.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lblMensaje.setText("Error al eliminar el libro de la base de datos.");
            }
        }
    }

    // Go back to the previous screen
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
    	App.changeScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/admin2.fxml");

        }
    

    // Go to the main menu
    @FXML
    private void handleInicioButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/inicio.fxml"));
        Parent inicioView = loader.load();
        Scene inicioScene = new Scene(inicioView);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(inicioScene);
        stage.show();
    }
}
