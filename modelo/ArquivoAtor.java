package modelo;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ElementoLista;
import entidades.Ator;

import java.util.ArrayList;
import java.util.List;

public class ArquivoAtor extends Arquivo<Ator> {
    ArvoreBMais<ParNomeID> indiceNome;
    ArvoreBMais<ParIDID> indiceAtuacaoSerie;
    ArvoreBMais<ParIDID> indiceAtuacaoAtor;
    private Buscador buscador;

    /*
     * Construtor da classe ArquivoAtor
     */
    public ArquivoAtor() throws Exception {
        super("ator", Ator.class.getConstructor());

        indiceNome = new ArvoreBMais<>(
            ParNomeID.class.getConstructor(),
            5,
            "./dados/ator/indiceNome.db"
        );

        indiceAtuacaoSerie = new ArvoreBMais<>(
            ParIDID.class.getConstructor(),
            5,
            "./dados/indice/indiceAtuacaoSerie.db"
        );

        indiceAtuacaoAtor = new ArvoreBMais<>(
            ParIDID.class.getConstructor(),
            5,
            "./dados/indice/indiceAtuacaoAtor.db"
        );

        // Chamar o construtor do Buscador para entidades do tipo Ator
        this.buscador = new Buscador("ator");
    }

    /*
     * create - Criação do Ator com adição aos índices
     */
    @Override
    public int create(Ator a) throws Exception {
        int id = super.create(a);

        indiceNome.create(new ParNomeID(a.getNome(), id));

        buscador.incluirEntidade(a.getID(), a.getNome());

        return id;
    }

    /*
     * delete - Exclusão do Ator caso não tenha atuações
     */
    @Override
    public boolean delete(int id) throws Exception {
        Ator a = super.read(id);

        List<ParIDID> atuacoes = indiceAtuacaoAtor.read(new ParIDID(id, -1));

        if (!atuacoes.isEmpty()) {
            throw new Exception("Não foi possível excluir o Ator, pois há atuações vinculadas a ele!");
        }

        if (super.delete(id)) {
            return indiceNome.delete(new ParNomeID(a.getNome(), id)) && buscador.excluirEntidade(a.getID(), a.getNome());
        }

        return false;
    }

    /*
     * update - Atualização do Ator e dos índices relacionados
     */
    @Override
    public boolean update(Ator novoAtor) throws Exception {
        Ator antigo = super.read(novoAtor.getID());

        if (super.update(novoAtor)) {
            if (!antigo.getNome().equals(novoAtor.getNome())) {
                indiceNome.delete(new ParNomeID(antigo.getNome(), antigo.getID()));
                indiceNome.create(new ParNomeID(novoAtor.getNome(), novoAtor.getID()));
                buscador.alterarEntidade(antigo.getID(), antigo.getNome(), novoAtor.getNome());
            }
            return true;
        }

        return false;
    }

    /*
     * readNome - Retorna todos os Atores cujo nome começa com determinada string
     */
    public Ator[] readNome(String nome) throws Exception {
        ParNomeID pnid = new ParNomeID(nome, -1);
        ArrayList<ParNomeID> pnis = indiceNome.read(pnid);

        if (pnis.isEmpty())
            throw new Exception("Não foi encontrado nenhum Ator com o nome buscado!");

        Ator[] atores = new Ator[pnis.size()];

        int i = 0;
        for (ParNomeID pni : pnis)
            atores[i++] = this.read(pni.getID());

        return atores;
    }

    /*
	 * readListaInvertida - Função para buscar Atores utilizando a lista invertida
	 * @param entrada - Texto da consulta inserido pelo usuário (ex: nome do ator ou termos associados)
     * @return series - Lista de Atores encontrados na busca da lista invertida
     */
    public List<Ator> readListaInvertida(String entrada) throws Exception {
        // Realizar a busca de Atores com base nos termos da entrada e total de séries indexados
        List<ElementoLista> elementos = buscador.buscarEntidades(entrada, this.buscador.getNumeroEntidades());

        // Testar se a busca teve sucesso
        if (elementos == null || elementos.isEmpty()) 
            throw new Exception("Nome não encontrado na Lista Invertida!");

        // Criar lista de Atores a ser retornada
        List<Ator> atores = new ArrayList<Ator>();

        // Buscar as Séries encontradas na lista invertida
        for (ElementoLista elemento : elementos) {
            Ator a = read(elemento.getId());
            atores.add(a);
        }

        // Retornar
        return atores;
    }
}
