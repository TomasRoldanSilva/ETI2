package Modelo;

import javafx.beans.property.*;

public class Alumno {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final IntegerProperty curso;
    private final StringProperty nombreMadrePadre;
    private final StringProperty tutor;
    private final StringProperty usuario;
    private final StringProperty contrasena;

    // Constructor
    public Alumno(int id, String nombre, int curso, String nombreMadrePadre, String tutor, String usuario, String contrasena) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.curso = new SimpleIntegerProperty(curso);
        this.nombreMadrePadre = new SimpleStringProperty(nombreMadrePadre);
        this.tutor = new SimpleStringProperty(tutor);
        this.usuario = new SimpleStringProperty(usuario);
        this.contrasena = new SimpleStringProperty(contrasena);
    }

    // Getters
    public int getId() {
        return id.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public int getCurso() {
        return curso.get();
    }

    public String getNombreMadrePadre() {
        return nombreMadrePadre.get();
    }

    public String getTutor() {
        return tutor.get();
    }

    public String getUsuario() {
        return usuario.get();
    }

    public String getContrasena() {
        return contrasena.get();
    }

    // Setters
    public void setId(int id) {
        this.id.set(id);
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setCurso(int curso) {
        this.curso.set(curso);
    }

    public void setNombreMadrePadre(String nombreMadrePadre) {
        this.nombreMadrePadre.set(nombreMadrePadre);
    }

    public void setTutor(String tutor) {
        this.tutor.set(tutor);
    }

    public void setUsuario(String usuario) {
        this.usuario.set(usuario);
    }

    public void setContrasena(String contrasena) {
        this.contrasena.set(contrasena);
    }

    // Métodos para acceder a las propiedades JavaFX
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public IntegerProperty cursoProperty() {
        return curso;
    }

    public StringProperty nombreMadrePadreProperty() {
        return nombreMadrePadre;
    }

    public StringProperty tutorProperty() {
        return tutor;
    }

    public StringProperty usuarioProperty() {
        return usuario;
    }

    public StringProperty contrasenaProperty() {
        return contrasena;
    }

    // Método para validar la contraseña (si es necesario)
    public boolean validarContrasena(String contrasena) {
        return this.contrasena.get().equals(contrasena); // Asegúrate de que la contraseña esté hasheada antes de comparar
    }
}