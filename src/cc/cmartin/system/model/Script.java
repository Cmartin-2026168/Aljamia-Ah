package cc.cmartin.system.model;

public enum Script {
    ESPAÑOL("Español"),
    ARABE("Árabe (aljamía)"),
    HEBREO("Hebreo (Ladino)");

    private final String etiqueta;

    Script(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}