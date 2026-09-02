package cc.cmartin.system.repository;

import cc.cmartin.system.repository.MotorTransliteracion;
import cc.cmartin.system.model.Script;
import java.util.LinkedHashMap;
import java.util.Map;

public class MotorLatino implements MotorTransliteracion {

    private final Script escritura;

    public MotorLatino(Script script) {
        if (script != Script.ESPAÑOL && script != Script.CASTELLANO) {
            throw new IllegalArgumentException(
                    "MotorLatino solo admite Script.ES_MODERNO o Script.ES_MEDIEVAL, se recibió: " + script);
        }
        this.escritura = script;
    }

    // ================================================================
    // MotorTransliteracion
    // ================================================================

    @Override
    public Script obtenerEscritura() {
        return escritura;
    }

    /**
     * Convierte texto en esta escritura al pivote (español moderno). Para
     * ES_MODERNO es una operación identidad, porque el pivote ya es
     * español moderno; para ES_MEDIEVAL, normaliza la ortografía antigua
     * a la moderna (con las limitaciones documentadas arriba, sobre todo
     * la falta de reconstrucción de tildes).
     */
    @Override
    public String aPivote(String palabraEnEstaEscritura) {
        if (escritura == Script.ESPAÑOL) {
            return palabraEnEstaEscritura;
        }
        return convertirTexto(palabraEnEstaEscritura, this::convertirPalabraAModerno);
    }

    /**
     * Convierte el pivote (español moderno) a esta escritura. Para
     * ES_MODERNO es una operación identidad; para ES_MEDIEVAL, "archaíza"
     * la ortografía moderna siguiendo las reglas documentadas arriba.
     */
    @Override
    public String desdePivote(String pivote) {
        if (escritura == Script.ESPAÑOL) {
            return pivote;
        }
        return convertirTexto(pivote, this::convertirPalabraAMedieval);
    }

    // ================================================================
    // Diccionario de irregularidades léxicas frecuentes
    // (alternancias f-/h-, b/v y h etimológica que no siguen una regla
    // fonética sistemática; ver fundamento histórico arriba)
    // ================================================================

    static final Map<String, String> PREDEFINIDOS_MODERNO_A_MEDIEVAL = mapaOrdenadoInmutable(
            // h etimológica latina, simplemente ausente en la Edad Media
            "haber", "aver",
            "he", "e",
            "has", "as",
            "ha", "a",
            "hemos", "emos",
            "habéis", "abéis",
            "han", "an",
            "había", "abía",
            "habrá", "abrá",
            "hombre", "omne",
            "honor", "onor",
            "hoy", "oy",
            "haya", "aya",
            "hubo", "ubo",
            "ahora", "agora",
            // f- inicial aspirada, escrita "f" en la Edad Media
            "hijo", "fijo",
            "hija", "fija",
            "hijos", "fijos",
            "hijas", "fijas",
            "hablar", "fablar",
            "hacer", "fazer",
            "hecho", "fecho",
            "hierro", "fierro",
            "hembra", "fembra",
            "harina", "farina",
            "horno", "forno",
            "hoja", "foja",
            "humo", "fumo",
            "hondo", "fondo",
            "hongo", "fongo",
            "huir", "fuir",
            "herir", "ferir",
            "hervir", "fervir",
            "hallar", "fallar",
            "hallazgo", "fallazgo",
            "hasta", "fasta",
            "hazaña", "fazaña",
            // b/v con alternancia léxica (no la del imperfecto, que es regla aparte)
            "beber", "bever",
            "vivir", "bivir",
            // otras grafías etimológicas/latinizantes repuestas en el XV
            "cristiano", "christiano",
            "cristiana", "christiana",
            "cristianos", "christianos",
            "cristianas", "christianas",
            "mujer", "muger"
    );

    static final Map<String, String> PREDEFINIDOS_MEDIEVAL_A_MODERNO =
            invertirMapaInmutable(PREDEFINIDOS_MODERNO_A_MEDIEVAL);

    // ================================================================
    // Sufijos del pretérito imperfecto de los verbos en -ar
    // (regla sistemática y regular, no léxica: ver fundamento histórico)
    // ================================================================

    private static final String[] SUFIJOS_IMPERFECTO_MODERNO =
            {"abamos", "abais", "aban", "abas", "aba"};
    private static final String[] SUFIJOS_IMPERFECTO_MEDIEVAL =
            {"avamos", "avais", "avan", "avas", "ava"};

    // ================================================================
    // Conversión palabra por palabra
    // ================================================================

    private String convertirPalabraAMedieval(String palabraOriginal) {
        String minuscula = palabraOriginal.toLowerCase();
        String predefinida = PREDEFINIDOS_MODERNO_A_MEDIEVAL.get(minuscula);
        String convertida = (predefinida != null) ? predefinida : reglasModernoAMedieval(minuscula);
        return igualarMayusculas(convertida, palabraOriginal);
    }

    private String convertirPalabraAModerno(String palabraOriginal) {
        String minuscula = palabraOriginal.toLowerCase();
        String predefinida = PREDEFINIDOS_MEDIEVAL_A_MODERNO.get(minuscula);
        String convertida = (predefinida != null) ? predefinida : reglasMedievalAModerno(minuscula);
        return igualarMayusculas(convertida, palabraOriginal);
    }

    /**
     * Reglas sistemáticas moderno → medieval, aplicadas solo cuando la
     * palabra no está en PREDEFINIDOS_MODERNO_A_MEDIEVAL.
     */
    private static String reglasModernoAMedieval(String entrada) {
        String palabra = entrada;

        // 1. Tildes: la marca gráfica del acento es una convención tardía
        // (ver fundamento histórico), así que se elimina.
        palabra = quitarTildes(palabra);

        // 2. Imperfecto de los verbos en -ar: -aba(...) → -ava(...).
        palabra = sustituirSufijo(palabra, SUFIJOS_IMPERFECTO_MODERNO, SUFIJOS_IMPERFECTO_MEDIEVAL);

        // 3. Sibilante dental sorda/sonora fundida: la "z" moderna se
        // archaíza como "ç" (letra reservada en la Edad Media para esta
        // familia de sonido delante de a/o/u; "c" delante de e/i ya
        // coincide con el uso medieval y no hace falta tocarla).
        palabra = palabra.replace("z", "ç");

        // 4. Sibilantes prepalatales fundidas: se protege primero "gue"/
        // "gui" (sonido /g/, no afectado por el reajuste), después se
        // reinterpreta "ge"/"gi" como el antiguo sonido palatal sonoro
        // (aquí normalizado, junto con toda "j", a la grafía "x").
        palabra = palabra.replace("gue", MARCA_GUE).replace("gui", MARCA_GUI);
        palabra = palabra.replace("ge", "xe").replace("gi", "xi");
        palabra = palabra.replace(MARCA_GUE, "gue").replace(MARCA_GUI, "gui");
        palabra = palabra.replace("j", "x");

        // 5. "h" muda: se protege el dígrafo "ch" (that no es una h muda,
        // es la africada che) y se elimina cualquier otra "h" restante
        // que no haya sido ya resuelta por el diccionario.
        palabra = palabra.replace("ch", MARCA_CH).replace("h", "");
        palabra = palabra.replace(MARCA_CH, "ch");

        return palabra;
    }

    /**
     * Reglas sistemáticas medieval → moderno, aplicadas solo cuando la
     * palabra no está en PREDEFINIDOS_MEDIEVAL_A_MODERNO. Es una
     * aproximación, no una inversa perfecta: no reconstruye tildes (ver
     * fundamento histórico) ni distingue qué "x"/"j" medieval era sorda
     * o sonora, porque esa distinción ya no existe en español moderno.
     */
    private static String reglasMedievalAModerno(String entrada) {
        String palabra = entrada;

        // 1. ç → z (inversa de la regla 3 de arriba).
        palabra = palabra.replace("ç", "z");

        // 2. x → j (inversa simplificada de la regla 4 de arriba; no se
        // reescribe como "g" ante e/i porque esa elección es léxica, no
        // sistemática, y el diccionario ya cubre los casos conocidos
        // como "muger" → "mujer").
        palabra = palabra.replace("x", "j");

        // 3. "hue-" inicial: el español moderno exige "h" delante de un
        // diptongo "ue" a comienzo de palabra por pura convención
        // gráfica (no etimológica); la grafía medieval no la llevaba.
        if (palabra.startsWith("ue")) {
            palabra = "h" + palabra;
        }

        // 4. Imperfecto de los verbos en -ar: -ava(...) → -aba(...).
        palabra = sustituirSufijo(palabra, SUFIJOS_IMPERFECTO_MEDIEVAL, SUFIJOS_IMPERFECTO_MODERNO);

        return palabra;
    }

    private static String sustituirSufijo(String palabra, String[] sufijosOrigen, String[] sufijosDestino) {
        for (int i = 0; i < sufijosOrigen.length; i++) {
            String sufijo = sufijosOrigen[i];
            if (palabra.length() > sufijo.length() && palabra.endsWith(sufijo)) {
                return palabra.substring(0, palabra.length() - sufijo.length()) + sufijosDestino[i];
            }
        }
        return palabra;
    }

    private static String quitarTildes(String palabra) {
        return palabra.replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o").replace("ú", "u");
    }

    // Marcas de protección temporal (caracteres de uso privado Unicode,
    // no aparecen nunca en texto real) para que los reemplazos de "ge"/
    // "gi"/"h" no toquen por error los dígrafos "gue"/"gui"/"ch".
    private static final String MARCA_GUE = "\uE000";
    private static final String MARCA_GUI = "\uE001";
    private static final String MARCA_CH = "\uE002";

    // ================================================================
    // Tokenización: separa el texto en palabras y no-palabras, convierte
    // solo las palabras y deja el resto (espacios, puntuación) intacto.
    // Mucho más simple que la de MotorAljamia/MotorAljamiaHebrea porque
    // aquí no hace falta fusionar tokens sin espacio ni remapear
    // puntuación: la puntuación española es la misma en ambas grafías.
    // ================================================================

    private static String convertirTexto(String texto, java.util.function.Function<String, String> mapeadorPalabra) {
        StringBuilder resultado = new StringBuilder();
        StringBuilder palabraActual = new StringBuilder();

        for (int i = 0; i <= texto.length(); i++) {
            boolean esLetra = i < texto.length() && Character.isLetter(texto.charAt(i));
            if (esLetra) {
                palabraActual.append(texto.charAt(i));
                continue;
            }
            if (palabraActual.length() > 0) {
                resultado.append(mapeadorPalabra.apply(palabraActual.toString()));
                palabraActual.setLength(0);
            }
            if (i < texto.length()) {
                resultado.append(texto.charAt(i));
            }
        }
        return resultado.toString();
    }

    private static String igualarMayusculas(String palabra, String modeloDeCaso) {
        if (modeloDeCaso.toUpperCase().equals(modeloDeCaso)) {
            return palabra.toUpperCase();
        } else if (!modeloDeCaso.isEmpty()
                && modeloDeCaso.substring(0, 1).toUpperCase().equals(modeloDeCaso.substring(0, 1))) {
            if (palabra.isEmpty()) {
                return palabra;
            }
            return palabra.substring(0, 1).toUpperCase() + palabra.substring(1);
        }
        return palabra;
    }

    // ================================================================
    // Helpers para construir colecciones ordenadas e inmutables (mismo
    // patrón que en MotorAljamia/MotorAljamiaHebrea, por consistencia)
    // ================================================================

    private static Map<String, String> mapaOrdenadoInmutable(String... paresClaveValor) {
        if (paresClaveValor.length % 2 != 0) {
            throw new IllegalArgumentException("Se esperaba un número par de argumentos (clave, valor, clave, valor, ...)");
        }
        Map<String, String> mapa = new LinkedHashMap<>();
        for (int i = 0; i < paresClaveValor.length; i += 2) {
            mapa.put(paresClaveValor[i], paresClaveValor[i + 1]);
        }
        return java.util.Collections.unmodifiableMap(mapa);
    }

    private static Map<String, String> invertirMapaInmutable(Map<String, String> original) {
        Map<String, String> invertido = new LinkedHashMap<>();
        for (Map.Entry<String, String> entrada : original.entrySet()) {
            invertido.put(entrada.getValue(), entrada.getKey());
        }
        return java.util.Collections.unmodifiableMap(invertido);
    }
}