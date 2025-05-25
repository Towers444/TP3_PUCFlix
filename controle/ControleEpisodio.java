package controle;

import modelo.ArquivoEpisodio;
import modelo.ArquivoSerie;
import entidades.Serie;
import entidades.Episodio;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ControleEpisodio {
    private ArquivoEpisodio arqEpisodio;
    private Serie serie;

    /*
     * Construtor da classe ControleEpisodio sem a vinculação de Série
     */
    public ControleEpisodio() throws Exception{
        this.arqEpisodio = new ArquivoEpisodio();
    }
    
    /*
     * Construtor da classe ControleEpisodio com a vinculação de Série
     * @param serie - Série referente à classe atual
     */
    public ControleEpisodio(Serie serie) throws Exception {
        // Testar se o objeto Série é válido
        if (serie == null)
            throw new Exception ("Série nula!");
           
        // Testar se o ID da Série é válido
        if (!ControleSerie.validarSerie(serie.getID()))
            throw new Exception ("Série inválida!");

        // Definir atributos da instância
        this.arqEpisodio = new ArquivoEpisodio();
        this.serie = serie;
    }

    /*
     * incluirEpisodio - Função para adicionar um Episódio ao Banco de Dados
     * @param e - Episódio a ser inserido
     * @param id - ID do Episódio inserido
     */
    public int incluirEpisodio(Episodio e) throws Exception {
        // Testar se o objeto Episódio é válido
        if (e == null)
            throw new Exception ("Episódio nulo!");

        // Testar se o Episódio pertence à Série da instância atual
        if ( this.serie != null &&  e.getIDSerie() != this.serie.getID() ) 
            throw new Exception ("Episódio não pertence à Série atual!");

        // Criar o Episódio a partir do ArquivoEpisódio
        int id = arqEpisodio.create(e);

        // Retornar
        return id;
    }

    /*
     * incluirEpisodio - Função para adicionar um Episódio ao Banco de Dados
     * @param e - Episódio a ser inserido
     * @param id - ID do Episódio inserido
     */
    public int incluirEpisodioPovoar(Episodio e) throws Exception {
        // Testar se o objeto Episódio é válido
        if (e == null)
            throw new Exception ("Episódio nulo!");

        // Criar o Episódio a partir do ArquivoEpisódio
        int id = arqEpisodio.create(e);

        // Retornar
        return id;
    }


    /*
     * excluirEpisodio - Função para excluir um Episódio a partir do seu ID
     * @param id - ID do Episódio a ser excluído
     * @return boolean - True se bem sucedido, False caso contrário
     */
    public boolean excluirEpisodio(int id) throws Exception {
        // Ler o Episódio a partir do ArquivoEpisódio usando seu ID
        Episodio e = arqEpisodio.read(id);

        // Testar se o Episódio pertence à Série da instância atual
        if ( e.getIDSerie() != this.serie.getID() ) 
            throw new Exception ("Episódio não pertence à Série atual!");

        // Excluir o Episódio a partir do ArquivoEpisódio e retornar o seu status
        return arqEpisodio.delete(id);
    }

    /*
     * excluirEpisodio - Função para excluir um Episódio a partir do seu objeto
     * @param e - Episódio a ser excluído
     * @return boolean - True se bem sucedido, False caso contrário
     */
    public boolean excluirEpisodio(Episodio e) throws Exception {
        // Testar se o objeto Episódio é válido
        if (e == null) 
            throw new Exception ("Episódio nulo!");

        // Testar se o Episódio pertence à Série da instância atual
        if ( e.getIDSerie() != this.serie.getID() ) 
            throw new Exception ("Episódio não pertence à Série atual!");

        // Excluir o Episódio a partir do ArquivoEpisodio e retornar o seu status
        return arqEpisodio.delete(e.getID());
    }

    /*
     * excluirEpisodio - Função para excluir o Episódio de uma determinada temporada
     * @param e - Episódio a ser excluído
     * @param temporada - Temporada da Série a qual o Episódio pertence
     * @return boolean - True se bem sucedido, False caso contrário
     */
    public boolean excluirEpisodio(Episodio e, int temporada) throws Exception {
        // Testar se o objeto Episódio é válido
        if (e == null) 
            throw new Exception ("Episódio nulo!");

        // Testar se o Episódio pertence à Série da instância atual
        if ( e.getIDSerie() != this.serie.getID() ) 
            throw new Exception ("Episódio não pertence à Série atual!");

        // Testar se o Episódio pertence à temporada correpondente
        if (e.getTemporada() != temporada)
            throw new Exception ("Episódio não pertence à temporada!");

        // Excluir o Episódio a partir do ArquivoEpisodio e retornar o seu status
        return arqEpisodio.delete(e.getID());
    }

    /*
     * alterarEpisodio - Função para alterar um Episódio
     * @param e - Objeto já alterado a ser inserido no Banco de Dados
     * @return boolean - True se bem sucedido, False caso contrário
     */
    public boolean alterarEpisodio(Episodio e) throws Exception {
        // Testar se o objeto Episódio é válido
        if (e == null) 
            throw new Exception ("Episódio nulo!");

        // Testar se o Episódio pertence à Série da instância atual
        if ( e.getIDSerie() != this.serie.getID() ) 
            throw new Exception ("Episódio não pertence à Série atual!");

        // Atualizar o Episódio a partir do ArquivoEpisodio e retornar o seu status
        return arqEpisodio.update(e);
    }

    /*
     * buscarEpisodio - Função para buscar todos os Episódios da Série da instância atual
     * @return episodios - Lista com todos os Epitódios pertencentes à Série atual
     */
    public List<Episodio> buscarEpisodio() throws Exception {
        // Definir instância do ArquivoSerie
        ArquivoSerie arqSerie = new ArquivoSerie();

        // Ler todos os Episódios da Série atual a partir do ArquivoSerie
        Episodio[] arrayEpisodios = arqSerie.readEpisodios(serie.getID());

        // Converter Episodio[] para List<Episodio>
        List<Episodio> episodios = new ArrayList<Episodio>( Arrays.asList(arrayEpisodios) );

        // Retornar lista de Episódios
        return episodios;
    }

    /*
     * buscarEpisodio - Função para buscar um Episódio a partir do seu ID
     * @param id - ID do Episódio a ser buscado
     * @return e - Objeto do Episódio buscado
     */
    public Episodio buscarEpisodio(int id) throws Exception {
        // Ler o Episódio a partir do ArquivoEpisodio usando seu id
        Episodio e = arqEpisodio.read(id);

        // Testar a leitura do Episódio
        if (e == null)
            throw new Exception("Episódio não encontrado!");

        // System.out.println("[ControleEpisodio]");
        // System.out.print(e);

        // Testar se o Episódio pertence à Série da instância atual
        if ( e.getIDSerie() != this.serie.getID() ) 
            throw new Exception ("Episódio não pertence à Série atual!");

        // Retornar Episódio
        return e;
    }

    /*
     * buscarEpisodio - Função para buscar um ou mais episódios a partir de uma String
     * @param entrada - String a ser buscada
     * @return episodiosValidos - Lista dos Episódios encontrados que pertencem à Série atual
     */
    public List<Episodio> buscarEpisodio(String entrada) throws Exception {
        // Ler todos os Episódios que têm o nome iniciando com a String correspondente
        Episodio episodios[] = arqEpisodio.readNome(entrada);

        // Definir lista de Episódios
        List<Episodio> episodiosValidos = new ArrayList<Episodio>();

        // Iterar pela lista de Episódios
        for (Episodio episodio : episodios) {
            // Testar se não há alguma Série vinculada ou o Episódio pertence à Série atual
            if (this.serie == null || episodio.getIDSerie() == serie.getID())
                episodiosValidos.add(episodio); // Inserir na lista
        }

        // Retornar lista de Episódios válidos
        return episodiosValidos;
    }

    /*
     * buscarEpisodioTemporada - Função para buscar todos os Episódios de uma temporada da Série atual
     * @param temporada - Temporada dos Episódios a serem buscados
     * @return episodios - Lista com os Episódios pertencentes à temporada especificada da Série atual
     */
    public List<Episodio> buscarEpisodioTemporada(int temporada) throws Exception {
        // Definir instância do ArquivoSerie
        ArquivoSerie arqSerie = new ArquivoSerie();

        // Ler todos os Episódios da Série atual a partir do ArquivoSerie
        Episodio[] arrayEpisodios = arqSerie.readEpisodios(serie.getID());

        // Converter Episodio[] para List<Episodio>
        List<Episodio> episodios = new ArrayList<Episodio>( Arrays.asList(arrayEpisodios) );
        List<Episodio> episodiosTemporada = new ArrayList<Episodio>();

        // Iterar sobre todos os Episódios encontrados e filtrar apenas os que pertencem à temporada
        for (Episodio episodio : episodios) {
            // Testar se o Episódio pertence à temporada especificada
            if (episodio.getTemporada() == temporada)
                episodiosTemporada.add(episodio);
        }

        // Testar se algum episódio da Temporada foi encontrado
        if (episodiosTemporada.isEmpty())
            throw new Exception("Não há Episódios vinculados a essa Temporada!");

        // Retornar os Episódios que pertencem à temporada da Série atual
        return episodiosTemporada;        
    }

    /*
     * buscarEpisodio - Função para buscar um objeto Episódio pelo ID de uma temporada específica da Série atual 
     * @param id - ID do Episódio a ser buscado
     * @param temporada - Temporada da Série atual
     * @return e - Episódio buscado de uma temporada específica da Série atual
     */
    public Episodio buscarEpisodio(int id, int temporada) throws Exception {
        // ler o episódio a partir do arquivoepisódio usando seu id
        Episodio e = arqEpisodio.read(id);

        // Testar se o Episódio pertence à Série da instância atual
        if ( e.getIDSerie() != this.serie.getID() ) 
            throw new Exception ("Episódio não pertence à Série atual!");

        // Testar se o Episódio pertence à temporada da Série da instância atual
        if (e.getTemporada() != temporada)
            throw new Exception ("Episódio não pertence à temporada!");

        // Retornar Episódio encontrada
        return e;
    }

    /*
     * verificarEpisodiosSerie - Função estática para verificar se existem episódios de uma Série a partir de seu ID
     * @param IDSerie - ID da Série a ser testada
     * @return resposta - True se existir algum Episódio da Série atual, False caso contrário
     */
    public static boolean verificarEpisodiosSerie(int IDSerie) {
        // Definir variável de resposta
        boolean resposta;

        // Iniciar bloco try-catch
        try {
        // Definir instância do ArquivoSerie
            ArquivoSerie arqSerie = new ArquivoSerie();
            Episodio[] eps = arqSerie.readEpisodios(IDSerie);

        // Testar se há algum episódio na Série encontrada
            if (eps.length > 0)
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
     * povoar - Função para popular o Banco de Dados com Episódios amostrais
     */
    public void povoar(){
        // Inicar bloco try-catch
        try {
            // Iniciar ArrayList de diretores, pois para inserir é precisso passar um ArrayList como parâmetro
            ArrayList<String> diretores = new ArrayList<String>();

            // Inserir Episódio
            diretores.add("Takahiro Ikezoe");
            diretores.add("Yasuhiro Irie");
            incluirEpisodioPovoar(new Episodio(this.serie.getID(), "Hagane no renkinjutsushi", 1, LocalDate.of(2009, 04, 05), 24.0F, 7, diretores));

            // Inserir Episódio
            diretores.clear();
            diretores.add("Hiromu Arakawa");
            diretores.add("Hiroshi Ônogi");
            incluirEpisodioPovoar(new Episodio(this.serie.getID(), "Hajimari no hi", 1, LocalDate.of(2009, 04, 12), 24.0F, 8, diretores));

            // Inserir Episódio
            diretores.clear();
            diretores.add("Yasuhiro Irie");
            diretores.add("Masao Ôkubo");
            incluirEpisodioPovoar(new Episodio(this.serie.getID(), "Jakyô no machi", 1, LocalDate.of(2009, 04, 19), 24.0F, 7, diretores));



            // Attack on Titan
            diretores.add("Tetsurō Araki");
            incluirEpisodioPovoar(new Episodio(12, "To You, 2,000 Years in the Future", 1, LocalDate.of(2013, 4, 7), 24.0F, 9, diretores));

            diretores.clear();
            diretores.add("Tetsurō Araki");
            incluirEpisodioPovoar(new Episodio(12, "That Day", 1, LocalDate.of(2013, 4, 14), 24.0F, 9, diretores));

            diretores.clear();
            diretores.add("Masashi Koizuka");
            incluirEpisodioPovoar(new Episodio(12, "A Dim Light Amid Despair", 1, LocalDate.of(2013, 4, 21), 24.0F, 8, diretores));

            diretores.clear();
            diretores.add("Shinji Higuchi");
            incluirEpisodioPovoar(new Episodio(12, "The Night of the Closing Ceremony", 1, LocalDate.of(2013, 4, 28), 24.0F, 8, diretores));

            diretores.clear();
            diretores.add("Tetsurō Araki");
            diretores.add("Masashi Koizuka");
            incluirEpisodioPovoar(new Episodio(12, "First Battle", 1, LocalDate.of(2013, 5, 5), 24.0F, 9, diretores));

            diretores.clear();
            diretores.add("Yūsuke Kaneda");
            incluirEpisodioPovoar(new Episodio(12, "The World the Girl Saw", 1, LocalDate.of(2013, 5, 12), 24.0F, 9, diretores));

            diretores.clear();
            diretores.add("Tetsurō Araki");
            incluirEpisodioPovoar(new Episodio(12, "Small Blade", 1, LocalDate.of(2013, 5, 19), 24.0F, 8, diretores));

            diretores.clear();
            diretores.add("Masashi Koizuka");
            incluirEpisodioPovoar(new Episodio(12, "I Can Hear His Heartbeat", 1, LocalDate.of(2013, 5, 26), 24.0F, 9, diretores));

            diretores.clear();
            diretores.add("Shinji Higuchi");
            incluirEpisodioPovoar(new Episodio(12, "Whereabouts of His Left Arm", 1, LocalDate.of(2013, 6, 2), 24.0F, 8, diretores));

            diretores.clear();
            diretores.add("Tetsurō Araki");
            diretores.add("Yūsuke Kaneda");
            incluirEpisodioPovoar(new Episodio(12, "Response", 1, LocalDate.of(2013, 6, 9), 24.0F, 9, diretores));
            System.out.println("\nEpisódios povoados!");

            // Black Mirror
            diretores.add("Owen Harris");  
            incluirEpisodioPovoar(new Episodio(1, "San Junipero", 4, LocalDate.of(2016, 10, 21), 61.0F, 10, diretores));  

            diretores.clear();  
            diretores.add("Joe Wright");  
            incluirEpisodioPovoar(new Episodio(1, "Nosedive", 3, LocalDate.of(2016, 10, 21), 63.0F, 8, diretores));  

            diretores.clear();  
            diretores.add("Dan Trachtenberg");  
            incluirEpisodioPovoar(new Episodio(1, "Playtest", 3, LocalDate.of(2016, 10, 21), 57.0F, 9, diretores));  

            diretores.clear();  
            diretores.add("Jodie Foster");  
            incluirEpisodioPovoar(new Episodio(1, "Arkangel", 4, LocalDate.of(2017, 12, 29), 52.0F, 7, diretores));  

            diretores.clear();  
            diretores.add("Colm McCarthy");  
            incluirEpisodioPovoar(new Episodio(1, "Hated in the Nation", 3, LocalDate.of(2016, 10, 21), 89.0F, 9, diretores));  

            diretores.clear();  
            diretores.add("Toby Haynes");  
            incluirEpisodioPovoar(new Episodio(1, "USS Callister", 4, LocalDate.of(2017, 12, 29), 76.0F, 10, diretores));  

            diretores.clear();  
            diretores.add("James Hawes");  
            incluirEpisodioPovoar(new Episodio(1, "The Entire History of You", 1, LocalDate.of(2011, 12, 18), 44.0F, 9, diretores));  

            diretores.clear();  
            diretores.add("John Hillcoat");  
            incluirEpisodioPovoar(new Episodio(1, "Metalhead", 4, LocalDate.of(2017, 12, 29), 41.0F, 7, diretores));  

            diretores.clear();  
            diretores.add("Tim Van Patten");  
            incluirEpisodioPovoar(new Episodio(1, "Striking Vipers", 5, LocalDate.of(2019, 6, 5), 61.0F, 8, diretores));  

            diretores.clear();  
            diretores.add("David Slade");  
            incluirEpisodioPovoar(new Episodio(1, "Bandersnatch", 5, LocalDate.of(2018, 12, 28), 90.0F, 9, diretores));  


            //Breaking Bad
            diretores.add("Vince Gilligan");
            incluirEpisodioPovoar(new Episodio(2, "Pilot", 1, LocalDate.of(2008, 1, 20), 58.0F, 9, diretores));

            diretores.clear();
            diretores.add("Adam Bernstein");
            incluirEpisodioPovoar(new Episodio(2, "Grilled", 2, LocalDate.of(2009, 3, 8), 48.0F, 10, diretores));

            diretores.clear();
            diretores.add("Rian Johnson");
            incluirEpisodioPovoar(new Episodio(2, "Fly", 3, LocalDate.of(2010, 5, 23), 47.0F, 8, diretores));

            diretores.clear();
            diretores.add("Michelle MacLaren");
            incluirEpisodioPovoar(new Episodio(2, "One Minute", 3, LocalDate.of(2010, 5, 2), 48.0F, 10, diretores));

            diretores.clear();
            diretores.add("Johan Renck");
            incluirEpisodioPovoar(new Episodio(2, "Crawl Space", 4, LocalDate.of(2011, 9, 25), 47.0F, 10, diretores));

            diretores.clear();
            diretores.add("Michael Slovis");
            incluirEpisodioPovoar(new Episodio(2, "Dead Freight", 5, LocalDate.of(2012, 8, 19), 48.0F, 9, diretores));

            diretores.clear();
            diretores.add("Bryan Cranston");
            incluirEpisodioPovoar(new Episodio(2, "Blood Money", 5, LocalDate.of(2013, 8, 11), 48.0F, 10, diretores));

            diretores.clear();
            diretores.add("Michelle MacLaren");
            incluirEpisodioPovoar(new Episodio(2, "Salud", 4, LocalDate.of(2011, 9, 18), 48.0F, 9, diretores));

            diretores.clear();
            diretores.add("Vince Gilligan");
            incluirEpisodioPovoar(new Episodio(2, "Ozymandias", 5, LocalDate.of(2013, 9, 15), 47.0F, 10, diretores));

            diretores.clear();
            diretores.add("Peter Gould");
            incluirEpisodioPovoar(new Episodio(2, "Felina", 5, LocalDate.of(2013, 9, 29), 55.0F, 10, diretores));

            // Chernobyl (ID: 3) - Limited Series
            diretores.add("Johan Renck");
            incluirEpisodioPovoar(new Episodio(3, "1:23:45", 1, LocalDate.of(2019, 5, 6), 65.0F, 10, diretores));

            diretores.clear();
            diretores.add("Johan Renck");
            incluirEpisodioPovoar(new Episodio(3, "Please Remain Calm", 2, LocalDate.of(2019, 5, 13), 65.0F, 10, diretores));

            diretores.clear();
            diretores.add("Johan Renck");
            incluirEpisodioPovoar(new Episodio(3, "Open Wide, O Earth", 3, LocalDate.of(2019, 5, 20), 65.0F, 9, diretores));

            diretores.clear();
            diretores.add("Johan Renck");
            incluirEpisodioPovoar(new Episodio(3, "The Happiness of All Mankind", 4, LocalDate.of(2019, 5, 27), 67.0F, 9, diretores));

            diretores.clear();
            diretores.add("Johan Renck");
            incluirEpisodioPovoar(new Episodio(3, "Vichnaya Pamyat", 5, LocalDate.of(2019, 6, 3), 72.0F, 10, diretores));

            // Dark (ID: 14)
            diretores.add("Baran bo Odar");
            incluirEpisodioPovoar(new Episodio(14, "Secrets", 1, LocalDate.of(2017, 12, 1), 51.0F, 8, diretores));

            diretores.clear();
            diretores.add("Baran bo Odar");
            incluirEpisodioPovoar(new Episodio(14, "Lies", 1, LocalDate.of(2017, 12, 1), 44.0F, 9, diretores));

            diretores.clear();
            diretores.add("Baran bo Odar");
            incluirEpisodioPovoar(new Episodio(14, "Beginnings and Endings", 2, LocalDate.of(2019, 6, 21), 52.0F, 9, diretores));

            diretores.clear();
            diretores.add("Baran bo Odar");
            incluirEpisodioPovoar(new Episodio(14, "The Travelers", 2, LocalDate.of(2019, 6, 21), 44.0F, 10, diretores));

            diretores.clear();
            diretores.add("Baran bo Odar");
            incluirEpisodioPovoar(new Episodio(14, "Deja-vu", 3, LocalDate.of(2020, 6, 27), 58.0F, 9, diretores));

            diretores.clear();
            diretores.add("Baran bo Odar");
            incluirEpisodioPovoar(new Episodio(14, "The Paradise", 3, LocalDate.of(2020, 6, 27), 63.0F, 10, diretores));

            diretores.clear();
            diretores.add("Baran bo Odar");
            incluirEpisodioPovoar(new Episodio(14, "Alpha and Omega", 2, LocalDate.of(2019, 6, 21), 47.0F, 10, diretores));

            diretores.clear();
            diretores.add("Baran bo Odar");
            incluirEpisodioPovoar(new Episodio(14, "An Endless Cycle", 3, LocalDate.of(2020, 6, 27), 55.0F, 9, diretores));

            diretores.clear();
            diretores.add("Baran bo Odar");
            incluirEpisodioPovoar(new Episodio(14, "The Origin", 3, LocalDate.of(2020, 6, 27), 72.0F, 10, diretores));

            diretores.clear();
            diretores.add("Baran bo Odar");
            incluirEpisodioPovoar(new Episodio(14, "Light and Shadow", 1, LocalDate.of(2017, 12, 1), 49.0F, 9, diretores));
        } catch (Exception e){
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }

}
