package sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertarDatos {

    public static void main(String[] args) {
        Connection conn = null;
        try {
            // Conectar a la base de datos
            conn = conexion.dameConexion();
            try (Statement statement = conn.createStatement()) {

                // Usar la base de datos
                String useDatabase = "USE ETI";
                statement.executeUpdate(useDatabase);

                // Insertar registros en la tabla de libros
                String insertLibros = "INSERT INTO libros (titulo, asignatura, curso, editorial, isbn, numero_de_copias) VALUES "
                        + "('Lengua Castellana 1º', 'Lengua', 1, 'Anaya', '1234567890123', 10), "
                        + "('Matemáticas 1º', 'Matemáticas', 1, 'Santillana', '1234567890124', 8), "
                        + "('Inglés 1º', 'Inglés', 1, 'Edebé', '1234567890125', 12), "
                        + "('Naturaleza 1º', 'Naturaleza', 1, 'SM', '1234567890126', 6), "
                        + "('Plástica 1º', 'Plástica', 1, 'Anaya', '1234567890127', 9), "
                        + "('Religión 1º', 'Religión', 1, 'Santillana', '1234567890128', 5), "
                        + "('Música 1º', 'Música', 1, 'Edebé', '1234567890129', 7), "
                        + "('Sociales 1º', 'Sociales', 1, 'SM', '1234567890130', 10), "
                        + "('Lengua Castellana 2º', 'Lengua', 2, 'Anaya', '1234567890131', 15), "
                        + "('Matemáticas 2º', 'Matemáticas', 2, 'Santillana', '1234567890132', 8), "
                        + "('Inglés 2º', 'Inglés', 2, 'Edebé', '1234567890133', 12), "
                        + "('Naturaleza 2º', 'Naturaleza', 2, 'SM', '1234567890134', 6), "
                        + "('Lengua Castellana 3º', 'Lengua', 3, 'Anaya', '1234567890135', 14), "
                        + "('Matemáticas 3º', 'Matemáticas', 3, 'Santillana', '1234567890136', 7), "
                        + "('Inglés 3º', 'Inglés', 3, 'Edebé', '1234567890137', 9), "
                        + "('Naturaleza 3º', 'Naturaleza', 3, 'SM', '1234567890138', 10), "
                        + "('Sociales 4º', 'Sociales', 4, 'Anaya', '1234567890139', 11), "
                        + "('Lengua Castellana 4º', 'Lengua', 4, 'Santillana', '1234567890140', 8), "
                        + "('Matemáticas 5º', 'Matemáticas', 5, 'Edebé', '1234567890141', 13), "
                        + "('Sociales 6º', 'Sociales', 6, 'SM', '1234567890142', 6)";
                
                statement.executeUpdate(insertLibros);
                
                System.out.println("Registros de libros insertados exitosamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexion.cerrarConexion(conn);
        }
    }
}