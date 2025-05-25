package visao;

import entidades.Ator;
import entidades.Atuacao;
import entidades.Serie;
import controle.ControleAtor;
import controle.ControleAtuacao;
import controle.ControleSerie;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class VisaoAtuacao {
    
    ControleAtuacao controleAtuacao;
    VisaoSerie visaoSerie;
    VisaoAtor visaoAtor;

    private static Scanner console = new Scanner(System.in);

    /*
     * Construtor da classe VisaoAtuacao
     */
    public VisaoAtuacao() throws Exception {
        controleAtuacao = new ControleAtuacao();
        visaoSerie = new VisaoSerie();
        visaoAtor = new VisaoAtor();
    }

    /*
     * menu - Função inicial para mostrar as opções da tela de Atuação
     * @param versao - Versão do código atual
     */
    public void menu(String versao) {
        // Definir variável de controle
        int opcao;
        // Iniciar bloco de seleção
        do {
            // Mostrar cabeçalho
            System.out.println("\nPUCFlix v" + versao);
            System.out.println("--------------------------");
            System.out.println("> Início > Séries > Elenco\n");
            System.out.println("1 - Incluir Atuação");
            System.out.println("2 - Excluir Atuação");
            System.out.println("3 - Alterar Atuação");
            System.out.println("4 - Buscar Atuação");
            System.out.println("5 - Buscar Atuações de Uma Série");
            System.out.println("0 - Sair");
            // Ler opção do usuário
            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch(NumberFormatException e) {
                opcao = -1;
            }
            // Testar opção do usuário
            switch (opcao) {
                case 1: incluirAtuacao(); break;
                case 2: excluirAtuacao(); break;
                case 3: alterarAtuacao(); break;
                case 4: buscarUmaAtuacao(); break;
                case 5: buscarAtuacoesSerie(); break;
                case 0: break;
                default: System.err.println("\n[ERRO]: Opção inválida!"); break;
            }
        } while (opcao != 0);
    }

    /*
     * incluirAtuacao - Função para ler informações de uma Atuação e confirmar a inclusão
     */
    public void incluirAtuacao() {
        // Exibir título da ação
        System.out.println("\n> Inclusão de Atuacao\n\n");
        // Ler dados da Atuação a ser incluída
        Atuacao a;
        try {
            a = lerAtuacao();
            // Confirmar a inclusão da Atuação
            System.out.print("\nConfirma a inclusão da Atuacao? (S/N) ");
            // Identificar escolha
            char resp = console.nextLine().charAt(0);
            // Testar escolha
            if (resp == 'S' || resp == 's') {
                // Tentar incluir a Atuação a partir do ControleAtuacao
                try {
                    controleAtuacao.incluirAtuacao(a);
                    System.out.println("\nAtuacao incluída com sucesso!");
                } catch(Exception e) {
                    System.err.println("\n[ERRO]: " + e.getMessage());
                }
            } else {
                System.out.println("\nOperação cancelada!");
            }
        } catch (Exception e1){
            System.err.println("\n[ERRO]: " + e1.getMessage());
        }
    }

    /*
     * excluirAtuacao - Função para buscar uma Atuação e excluí-la
     */
    public void excluirAtuacao() {
        // Mostrar cabeçalho
        System.out.println("\nExclusão de Atuação");
        // Iniciar bloco try-catch
        try {
            // Tentar ler o Atuacao com o ID fornecido
            Atuacao s = buscarUmaAtuacao();
            // Testar se a Atuação é válida
            if (s != null) {
                // Confirmar a exclusão da Atuação
                System.out.print("\nConfirma a exclusão do Atuacao? (S/N) ");
                // Ler a resposta do usuário
                char resp = console.nextLine().charAt(0);
                // Testar a resposta do usuário
                if (resp == 'S' || resp == 's') {
                    // Chama o método de exclusão no arquivo
                    boolean excluido = controleAtuacao.excluirAtuacao(s.getID());  
                    // Testar o status da exclusão
                    if (excluido) {
                        System.out.println("\nAtuacao excluída com sucesso.");
                    } else {
                        System.out.println("\n[ERRO]: Não foi possível excluir a Atuacao!");
                    }
                } else {
                    System.out.println("\nExclusão cancelada!");
                }
                // Limpar o buffer
                console.nextLine();
            }
        } catch (Exception e) {
            System.out.println("\n[ERRO]: " + e.getMessage());
        }
    }

    /*
     * alterarAtuacao - Função para ler novas informações de uma Atuação e alterá-la
     */
    public void alterarAtuacao() {
        System.out.println("\nAlteração de Atuacao");
        try {
            // Tenta ler o Atuacao com o ID fornecido
            Atuacao s = buscarUmaAtuacao();
            if (s != null) {
                System.out.println("\n> Insira os novos dados da Atuação (caso deseje manter os dados originais, apenas tecle Enter): \n");
                Atuacao nova = lerAtuacao(s);
                nova.setID(s.getID());

                // Confirmação da alteração
                System.out.print("\nConfirma as alterações? (S/N) ");
                char resp = console.next().charAt(0);
                if (resp == 'S' || resp == 's') {
                    // Salva as alterações no arquivo
                    boolean alterado = controleAtuacao.alterarAtuacao(nova);
                    if (alterado) {
                        System.out.println("\nAtuação alterada com sucesso!");
                    } else {
                        System.err.println("\n[ERRO]: Não foi possível alterar a Atuação!");
                    }
                } else {
                    System.out.println("Alterações canceladas!");
                }
                 console.nextLine(); // Limpar o buffer 
            }
        } catch (Exception e) {
            System.out.println("[ERRO]: " + e.getMessage());
        }
        
    }
    
    /*
     * lerAtuacao - Função para efetivamente obter as informações de uma Atuação do usuário
     * @return Atuacao - Objeto da Atuação lida pelo Usuário
     */
    public Atuacao lerAtuacao() throws Exception{
        // Definir atributos de uma Atuação
        String personagem;
        Serie s;
        Ator a;

        // Definir variável de controle
        boolean dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Vincular a série
            System.out.println("Qual a série? ");
            s = visaoSerie.buscarUmaSerie();
            // Testar se a entrada é válida
            if (s.getID() >= 0)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: Escolha uma série válida!");
        } while (!dadosCorretos);

        // Reiniciar variável de controle
        dadosCorretos = false;

        // Mostrar cabeçalho
        System.out.println("\n> Leitura de dados da Atuação\n");
        do {
            // Vincular o Ator
            System.out.print("Qual o Ator? ");
            try {
                a = visaoAtor.buscarUmAtor();
            } catch (Exception e){
                System.err.println("[ERRO]: "+e);
                a = new Ator();
            }
            // Testar se a entrada é válida
            if (a.getID() >= 0)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: Escolha um Ator válido!");
        } while (!dadosCorretos);

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler a sinopse da Atuação
            System.out.print("Qual o Personagem? ");
            personagem = console.nextLine();
            // Testar o tamanho da sinopse da Atuação
            if(personagem.length() > 2)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O personagem deve ter no mínimo 2 caracteres!");
        } while (!dadosCorretos);


        // Criar objeto Atuação resultante
        Atuacao atuacao = new Atuacao(a.getID(),s.getID(),personagem); 
        // Retornar objeto da Atuação preenchido com as informações
        return atuacao;
    }

    /*
     * lerAtuacao - Função para efetivamente obter as informações de uma NOVA Atuação do usuário
     * @param antiga - Atuação antiga
     * @return nova - Atuação nova
     */
    public Atuacao lerAtuacao(Atuacao antiga) throws Exception{
        // Definir atributos de uma Atuação
        String personagem;
        Serie s;
        Ator a;

        int IDSerie = -1;
        int IDAtor = -1;

        
        String aux;

        // Definir variável de controle
        boolean dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler opção 
            System.out.print("Deseja Alterar a Série? [y/n]");
            aux = console.nextLine();
            // Testar se é para manter os dados antigos
            if (aux.length() == 0 || aux.charAt(0) == 'n' || aux.charAt(0) == 'N') {
                IDSerie = antiga.getIDSerie();
                dadosCorretos = true;
            } else {
                s = visaoSerie.buscarUmaSerie();
                // Testar se a entrada é válida
                if (s.getID() >= 0){
                    dadosCorretos = true;
                    IDSerie = s.getID();
                }
                else
                    System.err.println("[ERRO]: Escolha uma série válida!");
            }
        } while (!dadosCorretos);

        do {
            // Ler o nome da Atuação
            System.out.print("Deseja Alterar o Ator? [y/n]");
            aux = console.nextLine();
            // Testar se é para manter os dados antigos
            if (aux.length() == 0 || aux.charAt(0) == 'n' || aux.charAt(0) == 'N') {
                IDAtor = antiga.getIDAtor();
                dadosCorretos = true;
            } else {
                try {
                    a = visaoAtor.buscarUmAtor();
                } catch (Exception e){
                    System.err.println("[ERRO]: "+e);
                    a = new Ator();
                }
                // Testar se a entrada é válida
                if (a.getID() >= 0){
                    dadosCorretos = true;
                    IDAtor = a.getID();
                }
                else
                    System.err.println("[ERRO]: Escolha uma série válida!");
            }
        } while (!dadosCorretos);

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler o Personagem da Atuação
            System.out.print("Qual o Personagem? ");
            personagem = console.nextLine();
            // Testar se é para manter os dados antigos
            if (personagem.length() == 0){
                personagem = antiga.getPersonagem();
                dadosCorretos = true;
            }
            // Testar se a entrada é válida
            if(personagem.length() >= 2)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O personagem deve ter no mínimo 2 caracteres!");
        } while(!dadosCorretos);


        // Criar o novo objeto
        Atuacao nova = new Atuacao(IDAtor, IDSerie, personagem);

        // Retornar objeto da Atuação preenchido com as informações!
        return nova;
    }

    /*
     * buscarUmaAtuacao - Função para iniciar a operação de busca de Atuação pelo nome
     * @return s - Atuação selecionada
     */
    public Atuacao buscarUmaAtuacao() {
        // Definir a variável Atuação a ser retornada
        Atuacao s = null;
        // Definir lista auxiliar de Atuações
        List<Atuacao> Atuacaos;
        // Definir variável de controle
        boolean dadosCorretos;
        // Buscar todas as Atuações pelo nome
        Atuacaos = buscarAtuacaoNome();
        // Reiniciar variável de controle
        dadosCorretos = false;
        // Testar lista de Atuações encontradas pelo nome
        if (Atuacaos.isEmpty()) {
            System.err.println("[ERRO]: Nenhuma Atuação foi encontrada!");
        } else if (Atuacaos.size() <= 1) {
            s = Atuacaos.get(0);
        } else {
            // Definir variável de controle  
            int opcao = 0;    
            // Iniciar bloco de seleção
            do {
                // Exibir todas as Atuações encontradas pelo nome
                System.out.println("Escolha uma Atuação: ");    
                int n = 0;    
                for (Atuacao l : Atuacaos) 
                    System.out.println((n++) + " - " + l.getPersonagem());    
                // Tentar ler a opção do console
                try {
                    opcao = Integer.valueOf(console.nextLine());
                } catch(NumberFormatException e) {
                    opcao = -1;
                }    
                // Testar a opção
                if (0 <= opcao && opcao <= Atuacaos.size()) {
                    // Identificar a Atuação selecionada pela sua posição
                    s = Atuacaos.get(opcao);    
                    // Atualizar variável de controle
                    dadosCorretos = true;
                } else {
                    System.err.println("[ERRO]: Atuação não está presente na lista!");
                }
            } while(!dadosCorretos);        
        }
        // Mostrar a Atuação selecionada
        mostraAtuacao(s);
        // Retornar Atuação selecionada
        return s;
    }

    /*
     * buscarAtuacaoNome - Função para ler um Nome e buscar uma Atuação a partir dele
     * @return Atuacaos - Lista de Atuações cujo nome se inicia com a String buscada
     */
    public List<Atuacao> buscarAtuacaoNome() {
        // Mostrar o cabeçalho
        System.out.println("\n> Busca de Atuação pelo Nome do Personagem");
        // Ler o nome digitado pelo usuário
        System.out.print("\nNome do Personagem: ");
        String nome = console.nextLine();  // Lê o título digitado pelo usuário
        // Definir lista de Atuações
        List<Atuacao> Atuacaos = new ArrayList<Atuacao>();
        // Tentar buscar Atuações a partir do Nome 
        try {
            Atuacaos = controleAtuacao.buscarAtuacao(nome);  // Chama o método de leitura da classe Arquivo
            return Atuacaos;
        } catch(Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
            return Atuacaos;
        }
    }   

    /*
     * buscarAtuacoesSerie - Ler uma Série com buscarUmaSerie() e buscar todas as Atuações vinculadas a ela 
     *                       com ControleAtuacao.buscarAtuacaoAtor(). Criar um menu de seleção e mostrar a
     *                       escolhida pelo usuário.
     */
    public void buscarAtuacoesSerie(){
        try {
            Atuacao a = null;
            VisaoSerie vs = new VisaoSerie();
            Serie s = vs.buscarUmaSerie();

            List<Atuacao> atuacoes = controleAtuacao.buscarAtuacaoSerie(s.getID());

            // Reiniciar variável de controle
            boolean dadosCorretos = false;
            // Testar lista de Atuações encontradas pelo nome
            if (atuacoes.isEmpty()) {
                System.err.println("[ERRO]: Nenhuma Atuação foi encontrada!");
            } else if (atuacoes.size() <= 1) {
                a = atuacoes.get(0);
            } else {
                // Definir variável de controle  
                int opcao = 0;    
                // Iniciar bloco de seleção
                do {
                    // Exibir todas as Atuações encontradas pelo nome
                    System.out.println("Escolha uma Atuação: ");    
                    int n = 0;    
                    for (Atuacao l : atuacoes) 
                        System.out.println((n++) + " - " + l.getPersonagem());    
                    // Tentar ler a opção do console
                    try {
                        opcao = Integer.valueOf(console.nextLine());
                    } catch(NumberFormatException e) {
                        opcao = -1;
                    }    
                    // Testar a opção
                    if (0 <= opcao && opcao <= atuacoes.size()) {
                        // Identificar a Atuação selecionada pela sua posição
                        a = atuacoes.get(opcao);    
                        // Atualizar variável de controle
                        dadosCorretos = true;
                    } else {
                        System.err.println("[ERRO]: Atuação não está presente na lista!");
                    }
                } while(!dadosCorretos);        
            }

            // Mostrar a Atuação selecionada
            mostraAtuacao(a);

        } catch (Exception e){
            System.err.println("\n[ERRO]: " + e.getMessage());
        }

    }

    /*
     * mostraAtuacao - Função para testar e mostrar a Atuação para o Usuário
     * @param Atuacao - Atuação a ser mostada
     */
    public void mostraAtuacao(Atuacao Atuacao) {
        if (Atuacao != null) {
            try {
                ControleSerie cs = new ControleSerie();
                ControleAtor ca = new ControleAtor();

                String serieNome = cs.buscarSerie(Atuacao.getIDSerie()).getNome();
                String atorNome = ca.buscarAtor(Atuacao.getIDAtor()).getNome();
                Pattern pattern = Pattern.compile("\\| IDSerie[.:]+\\s*\\d+\\s*\\|");

                Pattern pattern2 = Pattern.compile("\\| IDAtor[.:]+\\s*\\d+\\s*\\|");

                // Replace the IDSérie line with the new Série line
                String updated = Atuacao.toString().replaceAll(pattern.pattern(), 
                String.format("| Série.....: %s |", serieNome));

                updated = updated.replaceAll(pattern2.pattern(), 
                String.format("| Ator......: %s |", atorNome));

                // Split into lines
                String[] lines = updated.split("\n");
                
                String[] todos = {atorNome, serieNome, Atuacao.getPersonagem()};

                // Find maximum line length (excluding borders)
                int maxLength = 0;
                for (String line : todos) {
                    maxLength = Math.max(maxLength, line.length());
                }
                
                // Rebuild lines with proper padding
                int lastInfo = 0;
                String contentToPrint = " ";
                StringBuilder result = new StringBuilder();
                for (String line : lines) {
                    //System.out.println(line);
                    if (line.length() > 0 ){
                        if (line.startsWith("+")) {
                            // Adjust border length to match content
                            String border = "+" + "-".repeat(maxLength + 14) + "+";
                            result.append(border).append("\n");
                        } else {
                            // Pad content lines
                            result.append(line.substring(0, 13));
                            if (lastInfo == 0){
                                contentToPrint = String.valueOf(Atuacao.getID());
                            } else 
                                contentToPrint = todos[lastInfo-1];
                            String paddedLine = String.format(" %-" + (maxLength) + "s |", contentToPrint);
                            result.append(paddedLine).append("\n");
                            lastInfo ++;
                        }
                    }
                }
                
                String finalString = result.toString();

                System.out.println("\n"+finalString.trim());
                
            } catch (Exception e){
                e.printStackTrace();
                System.err.println("\n[ERRO]: " + e.getMessage());
            }
        }
    }

}
