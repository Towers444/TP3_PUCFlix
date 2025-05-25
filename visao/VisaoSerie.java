package visao;

import entidades.Serie;
import entidades.Episodio;
import controle.ControleSerie;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import aeds3.ElementoLista;
import aeds3.ListaInvertida;

import indexador.IndexadorTexto;

public class VisaoSerie {
    
    ControleSerie controleSerie;

    private static Scanner console = new Scanner(System.in);

    /*
     * Construtor da classe VisaoSerie
     */
    public VisaoSerie() throws Exception {
        controleSerie = new ControleSerie();
    }

    /*
     * menu - Função inicial para mostrar as opções da tela de Série
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
            System.out.println("> Início > Séries\n");
            System.out.println("1 - Incluir Série");
            System.out.println("2 - Excluir Série");
            System.out.println("3 - Alterar Série");
            System.out.println("4 - Buscar Série");
            System.out.println("5 - Buscar Episódios");
            System.out.println("6 - Editar Elenco");
            System.out.println("0 - Sair");
            // Ler opção do usuário
            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch(NumberFormatException e) {
                opcao = -1;
            }
            // Testar opção do usuário
            try {
                switch (opcao) {
                    case 1: incluirSerie(); break;
                    case 2: excluirSerie(); break;
                    case 3: alterarSerie(); break;
                    case 4: buscarUmaSerie(); break;
                    case 5: buscarEpisodios(); break;
                    case 6: new VisaoAtuacao().menu(versao); break;
                    case 0: break;
                    default: System.err.println("\n[ERRO]: Opção inválida!"); break;
                }
            } catch (Exception e) {
                System.err.println("[ERRO]: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    /*
     * incluirSerie - Função para ler informações de uma Série e confirmar a inclusão
     */
    public void incluirSerie() {
        // Exibir título da ação
        System.out.println("\n> Inclusão de Serie\n");
        // Ler dados da Série a ser incluída
        Serie s = lerSerie();
        // Confirmar a inclusão da Série
        System.out.print("\nConfirma a inclusão da Serie? (S/N) ");
        // Identificar escolha
        char resp = console.nextLine().charAt(0);
        // Testar escolha
        if (resp == 'S' || resp == 's') {
            // Tentar incluir a Série a partir do ControleSerie
            try {
                controleSerie.incluirSerie(s);
                System.out.println("\nSerie incluída com sucesso!");
            } catch(Exception e) {
                System.err.println("\n[ERRO]: " + e.getMessage());
            }
        } else {
            System.out.println("\nOperação cancelada!");
        }
    }

    /*
     * alterarSerie - Função para ler novas informações de uma Série e alterá-la
     */
    public void alterarSerie() {
        System.out.println("\nAlteração de Serie");
        try {
            // Tenta ler o Serie com o ID fornecido
            Serie s = buscarUmaSerie();
            if (s != null) {
                System.out.println("\n> Insira os novos dados da Série (caso deseje manter os dados originais, apenas tecle Enter): \n");
                Serie nova = lerSerie(s);
                nova.setID(s.getID());

                // Confirmação da alteração
                System.out.print("\nConfirma as alterações? (S/N) ");
                char resp = console.next().charAt(0);
                if (resp == 'S' || resp == 's') {
                    // Salva as alterações no arquivo
                    boolean alterado = controleSerie.alterarSerie(nova);
                    if (alterado) {
                        System.out.println("\nSérie alterada com sucesso!");
                    } else {
                        System.err.println("\n[ERRO]: Não foi possível alterar a Série!");
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
     * excluirSerie - Função para buscar uma Série e excluí-la
     */
    public void excluirSerie() {
        // Mostrar cabeçalho
        System.out.println("\nExclusão de Série");
        // Iniciar bloco try-catch
        try {
            // Tentar ler o Serie com o ID fornecido
            Serie s = buscarUmaSerie();
            // Testar se a Série é válida
            if (s != null) {
                // Confirmar a exclusão da Série
                System.out.print("\nConfirma a exclusão da Serie? (S/N) ");
                // Ler a resposta do usuário
                char resp = console.nextLine().charAt(0);
                // Testar a resposta do usuário
                if (resp == 'S' || resp == 's') {
                    // Chama o método de exclusão no arquivo
                    boolean excluido = controleSerie.excluirSerie(s.getID());  
                    // Testar o status da exclusão
                    if (excluido) {
                        System.out.println("\nSerie excluída com sucesso.");
                    } else {
                        System.out.println("\n[ERRO]: Não foi possível excluir a Serie!");
                    }
                } else {
                    System.out.println("\nExclusão cancelada!");
                }
                // Limpar o buffer
                //console.nextLine();
            }
        } catch (Exception e) {
            System.out.println("\n[ERRO]: " + e.getMessage());
        }
    }
    
    /*
     * lerSerie - Função para efetivamente obter as informações de uma Série do usuário
     * @return serie - Objeto da Série lida pelo Usuário
     */
    public Serie lerSerie() {
        // Definir atributos de uma série
        String nome;
        int anoLancamento = 0;
        String sinopse;
        String streaming;
        int nota = 0;
        ArrayList<String> criadores = new ArrayList<String>();
        String criador;
        String genero;

        // Definir variável de controle
        boolean dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler o nome da Série
            System.out.print("Qual o nome da Série? ");
            nome = console.nextLine();
            // Testar se a entrada é válida
            if (nome.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O nome deve ter no mínimo 4 caracteres!");
        } while (!dadosCorretos);

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler o ano de lançamento da Série
            System.out.print("Qual o ano de lançamento (yyyy)? ");
            if (console.hasNextInt()) {
                anoLancamento = console.nextInt();
                // Testar se o ano é válido
                if(1000 <= anoLancamento && anoLancamento <= 9999)
                    dadosCorretos = true;
            } else {
                System.err.println("[ERRO]: O ano deve ser de 4 dígitos!");
            }
            // Limpar o buffer
            console.nextLine();
        } while (!dadosCorretos);

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler a sinopse da Série
            System.out.print("Qual a sinopse? ");
            sinopse = console.nextLine();
            // Testar o tamanho da sinopse da Série
            if(sinopse.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: A sinopse deve ter no mínimo 4 caracteres!");
        } while (!dadosCorretos);

        // Reiniciar a variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler o streaming da Série
            System.out.print("Qual o streaming? ");
            streaming = console.nextLine();
            // Testar o tamanho do streaming da Série
            if(streaming.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O streaming deve ter no mínimo 4 caracteres!");
        } while (!dadosCorretos);

        // Reiniciar a variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler a nota da Série 
            System.out.print("Qual a nota (0 a 10)? ");
            if (console.hasNextInt()) {
                nota = console.nextInt();
                // Testar nota da Sèrie
                if (0 <= nota && nota <= 10)
                    dadosCorretos = true;
            } else {
                System.err.println("[ERRO]: A nota deve ser entre 0 e 10!");
            }
            // Limpar o buffer
            console.nextLine();
        } while (!dadosCorretos);

        // Reiniciar a variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler o gênero da Série
            System.out.print("Qual o gênero? ");
            genero = console.nextLine();
            // Testar o gênero da Série
            if (genero.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O gênero deve ter no mínimo 4 caracteres!");
        } while (!dadosCorretos);

        // Reiniciar a variável de controle
        dadosCorretos = false;
        do {
            // Ler os criadores da Série
            System.out.print("Quais são os criadores? (digite FIM para parar) ");
            criador = console.nextLine();
            // Testar a leitura dos criadores
            if (criador.equals("FIM") && criadores.size() > 0){
                dadosCorretos =  true;
            } else {
                // Testar se os criadores da série são válidos
                if (criador.length() >= 4) {
                    criadores.add(criador);
                } else if(criador.length() == 0) {
                    System.err.println("[ERRO]: A Série deve conter pelo menos um criador!");
                } else {
                    System.err.println("[ERRO]: O criador deve ter no mínimo 4 caracteres.");
                }
            }
        } while (!dadosCorretos);

        // Criar objeto Série resultante
        Serie serie = new Serie(nome, anoLancamento, sinopse, streaming, nota, criadores, genero); 
        // Retornar objeto da Série preenchido com as informações
        return serie;
    }

    /*
     * lerSerie - Função para efetivamente obter as informações de uma NOVA Série do usuário
     * @param antiga - Série antiga
     * @return nova - Série nova
     */
    public Serie lerSerie(Serie antiga) {
        // Definir atributos de uma série
        String nome;
        int anoLancamento = 0;
        String sinopse;
        String streaming;
        int nota = 0;
        ArrayList<String> criadores = new ArrayList<String>();
        String criador;
        String genero;
        String aux;

        // Definir variável de controle
        boolean dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler o nome da Série
            System.out.print("Qual o nome da Série? ");
            nome = console.nextLine();
            // Testar se é para manter os dados antigos
            if (nome.length() == 0) {
                nome = antiga.getNome();
                dadosCorretos = true;
            }
            /// Testar se a entrada é válida
            if (nome.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O nome deve ter no mínimo 4 caracteres!");
        } while (!dadosCorretos);

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler o ano de lançamento da Série
            System.out.print("Qual o ano de lançamento (yyyy)? ");
            aux = console.nextLine();
            // Testar se é para manter os dados antigos
            if (aux.length() == 0){
                anoLancamento = antiga.getAnoLancamento();
                dadosCorretos = true;
            } else {
                try {
                    anoLancamento = Integer.parseInt(aux);
                    // Testar se a entrada é válida
                    if (1000 <= anoLancamento && anoLancamento <= 9999) {
                        dadosCorretos = true;
                    } else {
                        System.err.println("[ERRO]: O ano deve ser de 4 dígitos!");
                    }
                } catch (NumberFormatException e){
                    dadosCorretos = false;
                    System.err.println("[ERRO]: Digite um número válido!");
                }
            }
        } while (!dadosCorretos);

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler a sinopse da Série
            System.out.print("Qual a sinopse? ");
            sinopse = console.nextLine();
            // Testar se é para manter os dados antigos
            if (sinopse.length() == 0){
                sinopse = antiga.getSinopse();
                dadosCorretos = true;
            }
            // Testar se a entrada é válida
            if(sinopse.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: A sinopse deve ter no mínimo 4 caracteres!");
        } while(!dadosCorretos);

        // Reiniciar a variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler o streaming da Série
            System.out.print("Qual o streaming? ");
            streaming = console.nextLine();
            // Testar se é para manter os dados antigos
            if (streaming.length() == 0){
                streaming = antiga.getStreaming();
                dadosCorretos = true;
            }
            // Testar se a entrada é válida
            if(streaming.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O streaming deve ter no mínimo 4 caracteres!");
        } while(!dadosCorretos);

        // Reiniciar variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler o ano de lançamento da Série
            System.out.print("Qual a nota (0 a 10)? ");
            aux = console.nextLine();
            // Testar se é para manter os dados antigos
            if (aux.length() == 0) {
                nota = antiga.getNota();
                dadosCorretos = true;
            } else {
                try {
                    nota = Integer.parseInt(aux);
                    // Testar se a entrada é válida
                    if(0 <= nota && nota <= 10) {
                        dadosCorretos = true;
                    } else {
                        System.err.println("[ERRO]: A nota deve ser entre 0 e 10!");
                    }
                } catch (NumberFormatException e){
                    dadosCorretos = false;
                    System.err.println("[ERRO]: Digite um número válido!");
                }
            }
        } while (!dadosCorretos);

        // Reiniciar a variável de controle
        dadosCorretos = false;
        // Iniciar bloco de seleção
        do {
            // Ler o gênero da Série
            System.out.print("Qual o gênero? ");
            genero = console.nextLine();
            // Testar se é para manter os dados antigos
            if (genero.length() == 0){
                genero = antiga.getGenero();
                dadosCorretos = true;
            }
            // Testar se a entrada é válida
            if (genero.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("[ERRO]: O gênero deve ter no mínimo 4 caracteres!");
        } while(!dadosCorretos);

        // Reiniciar a variável de controle
        dadosCorretos = false;
        do {
            // Ler os criadores da Série
            System.out.print("Quais são os criadores? (digite FIM para parar) ");
            criador = console.nextLine();
            if (criador.length() == 0){
            // Testar se é para manter os dados antigos
                criadores = antiga.getCriadores();
                dadosCorretos = true;
            } else if (criador.equals("FIM") && criadores.size() > 0){ // Testar se a entrada é válida
                dadosCorretos =  true;
            } else {
                // Testar se os criadores da série são válidos
                if (criador.length() >= 4) {
                    criadores.add(criador);
                } else if(criador.length() == 0) {
                    System.err.println("[ERRO]: A Série deve conter pelo menos um criador!");
                } else {
                    System.err.println("[ERRO]: O criador deve ter no mínimo 4 caracteres.");
                }
            }
        } while(!dadosCorretos);

        // Criar o novo objeto
        Serie nova = new Serie(nome, anoLancamento, sinopse, streaming, nota, criadores, genero);

        // Retornar objeto da Série preenchido com as informações!
        return nova;
    }

    /*
     * buscarUmaSerie - Função para iniciar a operação de busca de Série pelo nome
     * @return s - Série selecionada
     */
    public Serie buscarUmaSerie() {
        // Definir a variável Série a ser retornada
        Serie s = null;
        // Definir lista auxiliar de Séries
        List<Serie> series;
        // Definir variável de controle
        boolean dadosCorretos;
        // Buscar todas as Séries pelo nome
        series = buscarSerieNome();
        // Reiniciar variável de controle
        dadosCorretos = false;
        // Testar lista de Séries encontradas pelo nome
        if (series.isEmpty()) {
            System.err.println("[ERRO]: Nenhuma Série foi encontrada!");
        } else if (series.size() <= 1) {
            s = series.get(0);
        } else {
            // Definir variável de controle  
            int opcao = 0;    
            // Iniciar bloco de seleção
            do {
                // Exibir todas as Séries encontradas pelo nome
                System.out.println("\nEscolha uma Série: ");    
                int n = 0;    
                for (Serie l : series) 
                    System.out.println((n++) + " - " + l.getNome());    
                // Tentar ler a opção do console
                try {
                    opcao = Integer.valueOf(console.nextLine());
                } catch(NumberFormatException e) {
                    opcao = -1;
                }    
                // Testar a opção
                if (0 <= opcao && opcao <= series.size()) {
                    // Identificar a Série selecionada pela sua posição
                    s = series.get(opcao);    
                    // Atualizar variável de controle
                    dadosCorretos = true;
                } else {
                    System.err.println("[ERRO]: Série não está presente na lista!");
                }
            } while(!dadosCorretos);        
        }
        // Mostrar a Série selecionada
        mostraSerie(s);
        // Retornar Série selecionada
        return s;
    }

    /*
     * buscarSerieNome - Função para ler um Nome e buscar uma Série a partir dele
     * @return series - Lista de Séries cujo nome se inicia com a String buscada
     */
    public List<Serie> buscarSerieNome() {
        // Mostrar o cabeçalho
        System.out.println("\n> Busca de Série por Nome");
        // Ler o nome digitado pelo usuário
        System.out.print("\nNome: ");
        String nome = console.nextLine();  // Lê o título digitado pelo usuário
        // Definir lista de Séries
        List<Serie> series = new ArrayList<Serie>();
        // Tentar buscar Séries a partir do Nome 
        try {
            controleSerie.buscarSerieListaInvertida(nome);
            series = controleSerie.buscarSerie(nome);  // Chama o método de leitura da classe Arquivo
            return series;
        } catch(Exception e) {
            System.err.println("\n[ERRO]: " + e.getMessage());
            return series;
        }
    }   

    /*
     * buscarEpisodios - Função para buscar todos os Episódios de uma Série, listá-los e mostrar o escolhido pelo usuário
     */
    public void buscarEpisodios() {
        // Iniciar bloco try-catch
        try {
            // Tentar ler a Serie com o ID fornecido
            Serie s = buscarUmaSerie();
            // Testar a leitura da Série
            if (s == null) return;
            // Mostrar cabeçalho
            System.out.println("\n> Episódios da Série\n");
            // Buscar todos os episódios vinculados à Série
            List<Episodio> episodios = controleSerie.buscarSerieEpisodios(s.getID());
            // Mostrar as opções de Séries a serem escolhidas
            int i = 1;
            for (Episodio episodio : episodios) {
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
                    dadosCorretos = true;
                } catch(NumberFormatException e) {
                    System.err.println("\n[ERRO]: Opção inválida!");
                    dadosCorretos = false;
                }
            } while ( !dadosCorretos || !ControleSerie.validarSerie(opcao));            
            // Identificar Episódio escolhido
            System.out.print(episodios.get(opcao - 1));
        } catch (Exception e) {
            System.out.println("[ERRO]: " + e.getMessage());
        }
    }

    /*
     * mostraSerie - Função para testar e mostrar a Série para o Usuário
     * @param serie - Série a ser mostada
     */
    public void mostraSerie(Serie serie) {
        if (serie != null) {
            System.out.println(serie);
        }
    }
}
