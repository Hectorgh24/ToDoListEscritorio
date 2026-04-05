/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package todolist;

import dominio.UsuarioImp;
import dto.Respuesta;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojo.Usuario;
import utilidad.Utilidades;

public class FXMLRegistroUsuarioController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private TextField tfRfc;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lbError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización de componentes si es necesario
    }    

    @FXML
    private void clicRegistrar(ActionEvent event) {
        lbError.setText("");
        
        String nombre = tfNombre.getText().trim();
        String apellidoPaterno = tfApellidoPaterno.getText().trim();
        String apellidoMaterno = tfApellidoMaterno.getText().trim();
        LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
        String rfc = tfRfc.getText().trim();
        String password = pfPassword.getText();

        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || rfc.isEmpty() || password.isEmpty() || fechaNacimiento == null) {
            lbError.setText("Llene todos los campos obligatorios (Apellido Materno es opcional).");
            return;
        }

        if (rfc.length() != 13) {
            lbError.setText("El RFC debe contener exactamente 13 caracteres.");
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellidoPaterno(apellidoPaterno);
        nuevoUsuario.setApellidoMaterno(apellidoMaterno);
        nuevoUsuario.setFechaNacimiento(Date.valueOf(fechaNacimiento));
        nuevoUsuario.setRfc(rfc);
        nuevoUsuario.setPassword(password);

        Respuesta respuesta = UsuarioImp.registrarUsuario(nuevoUsuario);

        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Registro Exitoso", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            volverInicioSesion();
        } else {
            Utilidades.mostrarAlertaSimple("Error de Registro", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        volverInicioSesion();
    }
    
    private void volverInicioSesion() {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLInicioSesion.fxml"));
            Scene escena = new Scene(vista);
            Stage escenarioBase = (Stage) tfNombre.getScene().getWindow();
            escenarioBase.setScene(escena);
            escenarioBase.setTitle("Iniciar Sesión");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}