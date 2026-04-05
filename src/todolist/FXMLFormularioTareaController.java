/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package todolist;

import dominio.TareaImp;
import dto.Respuesta;
import interfaz.INotificador;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojo.Tarea;
import utilidad.Utilidades;

public class FXMLFormularioTareaController implements Initializable {

    @FXML
    private Label lbTituloPantalla;
    @FXML
    private TextField tfTitulo;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private DatePicker dpFechaRealizar;
    @FXML
    private Label lbError;

    private Tarea tareaEdicion;
    private int idUsuarioSesion;
    private INotificador observador;
    private boolean modoEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización básica
    }    

    /**
     * Método para recibir datos desde la pantalla principal.
     * Si tarea es null, es un registro nuevo.
     * Si tarea tiene datos, es modo edición.
     */
    public void inicializarInformacion(Tarea tarea, int idUsuario, INotificador observador) {
        this.idUsuarioSesion = idUsuario;
        this.observador = observador;
        this.tareaEdicion = tarea;
        
        if (this.tareaEdicion != null) {
            this.modoEdicion = true;
            cargarDatosEdicion();
        } else {
            lbTituloPantalla.setText("Agregar Nueva Tarea");
        }
    }

    private void cargarDatosEdicion() {
        lbTituloPantalla.setText("Editar Tarea");
        tfTitulo.setText(tareaEdicion.getTitulo());
        taDescripcion.setText(tareaEdicion.getDescripcion());
        if (tareaEdicion.getFechaRealizar() != null) {
            dpFechaRealizar.setValue(tareaEdicion.getFechaRealizar().toLocalDate());
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        lbError.setText("");
        
        String titulo = tfTitulo.getText().trim();
        String descripcion = taDescripcion.getText().trim();
        LocalDate fechaSeleccionada = dpFechaRealizar.getValue();
        
        // Validaciones básicas
        if (titulo.isEmpty() || fechaSeleccionada == null) {
            lbError.setText("Por favor, llena los campos obligatorios (*).");
            return;
        }

        Tarea tareaGuardar = new Tarea();
        tareaGuardar.setTitulo(titulo);
        tareaGuardar.setDescripcion(descripcion);
        tareaGuardar.setFechaRealizar(Date.valueOf(fechaSeleccionada));
        tareaGuardar.setIdUsuario(idUsuarioSesion);

        Respuesta respuesta;
        
        if (modoEdicion) {
            tareaGuardar.setIdTarea(tareaEdicion.getIdTarea());
            respuesta = TareaImp.editarTarea(tareaGuardar);
        } else {
            respuesta = TareaImp.agregarTarea(tareaGuardar);
        }

        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            observador.notificarOperacion("GuardarTarea"); // Actualiza la tabla de la pantalla principal
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage escenario = (Stage) tfTitulo.getScene().getWindow();
        escenario.close();
    }
}