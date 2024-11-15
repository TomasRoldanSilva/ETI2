package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDaoImpl implements LibroDao {

    private Connection connection;

    public LibroDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void crearLibro(Libro libro) {
        String sql = "INSERT INTO libro (titulo, asignatura, curso, editorial, isbn, numero_de_copias) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAsignatura());
            stmt.setInt(3, libro.getCurso());
            stmt.setString(4, libro.getEditorial());
            stmt.setString(5, libro.getIsbn());
            stmt.setInt(6, libro.getNumeroDeCopias());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modificarLibro(Libro libro) {
        String sql = "UPDATE libro SET titulo = ?, asignatura = ?, curso = ?, editorial = ?, isbn = ?, numero_de_copias = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAsignatura());
            stmt.setInt(3, libro.getCurso());
            stmt.setString(4, libro.getEditorial());
            stmt.setString(5, libro.getIsbn());
            stmt.setInt(6, libro.getNumeroDeCopias());
            stmt.setInt(7, libro.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarLibro(int id) {
        String sql = "DELETE FROM libro WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Libro obtenerLibro(int id) {
        String sql = "SELECT * FROM libro WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Libro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("asignatura"),
                    rs.getInt("curso"),
                    rs.getString("editorial"),
                    rs.getString("isbn"),
                    rs.getInt("numero_de_copias")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Libro no encontrado
    }

    @Override
    public List<Libro> listarLibros() {
        List<Libro> listaLibros = new ArrayList<>();
        String sql = "SELECT * FROM libro";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                listaLibros.add(new Libro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("asignatura"),
                    rs.getInt("curso"),
                    rs.getString("editorial"),
                    rs.getString("isbn"),
                    rs.getInt("numero_de_copias")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaLibros;
    }
}



