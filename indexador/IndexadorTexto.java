package indexador;

import java.text.Normalizer;
import java.util.*;

public class IndexadorTexto {

    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
    "de", "a", "o", "e", "que", "do", "da", "em", "um", "para", "com", "não", "uma", "os", "no", "se", "na", "dos"
    ));

    private String texto;

    public IndexadorTexto() {
        this.texto = "";
    }

    public IndexadorTexto(String texto) {
        this.texto = texto;
    }
    
    public String[] removeStopwords(String texto) {

        List<String> palavrasValidas = new ArrayList<>();

        //Divide o texto em palavras usando o espaço em branco como delimitador
        String[] palavras = texto.split("\\s+");

        for (String palavra : palavras) {
            // Remove pontuação e coloca em minúsculas
            palavra = palavra.replaceAll("[^a-zA-ZáéíóúãõâêôçÁÉÍÓÚÃÕÂÊÔÇ]", "").toLowerCase();

            // Remove acentos
            palavra = Normalizer.normalize(palavra, Normalizer.Form.NFD);
            palavra = palavra.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

            // Ignora strings vazias e stopwords
            if (!palavra.isEmpty() && !STOPWORDS.contains(palavra)) {
                palavrasValidas.add(palavra);
            }
        }

        // Converte a lista para array e retorna
        return palavrasValidas.toArray(new String[0]);
    }

    public Map<String, Float> calcularFrequencia(String[] palavras) {

        Map<String, Integer> contagem = new HashMap<>();
        Map<String, Float> frequencias = new HashMap<>();

        int total = palavras.length;

        // Conta quantas vezes cada palavra aparece
        for (String palavra : palavras) {
            contagem.put(palavra, contagem.getOrDefault(palavra, 0) + 1);
        }

        // Calcula a frequência relativa em decimal
        for (Map.Entry<String, Integer> entrada : contagem.entrySet()) {
            String palavra = entrada.getKey();
            int ocorrencias = entrada.getValue();
            float freq = (float) ocorrencias / total;
            frequencias.put(palavra, freq);
        }

        return frequencias;

    }

    public Map<String, Float> processar(String texto) {
        String[] palavrasFiltradas = removeStopwords(texto);
        return calcularFrequencia(palavrasFiltradas);
    }

}