package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import sql.conexion;

public class AlumnoDaoImpl implements AlumnoDao {

    @Override
    public void crearAlumno(Alumno alumno) {
        String sql = "INSERT INTO alumnos (nombre, curso, nombre_madre_padre, nombre_tutor, usuario, contrasena) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = conexion.dameConexion(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, alumno.getNombre());
            stmt.setInt(2, alumno.getCurso());
            stmt.setString(3, alumno.getNombreMadrePadre());
            stmt.setString(4, alumno.getTutor());
            stmt.setString(5, alumno.getUsuario());
            stmt.setString(6, hashContrasena(alumno.getContrasena())); // Hash de la contraseña
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modificarAlumno(Alumno alumno) {
        String sql = "UPDATE alumnos SET nombre = ?, curso = ?, nombre_madre_padre = ?, nombre_tutor = ?, usuario = ?, contrasena = ? WHERE id = ?";
        try (Connection connection = conexion.dameConexion(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, alumno.getNombre());
            stmt.setInt(2, alumno.getCurso());
            stmt.setString(3, alumno.getNombreMadrePadre());
            stmt.setString(4, alumno.getTutor());
            stmt.setString(5, alumno.getUsuario());
            stmt.setString(6, hashContrasena(alumno.getContrasena())); // Hash de la contraseña
            stmt.setInt(7, alumno.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarAlumno(int id) {
        String sql = "DELETE FROM alumnos WHERE id = ?";
        try (Connection connection = conexion.dameConexion(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Alumno obtenerAlumno(int id) {
        String sql = "SELECT * FROM alumnos WHERE id = ?";
        try (Connection connection = conexion.dameConexion(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Alumno(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("curso"),
                    rs.getString("nombre_madre_padre"),
                    rs.getString("nombre_tutor"),
                    rs.getString("usuario"),
                    rs.getString("contrasena") // Devuelve la contraseña hasheada
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Alumno no encontrado
    }

    @Override
    public List<Alumno> listarAlumnos() {
        List<Alumno> listaAlumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos";
        try (Connection connection = conexion.dameConexion(); 
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                listaAlumnos.add(new Alumno(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("curso"),
                    rs.getString("nombre_madre_padre"),
                    rs.getString("nombre_tutor"),
                    rs.getString("usuario"),
                    rs.getString("contrasena") // Aquí también debes tener en cuenta el hashing
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaAlumnos;
    }

    @Override
    public boolean validarContrasena(String usuario, String contrasena) {
        Alumno alumno = obtenerAlumnoPorUsuario(usuario);
        if (alumno != null) {
            return alumno.validarContrasena(contrasena); // Validación usando el método de la clase Alumno
        }
        return false; // Usuario no encontrado o contraseña incorrecta
    }

    private Alumno obtenerAlumnoPorUsuario(String usuario) {
        String sql = "SELECT * FROM alumnos WHERE usuario = ?";
        try (Connection connection = conexion.dameConexion(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Alumno(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("curso"),
                    rs.getString("nombre_madre_padre"),
                    rs.getString("nombre_tutor"),
                    rs.getString("usuario"),
                    rs.getString("contrasena") // Devuelve la contraseña hasheada
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Alumno no encontrado
    }

    private String hashContrasena(String contrasena) {
        // Implementa el hashing de la contraseña aquí (por ejemplo, usando BCrypt)
        return contrasena; // Cambia esto para devolver el password hasheado
    }

    private boolean validarContrasena2(String hashedContrasena, String inputContrasena) {
        // Implementa la comparación de contraseñas (por ejemplo, usando BCrypt)
        return hashedContrasena.equals(inputContrasena); // Cambia esto a comparación de hash
    }
}