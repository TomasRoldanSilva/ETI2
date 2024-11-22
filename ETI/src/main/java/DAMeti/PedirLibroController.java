package DAMeti;

import Modelo.Libro;
import Modelo.Alumno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.io.IOException;

public class PedirLibroController {

    @FXML
    private TableView<Libro> tablaLibros;
    @FXML
    private TableColumn<Libro, String> colTitulo;
    @FXML
    private TableColumn<Libro, String> colAsignatura;
    @FXML
    private TableColumn<Libro, Integer> colCurso;
    @FXML
    private TableColumn<Libro, String> colEditorial;
    @FXML
    private TableColumn<Libro, String> colIsbn;
    @FXML
    private TableColumn<Libro, Integer> colCopias;
    @FXML
    private Button btnSolicitarLibro;

    private Alumno alumno;
    private ObservableList<Libro> listaLibros = FXCollections.observableArrayList();

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
        cargarLibros();
    }

    // Cargar los libros correspondientes al curso del alumno
    public void cargarLibros() {
        if (alumno == null) {
            System.err.println("El objeto Alumno es null. No se puede cargar la lista de libros.");
            return;
        }

        listaLibros.clear();
        String sql = "SELECT * FROM libros WHERE curso = ?";

        try (Connection connection = dameConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, alumno.getCurso());
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titulo = resultSet.getString("titulo");
                String asignatura = resultSet.getString("asignatura");
                int curso = resultSet.getInt("curso");
                String editorial = resultSet.getString("editorial");
                String isbn = resultSet.getString("isbn");
                int numeroDeCopias = resultSet.getInt("numero_de_copias");

                listaLibros.add(new Libro(id, titulo, asignatura, curso, editorial, isbn, numeroDeCopias));
            }

            tablaLibros.setItems(listaLibros);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener la conexión
    private static Connection dameConexion() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/eti", "root", "");
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public void initialize() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAsignatura.setCellValueFactory(new PropertyValueFactory<>("asignatura"));
        colCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colCopias.setCellValueFactory(new PropertyValueFactory<>("numeroDeCopias"));
    }

    @FXML
    public void solicitarLibro() {
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (libroSeleccionado != null) {
            if (libroSeleccionado.getNumeroDeCopias() <= 0) {
                // Si no hay copias disponibles, mostramos un mensaje de error
                mostrarAlerta(Alert.AlertType.ERROR, "Libro No Disponible", 
                              "Ahora mismo el libro no está disponible. Por favor consulte en el mostrador.");
            } else {
                // Si hay copias disponibles, procesamos la solicitud
                registrarPeticion(libroSeleccionado);
            }
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Por favor, seleccione un libro antes de solicitarlo.");
        }
    }


    private void registrarPeticion(Libro libro) {
        String sql = "INSERT INTO peticiones (nombre_alumno, curso, id_libro, titulo_libro, isbn_libro, fecha_peticion, numero_copias) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dameConexion();
             PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {  // Recupera claves generadas

            stmt.setString(1, alumno.getNombre());              // Nombre del alumno
            stmt.setInt(2, alumno.getCurso());                  // Curso del alumno
            stmt.setInt(3, libro.getId());                      // ID del libro
            stmt.setString(4, libro.getTitulo());               // Título del libro
            stmt.setString(5, libro.getIsbn());                 // ISBN del libro
            stmt.setDate(6, Date.valueOf(LocalDate.now()));     // Fecha de la petición
            stmt.setInt(7, libro.getNumeroDeCopias());          // Número de copias disponibles

            stmt.executeUpdate();  // Ejecutar la inserción

            // Obtener el número de la petición generado automáticamente
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int numeroPeticion = generatedKeys.getInt(1); // Recupera el número de la petición

                // Llamamos a la pantalla de confirmación con el número de la petición y mensaje personalizado
                mostrarPantallaConfirmacion("Su número de petición es: " + numeroPeticion );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error en la Solicitud", "No se pudo registrar la petición en la base de datos.");
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
    private void filtrarLibrosPorAsignatura(javafx.event.ActionEvent event) {
        Button boton = (Button) event.getSource();
        String asignaturaSeleccionada = boton.getText();

        ObservableList<Libro> librosFiltrados = listaLibros.filtered(libro -> libro.getAsignatura().equals(asignaturaSeleccionada));
        tablaLibros.setItems(librosFiltrados);
    }

    @FXML
    private void verTodosLibros() {
        tablaLibros.setItems(listaLibros);
    }

    // Método para mostrar la pantalla de confirmación
    private void mostrarPantallaConfirmacion(String mensaje) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/Confirmacion.fxml"));

            Parent root = loader.load();

            // Obtener el controlador de la pantalla de confirmación y establecer el mensaje
            ConfirmacionController confirmacionController = loader.getController();
            confirmacionController.setMensajeConfirmacion(mensaje);

            // Mostrar la nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Confirmación de Solicitud");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual
            if (btnSolicitarLibro != null && btnSolicitarLibro.getScene() != null) {
                btnSolicitarLibro.getScene().getWindow().hide();
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error al mostrar la confirmación", "Hubo un problema al abrir la pantalla de confirmación.");
        }
    }
    
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
    	App.changeScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/bienvenidoAlumno.fxml");

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
}
