package indexador;

import java.text.Normalizer;
import java.util.*;

public class IndexadorTexto {

    // Conjunto de palavras irrelevantes (stopwords) que serão ignoradas no processamento
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

    /*
        removeStopwords - Função para remover palavras irrelevantes e limpar o texto
        @param texto - Texto original a ser processado
        @return palavrasValidas - Array de palavras significativas sem stopwords e pontuação
    */
    public String[] removeStopwords(String texto) {

        List<String> palavrasValidas = new ArrayList<>();

        // Divide o texto em palavras usando o espaço em branco como delimitador
        String[] palavras = texto.split("\\s+");

        for (String palavra : palavras) {
            // Remove pontuação e converte para minúsculas
            palavra = palavra.replaceAll("[^a-zA-ZáéíóúãõâêôçÁÉÍÓÚÃÕÂÊÔÇ]", "").toLowerCase();

            // Remove acentuação utilizando normalização Unicode
            palavra = Normalizer.normalize(palavra, Normalizer.Form.NFD);
            palavra = palavra.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

            // Adiciona apenas palavras não vazias e que não sejam stopwords
            if (!palavra.isEmpty() && !STOPWORDS.contains(palavra)) {
                palavrasValidas.add(palavra);
            }
        }

        // Retorna o array contendo apenas palavras relevantes
        return palavrasValidas.toArray(new String[0]);
    }

    /*
        calcularFrequencia - Função para calcular a frequência relativa das palavras
        @param palavras - Array de palavras relevantes sem stopwords
        @return frequencias - Mapa contendo cada palavra e sua frequência relativa (entre 0 e 1)
    */
    public Map<String, Float> calcularFrequencia(String[] palavras) {

        Map<String, Integer> contagem = new HashMap<>();
        Map<String, Float> frequencias = new HashMap<>();

        int total = palavras.length;

        // Conta a ocorrência de cada palavra
        for (String palavra : palavras) {
            contagem.put(palavra, contagem.getOrDefault(palavra, 0) + 1);
        }

        // Converte contagem para frequência relativa (valor entre 0 e 1)
        for (Map.Entry<String, Integer> entrada : contagem.entrySet()) {
            String palavra = entrada.getKey();
            int ocorrencias = entrada.getValue();
            float freq = (float) ocorrencias / total;
            frequencias.put(palavra, freq);
        }

        return frequencias;
    }

    /*
        processar - Função principal que combina limpeza e frequência
        @param texto - Texto original a ser processado
        @return frequencias - Mapa de termos relevantes e suas respectivas frequências no texto
    */
    public Map<String, Float> processar(String texto) {
        String[] palavrasFiltradas = removeStopwords(texto);
        return calcularFrequencia(palavrasFiltradas);
    }

}
