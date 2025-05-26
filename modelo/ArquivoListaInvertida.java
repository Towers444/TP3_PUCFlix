package modelo;

import aeds3.ElementoLista;
import aeds3.ListaInvertida;
import indexador.IndexadorTexto;

import java.util.List;
import java.util.Map;
import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ArquivoListaInvertida {
    private String nomeEntidade;
    private String caminhoDicionario;
    private String caminhoBlocos;

    /*
     * Construtor da classe ArquivoListaInvertida
     */
    public ArquivoListaInvertida(String nomeEntidade) {
        this.nomeEntidade = nomeEntidade;
        this.caminhoDicionario = "dados/dicionario." + nomeEntidade + ".listainv.db";
        this.caminhoBlocos = "dados/blocos." + nomeEntidade + ".listainv.db";
    }

    /*
	 * incluirListaInvertida - Função para indexar uma entidade e inserir seus termos na lista invertida
     * @param id - Identificador único da entidade
     * @param nome - Nome a ser adicionado à lista invertida
     */
    public void incluirListaInvertida(int id, String nome) throws Exception {
        // Criar da estrutura de lista invertida
        ListaInvertida lista;

        // Instanciar o indexador e processa o nome da entidade (remove stopwords e calcula TF)
        IndexadorTexto idx = new IndexadorTexto();
        Map<String, Float> freq = idx.processar(nome);

        // Criar o diretório "dados" se não existir
        File d = new File("dados");
        if (!d.exists())
            d.mkdir();

        // Inicializar a lista invertida com base nos arquivos da entidade
        lista = new ListaInvertida(4, caminhoDicionario, caminhoBlocos);

        // Para cada termo e sua frequência, criar uma entrada na lista invertida
        for (Map.Entry<String, Float> entrada : freq.entrySet()) {
            // Identificar os valores de cada entrada
            String termo = entrada.getKey();
            float frequencia = entrada.getValue();

            // Adicionar o termo com o ID da entidade e a frequência TF
            lista.create(termo, new ElementoLista(id, frequencia));
            
            // Exibir a lista invertida atual (apenas para debug/visualização)
            lista.print();
        }
    }

    /*
     * buscarEntidades - Função para buscar entidades que contêm os termos da consulta, ordenando-as por relevância TF-IDF
     * @param entrada - Texto de busca inserido pelo usuário
     * @param totalEntidades - Número total de entidades indexadas, utilizado para cálculo de IDF
     * @return resultado - Lista de objetos ElementoLista com os IDs das entidades e seus respectivos pesos TF-IDF
     */
    public List<ElementoLista> buscarEntidades(String entrada, int totalEntidades) throws Exception {
        // Criar lista a ser retornada
        List<ElementoLista> resultado = new ArrayList<>();

        // Pré-processamento da query: remove stopwords e pontuação
        IndexadorTexto idx = new IndexadorTexto();
        String[] termos = idx.normalizarTexto(entrada);

        System.out.println("Termos processados: " + Arrays.toString(termos));

        // Inicializa lista invertida para a entidade
        ListaInvertida lista = new ListaInvertida(4, caminhoDicionario, caminhoBlocos);

        // Criar Mapa para acumular os valores de TF-IDF para cada ID de entidade 
        Map<Integer, Float> mapaResultados = new HashMap<>();

        // Percorrer cada termo da busca
        for (String termo : termos) {
            // Lê os elementos (IDs e TFs) associados ao termo na lista invertida
            ElementoLista[] elementos = lista.read(termo);
    
            // Testar se o elemento foi encontrado no índice
            if (elementos != null && elementos.length > 0) {
                // Cálcular do IDF para o termo: log10(N / df) + 1
                float idf = (float) ( Math.log10( (double) totalEntidades / elementos.length ) + 1 );
                System.out.println("IDF para '" + termo + "': " + idf);

                // Percorrer cada ocorrência da palavra (documento com esse termo)
                for (ElementoLista elemento : elementos) {
                    // Calcular o TF-IF (TF * IDF)
                    float tfIdf = elemento.getFrequencia() * idf;

                    // Somar o TF-IDF no mapa. Se o ID já existe, acumula.
                    mapaResultados.put(elemento.getId(), mapaResultados.getOrDefault(elemento.getId(), 0f) + tfIdf);
                }
            } else {
                System.out.println("Termo '" + termo + "' não encontrado na lista invertida.");
            }
        }

        // Converter o mapa de resultados em uma lista de ElementoLista
        for (Map.Entry<Integer, Float> entradaMapa : mapaResultados.entrySet()) {
            resultado.add(new ElementoLista(entradaMapa.getKey(), entradaMapa.getValue()));
        }

        // Ordena os resultados por ordem decrescente de relevância (TF-IDF)
        resultado.sort((a, b) -> Float.compare(b.getFrequencia(), a.getFrequencia()));

        // Retorna a lista de séries ordenadas por relevância
        return resultado; 
    }
}
