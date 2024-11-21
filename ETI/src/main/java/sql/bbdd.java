package sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class bbdd {

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = conexion.dameConexion();
            try (Statement statement = conn.createStatement()) {

                // Crear la base de datos
                String createDatabase = "CREATE DATABASE IF NOT EXISTS ETI";
                statement.executeUpdate(createDatabase);

                // Usar la base de datos
                String useDatabase = "USE ETI";
                statement.executeUpdate(useDatabase);

                // Crear tabla de libros
                String createTableLibros = "CREATE TABLE IF NOT EXISTS libros ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                        + "titulo VARCHAR(50) NOT NULL, "
                        + "asignatura VARCHAR(20), "
                        + "curso INT, "
                        + "editorial VARCHAR(20), "
                        + "isbn CHAR(13) NOT NULL, "
                        + "numero_de_copias INT, "
                        + "CONSTRAINT isbn_valido CHECK (LENGTH(isbn) = 13 AND isbn REGEXP '^[0-9]+$')"
                        + ")";
                statement.executeUpdate(createTableLibros);

             // Crear tabla de alumnos
                String createTableAlumnos = "CREATE TABLE IF NOT EXISTS alumnos ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                        + "nombre VARCHAR(50) NOT NULL, "
                        + "curso INT NOT NULL, "
                        + "nombre_madre_padre VARCHAR(50), "
                        + "nombre_tutor VARCHAR(50) NOT NULL, "
                        + "usuario VARCHAR(50) NOT NULL, "
                        + "contrasena VARCHAR(255) NOT NULL" // Añadida columna contrasena
                        + ")";
                statement.executeUpdate(createTableAlumnos);

             // Crear tabla de préstamo con campo de fecha_prestamo
                String createTablePrestamo = "CREATE TABLE IF NOT EXISTS prestamo ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                        + "id_alumno INT NOT NULL, "
                        + "id_libro INT NOT NULL, "
                        + "nombre_alumno VARCHAR(50) NOT NULL, "
                        + "titulo_libro VARCHAR(50) NOT NULL, "
                        + "fecha_prestamo DATE NOT NULL, "      
                        + "fecha_devolucion DATE, "
                        + "FOREIGN KEY (id_alumno) REFERENCES alumnos(id), "
                        + "FOREIGN KEY (id_libro) REFERENCES libros(id)"
                        + ")";
                statement.executeUpdate(createTablePrestamo);
                
              



                System.out.println("Tablas de libros, alumnos y préstamo creadas exitosamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexion.cerrarConexion(conn);
        }
    }
}