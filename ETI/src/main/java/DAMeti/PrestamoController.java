package DAMeti;

import Modelo.Prestamo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.io.IOException;

public class PrestamoController {

    @FXML
    private TableView<Prestamo> tablaPrestamos;

    @FXML
    private TableColumn<Prestamo, Integer> colIdPrestamo;

    @FXML
    private TableColumn<Prestamo, Integer> colIdLibro;

    @FXML
    private TableColumn<Prestamo, String> colNombreAlumno;

    @FXML
    private TableColumn<Prestamo, String> colTituloLibro;

    @FXML
    private TableColumn<Prestamo, Date> colFechaPrestamo;

    @FXML
    private TableColumn<Prestamo, Date> colFechaDevolucion;

    private ObservableList<Prestamo> listaPrestamos;

    
    @FXML
    private void initialize() {
        // Configurar columnas de la tabla de préstamos con los nombres correctos
        colIdPrestamo.setCellValueFactory(new PropertyValueFactory<>("idPeticion")); // Cambiado a idPeticion
        colIdLibro.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        colNombreAlumno.setCellValueFactory(new PropertyValueFactory<>("nombreAlumno"));
        colTituloLibro.setCellValueFactory(new PropertyValueFactory<>("tituloLibro"));
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));

        cargarPrestamos();
    }

    void cargarPrestamos() {
        listaPrestamos = FXCollections.observableArrayList();
        String url = "jdbc:mysql://localhost:3306/eti";

        String sql = "SELECT p.id_peticion, p.id_libro, p.nombre_alumno, p.titulo_libro, " +
                "p.fecha_prestamo, p.fecha_devolucion, " +
                "(l.numero_de_copias ) AS numero_copias, " +
                "l.numero_de_copias " +
                "FROM prestamos p " +
                "JOIN libros l ON p.id_libro = l.id";



        try (Connection connection = DriverManager.getConnection(url, "root", "");
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            listaPrestamos.clear();

            while (resultSet.next()) {
                int idPeticion = resultSet.getInt("id_peticion");
                int idLibro = resultSet.getInt("id_libro");
                String nombreAlumno = resultSet.getString("nombre_alumno");
                String tituloLibro = resultSet.getString("titulo_libro");
                Date fechaPrestamo = resultSet.getDate("fecha_prestamo");
                Date fechaDevolucion = resultSet.getDate("fecha_devolucion");
                int numeroCopiasCalculado = resultSet.getInt("numero_copias"); 

                listaPrestamos.add(new Prestamo(idPeticion, idLibro, nombreAlumno, tituloLibro, fechaPrestamo, fechaDevolucion, numeroCopiasCalculado));
            }

            // Configuramos la tabla con los datos cargados
            tablaPrestamos.setItems(listaPrestamos);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar los préstamos", "No se pudieron cargar los préstamos desde la base de datos.");
        }
    }


//eliminar registro de prestamo cuando el libro se devuelve
    @FXML
    private void handleEliminarPrestamo(ActionEvent event) {
        Prestamo prestamoSeleccionado = tablaPrestamos.getSelectionModel().getSelectedItem();
        if (prestamoSeleccionado != null) {
            java.sql.Date fechaDevolucion = (Date) prestamoSeleccionado.getFechaDevolucion();

            if (fechaDevolucion != null) {
                LocalDate fechaDevolucionLocal = fechaDevolucion.toLocalDate();
                LocalDate fechaActual = LocalDate.now();
                long diasRetraso = ChronoUnit.DAYS.between(fechaDevolucionLocal, fechaActual);

                if (diasRetraso <= 0) {
                    mostrarAlerta("Libro devuelto en fecha", "El libro fue devuelto correctamente en la fecha prevista.");
                } else {
                    mostrarAlerta("Libro devuelto con retraso", "El libro se devolvió con " + diasRetraso + " días de retraso. Se notificará a sus tutores legales. ");
                }

                // Eliminar el préstamo  
                eliminarPrestamo(prestamoSeleccionado);                
                actualizarNumeroDeCopias(prestamoSeleccionado.getIdLibro(), 1);
                // Recargar la tabla visual con los datos actualizados
                cargarPrestamos();

               
            } else {
                mostrarAlerta("Fecha de devolución no válida", "La fecha de devolución no está disponible para este préstamo.");
            }
        } else {
            mostrarAlerta("No se seleccionó ningún préstamo", "Por favor seleccione un préstamo para eliminar.");
        }
    }
    //actualizar numero de copias en la tabla libros una vez eliminado el prestamo

    private void actualizarNumeroDeCopias(int idLibro, int cambioCopias) {
        String updateLibroSQL = "UPDATE libros SET numero_de_copias = numero_de_copias + ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eti", "root", "");
             PreparedStatement updateStmt = connection.prepareStatement(updateLibroSQL)) {

            // Configurar los parámetros de la consulta para incrementar el número de copias
            updateStmt.setInt(1, cambioCopias);  
            updateStmt.setInt(2, idLibro);

            updateStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al actualizar copias", "No se pudo actualizar el número de copias del libro.");
        }
    }

    private void eliminarPrestamo(Prestamo prestamoSeleccionado) {
        String deletePrestamoSQL = "DELETE FROM prestamos WHERE id_peticion = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eti", "root", "");
             PreparedStatement deleteStmt = connection.prepareStatement(deletePrestamoSQL)) {

            deleteStmt.setInt(1, prestamoSeleccionado.getIdPeticion());
            deleteStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al eliminar préstamo", "Hubo un problema al eliminar el préstamo de la base de datos.");
        }
    }
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        App.changeScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/admin2.fxml");
    }

    @FXML
    private void handleInicioButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DAM/ETI/inicio.fxml"));
        Parent inicioView = loader.load();
        Scene inicioScene = new Scene(inicioView);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(inicioScene);
        stage.show();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void irAVista(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de navegación", "No se pudo cargar la vista solicitada.");
        }
    }
}
