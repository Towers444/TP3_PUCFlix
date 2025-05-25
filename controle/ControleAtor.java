package controle;

import modelo.ArquivoAtor;
import modelo.ArquivoAtuacao;
import entidades.Ator;
import entidades.Atuacao;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ControleAtor {
    private ArquivoAtor arqAtor;

    /*
     * Construtor da classe ControleAtor sem a vinculação de Ator específico
     */
    public ControleAtor() throws Exception {
        this.arqAtor = new ArquivoAtor();
    }

    /*
     * incluirAtor - Função para adicionar um Ator ao Banco de Dados
     * @param a - Ator a ser inserido
     * @return id - ID do Ator inserido
     */
    public int incluirAtor(Ator a) throws Exception {
        if (a == null)
            throw new Exception("Ator nulo!");
        return arqAtor.create(a);
    }

    /*
     * excluirAtor - Função para excluir um Ator pelo ID
     * @param id - ID do Ator a ser excluído
     * @return boolean - true se bem sucedido, false caso contrário
     */
    public boolean excluirAtor(int id) throws Exception {
        if (ControleAtuacao.verificarAtuacaoAtor(id)) 
            throw new Exception ("Há atuações vinculados com esse Ator!");
        return arqAtor.delete(id);
    }

    /*
     * excluirAtor - Função para excluir um Ator a partir do seu objeto
     * @param a - Ator a ser excluído
     * @return boolean - true se bem sucedido, false caso contrário
     */
    public boolean excluirAtor(Ator a) throws Exception {
        if (a == null)
            throw new Exception("Ator nulo!");
        if (ControleAtuacao.verificarAtuacaoAtor(a.getID())) 
            throw new Exception ("Há atuações vinculados com esse Ator!");
        
        return arqAtor.delete(a.getID());
    }

    /*
     * alterarAtor - Função para atualizar os dados de um Ator
     * @param novo - Objeto Ator alterado
     * @return boolean - true se bem sucedido
     */
    public boolean alterarAtor(Ator novoAtor) throws Exception {
        if (novoAtor == null)
            throw new Exception("Ator nulo!");
        return arqAtor.update(novoAtor);
    }

    /*
     * buscarAtor - Retorna ator por ID, se for igual ao da instância
     * @param id - ID a ser buscado
     * @return Ator encontrado
     */
    public Ator buscarAtor(int id) throws Exception {
        Ator a = arqAtor.read(id);
        if (a == null)
        throw new Exception("Ator não encontrado!");
        return a;
    }

    /*
     * buscarAtor - Retorna lista de Atores cujo nome começa com a string fornecida
     * @param entrada - prefixo do nome a ser buscado
     * @return Lista de atores encontrados
     */
    public List<Ator> buscarAtor(String entrada) throws Exception {
        Ator[] resultado = arqAtor.readNome(entrada);
        return new ArrayList<>(Arrays.asList(resultado));
    }

    /*
    * buscarAtor - Retorna todos os Atores cadastrados
    * @return Lista de todos os Atores
    */
    public List<Ator> buscarAtor() throws Exception {
        // Faz uma leitura com prefixo vazio que retorna todos do índice
        Ator[] resultado = arqAtor.readNome("");
        return new ArrayList<>(Arrays.asList(resultado));
    }

    /*
     * buscarAtuacaoAtor - Retorna as atuações em que o Ator atuou
     * @param idAtor - ID do Ator
     * @return List<Atuacao> de atuacoes
     */
    public List<Atuacao> buscarAtuacaoAtor(int idAtor) throws Exception {
        ArquivoAtuacao arqAtuacao = new ArquivoAtuacao();
        return arqAtuacao.readAtor(idAtor);
    }

    /*
     * validarAtor - Verifica se o ID do Ator é válido no banco
     * @param idAtor - ID do Ator
     * @return boolean - true se existir, false caso contrário
     */
    public static boolean validarAtor(int idAtor) {
        boolean resposta;
        try {
            ArquivoAtor arqAtor = new ArquivoAtor();
            Ator a = arqAtor.read(idAtor);
            resposta = (a != null);
        } catch (Exception e) {
            resposta = false;
        }
        return resposta;
    }

    /*
     * povoar - Insere atores fictícios para testes (Dados gerados pelo Deepseek)
     */
    public void povoar() {
        try {
            // Breaking Bad cast
            incluirAtor(new Ator("Bryan Cranston", 'M', java.time.LocalDate.of(1956, 3, 7), "EUA"));
            incluirAtor(new Ator("Aaron Paul", 'M', java.time.LocalDate.of(1979, 8, 27), "EUA"));
            incluirAtor(new Ator("Anna Gunn", 'F', java.time.LocalDate.of(1968, 8, 11), "EUA"));
            incluirAtor(new Ator("Dean Norris", 'M', java.time.LocalDate.of(1963, 4, 8), "EUA"));
            incluirAtor(new Ator("Betsy Brandt", 'F', java.time.LocalDate.of(1973, 3, 14), "EUA"));

            // The Penguin
            incluirAtor(new Ator("Colin Farrell", 'M', java.time.LocalDate.of(1976, 5, 31), "Ireland"));
            incluirAtor(new Ator("Cristin Milioti", 'F', java.time.LocalDate.of(1985, 8, 16), "EUA"));
            incluirAtor(new Ator("Michael Kelly", 'M', java.time.LocalDate.of(1969, 5, 22), "EUA"));
            incluirAtor(new Ator("Rhenzy Feliz", 'M', java.time.LocalDate.of(1997, 10, 26), "EUA"));
            incluirAtor(new Ator("Shohreh Aghdashloo", 'F', java.time.LocalDate.of(1952, 5, 11), "Iran"));


            // Game of Thrones cast
            incluirAtor(new Ator("Emilia Clarke", 'F', java.time.LocalDate.of(1986, 10, 23), "UK"));
            incluirAtor(new Ator("Kit Harington", 'M', java.time.LocalDate.of(1986, 12, 26), "UK"));
            incluirAtor(new Ator("Peter Dinklage", 'M', java.time.LocalDate.of(1969, 6, 11), "EUA"));
            incluirAtor(new Ator("Lena Headey", 'F', java.time.LocalDate.of(1973, 10, 3), "UK"));
            incluirAtor(new Ator("Sophie Turner", 'F', java.time.LocalDate.of(1996, 2, 21), "UK"));

            // Friends cast
            incluirAtor(new Ator("Jennifer Aniston", 'F', java.time.LocalDate.of(1969, 2, 11), "EUA"));
            incluirAtor(new Ator("Courteney Cox", 'F', java.time.LocalDate.of(1964, 6, 15), "EUA"));
            incluirAtor(new Ator("Lisa Kudrow", 'F', java.time.LocalDate.of(1963, 7, 30), "EUA"));
            incluirAtor(new Ator("Matt LeBlanc", 'M', java.time.LocalDate.of(1967, 7, 25), "EUA"));
            incluirAtor(new Ator("Matthew Perry", 'M', java.time.LocalDate.of(1969, 8, 19), "Canada"));
            incluirAtor(new Ator("David Schwimmer", 'M', java.time.LocalDate.of(1966, 11, 2), "EUA"));

            // International actors
            incluirAtor(new Ator("Gong Li", 'F', java.time.LocalDate.of(1965, 12, 31), "China"));
            incluirAtor(new Ator("Shah Rukh Khan", 'M', java.time.LocalDate.of(1965, 11, 2), "India"));
            incluirAtor(new Ator("Penélope Cruz", 'F', java.time.LocalDate.of(1974, 4, 28), "Spain"));
            incluirAtor(new Ator("Wagner Moura", 'M', java.time.LocalDate.of(1976, 6, 27), "Brazil"));
            incluirAtor(new Ator("Lupita Nyong'o", 'F', java.time.LocalDate.of(1983, 3, 1), "Mexico"));

            // Hollywood legends
            incluirAtor(new Ator("Meryl Streep", 'F', java.time.LocalDate.of(1949, 6, 22), "EUA"));
            incluirAtor(new Ator("Denzel Washington", 'M', java.time.LocalDate.of(1954, 12, 28), "EUA"));
            incluirAtor(new Ator("Cate Blanchett", 'F', java.time.LocalDate.of(1969, 5, 14), "Australia"));
            incluirAtor(new Ator("Tom Hanks", 'M', java.time.LocalDate.of(1956, 7, 9), "EUA"));
            incluirAtor(new Ator("Viola Davis", 'F', java.time.LocalDate.of(1965, 8, 11), "EUA"));
            

            
            System.out.println("\nAtores povoados com sucesso!");
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }
}
