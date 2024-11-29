package DAMeti;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class ConfirmacionController {

    @FXML
    private Label mensajeConfirmacion;

    // Método para configurar el mensaje de confirmación
    public void setMensajeConfirmacion(String mensaje) {
        mensajeConfirmacion.setText(mensaje);
    }

    // Acción al hacer clic en "Aceptar"
    public void aceptarAction(ActionEvent event) {
        try {
            // Obtener el mensaje de confirmación
            String mensaje = mensajeConfirmacion.getText();

            // Generar el archivo PDF con el mensaje de confirmación
            String rutaArchivo = generarPDF("Confirmación de Préstamo", mensaje);

            // Mostrar el PDF usando el visor predeterminado del sistema operativo
            mostrarPDFConVisorExterno(rutaArchivo);

            // Cerrar la ventana de confirmación original
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para generar el PDF
    private String generarPDF(String titulo, String mensajeConfirmacion) {
        String rutaArchivo = "confirmacion_prestamo.pdf"; // Ruta donde se guardará el archivo
        try {
            // Crear un nuevo documento PDF
            PDDocument document = new PDDocument();

            // Crear una nueva página
            PDPage page = new PDPage();
            document.addPage(page);

            // Crear un flujo de contenido para escribir en la página
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Agregar el título (Confirmación de Préstamo)
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(100, 700); // Posición inicial del texto
            contentStream.showText(titulo);
            contentStream.endText();

            // Agregar el número de confirmación
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 20);
            contentStream.newLineAtOffset(100, 650); // Posición del texto debajo del título
            contentStream.showText("Número de Confirmación: " + mensajeConfirmacion);
            contentStream.endText();

            // Agregar el mensaje para recoger el libro
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 16);
            contentStream.newLineAtOffset(100, 600); // Posición del texto adicional
            contentStream.showText("Por favor, pase al mostrador para recoger su libro.");
            contentStream.endText();

            // Cerrar el flujo de contenido
            contentStream.close();

            // Guardar el documento PDF
            document.save(rutaArchivo);

            // Cerrar el documento
            document.close();

            System.out.println("PDF generado exitosamente: " + rutaArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rutaArchivo;
    }

    // Método para mostrar el PDF usando el visor predeterminado del sistema operativo
    private void mostrarPDFConVisorExterno(String rutaArchivo) {
        try {
            File archivoPDF = new File(rutaArchivo);
            if (archivoPDF.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(archivoPDF); // Abrir el PDF en el visor predeterminado
                } else {
                    System.err.println("El visor de PDF no es compatible con este sistema.");
                }
            } else {
                System.err.println("El archivo PDF no existe.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
