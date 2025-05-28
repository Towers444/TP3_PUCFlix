package modelo;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ElementoLista;
import entidades.Serie;
import entidades.Episodio;
import controle.ControleSerie;

import java.util.ArrayList;
import java.util.List;

public class ArquivoSerie extends Arquivo<Serie> {
    private ArvoreBMais<ParIDID> indiceSerieEpisodio;
    private ArvoreBMais<ParNomeID> indiceNome;
    private Buscador buscador;

    /*
     * Construtor da classe ArquivoSerie
     */
    public ArquivoSerie() throws Exception {
        // Chamar o construtor da classe herdada
        super("serie", Serie.class.getConstructor());

        // Chamar o construtor do índice de Série e Episódio
        indiceSerieEpisodio = new ArvoreBMais<> (
            ParIDID.class.getConstructor(), 
            5, 
            "./dados/indice/indiceSerieEpisodio.db"
        );

        // Chamar o construtor do índice de Nome da Série e seu ID
        indiceNome = new ArvoreBMais<>(
            ParNomeID.class.getConstructor(), 
            5, 
            "./dados/"+nomeEntidade+"/indiceNome.db");

        // Chamar o construtor do Buscador para entidades do tipo Série
        this.buscador = new Buscador("serie");
    }

    /*
     * create - Função para criar um objeto Série junto com os seus índices
     * @param s - Objeto da Série a ser inserido
     * @return id - ID da Série inserida
     */
    @Override
    public int create(Serie s) throws Exception {
        // Criar a Série
        int id = super.create(s);

        // Criar o índice Nome-Série
        ParNomeID pnid = new ParNomeID(s.getNome(), id);
        indiceNome.create(pnid);

        // Adicionar o nome da Série à lista invertida
        buscador.incluirEntidade(s.getID(), s.getNome());

        // Retornar o ID da Série
        return id;
    }

    /*
     * delete - Função para excluir uma Série a partir de um ID
     * @param id - ID da Série a ser excluída
     * @return boolean - True se sucedido, False se contrário
     */
    @Override
    public boolean delete(int id) throws Exception {
        // Definir variável de resposta
        boolean resposta;

        // Ler a Série a partir da superclasse
        Serie s = super.read(id);  

        // Definir Lista de Pares Série-Episódio que possuem o ID da Série especificada
        List<ParIDID> piis = indiceSerieEpisodio.read(new ParIDID(id, -1));

        // Testar se há algum Par encontrado
        if ( piis.size() > 0 )
            throw new Exception ("Não foi possível excluir a Série, pois há Episódios vinculados a ela!");

        // Excluir a Série a partir da superclasse e testar o seu status para excluir os índices
        if (super.delete(id)){
            resposta = indiceNome.delete(new ParNomeID(s.getNome(), id)) && buscador.excluirEntidade(s.getID(), s.getNome());
        } else {
            resposta = false;
        }
         
        // Retornar
        return resposta;
    }

    /*
     * update - Função para atualizar uma Série
     * @param novaSerie - Objeto já alterado da Série
     * @return boolean - True se sucedido, False se contrário
     */
    @Override
    public boolean update(Serie novaSerie) throws Exception {
        // Definir variável de resposta
        boolean resposta;

        // Ler a Série antiga a partir da superclasse
        Serie s = super.read(novaSerie.getID());

        // Atualizar a Série a partir da superclasse e testar o seu status
        if(super.update(novaSerie)) {
            // Testar se houve alteração no Nome 
            if( !s.getNome().equals(novaSerie.getNome()) ) {
                // Remover o Par Nome-Série anterior
                indiceNome.delete(new ParNomeID(s.getNome(), s.getID()));

                // Recriar o indice com o Nome alterado
                indiceNome.create(new ParNomeID(novaSerie.getNome(), novaSerie.getID()));

                // Atualizar os termos da entidade na lista invertida
                buscador.alterarEntidade(s.getID(), s.getNome(), novaSerie.getNome());
            }
            resposta = true;
        } else {
            resposta = false;
        }

        // Retornar
        return resposta;
    }

    /*
     * readEpisodios - Função para retornar todos os Episódios que pertencem à Série especificada
     * @param id - ID da Série
     * @return episodios - Array dos Episódios que pertencem à Série especificada
     */
    public Episodio[] readEpisodios(int id) throws Exception {
        // Definir ArquivoEpisodio
        ArquivoEpisodio arqEpisodio = new ArquivoEpisodio();

        // Testar se o ID existe no Banco de Dados
        if (!ControleSerie.validarSerie(id))
            throw new Exception("ID da Série inválido");

        // Buscar o ParIDID de Série-Episódio a partir do ID da Série
        ParIDID pnii = new ParIDID(id, -1);
        List<ParIDID> piis = indiceSerieEpisodio.read(pnii);

        // Testar se algum episódio foi encontrado
        if (piis.isEmpty()) 
            throw new Exception("Não há episódios vinculados a essa Série!");

        // Definir array de Episódios com o tamanho da lista de Pares Série-Episódio
        Episodio[] episodios = new Episodio[piis.size()];

        // Iterar sobre a lista de Par Série-Episódio
        int i = 0;

        for (ParIDID pii : piis) {
            // Buscar Episódio referente ao Par Série-Episódio
            Episodio e = arqEpisodio.read(pii.getID_Dependente());

            // Adicionar o episódio encontrado no array de Episódios
            episodios[i++] = e;
        }

        // Retornar lista de Episódios
        return episodios;
    }
    
    /*
     * readNome - Função para retornar uma array de Séries cujo nome começa com determinada String
     * @param nome - String a ser testada
     * @return series - Array de Séries cujo nome começa com determinada String
     */
    public Serie[] readNome(String nome) throws Exception {
        // Definir lista de Par Nome-ID que possuem a String especificada
        ParNomeID pnid = new ParNomeID(nome, -1);
        ArrayList<ParNomeID> pnis = indiceNome.read(pnid);

        // Testar se há algum Par encontrado
        if ( pnis.isEmpty() )
            throw new Exception ("Não foi encontrada nenhuma Série com o nome buscado!");

        // Definir array de Séries com o tamanho do número de pares
        Serie[] series = new Serie[pnis.size()];

        // Iterar sobre a lista de Pares Nome-ID a adicionar as Séries correspondentes ao array de Séries
        int i = 0;
        for(ParNomeID pni: pnis) 
            series[i++] = this.read(pni.getID());

        // Retornar
        return series;
    }

    /*
	 * readListaInvertida - Função para buscar Séries utilizando a lista invertida
	 * @param entrada - Texto da consulta inserido pelo usuário (ex: nome da Série ou termos associados)
     * @return series - Lista de Séries encontradas na busca da lista invertida
     */
    public List<Serie> readListaInvertida(String entrada) throws Exception {
        // Realizar a busca de Séries com base nos termos da entrada e total de séries indexados
        List<ElementoLista> elementos = buscador.buscarEntidades(entrada, this.buscador.getNumeroEntidades());

        // Testar se a busca teve sucesso
        if (elementos == null || elementos.isEmpty()) 
            throw new Exception("Nome não encontrado na Lista Invertida!");

        // Criar lista de Séries a ser retornada
        List<Serie> series = new ArrayList<Serie>();

        // Buscar as Séries encontradas na lista invertida
        for (ElementoLista elemento : elementos) {
            Serie s = read(elemento.getId());
            series.add(s);
        }

        // Retornar
        return series;
    }
}
