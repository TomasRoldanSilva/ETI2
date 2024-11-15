package Modelo;

import java.util.List;
public interface PrestamoDao {
   void crearPrestamo(Prestamo prestamo);
   void modificarPrestamo(Prestamo prestamo);
   void eliminarPrestamo(int id);
   Prestamo obtenerPrestamo(int id);
   List<Prestamo> listarPrestamos();
   List<Prestamo> listarPrestamosPorAlumno(int idAlumno);
}

