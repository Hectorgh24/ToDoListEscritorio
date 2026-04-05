/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package todolist;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import pojo.Tarea;

public class FXMLDetalleTareaController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private Label lbFechaRealizar;
    @FXML
    private Label lbFechaCreacion;
    @FXML
    private Label lbEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    public void inicializarDetalles(Tarea tarea) {
        if (tarea != null) {
            lbTitulo.setText(tarea.getTitulo());
            taDescripcion.setText(tarea.getDescripcion() != null && !tarea.getDescripcion().isEmpty() ? tarea.getDescripcion() : "Sin descripción agregada.");
            lbFechaRealizar.setText(tarea.getFechaRealizar() != null ? tarea.getFechaRealizar().toString() : "No definida");
            lbFechaCreacion.setText(tarea.getFechaCreacion() != null ? tarea.getFechaCreacion().toString() : "No definida");
            
            String estado = tarea.getNombreEstado();
            if(estado == null || estado.isEmpty()) {
                estado = (tarea.getIdEstado() == 1) ? "Pendiente" : "Terminada";
            }
            lbEstado.setText(estado);
        }
    }

    @FXML
    private void clicCerrar(ActionEvent event) {
        Stage escenario = (Stage) lbTitulo.getScene().getWindow();
        escenario.close();
    }
}