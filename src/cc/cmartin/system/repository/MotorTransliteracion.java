package cc.cmartin.system.repository;

import cc.cmartin.system.model.Script;

public interface MotorTransliteracion {

    Script obtenerEscritura();

    String aPivote(String palabraEnEstaEscritura);

    String desdePivote(String pivote);
}
