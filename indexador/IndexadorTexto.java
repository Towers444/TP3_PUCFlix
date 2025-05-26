package indexador;

import java.text.Normalizer;
import java.util.*;
import java.io.*;

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

    /**
     * extractLemmas - Função de rotina que executa código em Python e coleta os lemas de uma frase
     * @param text String a ser lematizada
     * @return List<String> com lemmas da frase
     * @throws IOException
     * @throws InterruptedException
     */ 
    public static List<String> extractLemmas(String text) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("python3", "indexador/lematizar.py")
            .redirectErrorStream(true)
            .start();
        
        // Enviar texto para o Python
        try (OutputStream os = process.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
            writer.write(text);
        }
        
        // Ler resultados
        List<String> lemmas = new ArrayList<>();
        try (InputStream is = process.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lemmas.add(line);
            }
        }

        StringBuilder errorMsg = new StringBuilder();
        try (BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream(), "UTF-8"))) {
            errorReader.lines().forEach(line -> errorMsg.append(line).append("\n"));
        }
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Erro no script Python. Código: " + exitCode);
        }
        
        return lemmas;
    }

    /*
        normalizarTexto - Função para remover palavras irrelevantes e limpar o texto
        @param texto - Texto original a ser processado
        @return palavrasValidas - Array de palavras significativas sem stopwords e pontuação
    */
    public String[] normalizarTexto(String texto) {

        List<String> palavrasValidas = new ArrayList<>();
        List<String> palavras = null;
        try {
            palavras = extractLemmas(texto);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        String[] palavrasFiltradas = normalizarTexto(texto);
        return calcularFrequencia(palavrasFiltradas);
    }

}
