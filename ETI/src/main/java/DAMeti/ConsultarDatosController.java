package DAMeti;

import sql.conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private ObservableList<Libro> listaLibrosSolicitados;
    private ObservableList<Libro> listaLibrosPosesion;

    // Usamos el nombre de usuario para cargar la información del alumno.
 

    @FXML
    public void initialize() {
        // Configurar las columnas de las tablas
        colTituloSolicitado.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colTituloPosesion.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        // Inicializar listas de libros
        listaLibrosSolicitados = FXCollections.observableArrayList();
        listaLibrosPosesion = FXCollections.observableArrayList();

        tblLibrosSolicitados.setItems(listaLibrosSolicitados);
        tblLibrosPosesion.setItems(listaLibrosPosesion);
    
        
    
    }

    
    
    
    public void setNombreAlumno(String nombreAlumno) {
        cargarDatosAlumno(nombreAlumno);
    }

    public void cargarDatosAlumno(String nombreAlumno) {
        Connection conn = null;
        try {
            conn = conexion.dameConexion();  // Obtener la conexión

            // Consultar los datos del alumno usando su nombre de usuario (o correo)
            String queryAlumno = "SELECT nombre, curso FROM alumnos WHERE nombre = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryAlumno)) {
                stmt.setString(1, nombreAlumno);  // Usamos el nombre de alumno para obtener los datos
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String nombre = rs.getString("nombre");
                        int curso = rs.getInt("curso");

                        // Actualizar las etiquetas de la interfaz
                        lblNombre.setText(nombre);
                        lblCurso.setText(String.valueOf(curso));
                    }
                }
            }

            // Cargar los libros solicitados
            String queryLibrosSolicitados = "SELECT l.titulo, p.fecha_prestamo FROM prestamos p "
                    + "JOIN libros l ON p.id_libro = l.id "
                    + "WHERE p.nombre_alumno = ? AND p.fecha_devolucion IS NOT NULL";
            try (PreparedStatement stmt = conn.prepareStatement(queryLibrosSolicitados)) {
                stmt.setString(1, nombreAlumno);  // Usamos el nombre de alumno para obtener los libros solicitados
                try (ResultSet rs = stmt.executeQuery()) {
                    listaLibrosSolicitados.clear();
                    while (rs.next()) {
                        String titulo = rs.getString("titulo");
                        String fecha = rs.getString("fecha_prestamo");
                        listaLibrosSolicitados.add(new Libro(titulo, fecha));
                    }
                }
            }

            // Cargar los libros en posesión
            String queryLibrosPosesion = "SELECT l.titulo, p.fecha_prestamo FROM prestamos p "
                    + "JOIN libros l ON p.id_libro = l.id "
                    + "WHERE p.nombre_alumno = ? AND p.fecha_devolucion IS NULL";
            try (PreparedStatement stmt = conn.prepareStatement(queryLibrosPosesion)) {
                stmt.setString(1, nombreAlumno);  // Usamos el nombre de alumno para obtener los libros en posesión
                try (ResultSet rs = stmt.executeQuery()) {
                    listaLibrosPosesion.clear();
                    while (rs.next()) {
                        String titulo = rs.getString("titulo");
                        String fecha = rs.getString("fecha_prestamo");
                        listaLibrosPosesion.add(new Libro(titulo, fecha));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Mostrar error en consola
        } finally {
            if (conn != null) {
                try {
                    conn.close();  // Cerrar la conexión
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleBackButtonAction() {
        System.out.println("Regresar a la pantalla anterior");
    }

    // Clase Libro para representar un libro en las tablas
    public static class Libro {
        private String titulo;
        private String fecha;

        public Libro(String titulo, String fecha) {
            this.titulo = titulo;
            this.fecha = fecha;
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
    }


}
