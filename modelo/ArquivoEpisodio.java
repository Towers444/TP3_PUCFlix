package modelo;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ElementoLista;
import entidades.Episodio;

import java.util.List;
import java.util.ArrayList;

public class ArquivoEpisodio extends Arquivo<Episodio> {
    private ArvoreBMais<ParIDID> indiceSerieEpisodio;
    private ArvoreBMais<ParNomeID> indiceNome;
    private Buscador buscador;

    /*
     * Construtor da classe ArquivoEpisodio
     */
    public ArquivoEpisodio() throws Exception {
        // Chamar o construtor da classe herdada
        super("episodio", Episodio.class.getConstructor());

        // Chamar o construtor do índice de Série e Episódio
        indiceSerieEpisodio = new ArvoreBMais<> (
            ParIDID.class.getConstructor(), 
            5, 
            "./dados/indice/indiceSerieEpisodio.db"
        );

        // Chamar o construtor do índice de Nome do Episódio e seu ID
        indiceNome = new ArvoreBMais<> (
            ParNomeID.class.getConstructor(), 
            5, 
            "./dados/"+nomeEntidade+"/indiceNome.db"
        );

        // Chamar o construtor do Buscador para entidades do tipo Episódio 
        this.buscador = new Buscador("episodio");
    }

    /*
     * create - Função para criar um Episódio no Banco de Dados
     * @param e - Episódio a ser inserido (sem o ID)
     * @return id - ID do Episódio inserido
     */
    @Override
    public int create(Episodio e) throws Exception {
        // Criar o Episódio 
        int id = super.create(e);

        // Criar os índices Serie-Episodio e Nome-Episodio
        indiceSerieEpisodio.create(new ParIDID(e.getIDSerie(), id));
        indiceNome.create(new ParNomeID(e.getNome(), id));

        // Adicionar o nome da Série à lista invertida
        buscador.incluirEntidade(e.getID(), e.getNome());

        // Retornar o ID
        return id;
    }

    /*
     * delete - Função para excluir um Episódio a partir de um ID
     * @param id - ID do Episódio a ser excluído
     * @return resposta - True se sucedido, False se contrário
     */
    @Override
    public boolean delete(int id) throws Exception {
        // Definir variável de resposta
        boolean resposta;

        // Ler o Episódio a partir da superclasse
        Episodio e = super.read(id);   

        // Excluir o Episódio a partir da superclasse e testar o seu status para excluir os índices
        if(super.delete(id))
            resposta = indiceSerieEpisodio.delete(new ParIDID(e.getIDSerie(), id)) && indiceNome.delete(new ParNomeID(e.getNome(), id)) && buscador.excluirEntidade(e.getID(), e.getNome());
        else
            resposta = false;

        // Retornar resposta
        return resposta;
    }

    /*
     * update - Função para atualizar um Episódio
     * @param novoEpisodio - Objeto já alterado do Episódio
     * @return boolean - True se sucedido, False se contrário
     */
    @Override
    public boolean update(Episodio novoEpisodio) throws Exception {
        // Definir variável de resposta
        boolean resposta;

        // Ler o Episódio antigo a partir da superclasse
        Episodio e = super.read(novoEpisodio.getID()); 

        // Atualizar o Episódio a partir da superclasse e testar o seu status
        if(super.update(novoEpisodio)) {
            // Testar se houve alteração no ID
            if( e.getIDSerie() != novoEpisodio.getIDSerie() ) {
                // Remover o Par Série-Episódio anterior
                indiceSerieEpisodio.delete( new ParIDID(e.getIDSerie(), e.getID()) );

                // Recriar o índice com o ID alterado
                indiceSerieEpisodio.create( new ParIDID(novoEpisodio.getIDSerie(), e.getID()) );
            }

            // Testar se houve alteração no Nome
            if( !e.getNome().equals(novoEpisodio.getNome()) ) {
                // Remover o Par Nome-Episódio anterior
                indiceNome.delete(new ParNomeID(e.getNome(), e.getID()));

                // Recriar o índice com o Nome alterado
                indiceNome.create(new ParNomeID(novoEpisodio.getNome(), novoEpisodio.getID()));
                
                // Atualizar os termos da entidade na lista invertida
                buscador.alterarEntidade(e.getID(), e.getNome(), novoEpisodio.getNome());
            }
            resposta = true;
        } else {
            resposta = false;
        }

        // Retornar
        return resposta;
    }

    /*
     * readNome - Função para buscar todos os Episódios cujo nome inicia com uma String especificada
     * @param nome - String a ser buscada
     * @return episodios - Array de Episódios encontrados
     */
    public Episodio[] readNome(String nome) throws Exception {
        // Definir Lista de Pares Nome-ID que possuem a String especificada
        ParNomeID pnid = new ParNomeID(nome, -1);
        // System.out.println(pnid);
        List<ParNomeID> pnis = indiceNome.read(pnid);
        // System.out.println(pnis + ", tamanho: "+pnis.size());
        // Testar se há algum Par encontrado
        if ( !(pnis.size() > 0) )
            throw new Exception ("Não foi encontrado nenhum Episódio com o nome buscado!");
        
        // Definir array de Episódios com o tamanho do número de pares
        Episodio[] episodios = new Episodio[pnis.size()];

        // Iterar sobre a lista de Pares Nome-ID a adicionar os Episódios correspondentes ao array de Episódios
        int i = 0;
        for(ParNomeID pni: pnis) {
            episodios[i++] = this.read(pni.getID());
        }

        // Retornar
        return episodios;
    }

    /*
	 * readListaInvertida - Função para buscar Episódios utilizando a lista invertida
	 * @param entrada - Texto da consulta inserido pelo usuário (ex: nome da Série ou termos associados)
     * @return episodios - Lista de Episódios encontrados na Lista Invertida
     */
    public List<Episodio> readListaInvertida(String entrada) throws Exception {
        // Realizar a busca de Episódios com base nos termos da entrada e total de Episódios indexados
        List<ElementoLista> elementos = buscador.buscarEntidades(entrada, this.buscador.getNumeroEntidades());

        // Testar se a busca teve sucesso
        if (elementos == null || elementos.isEmpty()) 
            throw new Exception("Nome não encontrado na Lista Invertida!");

        // Criar lista de Séries a ser retornada
        List<Episodio> episodios = new ArrayList<Episodio>();

        // Buscar as Séries encontradas na lista invertida
        for (ElementoLista elemento : elementos) {
            Episodio e = read(elemento.getId());
            episodios.add(e);
        }

        // Retornar
        return episodios;
    }
}
