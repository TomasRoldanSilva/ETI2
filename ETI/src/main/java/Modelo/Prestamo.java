package Modelo;

import java.util.Date;

public class Prestamo {

    private int idPeticion;
    private int idLibro;
    private String nombreAlumno;
    private String tituloLibro;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private int numeroCopias; 

    // Constructor
    public Prestamo(int idPeticion, int idLibro, String nombreAlumno, String tituloLibro, Date fechaPrestamo, Date fechaDevolucion, int numeroCopias) {
        this.idPeticion = idPeticion;
        this.idLibro = idLibro;
        this.nombreAlumno = nombreAlumno;
        this.tituloLibro = tituloLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.numeroCopias = numeroCopias;
    }

	public int getIdPeticion() {
		return idPeticion;
	}

	public void setIdPeticion(int idPeticion) {
		this.idPeticion = idPeticion;
	}

	public int getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(int idLibro) {
		this.idLibro = idLibro;
	}

	public String getNombreAlumno() {
		return nombreAlumno;
	}

	public void setNombreAlumno(String nombreAlumno) {
		this.nombreAlumno = nombreAlumno;
	}

	public String getTituloLibro() {
		return tituloLibro;
	}

	public void setTituloLibro(String tituloLibro) {
		this.tituloLibro = tituloLibro;
	}

	public Date getFechaPrestamo() {
		return fechaPrestamo;
	}

	public void setFechaPrestamo(Date fechaPrestamo) {
		this.fechaPrestamo = fechaPrestamo;
	}

	public Date getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(Date fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	public int getNumeroCopias() {
		return numeroCopias;
	}

	public void setNumeroCopias(int numeroCopias) {
		this.numeroCopias = numeroCopias;
	}

    

   
}
