package cc.cmartin.system.repository;

import cc.cmartin.system.repository.MotorTransliteracion;
import cc.cmartin.system.model.Script;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MotorAljamiaArabe implements MotorTransliteracion {

    public MotorAljamiaArabe() {
    }

    // ================================================================
    // MotorTransliteracion
    // ================================================================

    @Override
    public Script obtenerEscritura() {
        return Script.ARABE;
    }

    /**
     * Convierte texto en español (el "pivote" de entrada) a aljamía.
     * Delega en convertirTexto(...), que es la lógica que antes vivía en
     * el conversor original.
     */
    @Override
    public String desdePivote(String pivote) {
        return convertirTexto(pivote);
    }

    /**
     * NOTA: la conversión aljamía → español (inversa) no existía en
     * ninguno de los tres archivos originales que se combinaron aquí
     * (el conversor original solo convertía en un sentido). Se deja
     * este método explícito en vez de simularla, para no dar una
     * traducción incorrecta en silencio. Si se necesita, hay que
     * escribirla aparte (recorriendo MAPA_CONSONANTES_ALJAMIA /
     * MAPA_VOCALES_ALJAMIA al revés, similar a lo que hacía el motor
     * antiguo con sus mapas inversos de consonantes/vocales).
     */
    @Override
    public String aPivote(String textoArabe) {
        throw new UnsupportedOperationException(
                "MotorAljamia: la conversión aljamía → español todavía no está implementada.");
    }

    // ================================================================
    // Tablas de conversión
    // ================================================================

    static final Set<String> VOCALES = conjuntoOrdenadoInmutable(
            "a", "e", "i", "o", "u",
            "á", "é", "í", "ó", "ú",
            "ĕ", "ü",
            "à", "ä", "ò"
    );

    /**
     * Deletreos aljamiados predefinidos para palabras españolas de
     * origen árabe. Solo se usan cuando la opción "deletreos árabes"
     * está activada.
     */
    static final Map<String, String> DELETREOS_PREDEFINIDOS = mapaOrdenadoInmutable(
            "almohada", "اَلْمُخَدَّة",
            "elixir", "اَلْإكْسِيٗرْ",
            "alcohol", "اَلْكُحُلْ",
            "sandía", "سَنْدِيَّة",
            "tarea", "طَرِيٗحَة",
            "cifra", "صِفْرَ",
            "albahaca", "اَلْبَحَقَ",
            "asesino", "حَشَاشِيٗنُ",
            "taza", "طَاسَ",
            "limón", "لِيٗمُوٗنْ",
            "algodón", "اَلْقُطُنْ",
            "azúcar", "اَلْسُّكَّرْ",
            "árabe", "عَرَبَا",
            "arábigo", "عَرَبِغُ",
            "arábiga", "عَرَبِغُ",
            "aceite", "اَلْزَيْتَ",
            "azeyte", "اَلْزَيْتَ",
            "azeite", "اَلْزَيْتَ",
            "açotea", "اَلْسُّطَيْحَ"
    );

    /**
     * Palabras con "h" inicial muda (u otras irregularidades) que se
     * reescriben a su equivalente fonético en alfabeto latino antes de
     * que corra el algoritmo principal de conversión.
     */
    static final Map<String, String> PREDEFINIDOS_ESPANOL = mapaOrdenadoInmutable(
            "haber", "aber",
            "ha", "a",
            "he", "e",
            "has", "as",
            "hay", "ay",
            // Nota: 'ha' está intencionalmente repetido en el objeto
            // literal original de JS (la segunda aparición es un no-op
            // inofensivo ahí, porque solo sobrescribe la clave con el
            // mismo valor).
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

    static final Map<String, String> MAPA_CONSONANTES_ALJAMIA = mapaOrdenadoInmutable(
            "b", "ب",
            "p", "بّ",
            "t", "ت",
            "j", "ج",
            "č", "جّ",
            "D", "د",
            "d", "ذ",
            "r", "ر",
            "R", "رّ",
            "z", "ز",
            "ç", "س",
            "s", "ش",
            "x", "شّ",
            "g", "غ",
            "f", "ف",
            "k", "ک",
            "l", "ل",
            "L", "لّ",
            "m", "م",
            "n", "ن",
            "ñ", "نّ",
            "w", "و",
            "h", "ه",
            "y", "ي"
    );

    static final Map<String, String> MAPA_VOCALES_ALJAMIA = mapaOrdenadoInmutable(
            "a", "َ",
            "e", "َا",
            "i", "ِ",
            "u", "ُ",
            "é", "َاَا",
            "á", "َأَ",
            "ú", "ُؤُ"
    );

    static final Map<String, String> MAPA_VOCALES_ALJAMIA_ALT = mapaOrdenadoInmutable(
            "a", "اَ",
            "e", "ءَا",
            "i", "اِ",
            "u", "اُ",
            "é", "اَا",
            "á", "اَأَ",
            "ú", "اُؤُ"
    );

    /** Marcas diacríticas árabes, usadas cerca del final de la conversión. */
    static final Set<String> DIACRITICOS = conjuntoOrdenadoInmutable(
            "َ", "ِ", "ُ", "ّ", "ٗ", "ْ"
    );

    // --- helpers para construir colecciones ordenadas e inmutables ---
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
    // Conversión español → aljamía (antes en un conversor aparte)
    // ================================================================

    /**
     * Convierte un texto completo en español a aljamía.
     */
    public String convertirTexto(String texto) {
        Set<String> sinEspacioDespues = new LinkedHashSet<>(List.of(
                "el", "la", "los", "las", "lo", "que", "a", "al", "y", "de", "del",
                "para", "por", "si", "en", "con",
                "no", "porque", "sobre", "mi", "tu", "su", "me", "te", "se", "le"
        ));
        return convertirPalabras(texto, this::convertirPalabra, MotorAljamiaArabe::mapearPuntuacion, sinEspacioDespues);
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

        // Fusiona p.ej. "el" + " " + "gato" en un solo token "el:gato" para
        // que el mapeador después pueda unirlos sin espacio (elgato).
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
        return espanolAAljamiado(ortografiaCorrecta(palabraInicial).toLowerCase());
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
        Map<Character, String> mapaPuntuacion = new LinkedHashMap<>();
        mapaPuntuacion.put('?', "؟");
        mapaPuntuacion.put('¿', "");
        mapaPuntuacion.put('¡', "");
        mapaPuntuacion.put(',', "،");
        mapaPuntuacion.put(';', "؛");
        mapaPuntuacion.put('.', "۔");
        mapaPuntuacion.put('“', "”");
        mapaPuntuacion.put('”', "“");
        return mapaPuntuacion.getOrDefault(c, String.valueOf(c));
    }

    private String espanolAAljamiado(String entradaPalabra) {
        String palabra = entradaPalabra;

        Map<String, String> predefinidos = new LinkedHashMap<>();
        predefinidos.put("alá", "اٗلٗلٗهٗ");
        predefinidos.put("hay", "اَيْ");

        predefinidos.putAll(DELETREOS_PREDEFINIDOS);
        predefinidos.put("elicsir", predefinidos.get("elixir"));
        predefinidos.put("taça", predefinidos.get("taza"));
        predefinidos.put("açúcar", predefinidos.get("azúcar"));

        List<String> instantaneaClaves = new ArrayList<>(predefinidos.keySet());
        for (String palabraEntrada : instantaneaClaves) {
            if (!(palabraEntrada.endsWith("o") || palabraEntrada.endsWith("a") || palabraEntrada.endsWith("e"))) {
                if (palabraEntrada.endsWith("ón")) {
                    String base = palabraEntrada.substring(0, palabraEntrada.length() - 2);
                    String actual = predefinidos.get(palabraEntrada);
                    predefinidos.put(base + "ones", actual.substring(0, actual.length() - 1) + "َاشْ");
                }
                continue;
            }
            String valorActual = predefinidos.get(palabraEntrada);
            if (valorActual.endsWith("ة")) {
                predefinidos.put(palabraEntrada + "s", valorActual.substring(0, valorActual.length() - 1) + "شْ");
                continue;
            }
            predefinidos.put(palabraEntrada + "s", valorActual + "شْ");
        }

        Map<String, String> predefinidosLatino = PREDEFINIDOS_ESPANOL;

        if (palabra.equals("y")) {
            palabra = "i";
        }

        for (Map.Entry<String, String> entrada : predefinidos.entrySet()) {
            String clave = entrada.getKey();
            String valor = entrada.getValue();
            if (palabra.equals(clave)) {
                return valor.replace("ٗ", "");
            }
            palabra = palabra.replace(":" + clave + ":", ":" + valor + ":");
            if (palabra.startsWith(clave + ":")) {
                palabra = valor + ":" + palabra.substring(clave.length() + 1);
            } else if (palabra.endsWith(":" + clave)) {
                palabra = palabra.substring(0, palabra.length() - clave.length() - 1) + ":" + valor;
            }
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
                .replace("a:i", "a'i").replace("e:i", "e'i").replace("o:i", "o'i")
                .replace("a:u", "a'u").replace("e:u", "e'u").replace("o:u", "o'u")
                .replace(":", "");

        palabra = palabra.replace("aí", "ayi").replace("eí", "eyi").replace("oí", "oyi").replace("uí", "uyi");
        palabra = palabra.replace("aú", "awu").replace("eú", "ewu").replace("iú", "iwu").replace("oú", "owu");
        palabra = palabra.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
        palabra = palabra.replace("ge", "je").replace("gi", "ji").replace("gue", "ge").replace("gui", "gi");
        palabra = palabra.replace("ce", "çe").replace("ci", "çi").replace("que", "ke").replace("qui", "ki")
                .replace("q", "k").replace("güe", "guwe").replace("güi", "guwi").replace("ü", "u");
        palabra = palabra.replace("ch", "č").replace("c", "k").replace("zk", "çk");
        palabra = palabra.replace("ia", "iya").replace("ie", "iye").replace("io", "iyo").replace("iu", "iyu");
        palabra = palabra.replace("ua", "uwa").replace("ue", "uwe").replace("ui", "uwi").replace("uo", "uwo");
        palabra = palabra.replace("v", "b").replace("ll", "L").replace("rr", "R");
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

        palabra = "'" + palabra;
        palabra = palabra.replace("ee", "é").replace("aa", "á").replace("oo", "ú").replace("uu", "ú");
        for (String fuerte : new String[]{"a", "e", "o"}) {
            for (String fuerte2 : new String[]{"a", "o"}) {
                palabra = palabra.replace(fuerte + fuerte2, fuerte + "'" + fuerte2);
            }
            palabra = palabra.replace(fuerte + "e", fuerte + "'é");
        }
        palabra = palabra.replace("o", "u");

        Map<String, String> mapaConsonantes = MAPA_CONSONANTES_ALJAMIA;
        Map<String, String> mapaVocales = MAPA_VOCALES_ALJAMIA;
        Map<String, String> mapaVocalesAlt = MAPA_VOCALES_ALJAMIA_ALT;

        for (Map.Entry<String, String> entrada : mapaConsonantes.entrySet()) {
            palabra = palabra.replace(entrada.getKey(), entrada.getValue());
        }
        for (Map.Entry<String, String> entrada : mapaVocalesAlt.entrySet()) {
            palabra = palabra.replace("'" + entrada.getKey(), entrada.getValue());
        }
        for (Map.Entry<String, String> entrada : mapaVocales.entrySet()) {
            palabra = palabra.replace(entrada.getKey(), entrada.getValue());
        }

        palabra = palabra.replace("'", "").replace("´", "").replace("˜", "");

        Set<String> conjuntoConsonantes = new LinkedHashSet<>(mapaConsonantes.values());
        Set<String> conjuntoDiacriticos = DIACRITICOS;

        // Recorre la palabra de derecha a izquierda, insertando un sukún
        // (ْ) después de cualquier consonante "pelada" que no esté ya
        // seguida de un diacrítico.
        StringBuilder constructorPalabra = new StringBuilder(palabra);
        int longitudOriginal = constructorPalabra.length();
        for (int i = longitudOriginal; i >= 0; i--) {
            String caracter = subcadena(constructorPalabra.toString(), i, i + 1);
            String siguiente = (i == constructorPalabra.length()) ? "" : subcadena(constructorPalabra.toString(), i + 1, i + 2);
            if ((conjuntoConsonantes.contains(caracter) || caracter.equals("ّ")) && !conjuntoDiacriticos.contains(siguiente)) {
                constructorPalabra.insert(i + 1, 'ْ');
            }
        }
        palabra = constructorPalabra.toString();

        palabra = palabra.replace("ٗ", "");

        return palabra;
    }

    /**
     * Equivalente al String.prototype.substring(start, end) "tolerante"
     * de JavaScript: los índices fuera de rango se recortan en vez de
     * lanzar una excepción.
     */
    private static String subcadena(String s, int inicio, int fin) {
        int longitud = s.length();
        int inicioRecortado = Math.max(0, Math.min(inicio, longitud));
        int finRecortado = Math.max(inicioRecortado, Math.min(fin, longitud));
        return s.substring(inicioRecortado, finRecortado);
    }
}
