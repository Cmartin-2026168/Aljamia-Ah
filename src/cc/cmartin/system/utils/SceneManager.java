package cc.cmartin.system.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static SceneManager instanciaSceneManager;
    private Stage escenarioPrincipal;

    private SceneManager() {
    }

    public static SceneManager getInstanciaSceneManager() {
        if (instanciaSceneManager == null) {
            instanciaSceneManager = new SceneManager();
        }
        return instanciaSceneManager;
    }

    public void cambiarEscena(Scene escena) {
        try {
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.sizeToScene();
            escenarioPrincipal.show();

        } catch (NullPointerException objetoNulo) {
            System.out.println("Error en el Cambiar Escena"
                    + objetoNulo.getMessage());
        }

    }

    public void setEscenarioPrincipal(Stage escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public Stage getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

}
