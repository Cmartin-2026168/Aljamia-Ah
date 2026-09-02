package cc.cmartin.system.service;

import cc.cmartin.system.repository.MotorLatino;
import cc.cmartin.system.repository.MotorAljamiaHebrea;
import cc.cmartin.system.repository.MotorAljamiaArabe;
import cc.cmartin.system.repository.MotorTransliteracion;
import cc.cmartin.system.model.Script;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ServicioTransliteracion {

    private static final Pattern SEPARADOR = Pattern.compile("(?<=\\s)|(?=\\s)");

    private final Map<Script, MotorTransliteracion> motores = new EnumMap<>(Script.class);

    public ServicioTransliteracion() {
        registrar(new MotorLatino(Script.ESPAÑOL));
        registrar(new MotorLatino(Script.CASTELLANO));
        registrar(new MotorAljamiaArabe());
        registrar(new MotorAljamiaHebrea());
    }

    private void registrar(MotorTransliteracion motor) {
        motores.put(motor.obtenerEscritura(), motor);
    }

    public String convertir(String texto, Script origen, Script destino) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        MotorTransliteracion motorOrigen = requerirMotor(origen);
        MotorTransliteracion motorDestino = requerirMotor(destino);

        StringBuilder resultado = new StringBuilder();
        for (String fragmento : SEPARADOR.split(texto)) {
            if (fragmento.isBlank()) {
                resultado.append(fragmento);
            } else {
                String pivote = motorOrigen.aPivote(fragmento);
                resultado.append(motorDestino.desdePivote(pivote));
            }
        }
        return resultado.toString();
    }

    private MotorTransliteracion requerirMotor(Script script) {
        MotorTransliteracion motor = motores.get(script);
        if (motor == null) {
            throw new IllegalArgumentException("No hay motor registrado todavía para " + script);
        }
        return motor;
    }
}
