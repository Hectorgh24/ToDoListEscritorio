/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package todolist;

import dominio.TareaImp;
import dominio.UsuarioImp;
import dto.Respuesta;
import interfaz.INotificador;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojo.Tarea;
import pojo.Usuario;
import utilidad.Utilidades;

public class FXMLPrincipalController implements Initializable, INotificador {

    @FXML
    private Label lbBienvenida;
    @FXML
    private TableView<Tarea> tvTareasPendientes;
    @FXML
    private TableColumn<Tarea, String> colTituloPendiente;
    @FXML
    private TableColumn<Tarea, String> colFechaPendiente;
    @FXML
    private TableView<Tarea> tvTareasTerminadas;
    @FXML
    private TableColumn<Tarea, String> colTituloTerminada;
    @FXML
    private TableColumn<Tarea, String> colFechaTerminada;

    private Usuario usuarioSesion;
    private ObservableList<Tarea> tareasPendientes;
    private ObservableList<Tarea> tareasTerminadas;
    private javafx.scene.control.Dialog<String> dialogoActual;
    private PasswordField pfActual;
    private TextInputDialog dialogoNueva;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablas();
        inicializarComponentesPassword();
    }    

    public void inicializarValores(Usuario usuario) {
        this.usuarioSesion = usuario;
        lbBienvenida.setText("Bienvenido(a), " + usuario.getNombre() + " " + usuario.getApellidoPaterno());
        cargarTareasPendientes();
        cargarTareasTerminadas();
    }

    private void configurarTablas() {
        colTituloPendiente.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colFechaPendiente.setCellValueFactory(new PropertyValueFactory<>("fechaRealizar"));
        tvTareasPendientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        colTituloTerminada.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colFechaTerminada.setCellValueFactory(new PropertyValueFactory<>("fechaRealizar"));
        tvTareasTerminadas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void inicializarComponentesPassword() {
        dialogoActual = new javafx.scene.control.Dialog<>();
        dialogoActual.setTitle("Cambiar Contraseña");
        dialogoActual.setHeaderText("Verificación de seguridad");

        ButtonType btnAceptar = new ButtonType("Aceptar", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialogoActual.getDialogPane().getButtonTypes().addAll(btnAceptar, ButtonType.CANCEL);

        pfActual = new PasswordField();
        pfActual.setPromptText("Contraseña actual");

        VBox vboxActual = new VBox();
        vboxActual.setSpacing(10);
        vboxActual.getChildren().addAll(new Label("Ingrese su contraseña ACTUAL:"), pfActual);
        dialogoActual.getDialogPane().setContent(vboxActual);

        dialogoActual.setResultConverter(dialogButton -> {
            if (dialogButton == btnAceptar) {
                return pfActual.getText();
            }
            return null;
        });

        dialogoNueva = new TextInputDialog();
        dialogoNueva.setTitle("Cambiar Contraseña");
        dialogoNueva.setHeaderText("Actualización de credenciales");
        dialogoNueva.setContentText("Ingrese su NUEVA contraseña:");
    }

    private void cargarTareasPendientes() {
        Respuesta respuesta = TareaImp.obtenerTareasPorEstado(usuarioSesion.getIdUsuario(), 1); // 1 = Pendiente
        if (!respuesta.isError()) {
            List<Tarea> lista = (List<Tarea>) respuesta.getDatos();
            tareasPendientes = FXCollections.observableArrayList(lista);
            tvTareasPendientes.setItems(tareasPendientes);
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void cargarTareasTerminadas() {
        Respuesta respuesta = TareaImp.obtenerTareasPorEstado(usuarioSesion.getIdUsuario(), 2); // 2 = Terminada
        if (!respuesta.isError()) {
            List<Tarea> lista = (List<Tarea>) respuesta.getDatos();
            tareasTerminadas = FXCollections.observableArrayList(lista);
            tvTareasTerminadas.setItems(tareasTerminadas);
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicAgregarTarea(ActionEvent event) {
        irFormularioTarea(null);
    }

    @FXML
    private void clicEditarTarea(ActionEvent event) {
        Tarea tareaSeleccionada = tvTareasPendientes.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null) {
            irFormularioTarea(tareaSeleccionada);
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Por favor, selecciona una tarea pendiente para editar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicVerDetalle(ActionEvent event) {
        Tarea tareaSeleccionada = tvTareasPendientes.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada == null) {
            tareaSeleccionada = tvTareasTerminadas.getSelectionModel().getSelectedItem();
        }
        
        if (tareaSeleccionada != null) {
            try {
                FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLDetalleTarea.fxml"));
                Parent vista = cargador.load();
                
                FXMLDetalleTareaController controlador = cargador.getController();
                controlador.inicializarDetalles(tareaSeleccionada);

                Stage escenario = new Stage();
                escenario.setScene(new Scene(vista));
                escenario.setTitle("Detalles de la Tarea");
                escenario.initModality(Modality.APPLICATION_MODAL);
                escenario.showAndWait();
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una tarea para ver sus detalles.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicMarcarTerminada(ActionEvent event) {
        Tarea tareaSeleccionada = tvTareasPendientes.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Marcar como terminada");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Estás seguro de marcar esta tarea como terminada?");
            Optional<ButtonType> respuestaConfirmacion = confirmacion.showAndWait();
            
            if (respuestaConfirmacion.get() == ButtonType.OK) {
                Respuesta respuesta = TareaImp.cambiarEstadoTarea(tareaSeleccionada.getIdTarea(), 2);
                if (!respuesta.isError()) {
                    cargarTareasPendientes();
                    cargarTareasTerminadas();
                } else {
                    Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
                }
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una tarea para marcarla como terminada.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicEliminarTarea(ActionEvent event) {
        Tarea tareaSeleccionada = tvTareasPendientes.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada == null) {
            tareaSeleccionada = tvTareasTerminadas.getSelectionModel().getSelectedItem();
        }

        if (tareaSeleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar Tarea");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Estás seguro de eliminar esta tarea permanentemente?");
            Optional<ButtonType> respuestaConfirmacion = confirmacion.showAndWait();
            
            if (respuestaConfirmacion.get() == ButtonType.OK) {
                Respuesta respuesta = TareaImp.eliminarTarea(tareaSeleccionada.getIdTarea());
                if (!respuesta.isError()) {
                    cargarTareasPendientes();
                    cargarTareasTerminadas();
                } else {
                    Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
                }
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una tarea para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicCambiarPassword(ActionEvent event) {
        pfActual.clear();
        dialogoNueva.getEditor().clear();

        javafx.application.Platform.runLater(() -> pfActual.requestFocus());

        Optional<String> resultadoActual = dialogoActual.showAndWait();
        
        if (resultadoActual.isPresent()) {
            String passActual = resultadoActual.get().trim();
            
            if (passActual.isEmpty()) {
                Utilidades.mostrarAlertaSimple("Advertencia", "La contraseña no puede estar vacía.", Alert.AlertType.WARNING);
                return;
            }

            Respuesta validacion = UsuarioImp.iniciarSesion(usuarioSesion.getRfc(), passActual);
            if (validacion.isError()) {
                Utilidades.mostrarAlertaSimple("Error", "La contraseña actual ingresada es incorrecta.", Alert.AlertType.ERROR);
                return; 
            }

            Optional<String> resultadoNueva = dialogoNueva.showAndWait();
            
            if (resultadoNueva.isPresent()) {
                String nuevaPassword = resultadoNueva.get().trim();
                
                if (nuevaPassword.isEmpty()) {
                    Utilidades.mostrarAlertaSimple("Advertencia", "La nueva contraseña no puede estar vacía.", Alert.AlertType.WARNING);
                    return;
                }

                if (passActual.equals(nuevaPassword)) {
                    Utilidades.mostrarAlertaSimple("Advertencia", "Ingresó la misma contraseña actual, ingrese una distinta.", Alert.AlertType.WARNING);
                    return;
                }

                Respuesta respuesta = UsuarioImp.cambiarPassword(usuarioSesion.getRfc(), nuevaPassword);
                if (!respuesta.isError()) {
                    Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
                } else {
                    Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLInicioSesion.fxml"));
            Scene escena = new Scene(vista);
            Stage escenarioBase = (Stage) lbBienvenida.getScene().getWindow();
            escenarioBase.setScene(escena);
            escenarioBase.setTitle("Iniciar Sesión");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void irFormularioTarea(Tarea tarea) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLFormularioTarea.fxml"));
            Parent vista = cargador.load();
            
             FXMLFormularioTareaController controlador = cargador.getController();
             controlador.inicializarInformacion(tarea, usuarioSesion.getIdUsuario(), this);

            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle(tarea == null ? "Agregar Tarea" : "Editar Tarea");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void notificarOperacion(String operacion) {
        cargarTareasPendientes();
    }
}