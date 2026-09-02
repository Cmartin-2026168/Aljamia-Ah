package cc.cmartin.system.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;

import cc.cmartin.system.Main;

public class ViewFactory {

    private static final String RUTA_VISTAS = "/cc/cmartin/system/view/";

    public ViewFactory() {
    }

    public static Scene cargarArchivoView(String nombreArchivo, int ancho, int alto) {
        String rutaArchivo = RUTA_VISTAS + nombreArchivo;
        try {
            URL archivo = Main.class.getResource(rutaArchivo);

            if (archivo == null) {
                throw new IOException("No se encontró el archivo FXML en la ruta: " + rutaArchivo);
            }

            FXMLLoader cargador = new FXMLLoader(archivo);
            cargador.setBuilderFactory(new JavaFXBuilderFactory());

            return new Scene(cargador.load(), ancho, alto);
        } catch (IOException errorEntradaSalida) {
            System.out.println("error al cargar archivo: " + errorEntradaSalida.getMessage());
            throw new UncheckedIOException("ERROR DE ARCHIVO", errorEntradaSalida);
        }
    }

    public void montarEscena(String nombreVista) {
        try {
            Scene escena = null;
            switch (nombreVista) {
                case "Main-Menu" -> {
                    escena = cargarArchivoView("MainMenu.fxml", 400, 550);
                    SceneManager.getInstanciaSceneManager().cambiarEscena(escena);
                }
            }
        } catch (NullPointerException objetoNulo) {
            System.out.println("error al montar escena: montar escena "
                    + objetoNulo.getMessage());
        }
    }

    public void ventanaPrincipal() {
        montarEscena("Main-Menu");
    }
}