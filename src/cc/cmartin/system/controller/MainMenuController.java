package cc.cmartin.system.controller;

import cc.cmartin.system.model.Script;
import cc.cmartin.system.service.ServicioTransliteracion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class MainMenuController implements Initializable{
    @FXML
    private ComboBox<Script> cmbAlfabetoInicial;

    @FXML
    private ComboBox<Script> cmbAlfabetoDestino;

    @FXML
    private Button btnTransliterar;

    @FXML
    private TextArea txtTexto;

    @FXML
    private TextArea txtTextoTransliterado;

    private final ServicioTransliteracion servicio = new ServicioTransliteracion();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbAlfabetoInicial.getItems().addAll(Script.values());
        cmbAlfabetoInicial.setValue(Script.ESPAÑOL);

        cmbAlfabetoDestino.getItems().addAll(Script.values());
        cmbAlfabetoDestino.setValue(Script.ARABE);
    }

    @FXML
    private void transliterar() {
        String texto = txtTexto.getText();
        Script origen = cmbAlfabetoInicial.getValue();
        Script destino = cmbAlfabetoDestino.getValue();

        try {
            String resultado = servicio.convertir(texto, origen, destino);
            txtTextoTransliterado.setText(resultado);
        } catch (IllegalArgumentException ex) {
            txtTextoTransliterado.setText("Error: " + ex.getMessage());
        }
    }}
