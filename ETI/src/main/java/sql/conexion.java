package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexion {
    // URL de conexión con la base de datos especificada
    private static final String URL = "jdbc:mysql://localhost:3306/eti"; // Agrega el nombre de la base de datos aquí
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "";

    public static Connection dameConexion() throws SQLException {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            System.out.println("Conexión establecida.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();
            throw e;
        }
        return conexion;
    }

    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}