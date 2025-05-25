package controle;

import modelo.ArquivoAtuacao;
import entidades.Atuacao;


import java.util.List;


public class ControleAtuacao {
    private ArquivoAtuacao arqAtuacao;

    /*
     * Construtor da classe ControleAtuacao
     */
    public ControleAtuacao() throws Exception{
        this.arqAtuacao = new ArquivoAtuacao();
    }

    /*
     * incluirAtuacao - Função para adicionar uma Atuação ao Banco de Dados
     * @param a - Atuação a ser inserido
     * @param id - ID do Atuação inserido
     */
    public int incluirAtuacao(Atuacao a) throws Exception {
        // Testar se o objeto Atuação é válido
        if (a == null)
            throw new Exception ("Atuação nula!");

        // Criar o Atuação a partir do ArquivoAtuação
        int id = arqAtuacao.create(a);

        // Retornar
        return id;
    }

    /*
     * excluirAtuacao - Função para excluir uma Atuação a partir do seu ID
     * @param id - ID da Atuação a ser excluído
     * @return boolean - True se bem sucedido, False caso contrário
     */
    public boolean excluirAtuacao(int id) throws Exception {
        // Ler o Atuação a partir do ArquivoAtuação usando seu ID
        Atuacao a = arqAtuacao.read(id);

        // Verificar se tem Atores vinculados com a atuação
        if (verificarAtuacaoAtor(a.getIDAtor())) 
            throw new Exception ("Há atores vinculados com essa atuação!"); 

        // Verificar se tem Series vinculados com a atuação
        if (verificarAtuacaoSerie(a.getIDSerie()))
            throw new Exception ("Há séries vinculados com essa atuação!"); 

        // Excluir o Atuação a partir do ArquivoAtuação a retornar o seu status
        return arqAtuacao.delete(id);
    }

    /*
     * excluirAtuacao - Função para excluir uma Atuação a partir do seu objeto
     * @param a - Atuação a ser excluído
     * @return boolean - True se bem sucedido, False caso contrário
     */
    public boolean excluirAtuacao(Atuacao a) throws Exception {
        // Testar se o objeto Atuação é válido
        if (a == null) 
            throw new Exception ("Atuação nula!");

        // Verificar se tem Atores vinculados com a atuação
        if (verificarAtuacaoAtor(a.getIDAtor())) 
            throw new Exception ("Há atores vinculados com essa atuação!"); 

        // Verificar se tem Series vinculados com a atuação
        if (verificarAtuacaoSerie(a.getIDSerie()))
            throw new Exception ("Há séries vinculados com essa atuação!"); 

        // Excluir o Atuação a partir do ArquivoAtuacao a retornar o seu status
        return arqAtuacao.delete(a.getID());
    }

    /*
     * excluirAtuacaoSerie - Função para excluir todas as Atuações de uma Série
     * @param IDSerie - Serie que vai ter suas atuacoes deletadas
     * @return boolean - True se bem sucedido, False caso contrário
     */
    public static boolean excluirAtuacaoSerie(int IDSerie) throws Exception {
        boolean resposta = false;
        try {

            ArquivoAtuacao arqAtuacao = new ArquivoAtuacao();
            List<Atuacao> atuacoes = arqAtuacao.readSerie(IDSerie);

            for (Atuacao a : atuacoes){
                resposta = resposta && arqAtuacao.delete(a.getID());
            }

        } catch (Exception e) {
            resposta = false;
        }
        
        return resposta;
    }

    /*
     * alterarAtuacao - Função para alterar uma Atuação
     * @param novaAtuacao - Objeto já alterado a ser inserido no Banco de Dados
     * @return boolean - True se bem sucedido, False caso contrário
     */
    public boolean alterarAtuacao(Atuacao novaAtuacao) throws Exception {
        // Testar se o objeto Atuação é válido
        if (novaAtuacao == null) 
            throw new Exception ("Atuação nula!");

        // Atualizar o Atuação a partir do ArquivoAtuacao a retornar o seu status
        return arqAtuacao.update(novaAtuacao);
    }

    /*
     * buscarAtuacao - Função para buscar uma Atuação a partir do seu ID
     * @param id - ID do Atuação a ser buscado 
     * @return a - Objeto do Atuação buscado 
     */
    public Atuacao buscarAtuacao(int id) throws Exception {
        // Ler o Atuação a partir do ArquivoAtuacao usando seu id
        Atuacao a = arqAtuacao.read(id);

        // Testar a leitura do Atuação
        if (a == null)
            throw new Exception("Atuação não encontrada!");
        
        // Retornar Atuação
        return a;
    }

    /*
     * buscarAtuacao - Função para buscar uma ou mais Atuações a partir do nome do Personagem
     * @param entrada - String a ser buscada
     * @return atuacoes - Lista dos Atuações que correspondem a nome de personagem
     */
    public List<Atuacao> buscarAtuacao(String entrada) throws Exception {
        // Definir lista de Atuaçãos
        List<Atuacao> atuacoes = arqAtuacao.readPersonagem(entrada);

        // Retornar lista de Atuaçãos válidos
        return atuacoes;
    }

    /*
     * buscarAtuacao - Função para buscar uma ou mais Atuações dado um ID de Ator
     * @param IDAtor - ID de Ator que será pesquisado 
     * @return atuacoes - Lista dos Atuações que pertencem ao Ator
     */
    public List<Atuacao> buscarAtuacaoAtor(int idAtor) throws Exception {
        // Definir lista de Atuaçãos
        List<Atuacao> atuacoes = arqAtuacao.readAtor(idAtor);

        // Retornar lista de Atuaçãos válidos
        return atuacoes;
    }

    /*
     * buscarAtuacao - Função para buscar uma ou mais Atuações dado um ID de Serie
     * @param IDSerie- ID de Serie que será pesquisado 
     * @return atuacoes - Lista dos Atuações que pertencem à série
     */
    public List<Atuacao> buscarAtuacaoSerie(int idSerie) throws Exception {
        // Definir lista de Atuaçãos
        List<Atuacao> atuacoes = arqAtuacao.readSerie(idSerie);

        // Retornar lista de Atuaçãos válidos
        return atuacoes;
    }

    /*
     * verificarAtuacaoAtor - Função estática que, com um ID de Ator, retorna verdadeiro ou falso se tiver um ou mais Atores atrelados a essa Atuação.
     * @param idAtor - ID do Ator a ser testada
     * @return resposta - True se existir algum Ator relacionado à atuação e vice-versa
     */
    public static boolean verificarAtuacaoAtor(int idAtor) {
        // Definir variável de resposta
        boolean resposta;

        // Iniciar bloco try-catch
        try {
            // Definir instância do arquivoAtuacao
            ArquivoAtuacao arquivoAtuacao = new ArquivoAtuacao();
            List<Atuacao> atuacoes = arquivoAtuacao.readAtor(idAtor);

            // Testar se há alguma atuação no Ator encontrado
            if (atuacoes.size() > 0)
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
     * verificarAtuacaoSerie - Função estática que, com um ID de Série, retorna verdadeiro ou falso se tiver uma ou mais Séries atreladas a essa Atuação.
     * @param idSerie - ID da Série a ser testada
     * @return resposta - True se existir algum Atuação da Série atual, False caso contrário
     */
    public static boolean verificarAtuacaoSerie(int idSerie) {
        // Definir variável de resposta
        boolean resposta;

        // Iniciar bloco try-catch
        try {
            // Definir instância do arquivoAtuacao
            ArquivoAtuacao arquivoAtuacao = new ArquivoAtuacao();
            List<Atuacao> atuacoes = arquivoAtuacao.readSerie(idSerie);
    
            // Testar se há alguma atuação no Ator encontrado
            if (atuacoes.size() > 0)
                resposta = true;
            else
                resposta = false;
        } catch (Exception a) {
            resposta = false;
        }

        // Retornar
        return resposta;
    }

    /*
     * povoar - Função para popular o Banco de Dados com Atuaçãos amostrais
     */
    public void povoar(){
        // Inicar bloco try-catch
        try {
            // Breaking Bad
            incluirAtuacao(new Atuacao(1,2, "Walter White"));
            incluirAtuacao(new Atuacao(2,2, "Jeese Pinkman"));
            incluirAtuacao(new Atuacao(3,2, "Skyler White"));
            incluirAtuacao(new Atuacao(4,2, "Hank Schrader"));
            incluirAtuacao(new Atuacao(5,2, "Marie Schrader"));

            // The Penguin 
            incluirAtuacao(new Atuacao(6,6, "Oswald 'Oz' Cobb"));
            incluirAtuacao(new Atuacao(7,6, "Sofia Falcone"));
            incluirAtuacao(new Atuacao(8,6, "Johnny Vitti"));
            incluirAtuacao(new Atuacao(9,6, "Victor Aguilar"));
            incluirAtuacao(new Atuacao(10,6, "Nadia Maroni"));

            // Black Mirror
            incluirAtuacao(new Atuacao(7,1, "Nanette Cole"));

            
            System.out.println("\nAtuaçãos povoados!");

        } catch (Exception a){
            System.err.println("\n[ERRO]: " + a.getMessage());
        }
    }

}
