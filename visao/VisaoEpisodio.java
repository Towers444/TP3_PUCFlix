package visao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entidades.Episodio;
import entidades.Serie;
import controle.ControleEpisodio;
import controle.ControleSerie;

public class VisaoEpisodio {

    ControleEpisodio controle;
    Serie serie;
    int temporada;
    private static Scanner console = new Scanner(System.in);

    /*
     * Definir construtor da classe VisaoEpisodio sem vinculação de Série
     */
    public VisaoEpisodio() throws Exception {
        controle = new ControleEpisodio();
    }

    /*
     * Definir construtor da classe VisaoEpisodio com vinculação de Série
     * @param s - Série selecionada
     */
    public VisaoEpisodio(Serie s) throws Exception {
        controle = new ControleEpisodio(s);
        serie = s;
        temporada = 0;
    }

    /*
     * menu - Função para controlar as opções da tela 'Início > Episódios > Nome da Série'
     * @param versao - Versão do código atual
     */
    public void menu(String versao) {
        // Tentar ler a Série
        try {
            VisaoSerie visaoSerie = new VisaoSerie();
            serie = visaoSerie.buscarUmaSerie();
            controle = new ControleEpisodio(serie);
        } catch (Exception e){
            System.err.println("\n[ERRO]: " + e.getMessage());
        }

        // Testar se a Série foi selecionada
        if (serie == null) {
            System.err.println("[ERRO]: Série não encontrada!");
            return;
        }
        
        // Definir variável auxiliar
        int opcao;

        // Iniciar bloco de seleção
        do {
            // Mostrar Menu de seleção
            System.out.println("\nPUCFlix v" + versao);
            System.out.println("--------------------------");
            System.out.println("> Início > Episódios > " + serie.getNome());
            System.out.println("\n1 - Incluir Episódio");
            System.out.println("2 - Excluir Episódio");
            System.out.println("3 - Alterar Episódio");
            System.out.println("4 - Buscar Um Episódio");
            System.out.println("5 - Buscar Todos os Episódios");
            System.out.println("6 - Escolher Temporada");
            System.out.println("0 - Sair");
            System.out.print("\nOpção: ");

            // Ler a opção do console
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            // Testar a opção escolhida
            switch (opcao) {
                case 1: incluirEpisodio(); break;
                case 2: excluirEpisodio(); break;
                case 3: alterarEpisodio(); break;
                case 4: buscarUmEpisodio(); break;
                case 5: buscarEpisodios(); break;
                case 6: entrarTemporada(); break;
                case 0: break;
                default: System.err.println("[ERRO]: Opção inválida!"); break;
            }
        } while (opcao != 0);
    }

    /*
     * menuTemporada - Função para controlar as opções da tela 'Início > Episódios > Nome da Série > Temporada X'
     */
    public void menuTemporada() {
        // Definir variável auxiliar
        int opcao;

        // Testar se a Série foi selecionada
        if (serie == null) {
            System.err.println("[ERRO]: Série não selecionada");
            return;
        }

        // Iniciar bloco de seleção
        do {
            // Mostar
            System.out.println("\nPUCFlix v");
            System.out.println("--------------------------");
            System.out.println("> Início > Episódios > " + serie.getNome() + " > Temporada " + this.temporada);
            System.out.println("\n1 - Incluir Episódio");
            System.out.println("2 - Excluir Episódio");
            System.out.println("3 - Alterar Episódio");
            System.out.println("4 - Buscar Um Episódio");
            System.out.println("5 - Buscar Todos os Episódios");
            System.out.println("0 - Sair");
            System.out.print("\nOpção: ");

            // Ler a opção do console
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            // Testar a opção escolhida
            switch (opcao) {
                case 1: incluirEpisodio(); break;
                case 2: excluirEpisodio(); break;
                case 3: alterarEpisodio(); break;
                case 4: buscarUmEpisodioTemporada(this.temporada); break;
                case 5: buscarEpisodiosTemporada(this.temporada); break;
                case 0: this.temporada = 0; break;
                default: System.err.println("\n[ERRO]: Opção inválida!"); break;
            }
        } while (opcao != 0);
    }

    /*
     * entrarTemporada - Função para ler uma Temporada e mostrar o seu Menu
     */
    public void entrarTemporada() {
        // Definir dados
        int temporada = 0;
        boolean dadosCorretos = false;
        // Mostrar o cabeçalho
        System.out.println("\n> Entrar na Temporada");
        do {
            // Ler a temporada
            System.out.print("\nDigite a Temporada: ");
            if (console.hasNextInt()) {
                temporada = console.nextInt();
                // Testar se a entrada é válida
                if (0 < temporada && temporada <= 127)
                    dadosCorretos = true;
                else
                    System.err.println("[ERRO]: A Temporada deve ser entre 1 e 127!");
            } else {
                System.err.println("[ERRO]: A entrada deve ser um inteiro!");
            }
            // Limpar o buffer
            console.nextLine();
        } while (!dadosCorretos);
        // Iniciar o bloco try-catch
        try {
            // Tentar ler algum Episódio vinculado a essa temporada
            controle.buscarEpisodioTemporada(temporada);
            // Definir a temporada atual
            this.temporada = temporada;
            // Exibir o menu da temporada
            menuTemporada();
        } catch (Exception e) {
            System.err.println("\n[ERRO]: Temporada não existe na Série '" + serie.getNome() + "'!");
        }
    }
    
    /*
     * incluirEpisódio - Função para ler informações de um Episódio e confirmar a inclusão
     */
    public void incluirEpisodio() {
        // Testar se uma Temporada está selecionada
        if (this.temporada > 0) {
            // Mostrar cabeçalho
            System.out.println("\n> Inclusão de Episódio - " + this.temporada + " Temporada\n");
        } else {
            // Mostrar cabeçalho
            System.out.println("\n> Inclusão de Episodio\n");
        }
        // Tentar ler um Episódio com os atributos preenchidos pelo usuário
        try {
            Episodio ep = lerEpisodio();
            // Testar a leitura do Episódio
            if (ep != null) {
                // Confirmar a inclusão do Episódio
                System.out.print("\nConfirma a inclusão do Episodio? (S/N) ");
                char resp = console.nextLine().charAt(0);
                // Testar a opção do usuário
                if (resp == 'S' || resp == 's') {
                    // Tentar incluir o Episódio
                    controle.incluirEpisodio(ep);
                    System.out.println("\nEpisódio incluído com sucesso!");
                }
            }
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }
    
    /*
     * alterarEpisodio - Função para ler novas informações de um Episódio e alterá-lo
     */
    public void alterarEpisodio() {
        // Mostrar o cabeçalho
        System.out.println("\n> Alteração de Episodio\n");
        // Iniciar bloco try-catch
        try {
            // Tenta ler o Episodio com o ID fornecido
            Episodio ep = buscarUmEpisodio();
            if (ep != null) {
                // Mostrar o cabeçalho
                System.out.println("> Insira os novos dados do Episódio (caso deseje manter os dados originais, apenas tecle Enter): \n");
                // Ler o novo Episódio
                Episodio novo = lerEpisodio(ep);
                // Confirmar a alteração
                System.out.print("\nConfirma as alterações? (S/N) ");
                char resp = console.nextLine().charAt(0);
                // Testar a confirmação
                if (resp == 'S' || resp == 's') {
                    // Salva as alterações no arquivo
                    boolean alterado = controle.alterarEpisodio(novo);
                    if (alterado) {
                        System.out.println("\nEpisódio alterado com sucesso.");
                    } else {
                        System.err.println("\n[ERRO]: Não foi possível alterar o Episódio.");
                    }
                } else {
                    System.out.println("\nAlterações canceladas.");
                }
            }
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }

    /*
     * excluirEpisodio - Função para buscar um Episódio e excluí-lo
     */
    public void excluirEpisodio() {
        // Mostrar o cabeçalho
        System.out.println("\nExclusão de Série");
        // Iniciar o bloco try-catch
        try {
            // Tenta ler o Episodio com o ID fornecido
            Episodio ep = buscarUmEpisodio();
            // Testar a leitura do Episódio
            if (ep != null) {
                // Confirmar a exclusão do Episódio
                System.out.print("\nConfirma a exclusão do Episódio? (S/N) ");
                char resp = console.next().charAt(0); // Lê a resposta do usuário

                // Testar a confirmação
                if (resp == 'S' || resp == 's') {
                    boolean excluido = controle.excluirEpisodio(ep); // Chama o método de exclusão no arquivo
                    if (excluido) {
                        System.out.println("\nEpisódio excluído com sucesso.");
                    } else {
                        System.err.println("\n[ERRO]: Não foi possível excluir o Episódio.");
                    }
                } else {
                    System.err.println("\nExclusão cancelada.");
                }
                // Limpar o buffer
                console.nextLine(); 
            } else {
                System.err.println("\n[ERRO]: Episódio não encontrado!");
            }
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }
    
    /*
     * lerEpisodio - Função para preencher os atributos de um Episódio pelo usuário
     * @return ep - Episódio com os atributos preenchidos pelo usuário
     */
    public Episodio lerEpisodio() throws Exception {
        // Definir os atributos de um Episódio
        String nome;
        int IDSerie = serie.getID();
        int temporada = 0;
        LocalDate dataLancamento = null;
        float duracao = 0; // em minutos
        int nota = 0; // 0 a 10
        List<String> diretores = new ArrayList<String>();
        String diretor;

        // Definir variáveis auxiliares
        boolean dadosCorretos = false;
        String regex = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern = Pattern.compile(regex);

        // Ler o nome do Episódio
        do {
            // Ler o nome do Episódio do console
            System.out.print("Qual o nome do Episódio? ");
            nome = console.nextLine();

            // Testar se a entrada é válida
            if (nome.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O nome deve ter no mínimo 4 caracteres!");
        } while (!dadosCorretos);

        // Testar se a temporara do Episódio já está selecionada
        if (this.temporada > 0) {
            temporada = this.temporada;
        } else {
            // Reiniciar variável de controle
            dadosCorretos = false;
            // Ler a temporada do Episódio
            do {
                // Ler a temporada do Episódio do console
                System.out.print("Qual a Temporada? ");
                if (console.hasNextInt()) {
                    temporada = console.nextInt();
                    // Testar se a entrada é válida
                    if (0 < temporada && temporada <= 127)
                        dadosCorretos = true;
                    else 
                        System.err.println("[ERRO]: A Temporada deve ser entre 1 e 127!");
                } else {
                    System.err.println("[ERRO]: A Temporada deve ser um número inteiro!");
                }

                // Limpar o buffer
                console.nextLine();
            } while (!dadosCorretos);
        }

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Ler a data de lançamento do Episódio
        do {
            System.out.print("Qual a data de lançamento (dd/MM/yyyy)? ");
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

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Ler a duração do Episódio em minutos
        do {
            System.out.print("Qual a duração (minutos)? ");
            if (console.hasNextFloat()) {
                duracao = console.nextFloat();
                // Testar se a entrada é válida
                if (0 < duracao){
                    dadosCorretos = true;
                } else {
                    System.err.println("[ERRO]: O episódio deve ter mais que 0 minutos!");
                }
            } else {
                System.err.println("[ERRO]: Deve entrar com um número! Usar ponto para casas decimais");
            }

            // Limpar o buffer
            console.nextLine();
        } while (!dadosCorretos);

        // Reiniciar a variável de controle
        dadosCorretos = false;
        // Ler a nota do Episódio
        do {
            System.out.print("Qual a nota (0 a 10)? ");
            if (console.hasNextInt()) {
                nota = console.nextInt();
                // Testar se a entrada é válida
                if (0 <= nota && nota <= 10)
                    dadosCorretos = true;
            } else {
                System.err.println("[ERRO]: A nota deve ser entre 0 e 10!");
            }

            // Limpar o buffer
            console.nextLine();
        } while (!dadosCorretos);

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Ler os diretores do Episódio
        do {
            System.out.print("Quais são os diretores (digite FIM para parar)? ");
            diretor = console.nextLine();

            // Testar se a leitura chegou ao fim
            if (diretor.equals("FIM") && diretores.size() > 0) {
                dadosCorretos = true;
            } else {
                // Testar se a entrada é válida
                if (diretor.length() >= 4) {
                    diretores.add(diretor);
                } else if (diretor.length() == 0) {
                    System.err.println("[ERRO]: A Série deve conter pelo menos um diretor!");
                } else {
                    System.err.println("[ERRO]: O diretor deve ter no mínimo 4 caracteres!");
                }
            }
        } while (!dadosCorretos);

        // Criar um Episódio a partir das informações obtidas
        Episodio ep = new Episodio(IDSerie, nome, temporada, dataLancamento, duracao, nota, diretores);

        // Retornar o objeto Episódio
        return ep;
    }

    /*
     * lerEpisodio - Função para preencher os atributos de um NOVO Episódio pelo usuário
     * @param antigo - Episódio antigo a ser alterado
     * @return ep - Novo Episódio com os atributos preenchidos pelo usuário
     */
    public Episodio lerEpisodio(Episodio antigo) throws Exception {
        // Definir os atributos de um Episódio
        String nome;
        int IDSerie = antigo.getIDSerie();
        int temporada = 0;
        LocalDate dataLancamento = null;
        float duracao = 0; // em minutos
        int nota = 0; // 0 a 10
        List<String> diretores = new ArrayList<String>();
        String diretor;
        
        // Definir variáveis auxiliares
        String aux;
        boolean dadosCorretos = false;
        String regex = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern = Pattern.compile(regex);

        // Ler o nome do Episódio
        do {
            System.out.print("Qual o nome do Episódio? ");
            nome = console.nextLine();

            // Testar se é para manter os dados antigos
            if (nome.length() == 0) {
                nome = antigo.getNome();
                dadosCorretos = true;
            }

            // Testar se a entrada é válida
            if (nome.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O nome deve ter no mínimo 4 caracteres!");
        } while (!dadosCorretos);

        // Testar se a temporada do Episódio já está selecionada
        if (this.temporada > 0) {
            temporada = this.temporada;
        } else {
            // Reiniciar variável de controle
            dadosCorretos = false;
            // Ler a temporada do Episódio
            do {
                System.out.print("Qual a Temporada? ");
                aux = console.nextLine();

                // Testar se é para manter os dados antigos
                if (aux.length() == 0) {
                    temporada = antigo.getTemporada();
                    dadosCorretos = true;
                } else {
                    // Tentar ler a temporada
                    try {
                        temporada = Integer.parseInt(aux);

                        // Testar se a entrada é válida
                        if (0 <= temporada && temporada <= 127) {
                            dadosCorretos = true;
                        } else {
                            System.err.println("[ERRO]: A Temporada deve ser entre 1 e 127!");
                        }
                    } catch (NumberFormatException e) {
                        dadosCorretos = false;
                        System.err.println("[ERRO]: Digite um número válido!");
                    }
                }
            } while (!dadosCorretos);
        }

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Ler a data de lançamento do Episódio
        do {
            System.out.print("Qual a data de lançamento (dd/MM/yyyy)? ");
            String data = console.nextLine();
            Matcher matcher = pattern.matcher(data);

            // Testar se é para manter os dados antigos
            if (data.length() == 0) {
                dataLancamento = antigo.getDataLancamento();
                dadosCorretos = true;
            } else {
                // Testar se a data está no formado correto
                if (matcher.matches()) {
                    dadosCorretos = true;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    dataLancamento = LocalDate.parse(data, formatter);
                } else
                    System.err.println("[ERRO]: O formato deve ser (dd/MM/yyyy)!");
            }
        } while (!dadosCorretos);

        // Reiniciar variável de controle  
        dadosCorretos = false;
        // Ler a duração do Episódio
        do {
            System.out.print("Qual a duração (minutos)? ");
            aux = console.nextLine();

            // Testar se é para manter os dados antigos
            if (aux.length() == 0) {
                duracao = antigo.getDuracao();
                dadosCorretos = true;
            } else {
                // Tentar ler a duração
                try {
                    duracao = Float.parseFloat(aux);
                    
                    // Testar se a entrada é válida
                    if (0 < duracao) {
                        dadosCorretos = true;
                    } else {
                        System.err.println("[ERRO]: O episódio deve ter mais que 0 minutos!");
                    }
                } catch (NumberFormatException e) {
                    dadosCorretos = false;
                    System.err.println("[ERRO]: Digite um número válido!");
                }
            }
        } while (!dadosCorretos);

        // Reiniciar a variável de controle
        dadosCorretos = false;
        // Ler a nota do Episódio
        do {
            System.out.print("Qual a nota (0 a 10)? ");
            aux = console.nextLine();

            // Testar se é para manter os dados antigos
            if (aux.length() == 0) {
                nota = antigo.getNota();
                dadosCorretos = true;
            } else {
                // Tentar ler a nota
                try {
                    nota = Integer.parseInt(aux);

                    // Testar se a entrada é válida
                    if (0 <= nota && nota <= 10) {
                        dadosCorretos = true;
                    } else {
                        System.err.println("[ERRO]: A nota deve ser entre 0 e 10!");
                    }
                } catch (NumberFormatException e) {
                    dadosCorretos = false;
                    System.err.println("[ERRO]: Digite um número válido!");
                }
            }
        } while (!dadosCorretos);

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Ler os diretores do Episódio
        do {
            // Ler os criadores da Série
            System.out.print("Quais são os diretores (digite FIM para parar)? ");
            diretor = console.nextLine();

            // Testar se é para manter os dados antigos
            if (diretor.length() == 0) {
                diretores = antigo.getDiretores();
                dadosCorretos = true;
            } else if (diretor.equals("FIM") && diretores.size() > 0) {
                // Testar a leitura dos criadores
                dadosCorretos = true;
            } else {
                // Testar se os criadores da série são válidos
                if (diretor.length() >= 4) {
                    diretores.add(diretor);
                } else if (diretor.length() == 0) {
                    System.err.println("[ERRO]: O episódio deve conter pelo menos um diretor!");
                } else {
                    System.err.println("[ERRO]: O diretor deve ter no mínimo 4 caracteres.");
                }
            }
        } while (!dadosCorretos);

        // Criar um Episódio a partir das informações obtidas
        Episodio ep = new Episodio(antigo.getID(),IDSerie, nome, temporada, dataLancamento, duracao, nota, diretores);
        
        // Retornar
        return ep;
    }

    /*
     * buscarUmEpisodio - Função para buscar e mostrar UM Episódio da Série
     * @return ep - Episódio selecionado
     */
    public Episodio buscarUmEpisodio() {
        // Definir a variável Episódio a ser retornada
        Episodio ep = null;
        // Definir lista auxiliar de Episódios
        List<Episodio> episodios;
        // Definir variável de controle
        boolean dadosCorretos;
        // Buscar todos os Episódios pelo nome
        episodios = buscarEpisodioNome();
        // Testar se é para filtrar por Temporada
        if (this.temporada != 0) {
            for (int i = 0; i < episodios.size(); i++) {
                if (episodios.get(i).getTemporada() != this.temporada)
                    episodios.remove(i);
            }
        }
        // Reiniciar variável de controle
        dadosCorretos = false;
        // Testar lista de Episódios encontrados pelo nome
        if (episodios.isEmpty()) {
            System.err.println("\n[ERRO]: Nenhum Episódio foi encontrado!");
        } else if (episodios.size() <= 1) {
            ep = episodios.get(0);
            mostraEpisodio(ep);
        } else {
            // Definir variável de controle  
            int opcao = 0;    
            // Iniciar bloco de seleção
            do {
                // Exibir todos os Episódios encontrados pelo nome
                System.out.println("\n> Escolha um Episódio: \n");    
                int n = 0;    
                for (Episodio l : episodios)
                    System.out.println((n++) + " - " + l.getNome());    
                // Tentar ler a opção do console
                System.out.print("\nOpção: ");
                try {
                    opcao = Integer.valueOf(console.nextLine());
                } catch(NumberFormatException e) {
                    opcao = -1;
                }    
                // Testar a opção
                if (0 <= opcao && opcao <= episodios.size()) {
                    // Identificar a Série selecionada pela sua posição
                    ep = episodios.get(opcao);    
                    // Atualizar variável de controle
                    dadosCorretos = true;
                } else {
                    System.err.println("[ERRO]: Episódio não está presente na lista!");
                }
            } while(!dadosCorretos);  
            mostraEpisodio(ep);      
        }
        // Retornar Episódio selecionado
        return ep;
    }

    /*
     * buscarEpisodios - Função para buscar e mostrar TODOS os Episódios da Série
     */
    public void buscarEpisodios() {
        // Iniciar bloco try-catch
        try {
            // Testar serie atual
            if (this.serie == null) return;
            // Mostrar cabeçalho
            System.out.println("\n> Episódios da Série '" + this.serie.getNome() + "'\n");
            // Definir ControleSerie
            ControleSerie controleSerie = new ControleSerie();
            // Buscar todos os episódios vinculados à Série
            List<Episodio> episodios = controleSerie.buscarSerieEpisodios(serie.getID());
            // Mostrar as opções de Episódios a serem escolhidos
            int i = 1;
            for (Episodio episodio : episodios)
                System.out.println("(" + (i++) + ") - " + episodio.getNome() + " - " + episodio.getTemporada() + " Temporada");
            // Definir variável auxiliar
            int opcao = 0;
            boolean dadosCorretos = false;
            // Tentar ler a opção do console
            do {
                System.out.print("\nOpção: ");
                try {
                    opcao = Integer.valueOf(console.nextLine());
                    dadosCorretos = true;
                } catch(NumberFormatException e) {
                    System.err.println("\n[ERRO]: Opção inválida!");
                    dadosCorretos = false;
                }
            } while (!dadosCorretos);            
            // Identificar Episódio escolhido
            System.out.print(episodios.get(opcao - 1));
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }
   
    /*
     * buscarUmEpisodioTemporada - Função para buscar e mostrar UM Episódio da Série em uma determinada Temporada
     * @param temporada - Temporada da Série a ser buscada
     * @return ep - Episódio selecionado
     */
    public Episodio buscarUmEpisodioTemporada(int temporada) {
        // Definir a variável Episódio a ser retornada
        Episodio ep = null;
        // Definir lista auxiliar de Episódios
        List<Episodio> episodios;
        // Definir variável de controle
        boolean dadosCorretos;
        // Buscar todos os Episódios pelo nome
        episodios = buscarEpisodioNome();
        // Testar se é para filtrar por Temporada
        if (this.temporada != 0) {
            for (int i = 0; i < episodios.size(); i++) {
                if (episodios.get(i).getTemporada() != this.temporada)
                    episodios.remove(i);
            }
        }
        System.out.println("[VisaoEpisodio] - size: " + episodios.size());
        // Reiniciar variável de controle
        dadosCorretos = false;
        // Testar lista de Episódios encontrados pelo nome
        if (episodios.isEmpty()) {
            System.err.println("\n[ERRO]: Nenhum Episódio foi encontrado!");
        } else if (episodios.size() <= 1) {
            ep = episodios.get(0);
            mostraEpisodio(ep);
        } else {
            // Definir variável de controle  
            int opcao = 0;    
            // Iniciar bloco de seleção
            do {
                // Exibir todos os Episódios encontrados pelo nome
                System.out.println("\n> Escolha um Episódio: \n");    
                int n = 0;    
                for (Episodio l : episodios)
                    System.out.println((n++) + " - " + l.getNome());    
                // Tentar ler a opção do console
                System.out.print("\nOpção: ");
                try {
                    opcao = Integer.valueOf(console.nextLine());
                } catch(NumberFormatException e) {
                    opcao = -1;
                }    
                // Testar a opção
                if (0 <= opcao && opcao <= episodios.size()) {
                    // Identificar a Série selecionada pela sua posição
                    ep = episodios.get(opcao);    
                    // Atualizar variável de controle
                    dadosCorretos = true;
                } else {
                    System.err.println("[ERRO]: Episódio não está presente na lista!");
                }
            } while(!dadosCorretos);  
            mostraEpisodio(ep);      
        }
        // Retornar Episódio selecionado
        return ep;
    }
    
    /*
     * buscarEpisodiosTemporada - Função para buscar e mostrar TODOS os Episódios da Série em uma determinada Temporada
     * @param temporada - Temporada da Série a ser buscada
     */
    public void buscarEpisodiosTemporada(int temporada) {
        // Iniciar bloco try-catch
        try {
            // Testar serie atual
            if (this.serie == null) return;
            // Mostrar cabeçalho
            System.out.println("\n> Episódios da Temporada\n");
            // Definir ControleSerie
            ControleSerie controleSerie = new ControleSerie();
            // Buscar todos os episódios vinculados à Série
            List<Episodio> episodios = controleSerie.buscarSerieEpisodios(serie.getID());
            // Mostrar as opções de Episódios a serem escolhidos
            int i = 1;
            for (Episodio episodio : episodios) {
                if (episodio.getTemporada() == temporada)
                    System.out.println("(" + (i++) + ") - " + episodio.getNome() + " - " + episodio.getTemporada() + " Temporada");
            }
            // Definir variável auxiliar
            int opcao = 0;
            boolean dadosCorretos = false;
            // Tentar ler a opção do console
            do {
                System.out.print("\nOpção: ");
                try {
                    opcao = Integer.valueOf(console.nextLine());
                    if (1 <= opcao && opcao <= (i - 1))
                        dadosCorretos = true;
                    else
                        System.err.println("[ERRO]: Opção inválida!");
                } catch(NumberFormatException e) {
                    System.err.println("\n[ERRO]: A opção deve ser um número inteiro!");
                    dadosCorretos = false;
                }
            } while (!dadosCorretos);            
            // Identificar Episódio escolhido
            System.out.print(episodios.get(opcao - 1));
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
        }
    }

    /*
     * buscarEpisodioNome - Função para ler um Nome e buscar todos os Episódios cujo nome se inicia com a String lida
     * @return episodios - Lista dos Episódios cujo nome se inicia com a String lida
     */
    public List<Episodio> buscarEpisodioNome() {
        // Mostrar cabeçalho
        System.out.println("\n> Busca de Episódio por Nome");
        // Ler o Nome do Episódio
        System.out.print("\nNome: ");
        String nome = console.nextLine();

        // Testar se o nome buscado é válido
        if (nome.isEmpty())
            return null;
        
        // Tentar ler os Episódios com o Nome buscado
        try {
            // Chama o método de leitura da classe Arquivo
            List<Episodio> episodios = controle.buscarEpisodio(nome); 
            // Testar se algum Episódio foi encontrado
            if (episodios.size() > 0) {
                return episodios;
            } else {
                System.err.println("\n[ERRO]: Nenhum Episódio encontrado!");
                return null;
            }
        } catch (Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
            return null;
        }
    }

    /*
     * mostraEpisodio - Função para testar e mostrar o Episódio para o Usuário
     * @param episodio - Episódio a ser mostrado
     */
    public void mostraEpisodio(Episodio episodio) {
        if (episodio != null) {
            System.out.println(episodio);
        }
    }

}
