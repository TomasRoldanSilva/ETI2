package Modelo;

import java.util.List;
public interface LibroDao {
   void crearLibro(Libro libro);
   void modificarLibro(Libro libro);
   void eliminarLibro(int id);
   Libro obtenerLibro(int id);
   List<Libro> listarLibros();
}

