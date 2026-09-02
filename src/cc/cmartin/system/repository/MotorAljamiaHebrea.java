package cc.cmartin.system.repository;

import cc.cmartin.system.repository.MotorTransliteracion;
import cc.cmartin.system.model.Script;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MotorAljamiaHebrea implements MotorTransliteracion {

    public MotorAljamiaHebrea() {
    }

    // ================================================================
    // MotorTransliteracion
    // ================================================================

    @Override
    public Script obtenerEscritura() {
        return Script.HEBREO;
    }

    @Override
    public String desdePivote(String pivote) {
        return convertirTexto(pivote);
    }

    /**
     * NOTA: igual que en MotorAljamia, la dirección inversa (hebreo →
     * español) no está implementada. No se simula para no dar una
     * traducción incorrecta en silencio.
     */
    @Override
    public String aPivote(String textoHebreo) {
        throw new UnsupportedOperationException(
                "MotorAljamiaHebrea: la conversión hebreo → español todavía no está implementada.");
    }

    // ================================================================
    // Tablas de conversión
    // ================================================================

    static final Set<String> VOCALES = conjuntoOrdenadoInmutable(
            "a", "e", "i", "o", "u",
            "á", "é", "í", "ó", "ú",
            "ĕ", "ü"
    );

    /**
     * Igual que en MotorAljamia: palabras con "h" inicial muda (u otras
     * irregularidades) que se reescriben a su forma fonética en alfabeto
     * latino antes de que corra el algoritmo principal. Es un hecho de
     * la ortografía española, independiente del alfabeto de destino.
     */
    static final Map<String, String> PREDEFINIDOS_ESPANOL = mapaOrdenadoInmutable(
            "haber", "aber",
            "ha", "a",
            "he", "e",
            "has", "as",
            "hay", "ay",
            "hemos", "emos",
            "habéis", "abéis",
            "han", "an",
            "había", "abía",
            "habrá", "abrá",
            "habían", "abían",
            "habría", "abría",
            "habrían", "abrían",
            "hubiera", "ubiera",
            "hubieras", "ubieras",
            "hubieran", "ubieran",
            "haya", "aya",
            "hayan", "ayan",
            "hubo", "ubo",
            "hora", "ora",
            "horas", "oras",
            "christiano", "cristiano",
            "christiana", "cristiana",
            "christianos", "cristianos",
            "christianas", "cristianas"
    );

    /**
     * Consonantes del pivote fonémico → letras hebreas. Las que llevan
     * guerésh (׳) representan la letra base modificada con "rafe",
     * siguiendo el sistema documentado para la escritura Rashí sefardí.
     * Fuente directa (b/v, g-ch/dj, d/dh, p/f, s romance=samej,
     * sh=shin): tabla de "rafe" de la tradición judeoespañola.
     * "ñ" está marcada como extensión propia (ver advertencia arriba).
     */
    static final Map<String, String> MAPA_CONSONANTES_HEBREO = mapaOrdenadoInmutable(
            "b", "ב",
            "v", "ב׳",          // rafe sobre bet: b → v (documentado)
            "p", "פ",
            "f", "פ׳",          // rafe sobre pe: p → f (documentado)
            "t", "ת",
            "j", "ש",           // valor histórico judeoespañol de j/g(e,i): "sh"
            "č", "ג׳",          // rafe sobre guímel: ch/dj (documentado)
            "g", "ג",
            "D", "ד",           // d fuerte (inicial de palabra)
            "d", "ד׳",          // rafe sobre dálet: d suave/fricativa (documentado)
            "r", "ר",
            "R", "ר",           // hebreo no distingue por escrito r simple/vibrante
            "z", "ז",
            "ç", "ס",           // s histórica (de z, ce, ci) → samej, igual que la "s" llana
            "s", "ס",           // s romance llana → samej (documentado)
            "x", "ש",           // aproximación al sonido "sh"
            "k", "כ",
            "l", "ל",
            "L", "ל",           // hebreo no distingue por escrito l simple/palatal (ll)
            "m", "מ",
            "n", "נ",
            "ñ", "נ׳",          // extensión propia: rafe sobre nun para la nasal palatal
            "w", "ו",
            "h", "ה",
            "y", "י"
    );

    /**
     * Vocales del pivote fonémico → letras hebreas (matres lectionis).
     * El hebreo escrito, como el árabe, distingue de forma nativa solo
     * 3 timbres (a/i/u); por eso "e" se funde con "a" y "o" se funde
     * con "u" en la letra usada — igual en espíritu a como MotorAljamia
     * funde "o" con "u" en la tabla árabe. A final de palabra, la "a"
     * (y por la misma fusión, la "e") se escribe con ה en vez de א,
     * siguiendo la convención hebrea estándar para la vocal final.
     */
    static final Map<String, String> MAPA_VOCALES_HEBREO = mapaOrdenadoInmutable(
            "a", "א",
            "e", "א",
            "i", "י",
            "o", "ו",
            "u", "ו",
            "á", "א",
            "é", "א",
            "í", "י",
            "ó", "ו",
            "ú", "ו"
    );

    /** Letra final de palabra usada para la vocal "a"/"e" en esa posición. */
    static final String VOCAL_FINAL_A_E = "ה";

    /** Formas finales especiales (sofit) del hebreo, aplicadas al último carácter de cada palabra. */
    static final Map<String, String> FORMAS_FINALES = mapaOrdenadoInmutable(
            "כ", "ך",
            "מ", "ם",
            "נ", "ן",
            "פ", "ף",
            "צ", "ץ"
    );

    private static Set<String> conjuntoOrdenadoInmutable(String... elementos) {
        Set<String> conjunto = new LinkedHashSet<>();
        for (String elemento : elementos) {
            conjunto.add(elemento);
        }
        return java.util.Collections.unmodifiableSet(conjunto);
    }

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

    // ================================================================
    // Conversión español → aljamía hebrea
    // (misma tokenización/orquestación que MotorAljamia; solo cambia
    // la tabla final de mapeo y el paso de formas finales)
    // ================================================================

    public String convertirTexto(String texto) {
        Set<String> sinEspacioDespues = new LinkedHashSet<>(List.of(
                "el", "la", "los", "las", "lo", "que", "a", "al", "y", "de", "del",
                "para", "por", "si", "en", "con",
                "no", "porque", "sobre", "mi", "tu", "su", "me", "te", "se", "le"
        ));
        return convertirPalabras(texto, this::convertirPalabra, MotorAljamiaHebrea::mapearPuntuacion, sinEspacioDespues);
    }

    private static String convertirPalabras(String texto, java.util.function.Function<String, String> mapeador,
                                        java.util.function.Function<Character, String> mapeadorNoPalabra,
                                        Set<String> sinEspacioDespues) {
        List<Character> caracteres = new ArrayList<>();
        List<Boolean> esPalabra = new ArrayList<>();

        Set<Character> puntuacion = new LinkedHashSet<>(List.of(
                ' ', ',', ':', ';', '.', '!', '?', ';', '\n', '¿', '¡', '#', '"', '“', '”', '-'
        ));

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            caracteres.add(c);
            esPalabra.add(!puntuacion.contains(c));
        }

        List<String> palabras = new ArrayList<>();
        StringBuilder palabra = new StringBuilder();
        if (sinEspacioDespues == null) {
            sinEspacioDespues = new LinkedHashSet<>();
        }

        List<Boolean> combinarConSiguiente = new ArrayList<>();
        List<Boolean> necesitaMapeo = new ArrayList<>();

        for (int i = 0; i < caracteres.size(); i++) {
            if (esPalabra.get(i)) {
                palabra.append(caracteres.get(i));
            } else {
                if (palabra.length() > 0) {
                    combinarConSiguiente.add(sinEspacioDespues.contains(palabra.toString().toLowerCase()));
                    necesitaMapeo.add(true);
                    palabras.add(palabra.toString());
                    palabra = new StringBuilder();
                }
                palabras.add(mapeadorNoPalabra.apply(caracteres.get(i)));
                necesitaMapeo.add(false);
                combinarConSiguiente.add(false);
            }
        }
        if (palabra.length() > 0) {
            palabras.add(palabra.toString());
            combinarConSiguiente.add(false);
            necesitaMapeo.add(true);
        }

        for (int i = palabras.size() - 3; i >= 0; i--) {
            if (combinarConSiguiente.get(i) && palabras.get(i + 1).equals(" ")) {
                palabras.set(i, palabras.get(i) + ":" + palabras.get(i + 2));
                eliminarRango(palabras, i + 1, i + 3);
                eliminarRango(necesitaMapeo, i + 1, i + 3);
                eliminarRango(combinarConSiguiente, i + 1, i + 3);
            }
        }

        for (int i = 0; i < palabras.size(); i++) {
            if (necesitaMapeo.get(i)) {
                palabras.set(i, mapeador.apply(palabras.get(i)));
            }
        }

        StringBuilder resultado = new StringBuilder();
        for (String p : palabras) {
            resultado.append(p);
        }
        return resultado.toString();
    }

    private static <T> void eliminarRango(List<T> lista, int desdeInclusive, int hastaExclusive) {
        lista.subList(desdeInclusive, hastaExclusive).clear();
    }

    private String convertirPalabra(String palabraInicial) {
        return espanolAHebreo(ortografiaCorrecta(palabraInicial).toLowerCase());
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

    private String ortografiaCorrecta(String entrada) {
        String salida = entrada.toLowerCase();

        salida = salida.replace("méxico", "méjico")
                .replace("mexicano", "mejicano")
                .replace("mexicana", "mejicana");
        salida = salida.replace("x", "cs");
        salida = salida.replace("z", "ç");

        salida = salida.replace("'", "");
        salida = igualarMayusculas(salida, entrada);
        return salida;
    }

    private static String mapearPuntuacion(char c) {
        // El judeoespañol aljamiado, a diferencia del árabe, no adoptó
        // signos de puntuación propios: se siguió usando la puntuación
        // latina/europea de siempre. Solo invertimos las comillas
        // tipográficas por el cambio de sentido de escritura (RTL).
        Map<Character, String> mapaPuntuacion = new LinkedHashMap<>();
        mapaPuntuacion.put('“', "”");
        mapaPuntuacion.put('”', "“");
        return mapaPuntuacion.getOrDefault(c, String.valueOf(c));
    }

    private String espanolAHebreo(String entradaPalabra) {
        String palabra = entradaPalabra;

        Map<String, String> predefinidosLatino = PREDEFINIDOS_ESPANOL;

        if (palabra.equals("y")) {
            palabra = "i";
        }

        for (Map.Entry<String, String> entrada : predefinidosLatino.entrySet()) {
            String clave = entrada.getKey();
            String valor = entrada.getValue();
            if (palabra.equals(clave)) {
                palabra = valor;
            }
            palabra = palabra.replace(":" + clave + ":", ":" + valor + ":");
            if (palabra.startsWith(clave + ":")) {
                palabra = valor + ":" + palabra.substring(clave.length() + 1);
            } else if (palabra.endsWith(":" + clave)) {
                palabra = palabra.substring(0, palabra.length() - clave.length() - 1) + ":" + valor;
            }
        }

        palabra = palabra.replace("de:e", "de").replace("sobre:e", "sobre")
                .replace("s:s", "s s").replace("l:l", "l l")
                .replace(":h", ":H").replace("y:", "i:").replace(":r", ":rr")
                .replace(":", "");

        // Normalización fonética española (independiente del alfabeto de
        // destino): mismos pasos que en MotorAljamia hasta producir el
        // pivote fonémico.
        palabra = palabra.replace("aí", "ayi").replace("eí", "eyi").replace("oí", "oyi").replace("uí", "uyi");
        palabra = palabra.replace("aú", "awu").replace("eú", "ewu").replace("iú", "iwu").replace("oú", "owu");
        palabra = palabra.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
        palabra = palabra.replace("ge", "je").replace("gi", "ji").replace("gue", "ge").replace("gui", "gi");
        palabra = palabra.replace("ce", "çe").replace("ci", "çi").replace("que", "ke").replace("qui", "ki")
                .replace("q", "k").replace("güe", "guwe").replace("güi", "guwi").replace("ü", "u");
        palabra = palabra.replace("ch", "č").replace("c", "k").replace("zk", "çk");
        palabra = palabra.replace("ia", "iya").replace("ie", "iye").replace("io", "iyo").replace("iu", "iyu");
        palabra = palabra.replace("ua", "uwa").replace("ue", "uwe").replace("ui", "uwi").replace("uo", "uwo");
        // A diferencia de MotorAljamia, aquí NO se funde "v" con "b": el
        // hebreo sí puede distinguirlas (ב vs ב׳), así que se conserva.
        palabra = palabra.replace("ll", "L").replace("rr", "R");
        palabra = palabra.replace("nd", "nD").replace("md", "mD");
        palabra = palabra.replace("ss", "s");

        if (palabra.startsWith("d")) {
            palabra = "D" + palabra.substring(1);
        }
        if (palabra.startsWith("r")) {
            palabra = "R" + palabra.substring(1);
        }
        if (palabra.startsWith("h")) {
            palabra = "H" + palabra.substring(1);
        }
        palabra = palabra.replace("Huwa", "huwa").replace("Huwe", "huwe");
        palabra = palabra.replace("h", "").replace("H", "h");
        palabra = palabra.replace("ai", "ay").replace("ei", "ey").replace("oi", "oy");
        palabra = palabra.replace("au", "aw").replace("eu", "ew").replace("ou", "ow");

        // A diferencia de MotorAljamia, aquí NO hace falta el mecanismo
        // de apóstrofo de hiato ni la fusión final "o"→"u": como cada
        // vocal se escribe con su propia letra hebrea (mater lectionis)
        // y no con un diacrítico, dos vocales seguidas simplemente se
        // escriben como dos letras seguidas, sin ambigüedad que resolver.

        boolean terminaEnAoE = palabra.endsWith("a") || palabra.endsWith("e");

        Map<String, String> mapaConsonantes = MAPA_CONSONANTES_HEBREO;
        Map<String, String> mapaVocales = MAPA_VOCALES_HEBREO;

        for (Map.Entry<String, String> entrada : mapaConsonantes.entrySet()) {
            palabra = palabra.replace(entrada.getKey(), entrada.getValue());
        }
        for (Map.Entry<String, String> entrada : mapaVocales.entrySet()) {
            palabra = palabra.replace(entrada.getKey(), entrada.getValue());
        }

        // Vocal final "a"/"e" → ה en vez de א (convención hebrea estándar
        // para la vocal final de palabra).
        if (terminaEnAoE && palabra.endsWith("א")) {
            palabra = palabra.substring(0, palabra.length() - 1) + VOCAL_FINAL_A_E;
        }

        // Formas finales especiales (sofit): sustituye la última letra
        // si es una de las 5 que cambian de forma al final de palabra,
        // sin tocar un guerésh ׳ que pueda venir justo después.
        palabra = aplicarFormaFinal(palabra);

        return palabra;
    }

    private static String aplicarFormaFinal(String palabra) {
        if (palabra.isEmpty()) {
            return palabra;
        }
        boolean llevaGueresh = palabra.endsWith("׳");
        String base = llevaGueresh ? palabra.substring(0, palabra.length() - 1) : palabra;
        if (base.isEmpty()) {
            return palabra;
        }
        String ultima = base.substring(base.length() - 1);
        String finalEquivalente = FORMAS_FINALES.get(ultima);
        if (finalEquivalente == null) {
            return palabra;
        }
        String nuevaBase = base.substring(0, base.length() - 1) + finalEquivalente;
        return llevaGueresh ? nuevaBase + "׳" : nuevaBase;
    }
}
