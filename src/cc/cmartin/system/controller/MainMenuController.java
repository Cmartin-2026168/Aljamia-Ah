package cc.cmartin.system.controller;

import cc.cmartin.system.model.Script;
import cc.cmartin.system.service.ServicioTransliteracion;
import cc.cmartin.system.utils.AnimationUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainMenuController implements Initializable {

    private static final Script ORIGEN = Script.ESPAÑOL;

    @FXML
    private StackPane medallon;

    @FXML
    private VBox bloqueTitulo;

    @FXML
    private VBox panelSelector;

    @FXML
    private VBox tarjetaOrigen;

    @FXML
    private VBox tarjetaResultado;

    @FXML
    private ComboBox<Script> cmbAlfabetoDestino;

    @FXML
    private Button btnTransliterar;

    @FXML
    private TextArea txtTexto;

    @FXML
    private TextArea txtTextoTransliterado;

    @FXML
    private VBox zonaLibre;

    private final ServicioTransliteracion servicio = new ServicioTransliteracion();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbAlfabetoDestino.getItems().addAll(Script.ARABE, Script.HEBREO);
        cmbAlfabetoDestino.setValue(Script.ARABE);

        actualizarAcento(Script.ARABE);
        cmbAlfabetoDestino.valueProperty().addListener(
                (obs, anterior, nuevo) -> actualizarAcento(nuevo));

        AnimationUtils.aplicarFocoAnimado(txtTexto);
        AnimationUtils.aplicarEfectoHover(btnTransliterar);
        AnimationUtils.aplicarEfectoPresion(btnTransliterar);

        Platform.runLater(() -> {
            AnimationUtils.aplicarEntradaEscalonada(24, 90,
                    medallon, bloqueTitulo, panelSelector,
                    tarjetaOrigen, btnTransliterar, tarjetaResultado);
            AnimationUtils.aplicarEntradaEscalonada(24, 90,
                    medallon, bloqueTitulo, panelSelector,
                    tarjetaOrigen, btnTransliterar, tarjetaResultado, zonaLibre);
            AnimationUtils.aplicarPulsoContinuo(medallon);
        });
    }

    private void actualizarAcento(Script destino) {
        tarjetaResultado.getStyleClass().removeAll("card-result-arabe", "card-result-hebreo");
        tarjetaResultado.getStyleClass().add(
                destino == Script.HEBREO ? "card-result-hebreo" : "card-result-arabe");
        txtTextoTransliterado.setNodeOrientation(
                destino == Script.ARABE || destino == Script.HEBREO
                        ? javafx.geometry.NodeOrientation.RIGHT_TO_LEFT
                        : javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
    }

    @FXML
    private void transliterar() {
        String texto = txtTexto.getText();
        Script destino = cmbAlfabetoDestino.getValue();

        if (texto == null || texto.isBlank()) {
            txtTextoTransliterado.clear();
            AnimationUtils.aplicarSacudida(tarjetaOrigen);
            return;
        }

        try {
            String resultado = servicio.convertir(texto, ORIGEN, destino);
            AnimationUtils.aplicarEscrituraProgresiva(txtTextoTransliterado, resultado);
            AnimationUtils.aplicarDestelloDorado(tarjetaResultado);
        } catch (IllegalArgumentException | UnsupportedOperationException ex) {
            txtTextoTransliterado.setText("Error: " + ex.getMessage());
            AnimationUtils.aplicarDestelloDorado(txtTextoTransliterado);
        }
    }
}
