package DAMeti;

import sql.conexion;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultarDatosController {

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblCurso;

    @FXML
    private TableView<Libro> tblLibrosSolicitados;

    @FXML
    private TableView<Libro> tblLibrosPosesion;

    @FXML
    private TableColumn<Libro, String> colTituloSolicitado;

    @FXML
    private TableColumn<Libro, String> colFechaSolicitud;

    @FXML
    private TableColumn<Libro, String> colTituloPosesion;

    @FXML
    private TableColumn<Libro, String> colFechaEntrega;

    @FXML
    private TableColumn<Libro, String> colFechaDevolucion; // Nueva columna para la fecha de devolución

    private ObservableList<Libro> listaLibrosSolicitados;
    private ObservableList<Libro> listaLibrosPosesion;

    @FXML
    public void initialize() {
        // Verificación de null para evitar errores.
        if (colTituloSolicitado != null && colFechaSolicitud != null && colFechaDevolucion != null) {
            colTituloSolicitado.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<>("fecha"));
            colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion")); // Configuración de la nueva columna
        }

        if (colTituloPosesion != null && colFechaEntrega != null) {
            colTituloPosesion.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        }

        // Inicializar listas de libros
        listaLibrosSolicitados = FXCollections.observableArrayList();
        listaLibrosPosesion = FXCollections.observableArrayList();

        if (tblLibrosSolicitados != null) {
            tblLibrosSolicitados.setItems(listaLibrosSolicitados);
        }

        if (tblLibrosPosesion != null) {
            tblLibrosPosesion.setItems(listaLibrosPosesion);
        }
    }

    public void setNombreAlumno(String nombreAlumno) {
        cargarDatosAlumno(nombreAlumno);
    }

    public void cargarDatosAlumno(String nombreAlumno) {
        Connection conn = null;
        try {
            conn = conexion.dameConexion(); // Obtener la conexión

            String queryAlumno = "SELECT nombre, curso FROM alumnos WHERE nombre = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryAlumno)) {
                stmt.setString(1, nombreAlumno);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String nombre = rs.getString("nombre");
                        int curso = rs.getInt("curso");

                        lblNombre.setText(nombre);
                        lblCurso.setText(String.valueOf(curso));
                    }
                }
            }

            // Consulta para obtener los libros solicitados (con fecha de devolución)
            String queryLibrosSolicitados = "SELECT p.titulo_libro AS titulo, p.fecha_prestamo, p.fecha_devolucion "
                    + "FROM prestamos p "
                    + "WHERE p.nombre_alumno = ? "
                    + "  AND p.fecha_devolucion IS NOT NULL;"; // Libros que ya han sido devueltos

            try (PreparedStatement stmt = conn.prepareStatement(queryLibrosSolicitados)) {
                stmt.setString(1, nombreAlumno);
                try (ResultSet rs = stmt.executeQuery()) {
                    listaLibrosSolicitados.clear();
                    while (rs.next()) {
                        String titulo = rs.getString("titulo");
                        String fechaPrestamo = rs.getString("fecha_prestamo");
                        String fechaDevolucion = rs.getString("fecha_devolucion");

                        // Agregar libro a la lista de solicitados
                        listaLibrosSolicitados.add(new Libro(titulo, fechaPrestamo, fechaDevolucion)); // Ahora incluimos la fecha de devolución
                    }
                }
            }

            // Consulta para obtener los libros en posesión (libros no devueltos)
            String queryLibrosPosesion = "SELECT p.titulo_libro AS titulo, p.fecha_prestamo, p.fecha_devolucion "
                    + "FROM prestamos p "
                    + "WHERE p.nombre_alumno = ? AND p.fecha_devolucion IS NULL;"; // Libros no devueltos
            try (PreparedStatement stmt = conn.prepareStatement(queryLibrosPosesion)) {
                stmt.setString(1, nombreAlumno);
                try (ResultSet rs = stmt.executeQuery()) {
                    listaLibrosPosesion.clear();
                    while (rs.next()) {
                        String titulo = rs.getString("titulo");
                        String fechaPrestamo = rs.getString("fecha_prestamo");
                        String fechaDevolucion = rs.getString("fecha_devolucion");

                        // Agregar libro a la lista de posesión
                        listaLibrosPosesion.add(new Libro(titulo, fechaPrestamo, fechaDevolucion));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Mostrar error en consola
        } finally {
            if (conn != null) {
                try {
                    conn.close(); // Cerrar la conexión
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        App.changeScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/DAM/ETI/login.fxml");

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

    public static class Libro {
        private String titulo;
        private String fecha;
        private String fechaDevolucion;

        public Libro(String titulo, String fecha, String fechaDevolucion) {
            this.titulo = titulo;
            this.fecha = fecha;
            this.fechaDevolucion = fechaDevolucion;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getFechaDevolucion() {
            return fechaDevolucion;
        }

        public void setFechaDevolucion(String fechaDevolucion) {
            this.fechaDevolucion = fechaDevolucion;
        }
    }
}
