package Modelo;

import java.util.List;

public interface AlumnoDao {
	   void crearAlumno(Alumno alumno);
	   void modificarAlumno(Alumno alumno);
	   void eliminarAlumno(int id);
	   Alumno obtenerAlumno(int id);
	   List<Alumno> listarAlumnos();
	   boolean validarContrasena(String usuario, String contrasena);
	}

