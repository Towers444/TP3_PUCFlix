package modelo;

import entidades.Ator;

import java.util.ArrayList;
import java.util.List;

import aeds3.*;

public class ArquivoAtor extends Arquivo<Ator> {
    ArvoreBMais<ParNomeID> indiceNome;
    ArvoreBMais<ParIDID> indiceAtuacaoSerie;
    ArvoreBMais<ParIDID> indiceAtuacaoAtor;

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
    }

    /*
     * create - Criação do Ator com adição aos índices
     */
    @Override
    public int create(Ator a) throws Exception {
        int id = super.create(a);

        indiceNome.create(new ParNomeID(a.getNome(), id));

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
            return indiceNome.delete(new ParNomeID(a.getNome(), id));
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

}
