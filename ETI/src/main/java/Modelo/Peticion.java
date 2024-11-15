package Modelo;

import java.time.LocalDate;

public class Peticion {
    private int id;
    private String nombreAlumno;
    private int curso;
    private int idLibro;
    private String tituloLibro;
    private String isbnLibro;
    private LocalDate fechaPeticion;
    private int numeroCopias;

    // Constructor
    public Peticion(int id, String nombreAlumno, int curso, int idLibro, String tituloLibro, String isbnLibro, LocalDate fechaPeticion, int numeroCopias) {
        this.id = id;
        this.nombreAlumno = nombreAlumno;
        this.curso = curso;
        this.idLibro = idLibro;
        this.tituloLibro = tituloLibro;
        this.isbnLibro = isbnLibro;
        this.fechaPeticion = fechaPeticion;
        this.numeroCopias = numeroCopias;
    }

    // Métodos getter y setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getIsbnLibro() {
        return isbnLibro;
    }

    public void setIsbnLibro(String isbnLibro) {
        this.isbnLibro = isbnLibro;
    }

    public LocalDate getFechaPeticion() {
        return fechaPeticion;
    }

    public void setFechaPeticion(LocalDate fechaPeticion) {
        this.fechaPeticion = fechaPeticion;
    }

    public int getNumeroCopias() {
        return numeroCopias;
    }

    public void setNumeroCopias(int numeroCopias) {
        this.numeroCopias = numeroCopias;
    }

    // Método toString para representar el objeto como texto
    @Override
    public String toString() {
        return "Peticion{" +
               "id=" + id +
               ", nombreAlumno='" + nombreAlumno + '\'' +
               ", curso=" + curso +
               ", idLibro=" + idLibro +
               ", tituloLibro='" + tituloLibro + '\'' +
               ", isbnLibro='" + isbnLibro + '\'' +
               ", fechaPeticion=" + fechaPeticion +
               ", numeroCopias=" + numeroCopias +
               '}';
    }
}
