package DAMeti;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import Modelo.Alumno;

public class AlumnoFormControlador {

    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<Integer> comboCurso; // Cambiado a ComboBox<Integer>
    @FXML
    private TextField txtTutor;
    @FXML
    private TextField txtUsuario;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private Alumno alumno;
    private boolean modificado = false;  // Bandera para indicar si el alumno fue modificado

    public void initAttributes(Alumno alumno) {
        this.alumno = alumno;

        // Agrega los números de curso al ComboBox
        comboCurso.getItems().addAll(1, 2, 3, 4, 5, 6);

        if (alumno != null) {
            txtNombre.setText(alumno.getNombre());
            comboCurso.setValue(alumno.getCurso());  // Set el valor del curso actual
            txtTutor.setText(alumno.getTutor());
            txtUsuario.setText(alumno.getUsuario());
        }
    }

    @FXML
    private void guardar() {
        if (alumno != null) {
            try {
                if (txtNombre.getText().isEmpty() || comboCurso.getValue() == null ||
                    txtTutor.getText().isEmpty() || txtUsuario.getText().isEmpty()) {
                    throw new IllegalArgumentException("Todos los campos son obligatorios.");
                }

                int curso = comboCurso.getValue();
                alumno.setNombre(txtNombre.getText());
                alumno.setCurso(curso);
                alumno.setTutor(txtTutor.getText());
                alumno.setUsuario(txtUsuario.getText());

                modificado = true;  // Indicar que el alumno fue modificado

                Stage stage = (Stage) btnGuardar.getScene().getWindow();
                stage.close();
            } catch (IllegalArgumentException e) {
                showAlert("Error", e.getMessage());
            }
        }
    }

    @FXML
    private void cancelar() {
        modificado = false;  // Si se cancela, no se considera ninguna modificación
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    public Alumno getAlumno() {
        return modificado ? alumno : null;  // Solo devuelve el alumno si fue modificado
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
