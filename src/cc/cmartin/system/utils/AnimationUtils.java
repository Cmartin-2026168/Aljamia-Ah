
package cc.cmartin.system.utils;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.TextInputControl;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.input.MouseEvent;

public class AnimationUtils {

    // Constructor privado para evitar instanciación
    private AnimationUtils() {
    }

    // --- Ya existentes ---

    public static void aplicarFadeIn(Node nodo) {
        nodo.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(400), nodo);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    public static void aplicarEfectoHover(Node nodo) {
        ScaleTransition st = new ScaleTransition(Duration.millis(120), nodo);
        nodo.setOnMouseEntered(e -> {
            st.setToX(1.04);
            st.setToY(1.04);
            st.playFromStart();
        });
        nodo.setOnMouseExited(e -> {
            st.setToX(1.0);
            st.setToY(1.0);
            st.playFromStart();
        });
    }

    // --- Nuevas (punto 1: hover extendido reutiliza aplicarEfectoHover arriba) ---

    /**
     * Variante de hover pensada para tarjetas (libro-tarjeta): eleva
     * ligeramente el nodo y añade sombra dorada en vez de solo escalar.
     */
    public static void aplicarEfectoHoverTarjeta(Node nodo) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(150), nodo);
        DropShadow sombra = new DropShadow(18, Color.web("#FFB52E", 0.0));
        nodo.setEffect(sombra);

        Timeline sombraIn = new Timeline(
                new KeyFrame(Duration.millis(150),
                        new KeyValue(sombra.colorProperty(), Color.web("#FFB52E", 0.35))));
        Timeline sombraOut = new Timeline(
                new KeyFrame(Duration.millis(150),
                        new KeyValue(sombra.colorProperty(), Color.web("#FFB52E", 0.0))));

        nodo.setOnMouseEntered(e -> {
            tt.setToY(-4);
            tt.playFromStart();
            sombraIn.playFromStart();
        });
        nodo.setOnMouseExited(e -> {
            tt.setToY(0);
            tt.playFromStart();
            sombraOut.playFromStart();
        });
    }

    // --- Punto 2: entrada deslizante por nodo ---

    /**
     * Entrada con desvanecido + deslizamiento horizontal. desplazamientoX
     * positivo entra desde la derecha, negativo desde la izquierda.
     */
    public static void aplicarSlideIn(Node nodo, double desplazamientoX) {
        nodo.setOpacity(0);
        nodo.setTranslateX(desplazamientoX);

        FadeTransition fade = new FadeTransition(Duration.millis(320), nodo);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(320), nodo);
        slide.setFromX(desplazamientoX);
        slide.setToX(0);

        ParallelTransition entrada = new ParallelTransition(nodo, fade, slide);
        entrada.play();
    }

    // --- Punto 3: foco animado en campos de texto ---

    /**
     * Brillo dorado animado alrededor del campo al enfocar/desenfocar,
     * ya que -fx-border-color no admite transición nativa en CSS de JavaFX.
     */
    public static void aplicarFocoAnimado(TextInputControl campo) {
        DropShadow brillo = new DropShadow(0, Color.web("#FFB52E", 0.0));
        campo.setEffect(brillo);

        campo.focusedProperty().addListener((obs, sinFoco, conFoco) -> {
            Timeline t = new Timeline();
            if (conFoco) {
                t.getKeyFrames().add(new KeyFrame(Duration.millis(180),
                        new KeyValue(brillo.radiusProperty(), 10),
                        new KeyValue(brillo.colorProperty(), Color.web("#FFB52E", 0.55))));
            } else {
                t.getKeyFrames().add(new KeyFrame(Duration.millis(180),
                        new KeyValue(brillo.radiusProperty(), 0),
                        new KeyValue(brillo.colorProperty(), Color.web("#FFB52E", 0.0))));
            }
            t.play();
        });
    }

    // --- Punto 4: sacudida de error ---

    /**
     * Shake horizontal para mensajes o campos de error (login, registro).
     */
    public static void aplicarSacudida(Node nodo) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(60), nodo);
        shake.setFromX(0);
        shake.setByX(8);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.setOnFinished(e -> nodo.setTranslateX(0));
        shake.play();
    }
    
        // --- Entrada escalonada: cada nodo sube y aparece con un pequeño retraso ---

    public static void aplicarEntradaEscalonada(double desplazamientoY, int retrasoMs, Node... nodos) {
        int indice = 0;
        for (Node nodo : nodos) {
            nodo.setOpacity(0);
            nodo.setTranslateY(desplazamientoY);

            FadeTransition fade = new FadeTransition(Duration.millis(380), nodo);
            fade.setFromValue(0);
            fade.setToValue(1);

            TranslateTransition subida = new TranslateTransition(Duration.millis(380), nodo);
            subida.setFromY(desplazamientoY);
            subida.setToY(0);
            subida.setInterpolator(Interpolator.EASE_OUT);

            ParallelTransition entrada = new ParallelTransition(fade, subida);
            entrada.setDelay(Duration.millis(indice * retrasoMs));
            entrada.play();
            indice++;
        }
    }

    // --- Escritura progresiva: el texto aparece letra por letra ---

    public static void aplicarEscrituraProgresiva(TextInputControl campo, String texto) {
        Object anterior = campo.getProperties().get("animEscritura");
        if (anterior instanceof Transition) {
            ((Transition) anterior).stop();
        }

        campo.clear();
        if (texto == null || texto.isEmpty()) {
            return;
        }

        int duracionMs = Math.min(900, Math.max(250, texto.length() * 15));
        Transition escritura = new Transition() {
            {
                setCycleDuration(Duration.millis(duracionMs));
                setInterpolator(Interpolator.LINEAR);
            }

            @Override
            protected void interpolate(double fraccion) {
                int cantidad = (int) Math.round(fraccion * texto.length());
                campo.setText(texto.substring(0, cantidad));
            }
        };
        escritura.setOnFinished(e -> campo.setText(texto));
        campo.getProperties().put("animEscritura", escritura);
        escritura.play();
    }

    // --- Pulso continuo y suave (para el medallón de la cabecera) ---

    public static void aplicarPulsoContinuo(Node nodo) {
        ScaleTransition pulso = new ScaleTransition(Duration.millis(2600), nodo);
        pulso.setFromX(1.0);
        pulso.setFromY(1.0);
        pulso.setToX(1.05);
        pulso.setToY(1.05);
        pulso.setAutoReverse(true);
        pulso.setCycleCount(Timeline.INDEFINITE);
        pulso.setInterpolator(Interpolator.EASE_BOTH);
        pulso.play();
    }

    // --- Efecto de presión: el botón se hunde al pulsarlo ---

    public static void aplicarEfectoPresion(Node nodo) {
        nodo.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            nodo.setScaleX(0.97);
            nodo.setScaleY(0.97);
        });
        nodo.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            double escala = nodo.isHover() ? 1.04 : 1.0;
            nodo.setScaleX(escala);
            nodo.setScaleY(escala);
        });
    }

    // --- Destello dorado: resalta una tarjeta cuando llega el resultado ---

    public static void aplicarDestelloDorado(Node nodo) {
    javafx.scene.effect.Effect previo = nodo.getEffect();
    DropShadow brillo = new DropShadow(0, Color.web("#FFC95C", 0.0));
    nodo.setEffect(brillo);

    Timeline destello = new Timeline(
            new KeyFrame(Duration.ZERO,
                    new KeyValue(brillo.radiusProperty(), 0),
                    new KeyValue(brillo.colorProperty(), Color.web("#FFC95C", 0.0))),
            new KeyFrame(Duration.millis(220),
                    new KeyValue(brillo.radiusProperty(), 26),
                    new KeyValue(brillo.colorProperty(), Color.web("#FFC95C", 0.90))),
            new KeyFrame(Duration.millis(700),
                    new KeyValue(brillo.radiusProperty(), 0),
                    new KeyValue(brillo.colorProperty(), Color.web("#FFC95C", 0.0))));
    destello.setOnFinished(e -> nodo.setEffect(previo));
    destello.play();
}
}