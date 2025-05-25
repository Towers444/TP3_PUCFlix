package modelo;

import entidades.Atuacao;
import controle.ControleAtor;
import controle.ControleSerie;
import aeds3.Arquivo;
import aeds3.ArvoreBMais;

import java.util.List;
import java.util.Arrays;

public class ArquivoAtuacao extends Arquivo<Atuacao> {
    ArvoreBMais<ParNomeID> indicePersonagem;
    ArvoreBMais<ParIDID> indiceSerieAtuacao;
    ArvoreBMais<ParIDID> indiceAtorAtuacao;

    /*
     * Construtor da classe ArquivoAtuacao
     */
    public ArquivoAtuacao() throws Exception {
        // Chamar o construtor da classe herdada
        super("Atuacao", Atuacao.class.getConstructor());

        // Chamar o construtor do índice de Nome do Ator e ID da sua atuação
        indicePersonagem = new ArvoreBMais<> (
            ParNomeID.class.getConstructor(), 
            5, 
            "./dados/"+nomeEntidade+"/indicePersonagem.db"
        );

        // Chamar o construtor do índice de Série e Episódio
        indiceSerieAtuacao = new ArvoreBMais<> (
            ParIDID.class.getConstructor(), 
            5, 
            "./dados/indice/indiceSerieAtuacao.db"
        );

        indiceAtorAtuacao = new ArvoreBMais<> (
            ParIDID.class.getConstructor(), 
            5, 
            "./dados/indice/indiceAtorAtuacao.db"
        );   
    }


    /*
     * create - Função para criar uma Atuacao no Banco de Dados
     * @param a - Atuação a ser inserido (sem o ID)
     * @return id - ID da Atuação inserida
     */
    @Override
    public int create(Atuacao a) throws Exception {
        // Criar o Episódio 
        int id = super.create(a);

        // Criar os índices Atuacao-Serie e Atuacao-Ator
        indiceSerieAtuacao.create(new ParIDID(a.getIDSerie(), id));
        indiceAtorAtuacao.create(new ParIDID(a.getIDAtor(), id));

        // Criar o índice AtorNome-Atuacao
        indicePersonagem.create(new ParNomeID(a.getPersonagem(), id));

        // Retornar o ID
        return id;
    }

    /*
     * delete - Função para excluir uma Atuação a partir de um ID
     * @param id - ID da Atuação a ser excluído
     * @return resposta - True se sucedido, False se contrário
     */
    @Override
    public boolean delete(int id) throws Exception {
        // Definir variável de resposta
        boolean resposta;

        // Ler o Episódio a partir da superclasse
        Atuacao a = super.read(id);   

        // Definir Lista de Pares Série-Episódio que possuem o ID da Série especificada
        List<ParIDID> piis = indiceSerieAtuacao.read(new ParIDID(a.getIDSerie(), -1));

        // Testar se há algum Par encontrado
        if ( piis.size() > 0 )
            throw new Exception ("Não foi possível excluir a Atuação, pois há Séries vinculados a ela!");

        // Definir Lista de Pares Série-Episódio que possuem o ID da Série especificada
        piis = indiceAtorAtuacao.read(new ParIDID(a.getIDAtor(), -1));
        
        // Testar se há algum Par encontrado
        if ( piis.size() > 0 )
            throw new Exception ("Não foi possível excluir a Atuação, pois há Atores vinculados a ela!");

        // Excluir o Episódio a partir da superclasse e testar o seu status para excluir os índices
        if(super.delete(id))
            resposta = indiceSerieAtuacao.delete(new ParIDID(a.getIDSerie(), id)) 
            && indiceAtorAtuacao.delete(new ParIDID(a.getIDSerie(), id))
            && indicePersonagem.delete(new ParNomeID(a.getPersonagem(), id));
        else
            resposta = false;

        // Retornar resposta
        return resposta;
    }

    /*
     * update - Função para atualizar uma Atuação
     * @param novoAtuacao - Objeto já alterado da Atuação
     * @return boolean - True se sucedido, False se contrário
     */
    @Override
    public boolean update(Atuacao novoAtuacao) throws Exception {
        // Definir variável de resposta
        boolean resposta;

        // Ler o Episódio antigo a partir da superclasse
        Atuacao a = super.read(novoAtuacao.getID()); 

        // Atualizar o Episódio a partir da superclasse e testar o seu status
        if(super.update(novoAtuacao)) {

            // Testar se houve alteração na Série relacionada
            if( a.getIDSerie() != novoAtuacao.getIDSerie() ) {
                // Remover o Par Atuação-Série anterior
                indiceSerieAtuacao.delete( new ParIDID(a.getIDSerie(), a.getID()) );

                // Recriar o índice com o ID alterado
                indiceSerieAtuacao.create( new ParIDID(novoAtuacao.getIDSerie(), a.getID()) );
            }

            // Testar se houve alteração no Ator relacionado
            if( a.getIDAtor() != novoAtuacao.getIDAtor() ) {
                // Remover o Par Atuação-Ator anterior
                indiceAtorAtuacao.delete( new ParIDID(a.getIDAtor(), a.getID()) );

                // Recriar o índice com o ID alterado
                indiceAtorAtuacao.create( new ParIDID(novoAtuacao.getIDAtor(), a.getID()) );
            }

            // Testar se houve alteração no Nome
            if( !a.getPersonagem().equals(novoAtuacao.getPersonagem()) ) {
                // Remover o Par Nome-Atuação anterior
                indicePersonagem.delete(new ParNomeID(a.getPersonagem(), a.getID()));

                // Recriar o índice com o Nome alterado
                indicePersonagem.create(new ParNomeID(novoAtuacao.getPersonagem(), novoAtuacao.getID()));
            }

            resposta = true;
        } else {
            resposta = false;
        }

        // Retornar
        return resposta;
    }

    /*
     * readPersonagem - Função para buscar todos as Atuações cujo Personagem inicia com uma String especificada
     * @param nome - String a ser buscada
     * @return Atuacoes - Array de Episódios encontrados
     */
    public List<Atuacao> readPersonagem(String nome) throws Exception {
        // Definir Lista de Pares Nome-ID que possuem a String especificada
        ParNomeID pnid = new ParNomeID(nome, -1);
        // System.out.println(pnid);
        List<ParNomeID> pnis = indicePersonagem.read(pnid);
        // System.out.println(pnis + ", tamanho: "+pnis.size());
        
        // Definir array de Episódios com o tamanho do número de pares
        Atuacao[] Atuacoes = new Atuacao[pnis.size()];

        // Iterar sobre a lista de Pares Nome-ID a adicionar os Episódios correspondentes ao array de Episódios
        int i = 0;
        for(ParNomeID pni: pnis) {
            Atuacoes[i++] = this.read(pni.getID());
        }

        // Retornar
        return Arrays.asList(Atuacoes);
    }

    /*
     * readSerie - Função para ler todas as Atuações de uma Série
     * @param IDSerie - ID da Série
     * @return Atuacoes - Lista de Atuações que pertencem à Série especificada
     */
    public List<Atuacao> readSerie(int IDSerie) throws Exception {
        // Testar se o ID da Série procurada é válido
        if( !ControleSerie.validarSerie(IDSerie) )
            throw new Exception("IDSerie inválido");

        // Definir Lista de Pares Série-Episódio que possuem o ID da Série especificada
        List<ParIDID> piis = indiceSerieAtuacao.read(new ParIDID(IDSerie, -1));

        // Testar se há algum Par encontrado
        if ( piis.size() <= 0)
            throw new Exception ("Não foi encontrado nenhuma Atuação pertencente à Série procurada!");
        
        // Definir array de Episódios com o tamanho do número de pares
        Atuacao[] Atuacoes = new Atuacao[piis.size()];

        // Iterar sobre a lista de Pares Série-Episódio a adicionar os Episódios correspondentes ao array de Episódios
        int i = 0;
        for(ParIDID pii: piis) 
            Atuacoes[i++] = this.read(pii.getID_Dependente());

        // Retornar
        return Arrays.asList(Atuacoes);
    }

    /*
     * readAtor - Função para ler todas as Atuações de um Ator
     * @param IDSerie - ID do Ator
     * @return Atuacoes - Lista de Atuações que pertencem ao Ator especificado
     */
    public List<Atuacao> readAtor(int IDAtor) throws Exception {
        // Testar se o ID da Série procurada é válido
        if( !ControleAtor.validarAtor(IDAtor) )
        // if( IDAtor < 0 ) Caso não tenha essa funcao em controleAtor
            throw new Exception("IDAtor inválido");

        // Definir Lista de Pares Série-Episódio que possuem o ID da Série especificada
        List<ParIDID> piis = indiceAtorAtuacao.read(new ParIDID(IDAtor, -1));

        // Testar se há algum Par encontrado
        if ( piis.size() <= 0)
            throw new Exception ("Não foi encontrado nenhuma Atuação pertencente ao Ator procurado!");
        
        // Definir array de Episódios com o tamanho do número de pares
        Atuacao[] Atuacoes = new Atuacao[piis.size()];

        // Iterar sobre a lista de Pares Série-Episódio a adicionar os Episódios correspondentes ao array de Episódios
        int i = 0;
        for(ParIDID pii: piis) 
            Atuacoes[i++] = this.read(pii.getID_Dependente());

        // Retornar
        return Arrays.asList(Atuacoes);
    }

}
