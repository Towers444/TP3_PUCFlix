package visao;

import entidades.Ator;
import entidades.Atuacao;
import controle.ControleAtor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VisaoAtor {

    private static Scanner console = new Scanner(System.in);
    ControleAtor controleAtor;

    /*
     * Construtor da classe VisaoAtor
     */
    public VisaoAtor() throws Exception {
        this.controleAtor = new ControleAtor();
    }

    /*
     * menu - Função principal de menu da VisaoAtor
     * @param versao - Versão do sistema
     */
    public void menu(String versao) throws Exception {
        int opcao;

        do {
            // Cabeçalho do menu
            System.out.println("\nPUCFlix v" + versao);
            System.out.println("--------------------------");
            System.out.println("> Início > Atores");
            System.out.println("1 - Incluir Ator");
            System.out.println("2 - Excluir Ator");
            System.out.println("3 - Alterar Ator");
            System.out.println("4 - Buscar Um Ator");
            System.out.println("5 - Buscar Atuações de Um Ator");
            System.out.println("0 - Sair");
            System.out.print("\nOpção: ");

            // Leitura da opção
            try {
                opcao = Integer.parseInt(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            // Execução da ação conforme a opção
            switch (opcao) {
                case 1: incluirAtor(); break;
                case 2: excluirAtor(); break;
                case 3: alterarAtor(); break;
                case 4: buscarUmAtor(); break;
                case 5: buscarAtuacoes(); break;
                case 0: System.out.println("Saindo..."); break;
                default: System.err.println("[ERRO]: Opção inválida!"); break;
            }

        } while (opcao != 0);
    }

    /*
     * incluirAtor - Função para incluir um novo ator no sistema
     */
    public void incluirAtor() {
        try {
            Ator a = lerAtor();
            int id = controleAtor.incluirAtor(a);
            System.out.println("\nAtor incluído com sucesso! ID: " + id);
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }

    /*
     * alterarAtor - Função para alterar um ator já existente
     */
    public void alterarAtor() {
        try {
            Ator antigo = buscarUmAtor();
            Ator novo = lerAtor(antigo);

            // Confirmação da alteração
            System.out.print("\nConfirma as alterações? (S/N) ");
            char resp = console.nextLine().charAt(0);
            if (resp == 'S' || resp == 's') {
                if (controleAtor.alterarAtor(novo)) {
                    System.out.println("\nAtor alterado com sucesso.");
                } else {
                    System.err.println("\n[ERRO]: Não foi possível alterar o ator.");
                }
            }
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }

    /*
     * excluirAtor - Função para excluir um ator do sistema
     */
    public void excluirAtor() {
        try {
            Ator a = buscarUmAtor();

            // Confirmação da exclusão
            System.out.print("Confirma a exclusão? (S/N) ");
            char resp = console.nextLine().charAt(0);
            if (resp == 'S' || resp == 's') {
                if (controleAtor.excluirAtor(a)) {
                    System.out.println("\nAtor excluído com sucesso.");
                } else {
                    System.err.println("\n[ERRO]: Não foi possível excluir o ator.");
                }
            }
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }

    /*
     * lerAtor - Lê os dados de um novo ator a ser incluído
     * @return Ator criado
     */
    public Ator lerAtor() throws Exception {
        // Definir os atributos de um Episódio
        String nome, nacionalidade;
        char genero = ' ';
        LocalDate dataLancamento = null;

        // Definir variáveis auxiliares
        boolean dadosCorretos = false;
        String regex = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        String aux;

        // Ler o nome do Ator
        do {
            // Ler o nome do Episódio do console
            System.out.print("\nQual o nome do ator? (mínimo 3 caracteres): ");
            nome = console.nextLine();

            // Testar se a entrada é válida
            if (nome.length() >= 3)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O nome deve ter no mínimo 3 caracteres!");
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Qual o gênero (M/F/I)? ");
            aux = console.nextLine();
            if (aux.length() > 0){
                genero = aux.toUpperCase().charAt(0);
                dadosCorretos = ("MFI".indexOf(genero) == -1) ? false : true;
                if (!dadosCorretos) System.err.println("[ERRO]: O gênero deve ser M/F/I");
            } else {
                dadosCorretos = false;
                System.err.println("[ERRO]: Insira algum valor");
            }
        } while (!dadosCorretos);


        // Reiniciar variável de controle
        dadosCorretos = false;
        // Ler a data de nascimento
        do {
            System.out.print("Qual a data de nascimento (dd/MM/yyyy)? ");
            String data = console.nextLine();
            Matcher matcher = pattern.matcher(data);

            // Testar se a data está no formato correto
            if (matcher.matches()) {
                dadosCorretos = true;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataLancamento = LocalDate.parse(data, formatter);
            } else {
                dadosCorretos = false;
                System.err.println("[ERRO]: O formato deve ser (dd/MM/yyyy)!");
            }
        } while (!dadosCorretos);
        
        
        
        dadosCorretos = false;
        // Ler a nacionalidade
        do {
            System.out.print("Qual a nacionalidade? (mínimo 2 caracteres): ");
            nacionalidade = console.nextLine();

            // Testar se a entrada é válida
            if (nacionalidade.length() >= 2)
                dadosCorretos = true;
            else{
                System.err.println("[ERRO]: A nacionalidade deve ter no mínimo 2 caracteres!");
                dadosCorretos = false;
            }
        } while (!dadosCorretos);

        Ator a = new Ator(nome, genero, dataLancamento, nacionalidade);
        return a;
    }

    /*
     * lerAtor - Lê os dados de um ator existente e permite edição
     * @param antigo - Ator a ser editado
     * @return Ator novo com dados atualizados
     */
    public Ator lerAtor(Ator antigo) throws Exception {
        String nome, nacionalidade;
        char genero;
        LocalDate dataNascimento = null;

        // Definir variáveis auxiliares
        boolean dadosCorretos = false;
        String regex = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        String aux;

        System.err.println("Caso deseja manter a informção antiga pressione ENTER em cada campo!!!");


        do {
            System.out.print("Qual o nome do Ator (Original: "+antigo.getNome()+")? ");
            nome = console.nextLine();

            // Testar se é para manter os dados antigos
            if (nome.length() == 0) {
                nome = antigo.getNome();
                dadosCorretos = true;
            }

            // Testar se a entrada é válida
            if (nome.length() >= 2)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O nome deve ter no mínimo 2 caracteres!");
        } while (!dadosCorretos);

        do {
            System.out.print("Qual o gênero do Ator (Original: "+antigo.getGenero()+")? ");
            aux = console.nextLine();
            
            // Testar se é para manter os dados antigos
            if (aux.length() == 0) {
                genero = antigo.getGenero();
                dadosCorretos = true;
            } else {
                genero = aux.toUpperCase().charAt(0);

                // Testar se a entrada é válida
                if (!("MFI".indexOf(genero) == -1))
                    dadosCorretos = true;
                else{
                    System.err.println("[ERRO]: O gênero deve ser M/F/I!");
                    dadosCorretos = false;
                }
            }

        } while (!dadosCorretos);

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Ler a data de lançamento do Episódio
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        do {
            System.out.print("Qual a data de nascimento (Original: "+antigo.getDataNascimento().format(formatter)+")? ");
            String data = console.nextLine();
            Matcher matcher = pattern.matcher(data);

            // Testar se é para manter os dados antigos
            if (data.length() == 0) {
                dataNascimento = antigo.getDataNascimento();
                dadosCorretos = true;
            } else {
                // Testar se a data está no formado correto
                if (matcher.matches()) {
                    dadosCorretos = true;
                    dataNascimento = LocalDate.parse(data, formatter);
                } else
                    System.err.println("[ERRO]: O formato deve ser (dd/MM/yyyy)!");
            }
        } while (!dadosCorretos);

        do {
            System.out.print("Qual a nacionalidade do Ator (Original: "+antigo.getNacionalidade()+")? ");
            nacionalidade = console.nextLine();

            // Testar se é para manter os dados antigos
            if (nacionalidade.length() == 0) {
                nacionalidade = antigo.getNacionalidade();
                dadosCorretos = true;
            }

            // Testar se a entrada é válida
            if (nacionalidade.length() >= 2)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: A nacionalidade deve ter no mínimo 2 caracteres!");
        } while (!dadosCorretos);


        return new Ator(antigo.getID(), nome, genero, dataNascimento, nacionalidade);
    }

    /*
     * buscarUmAtor - Busca um único ator pelo nome
     * @return Ator selecionado
     */
    public Ator buscarUmAtor() {
        Ator a = null;
        Boolean dadosCorretos = false;
        int idx;

        List<Ator> atores = buscarAtorNome();
        if (atores == null || atores.isEmpty())
            System.err.println("[ERRO]: Nenhum ator encontrado!");
        else if (atores.size() == 1)
            a = atores.get(0);
        else {
            do {
                // Exibir todas as Séries encontradas pelo nome
                System.out.println("Escolha um Ator: ");    
                int n = 0;    
                for (Ator at : atores) 
                    System.out.println((n++) + " - " + at.getNome());    
                // Tentar ler a opção do console
                try {
                    idx = Integer.valueOf(console.nextLine());
                } catch(NumberFormatException e) {
                    idx = -1;
                }    
                // Testar a opção
                if (0 <= idx && idx <= atores.size()) {
                    // Identificar a Série selecionada pela sua posição
                    a = atores.get(idx);    
                    // Atualizar variável de controle
                    dadosCorretos = true;
                } else {
                    System.err.println("[ERRO]: Ator não está presente na lista!\n");
                }
            } while(!dadosCorretos); 
        }
        if (a != null) 
            mostrarAtor(a);
        return a;
    }

    /*
     * buscarAtorNome - Solicita entrada de nome e busca atores correspondentes
     * @return Lista de atores encontrados
     */
    public List<Ator> buscarAtorNome() {
        System.out.print("\nNome: ");
        String nome = console.nextLine();
        try {
            return controleAtor.buscarAtor(nome);
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
            return null;
        }
    }

    /*
     * buscarAtuacoes - Busca as atuações de um ator
     */
    public void buscarAtuacoes() {
        try {
            Ator a = buscarUmAtor();
            List<Atuacao> atuacoes = controleAtor.buscarAtuacaoAtor(a.getID());

            System.out.println("\nAtuações de " + a.getNome() + ":");
            for (Atuacao at : atuacoes) {
                (new VisaoAtuacao()).mostraAtuacao(at);
            }
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }

    /*
     * mostrarAtor - Exibe informações completas do ator
     * @param a - Ator a ser exibido
     */
    public void mostrarAtor(Ator a) {
        if (a != null) {
            System.out.println(a);
        }
    }
}
