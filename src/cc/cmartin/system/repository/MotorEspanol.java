package cc.cmartin.system.repository;

import cc.cmartin.system.model.Script;

/** Motor del español moderno. Es el pivote, así que no transforma nada. */
public class MotorEspanol implements MotorTransliteracion {

    @Override
    public Script obtenerEscritura() {
        return Script.ESPAÑOL;
    }

    @Override
    public String aPivote(String palabra) {
        return palabra;
    }

    @Override
    public String desdePivote(String pivote) {
        return pivote;
    }
}