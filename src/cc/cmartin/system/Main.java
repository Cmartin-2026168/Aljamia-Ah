package cc.cmartin.system;

import javafx.application.Application;
import javafx.stage.Stage;
import cc.cmartin.system.utils.SceneManager;
import cc.cmartin.system.utils.ViewFactory;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage rootStage) {
        SceneManager
                .getInstanciaSceneManager()
                .setEscenarioPrincipal(rootStage);
        ViewFactory instanciaViewFactory = new ViewFactory();
        instanciaViewFactory.ventanaPrincipal();
    }

}
