package Modelo;

public class Libro {
    private int id;
    private String titulo;
    private String asignatura;
    private int curso;
    private String editorial;
    private String isbn;
    private int numeroDeCopias;
	/**
	 * @param id
	 * @param titulo
	 * @param asignatura
	 * @param curso
	 * @param editorial
	 * @param isbn
	 * @param numeroDeCopias
	 */
	public Libro(int id, String titulo, String asignatura, int curso, String editorial, String isbn,
			int numeroDeCopias) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.asignatura = asignatura;
		this.curso = curso;
		this.editorial = editorial;
		this.isbn = isbn;
		this.numeroDeCopias = numeroDeCopias;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAsignatura() {
		return asignatura;
	}
	public void setAsignatura(String asignatura) {
		this.asignatura = asignatura;
	}
	public int getCurso() {
		return curso;
	}
	public void setCurso(int curso) {
		this.curso = curso;
	}
	public String getEditorial() {
		return editorial;
	}
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public int getNumeroDeCopias() {
		return numeroDeCopias;
	}
	public void setNumeroDeCopias(int numeroDeCopias) {
		this.numeroDeCopias = numeroDeCopias;
	}
   
   
}

