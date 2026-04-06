package todolist;

import dominio.UsuarioImp;
import dto.Respuesta;
import pojo.Usuario;
import utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfRfc;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lbError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Refuerzo visual para asegurar que los campos resalten sobre el fondo oscuro
        tfRfc.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #172B4D;");
        pfPassword.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #172B4D;");
    }    

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        lbError.setText("");
        String rfc = tfRfc.getText().trim();
        String password = pfPassword.getText();

        if (rfc.isEmpty() || password.isEmpty()) {
            lbError.setText("Error: RFC y contraseña obligatorios.");
            return;
        }

        Respuesta respuesta = UsuarioImp.iniciarSesion(rfc, password);

        if (!respuesta.isError()) {
            Usuario usuario = (Usuario) respuesta.getDatos();
            irPantallaPrincipal(usuario);
        } else {
            Utilidades.mostrarAlertaSimple("Error de Autenticación", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicIrRegistro(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLRegistroUsuario.fxml"));
            Scene escena = new Scene(vista);
            Stage escenarioBase = (Stage) tfRfc.getScene().getWindow();
            escenarioBase.setScene(escena);
            escenarioBase.setTitle("Registro de Usuario");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void irPantallaPrincipal(Usuario usuario) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLPrincipal.fxml"));
            Parent vista = cargador.load();
            
            FXMLPrincipalController controlador = cargador.getController();
            controlador.inicializarValores(usuario); 

            Scene escena = new Scene(vista);
            Stage escenarioBase = (Stage) tfRfc.getScene().getWindow();
            escenarioBase.setScene(escena);
            escenarioBase.setTitle("To-Do List - Mis Tareas");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}