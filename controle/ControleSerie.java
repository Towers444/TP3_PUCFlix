package controle;

import modelo.ArquivoSerie;
import entidades.Serie;
import entidades.Episodio;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ControleSerie {
    private ArquivoSerie arqSerie;

    /*
     * Construtor da classe ControleSerie
     */
    public ControleSerie() throws Exception {
        this.arqSerie = new ArquivoSerie();
    }

    /*
     * incluirSerie - Função para incluir uma Série no Banco de Dados
     * @param s - Objeto da Série a ser incluído
     * @return id - ID da Série incluída
     */
    public int incluirSerie(Serie s) throws Exception {
        // Testar o objeto da Série a ser inserido
        if (s == null)
            throw new Exception ("Série nula!");

        // Criar a Série a partir do ArquivoSerie
        int id = arqSerie.create(s);

        // Retornar ID
        return id;
    }

    /*
     * excluirSerie - Função para excluir uma Série a partir do seu ID
     * @param id  ID da Série a ser excluída
     * @return boolean - True se sucedido, False se contrário
     */
    public boolean excluirSerie(int id) throws Exception {
        // Buscar a Série com o ID especificado
        Serie s = arqSerie.read(id);

        // Verificar se a Série possui Episódios vinculados à ela
        if (ControleEpisodio.verificarEpisodiosSerie(s.getID())) 
            throw new Exception ("Há episódios vinculados com essa série!");
        
        // Excluir todas as atuações relacionadas com a Série
        ControleAtuacao.excluirAtuacaoSerie(id);

        // Exlcuir a Série a partir do ArquivoSerie e retornar o seu status
        return arqSerie.delete(id);
    }

    /*
     * alterarSerie - Função para alterar uma Série
     * @param s - Objeto já alterado a ser inserido no Banco de Dados
     * @return boolean - True se bem sucedido, False caso contrário
     */
    public boolean alterarSerie(Serie s) throws Exception {
        // Testar se o objeto Série é válido
        if (s == null) 
            throw new Exception ("Série nula!");

        // Atualizar o Episódio a partir do ArquivoEpisodio e retornar o seu status
        return arqSerie.update(s);
    }

    /*
     * buscarSerie - Função para buscar uma Série a partir do seu ID
     * @param id - ID da Série a ser buscada
     * @return s - Objeto da Série buscada
     */
    public Serie buscarSerie(int id) throws Exception {
        // Testar se existe um ArquivoSerie
        if (arqSerie == null)
            System.out.println("[ERRO]: Arquivo nulo");

        // Buscar a Série a partir do ArquivoSerie
        Serie s = arqSerie.read(id);

        // Retornar o objeto da Série
        return s;
    }

    /*
     * buscarSerie - Função para buscar uma ou mais Séries cujo nome inicia com uma determinada String
     * @param entrada - String a ser buscada
     * @return series - Lista de Séries cujo nome inicia com a String determinada
     */
    public List<Serie> buscarSerie(String entrada) throws Exception {    
        // Ler todas as Séries cujo nome inicia com a String determinada a partir do ArquivoSerie
        Serie[] arraySeries = arqSerie.readNome(entrada);

        // Converter Serie[] para List<Serie>
        List<Serie> series = new ArrayList<Serie>( Arrays.asList(arraySeries) );

        // Retornar lista de Séries
        return series;
    }

    /*
	 * buscarSerieListaInvertida - Função para buscar Séries utilizando a lista invertida
	 * @param entrada - Texto da consulta inserido pelo usuário (ex: nome da Série ou termos associados)
     * @return series - Lista de Séries encontradas na busca da lista invertida
     */
    public List<Serie> buscarSerieListaInvertida(String entrada) throws Exception {
        // Ler todas as Séries encontradas na lista invertida
        List<Serie> series = arqSerie.readListaInvertida(entrada);

        // Retornar
        return series;
    }

    /*
     * buscarSerieEpisodios - Função para buscar uma lista com todos os Episódios de uma determinada Série
     * @param id - ID da Série a ser buscada
     * @return episodios - Lista de Episódios da Série buscada
     */
    public List<Episodio> buscarSerieEpisodios(int id) throws Exception {
        // Ler todos os Episódios da Série atual a partir do ArquivoSerie
        Episodio[] arrayEpisodios = arqSerie.readEpisodios(id);

        // Converter Episodio[] para List<Episodio>
        List<Episodio> episodios = new ArrayList<Episodio>( Arrays.asList(arrayEpisodios) );    

        // Retornar lista de Episódios
        return episodios;
    }

    /*
     * buscarSerieEpisodiosPorTemporada - Função para buscar todos os Episódios de uma Série de uma determinada Temporada
     * @param id - ID da Série a ser buscada
     * @param temporada - Temporada da Série a ser buscada
     * @return episodios - Lista de Episódios que pertencem à temporada da Série especificada
     */
    public List<Episodio> buscarSerieEpisodiosPorTemporada(int id, int temporada) throws Exception {
        // Ler todos os Episódios da Série atual a partir do ArquivoSerie
        Episodio[] arrayEpisodios = arqSerie.readEpisodios(id); 

        // Converter Episodio[] para List<Episodio>
        List<Episodio> episodios = new ArrayList<Episodio>( Arrays.asList(arrayEpisodios) );

        // Iterar sobre a lista de todos os Episódios encontrados na Série
        int i = 0;
        for (Episodio e : episodios) {
            // Testar se o Episódio pertence à temporada especificada
            if (e.getTemporada() != temporada) // Remover da lista
                episodios.remove(i);
            i++;
        }

        // Retornar lista de Episódios
        return episodios;
    }

    /*
     * validarSerie - Função estática para verificar se uma Série existe a partir do seu ID
     * @param id - ID da Série a ser testada
     * @return resposta - True se a Série existir, False caso contrário
     */
    public static boolean validarSerie(int id) {
        // Definir variável de resposta
        boolean resposta;

        // Iniciar bloco try-catch
        try {
            // Definir instância do ArquivoSerie (Necessário, pois o método é estático, então o atributo arqSerie não é instanciado)
            ArquivoSerie arqSerie = new ArquivoSerie();

            // Testar se a Série foi encontrada
            if (arqSerie.read(id) != null)
                resposta = true;   
            else
                resposta = false;
        } catch (Exception e) {
            resposta = false;
        }

        // Retornar
        return resposta;
    }

    /*
     * povoar - Função para popular o Banco de Dados com Séries amostrais
     */
    public void povoar(){
        try {
            ArrayList<String> criadores = new ArrayList<String>();
            criadores.add("Charlie Brooker");
            incluirSerie(new Serie("Black Mirror", 2011, 
            "Contos de ficção científica que refletem o lado negro das telas e da tecnologia", 
            "Netflix", 9, criadores, 
            "Ficção científica"));

            criadores.clear();
            criadores.add("Vince Gilligan");
            incluirSerie(new Serie("Breaking Bad", 2008, 
            "Um professor de química diagnosticado com câncer de pulmão se transforma em fabricante e vendedor de metanfetamina, a fim de garantir o futuro da sua família", 
            "Netflix", 10, criadores, 
            "Drug Crime"));

            criadores.clear();
            criadores.add("Craig Mazin");
            incluirSerie(new Serie("Chernobyl", 2019, 
            "Minissérie em cinco partes que conta a história do pior acidente causado pelo homem na história, o desastre da Usina Nuclear de Chernobyl", 
            "HBO MAX", 9, criadores, 
            "Drama"));

            criadores.clear();
            criadores.add("Hiromo Arakawa");
            criadores.add("Makoto Inoue");
            incluirSerie(new Serie("Fullmetal Alchemist", 2003, 
            "Quando um fracassado ritul alquímico deixa os irmãos Edward e Alphonse Elric com corpos severamente danificados, eles começam a procurar a única coisa que pode salvá-los", 
            "Crunchyroll", 8, criadores, 
            "Anime"));

            criadores.clear();
            criadores.add("David Crane");
            criadores.add("Marta Kauffman");
            incluirSerie(new Serie("Friends", 1994, 
            "Relata a vida pessoal de seis amigos em seus 30 anos na cidade de Manhattan", 
            "HBO MAX", 6, criadores, 
            "Feel-good Romance"));

            criadores.clear();
            criadores.add("Lauren LeFranc");
            incluirSerie(new Serie("The Penguin", 2024, 
            "Acompanha a transformação de Oz Cobb de um desconhecido desfigurado em um famoso gângster de Gotham", 
            "HBO MAX", 8, criadores, 
            "Gangster"));

            // Netflix Originals
            criadores.add("Matt Duffer");
            criadores.add("Ross Duffer");
            incluirSerie(new Serie("Stranger Things", 2016, 
            "Quando um garoto desaparece, uma pequena cidade descobre um mistério envolvendo experimentos secretos, forças sobrenaturais e uma garota estranha.",
            "Netflix", 9, criadores, 
            "Sci-Fi Horror"));

            criadores.clear();
            criadores.add("Phoebe Waller-Bridge");
            incluirSerie(new Serie("Fleabag", 2016, 
            "Uma mulher jovem, livre, mas confusa, tenta lidar com a vida em Londres enquanto lembra de sua melhor amiga que morreu.",
            "Amazon Prime", 8, criadores, 
            "Comedy Drama"));

            criadores.clear();
            criadores.add("David Benioff");
            criadores.add("D.B. Weiss");
            incluirSerie(new Serie("Game of Thrones", 2011, 
            "Nove famílias nobres lutam pelo controle das terras de Westeros, enquanto um antigo inimigo retorna após milênios dormindo.",
            "HBO MAX", 9, criadores, 
            "Fantasy Drama"));

            criadores.clear();
            criadores.add("Greg Daniels");
            incluirSerie(new Serie("The Office (US)", 2005, 
            "Uma equipe disfuncional de escritório da Dunder Mifflin Paper Company, onde o comportamento do chefe excêntrico é a principal fonte de entretenimento.",
            "Netflix", 8, criadores, 
            "Mockumentary Comedy"));

            criadores.clear();
            criadores.add("Shonda Rhimes");
            incluirSerie(new Serie("Grey's Anatomy", 2005, 
            "Segue a vida pessoal e profissional de um grupo de médicos no Grey Sloan Memorial Hospital em Seattle.",
            "Disney+", 7, criadores, 
            "Medical Drama"));

            // Anime Series
            criadores.clear();
            criadores.add("Hajime Isayama");
            incluirSerie(new Serie("Attack on Titan", 2013, 
            "Em um mundo onde a humanidade vive dentro de cidades cercadas por enormes muralhas que os protegem de titãs gigantescos.",
            "Crunchyroll", 9, criadores, 
            "Anime Action"));

            // British Classics
            criadores.clear();
            criadores.add("Steven Moffat");
            criadores.add("Mark Gatiss");
            incluirSerie(new Serie("Sherlock", 2010, 
            "Uma atualização moderna das famosas aventuras de Sir Arthur Conan Doyle, com Sherlock Holmes e Dr. Watson no século 21.",
            "BBC", 9, criadores, 
            "Crime Drama"));

            // Recent Hits
            criadores.clear();
            criadores.add("Jantje Friese");
            criadores.add("Baran bo Odar");
            incluirSerie(new Serie("Dark", 2017, 
            "Uma criança desaparecida faz com que quatro famílias se ajudem a procurar respostas enquanto descobrem um mistério que abrange três gerações.",
            "Netflix", 8, criadores, 
            "Sci-Fi Thriller"));

            // Sitcoms
            criadores.clear();
            criadores.add("Chuck Lorre");
            criadores.add("Bill Prady");
            incluirSerie(new Serie("The Big Bang Theory", 2007, 
            "Uma mulher que se muda para o apartamento ao lado de dois físicos brilhantes, mas socialmente desajeitados, mostra a eles como pouco sabem sobre a vida fora do laboratório.",
            "HBO MAX", 7, criadores, 
            "Sitcom"));

            // Fantasy
            criadores.clear();
            criadores.add("Henry Cavill");
            criadores.add("Lauren Schmidt Hissrich");
            incluirSerie(new Serie("The Witcher", 2019, 
            "Geralt de Rívia, um caçador de monstros mutante, viaja em busca de seu destino em um mundo turbulento onde as pessoas muitas vezes são mais perversas que as bestas.",
            "Netflix", 8, criadores, 
            "Fantasy Adventure"));

            System.out.println("\nSéries povoadas!");

        } catch (Exception e){
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }
}
