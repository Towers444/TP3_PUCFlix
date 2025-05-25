# 📺 TP_PUCFlix

O projeto PUCFlix é um cadastro de séries, episódios e atores, com uma interface textual para mostrar os registros. O nosso projeto é uma implementação simples de um Banco de Dados sem utilizar um SGBD, contendo todas as operações de CRUD (Create, Read, Update e Delete) para as três entidades, garantindo seus relacionamentos.

Sobre o nosso grupo: [Membros!](#%EF%B8%8F-autores)

# ‼️ Checklist

+ As operações de inclusão, busca, alteração e exclusão de atores estão implementadas e funcionando corretamente? ✅
+ O relacionamento entre séries e atores foi implementado com árvores B+ e funciona corretamente, assegurando a consistência entre as duas entidades? ✅
+ É possível consultar quais são os atores de uma série? ✅
+ É posssível consultar quais são as séries de um ator? ✅
+ A remoção de séries remove os seus vínculos de atores? ✅
+ A inclusão de um ator em uma série em um episódio se limita aos atores existentes? ✅
+ A remoção de um ator checa se há alguma série vinculado a ele?✅
+ O trabalho está funcionando corretamente? ✅
+ O trabalho está completo? ✅
+ O trabalho é original e não a cópia de um trabalho de outro grupo? ✅

# 📦 Classes do sistema

Uma breve descrição de cada classe do Sistema, organizado semelhante à pastas de arquivos do projeto. Cada Classe com a descrição de todas as funções e atributos

### Principal

Classe principal do projeto, possuindo apenas duas funções: main e povoar. A função povoar realiza o povoar() de cada visão. Já a função main() cria a primeira interface com, atualmente, 5 opções: as três primeiras opções exibem a visão da classe escolhida, a quarta executa o povoar() e a última encerra o programa. Por fim, o único atributo dessa classe é a String "versão", que tem o formato "x.x", em que x é um dígito. Neste caso, a versão criada é a "2.0".

## Entidades

### Serie

Classe para criar a Entidade Serie. Implementa a interface EntidadeArquivo

#### Atributos

+ ID (int)
+ nome (String)
+ anoLancamento (int -> 4 dígitos)
+ sinopse (String)
+ streaming (String)
+ nota (int -> 0:10)
+ criadores (ArrayList de String)
+ genero (String)

#### Funções

+ Set e Get de todos os atributos
+ Construtor com todos os atributos (Nesse método, deve haver a validação das entradas, permitindo valores vazios convencionados).
+ Construtor com todos os atributos, exceto o ID. Nesse caso, considerar o seu valor como -1.
+ Construtor com parâmetros vazios. ID = -1, String = "", Date = Data atual, Float = 0.0.
+ String toString(): Formato tabelado, com uma linha para cada atributo, com o nome “completo” e gramaticalmente correto. Para a formação da tabela, preencher com pontos (‘.’) até o atributo com maior nome. Começar cada linha com ‘| ’ e terminar com ‘ |’, preenchendo com espaços em branco para manter as | alinhadas. Além disso, no começo e no final da função, colocar a sequência de (“+---+”) englobando todos as informações. Ex.:
```
+--------------------------+
| ID...............: 1     |
| Nome.............: XXXXX |
| Temporada........: 2     |
| Ano de lançamento: 2020  |
|            ...           |
+--------------------------+
```
+ byte [] toByteArray(): Função para retornar um arranjo de bytes dado uma instância da classe, registrando os atributos na mesma ordem dos atributos.
+ void fromByteArray(byte[]): Função para preencher um objeto com dados vindos de um arranjo de bytes.

### Episodio

Classe para criar a Entidade Episódio. Implementa a interface EntidadeArquivo

#### Atributos

+ ID (int)
+ IDSerie (int)
+ nome (String)
+ temporada (byte)
+ dataLancamento (LocalDate)
+ duracao (float) (em minutos)
+ nota (byte -> 0:10)
+ diretores (ArrayList de String)

#### Funções
+ Set e Get de todos os atributos
+ Construtor com todos os atributos (Nesse método, deve haver a validação das entradas, permitindo valores vazios convencionados).
+ Construtor com todos os atributos, exceto o ID. Nesse caso, considerar o seu valor como -1.
+ Construtor com parâmetros vazios. ID = -1, String = "", Date = Data atual, Float = 0.0.
+ String toString(): Formato tabelado, com uma linha para cada atributo, com o nome “completo” e gramaticalmente correto. Para a formação da tabela, preencher com pontos (‘.’) até o atributo com maior nome. Começar cada linha com ‘| ’ e terminar com ‘ |’, preenchendo com espaços em branco para manter as | alinhadas. Além disso, no começo e no final da função, colocar a sequência de (“+---+”) englobando todos as informações. Ex.:
```
+---------------------------------+
| ID................: 1           |
| Nome..............: XXXXX       |
| Temporada.........: 2           |
| Data de lançamento: 11/11/2021  |
|            ...                  |
+---------------------------------+
```
+ byte [] toByteArray(): Função para retornar um arranjo de bytes dado uma instância da classe, registrando os atributos na mesma ordem dos atributos.
+ void fromByteArray(byte[]): Função para preencher um objeto com dados vindos de um arranjo de bytes.

### Ator

Classe para criar a Entidade Ator. Implementa a interface EntidadeArquivo

#### Atributos

+ ID (int)
+ nome (String)
+ genero (char)
+ dataNascimento (LocalDate)
+ nacionalidade (String)

#### Funções

+ Set e Get de todos os atributos
+ Contrutor com todos os parâmetros (Nesse método, deve haver a validação das entradas, permitindo valores vazios convencionados)
+ Construtor com todos os parâmetros, exceto o ID (Executa o construtor completo com ID = -1)
+ Construtor com todos os parâmetros vazios (Executa o construtor completo com os valores vazios convencionados: ID = -1, String = "", char = 'I' (indeterminado), LocalDate = data atual)
+ String toString(): Formato tabelado, com uma linha para cada atributo, com o nome “completo” e gramaticalmente correto. Para a formação da tabela, preencher com pontos (‘.’) até o atributo com maior nome. Começar cada linha com ‘| ’ e terminar com ‘ |’, preenchendo com espaços em branco para manter as | alinhadas. Além disso, no começo e no final da função, colocar a sequência de (“+---+”) englobando todos as informações. Ex.:
```
+---------------------------+
| ID................: 1     |
| Nome..............: XXXXX |
| Gênero............: 'M'   |
| Data de Nascimento: 2020  |
|            ...            |
+---------------------------+
```
+ byte [] toByteArray(): Função para retornar um arranjo de bytes dado uma instância da classe, registrando os atributos na mesma ordem dos atributos.
+ void fromByteArray(byte[]): Função para preencher um objeto com dados vindos de um arranjo de bytes.

### Atuação

Classe para criar a Entidade Atuação. Implementa a interface EntidadeArquivo

#### Atributos

+ IDAtuacao (int)
+ IDAtor (int)
+ IDSerie (int)
+ personagem (String)

#### Funções

+ Set e Get de todos os atributos
+ Construtor com todos os parâmetros (Nesse método, deve haver a validação das entradas, permitindo valores vazios convencionados)
+ Construtor com todos os parâmetros, exceto o IDAtuacao (Executa o construtor completo com o IDAtuacao = -1)
+ Construtor com todos os parâmetros vazios (Executa o construtor vazio completo com os valores vazios convencionados: IDs = -1, String = "")
+ String toString(): Formato tabelado, com uma linha para cada atributo, com o nome “completo” e gramaticalmente correto. Para a formação da tabela, preencher com pontos (‘.’) até o atributo com maior nome. Começar cada linha com ‘| ’ e terminar com ‘ |’, preenchendo com espaços em branco para manter as | alinhadas. Além disso, no começo e no final da função, colocar a sequência de (“+---+”) englobando todos as informações. Ex.:
```
+-------------------+
| IDAtuação.: 1     |
| IDAtor....: XXXXX |
| IDSérie...: 2     |
| Personagem: 2020  |
|        ...        |
+-------------------+
```
+ byte [] toByteArray(): Função para retornar um arranjo de bytes dado uma instância da classe, registrando os atributos na mesma ordem dos atributos.
+ void fromByteArray(byte[]): Função para preencher um objeto com dados vindos de um arranjo de bytes.

## Modelo

Classes para o tratamento dos arquivos, como expecializações da classe Arquivo e Classes de Pares para o uso dos índices

### ArquivoSerie extends Arquivo<Serie>

#### Atributos

+ ArvoreBMais<ParNomeId> indiceNome;
+ HashExtensivel <ParIDID> indiceSerieEpisodio;

#### Funções

+ Contrutor: Inicializa atributos com super() e cria instâncias do indiceNome e do indiceSerieEpisodio (Se o Hash já existir, não sobrescrever)
+ Serie read(int id): Função herdada da classe Arquivo, não sendo necessária a sua implementação novamente
+ int create(Serie s): Override da função herdada da classe Arquivo, adicionando a nova entidade aos índices
+ boolean delete(int id): Override da função herdada da classe Arquivo, testando se a exclusão é válida (Série existe no BD e não possui Episódios vinculados a ela) e excluindo os índices
+ boolean update(Serie novaSerie): Override da função herdada da classe Arquivo, atualizando o indiceNome
+ Episodio[] readEpisodios(int id): Função que retorna todos os Episódios da Serie. Utilizar nova instância ArquivoEpisodios para isso. 
+ Serie[] readNome(String nome): Função que retorna todas as Séries cujo nome começa com a string especificada.

### ArquivoEpisodio extends Arquivo<Episodio>

#### Atributos

+ ArvoreBMais<ParNomeId> indiceNome;
+ HashExtensivel<ParIdId> indiceEpisodioSerie;

#### Funções

+ Contrutor: Inicializa atributos com super() e cria instâncias do indiceNome e do indiceEpisodioSerie (Se o Hash já existir, não sobrescrever)
+ Serie read(int id): Função herdada da classe Arquivo, não sendo necessária a sua implementação novamente
+ int create(Episodio e): Override da função herdada da classe Arquivo, adicionando a nova entidade aos índices
+ boolean delete(int id): Override da função herdada da classe Arquivo, excluindo os índices
+ boolean update(Episodio novoEpisodio): Override da função herdada da classe Arquivo, atualizando os índices
+ Episodio[] readNome(String nome): Função que retorna todas os Episódios cujo nome começa com a string especificada.

### ArquivoAtor extends Arquivo<Ator>

#### Atributos

+ ArvoreBMais<ParNomeId> indiceNome;
+ ArvoreBMais<ParIDID> indiceAtuacaoSerie;
+ ArvoreBMais<ParIDID> indiceAtuacaoAtor;

#### Funções

+ Contrutor: Inicializa atributos com super() e cria instâncias de indiceNome, indiceAtuacaoSerie e indiceAtuacaoAtor 
+ Serie read(int id): Função herdada da classe Arquivo, não sendo necessária a sua implementação novamente
+ int create(Ator a): Override da função herdada da classe Arquivo, adicionando a nova entidade aos índices
+ boolean delete(int id): Override da função herdada da classe Arquivo, testando se a exclusão é válida (Ator existe no BD e não possui Atuações vinculadas a ele) e excluindo os índices
+ boolean update(Ator novoAtor): Override da função herdada da classe Arquivo, atualizando os índices
+ Ator[] readNome(String nome): Função que retorna todas os Atores cujo nome começa com a string especificada.

### ArquivoAtuacao extends Arquivo<Atuacao>

#### Atributos

+ ArvoreBMais<ParNomeId> indiceNome;
+ ArvoreBMais<ParIDID> indiceAtuacaoSerie;
+ ArvoreBMais<ParIDID> indiceAtuacaoAtor;

#### Funções

+ Contrutor: Inicializa atributos com super() e cria instâncias indiceNome, indiceAtuacaoSerie e indiceAtuacaoAtor 
+ Serie read(int id): Função herdada da classe Arquivo, não sendo necessária a sua implementação novamente
+ int create(Atuacao a): Override da função herdada da classe Arquivo, adicionando a nova entidade aos índices
+ boolean delete(int id): Override da função herdada da classe Arquivo, testando se a exclusão é válida (Atuação existe no BD e não possui Séries ou Atores vinculados a ela) e excluindo os índices
+ boolean update(Atuacao novaAtuacao): Override da função herdada da classe Arquivo, atualizando os índices
+ List<Atuacao> readPersonagem(String nome): Função que retorna todas as atuações cujo nome do personagem inicia com a string especificada.
+ List<Atuacao> readSerie(int IDSerie): Função que retorna todas as Atuações de uma Série.
+ List<Atuacao> readAtor(int IDAtor): Função que retorna todas as Atuações de um Ator.

### ParIDID implements RegistroArvoreBMais <ParIDID>

#### Atributos

+ int ID;
+ int ID_Dependente;
+ short TAMANHO = 8;
  
#### Funções

+ Construtores -> Um sem parâmetros e outro com ID e ID_Dependente
+ Get e Set de ID e ID_Dependente
+ size(): Retorna o tamanho do registro
+ toByteArray(): Converte o objeto para um array de bytes
+ fromByteArray(byte[] ba): Preenche o objeto a partir de um array de bytes
+ compareTo(ParIDID obj): Compara este objeto com outro ParIDID baseado no ID do Episódio
+ clone(): Retorna uma cópia do objeto


### ParNomeID implements RegistroArvoreBMais <ParNomeID>

#### Atributos

+ String nome
+ int ID
+ short TAMANHO = 34;
+ short TAMANHO_NOME = 30;

#### Funções

+ Construtores -> Um sem parâmetros e outro com String nome e int ID
+ get de ID e Nome
+ size( ): Retorna o tamanho do registro
+ toByteArray(): Converte o objeto para um array de bytes
+ fromByteArray(byte[] ba): Preenche o objeto a partir de um array de bytes
+ compareTo(ParNomeID obj): Compara este objeto com outro ParNomeID baseado no nome
+ clone(): Retorna uma cópia do objeto
+ toString(): Método para fazer uma versão de String do ParNomeID
+ transforma(): Função para normalizar as strings, deixando minúsculas e sem espaço

## Controle

Classes que realiza a mediação entre as operações dos arquivos e a entrada de dados da Visão

### ControleSerie

#### Atributos

+ ArquivoSerie arqSerie

#### Funções

+ Construtor: Cria a instância de arqSerie.
+ int incluirSerie(Serie serie): Função para inserie Série utilizando os métodos de ArquivoSerie.
+ boolean excluirSerie(int id): Função para excluir serie por ID. Testar antes de deletar se tem algum episodio dessa serie usando ControleEpisodio.verificarEpisodiosSerie(). Excluir também os vínculos de Atuação da Série excluída.
+ boolean alterarSerie (Serie s): Função para alterar algum valor da Serie, usar parâmetro para representar objeto a ser alterado.
+ Serie buscarSerie(int id): Função que recebe um ID e retorna um Objeto Serie.
+ List<Serie> buscarSerie(String entrada): Função que retorna uma ou mais séries que contém a sequência inserida. Ex.: Entrada: “F” -> retorna [Fullmetal Alchemist, Friends].
+ List<Episodio> buscarSerieEpisodios(int id): Função para buscar todos os Episódios de uma determinada Série.
+ List<Episodio> buscarSerieEpisodiosPorTemporada(int id, int temporada): Função para buscar todos os Episódios de uma Série de uma determinada Temporada.
+ boolean validarSerie(int id): Função estática que recebe um id de Série como parâmetro e retorna True ou False de acordo com sua existência válida no banco de dados. Para a leitura do objeto, instanciar um novo ArquivoSerie e usar o seu read com o id.
+ void povoar(): Primeiro carregamento de dados para o sistema.

### ControleEpisodio

#### Atributos

+ ArquivoEpisodio arqEpisodio
+ Serie serie

#### Funções

+ Construtor: Cria a instância de arqEpisodio sem a Série.
+ Construtor: Pede uma Série válida como parâmetro e cria a instância de arqEpisodio.
+ int incluirEpisodio(Episodio e): Função para inserir Episódio utilizando os métodos de ArquivoEpisodio.
+ boolean excluirEpisodio(int id): Função para excluir Episodio por ID. Testar se o episódio é válido para remoção (existe no BD e o ID pertence a série).
+ boolean excluirEpisodio(Episódio e): Função para excluir Episodio dado um objeto Episódio. Testar se o episódio é válido para remoção (existe no BD e o ID pertence à série).
+ boolean excluirEpisodio(int id, int temp): Função para excluir Episodio por ID de uma temporada. Testar se o episódio é válido para remoção (existe no BD, pertence à série e está na temporada especificada).
+ boolean alterarEpisodio(Episodio e): Função para alterar algum valor da Episodio.
+ List<Episodio> buscarEpisodio(): Função que retorna todos os episódios da série correspondente à instância da classe de Controle.
+ Episodio buscarEpisodio(int id): Função que busca um objeto Episódio pelo ID e retorna caso esteja na série.
+ List<Episodio> buscarEpisodio(String entrada): Função que lê um nome e retorna um episódio que contém a sequência inserida que está na série especificada.
+ List<Episodio> buscarEpisodioTemporada(int temp): Função que retorna uma lista de episódios que estão na sérieAtual e presentes na temporada temp.
+ Episodio buscarEpisodio(int id, int temp): Função que busca um objeto Episódio pelo ID e retorna caso esteja na série e na temporada.
+ boolean verificarEpisodiosSerie(int idSerie): Função estática que, com um ID de Série, retorna verdadeiro ou falso se tiver um ou mais episódios atrelados a essa série.
+ void povoar(): Primeiro carregamento de dados para o sistema.

### ControleAtor

#### Atributos

+ ArquivoAtor arqAtor

#### Funções

+ Construtor: Cria a instância de arqAtor.
+ int incluirAtor(Ator a): Função para inserir Ator utilizando os métodos de ArquivoAtor.
+ boolean excluirAtor(int id): Função para excluir Ator por ID. Testar se o episódio é válido para remoção (existe no BD e não tem nenhuma Atuação).
+ boolean excluirAtor(Ator a): Função para excluir Ator dado um objeto Ator. Testar se o Ator é válido para remoção (existe no BD e não pertence a nenhuma Série).
+ boolean alterarAtor(Ator novoAtor): Função para alterar algum valor do Ator, usando parâmetro para representar objeto a ser alterado.
+ Ator buscarAtor(int id): Função que recebe um ID e retorna um Objeto Ator.
+ List<Ator> buscarAtor(String entrada): Função que retorna um ou mais Atores cujo nome inicia com a String de entrada.
+ List<Atuacao> buscarAtuacaoAtor(int idAtor): Função para buscar todas as Atuações de um determinado Ator. Para isso, utiliza-se a função ArquivoAtuacao.readAtor().
+ boolean validarAtor(int idAtor): Função estática que recebe um id de Ator como parâmetro e retorna True ou False de acordo com sua existência válida no banco de dados. Para a leitura do objeto, instanciar um novo ArquivoAtor e usar o seu read com o id.
+ void povoar(): Primeiro carregamento de dados para o sistema.

### ControleAtuacao

#### Atributos

+ ArquivoAtuacao arqAtuacao

#### Funções

+ Construtor: Cria a instância de arqAtuacao.
+ int incluirAtuacao(Atuacao a): Função para inserir Atuação utilizando os métodos de ArquivoAtuacao .
+ boolean excluirAtuacao(int id): Função para excluir Atuação por ID. Testar se o Episódio é válido para remoção (existe no BD e não se relaciona com nenhuma Série e nenhum Ator).
+ boolean excluirAtuacao(Atuacao a): Função para excluir Atuação dado um objeto Atuacao. Testar se a Atuação é válida para remoção (existe no BD e não se relaciona com nehuma Série e nenhum Ator).
+ boolean excluirAtuacaoSerie(int IDSerie): Função para excluir todas as Atuações de uma Série.
+ boolean alterarAtuacao(Atuacao novaAtuacao): Função para alterar algum valor de Atuação.
+ Atuacao buscarAtuacao(int id): Função que busca um objeto Atuação pelo ID.
+ List<Atuacao> buscarAtuacao(String entrada): Função que lê um nome e retorna uma lista de Atuações cujo nome do Personagem começa com a String de entrada.
+ List<Atuacao> buscarAtuacaoAtor(int idAtor): Função que retorna uma lista de Atuações de um determinado Ator por meio de ArquivoAtuacao.readAtor(idAtor).
+ List<Atuacao> buscarAtuacaoSerie(int idSerie): Função que retorna uma lista de Atuações de uma determinada Série por meio de ArquivoAtuacao.readSerie(idSerie).
+ boolean verificarAtuacaoAtor(int idAtor): Função estática que, com um ID de Atuacao, retorna verdadeiro ou falso se tiver um ou mais Atores atrelados a essa Atuação.
+ boolean verificarAtuacaoSerie(int idAtuacao): Função estática que, com um ID de Atuacao, retorna verdadeiro ou falso se tiver uma ou mais Séries atreladas a essa Atuação.
+ void povoar(): Primeiro carregamento de dados para o sistema.

## Visao

Classes para fazer o controle de Entradas e Saídas do Sistema para cada Entidade

### VisaoSerie

#### Atributos

+ private static Scanner console = new Scanner(System.in);
+ ControleSerie controleSerie;

#### Funções

+ Construtor único criando a instância de ControleSerie.
+ menu(): Função que cria um menu, pede uma entrada de dados enquanto for diferente de 0. 
```
PucFlix v(versao)
--------------------------
> Início > Séries

1 - Incluir Série 
2 - Excluir Série
3 - Alterar Série
4 - Buscar Série
5 - Buscar Episódios
6 - Editar Elenco
0 - Sair
```
+ void incluirSerie(): Função para chamar ControleSerie.incluirSerie(), passando o resultado de lerSerie como parâmetro. Mostrar mensagem de sucesso ou falha.
+ void alterarSerie(): Chamar buscarUmaSerie() para encontrar uma Série existente, depois passar o resultado como parâmetro para lerSerie(serieAntiga). Em seguida, utilizar retorno como parâmetro em ControleSerie.alterarSerie(). Mostrar mensagem de sucesso ou falha.
+ void excluirSerie(): Função para chamar ControleSerie.excluirSerie(). Chamar buscarUmaSerie() para encontrar uma Série existente e utilizar retorno como parâmetro em ControleSerie.excluirSerie(). Mostrar mensagem de sucesso ou falha.
+ Serie lerSerie(): Função para ler uma entrada de dados com suas devidas verificações e gerar um objeto da Classe Serie e retorná-lo. Caso objeto não seja válido, levantar exceção. O questionário deve ter seguinte forma:
“Qual o/a (atributo) (tipo do atributo e/ou regras/formato)? ” para cada atributo. Ao final, pedir uma confirmação com (S/N).
+ Serie lerSerie(Serie antiga): Funcionamento parecido com o lerSerie sem parâmetro, porém caso o usuário aperte Enter em um atributo ele terá o mesmo valor que o atributo da Serie antiga. Usado no método de alterarSerie().
+ Serie buscarUmaSerie(): Primeiro, buscar Séries pelo nome com buscarSerieNome(). Em seguida, criar um menu de seleção para escolher e mostrar apenas um.
+ List<Serie> buscarSerieNome(): Ler uma String do usuário e passar como parâmetro para ControleSerie.buscarSerie(String entrada). O objeto retornado é uma Lista, podendo conter elementos ou não.
+ void buscarEpisodios(): Função para buscar todos os Episódios de uma Série, listá-los e mostrar o escolhido pelo usuário.
+ void mostrarSerie(Serie s): Chama o toString da classe Serie

### VisaoEpisodio

#### Atributos

+ private static Scanner console = new Scanner(System.in);
+ Serie serie -> representa a série atual
+ int temp -> representa a temporada atual
+ ControleEpisodio controle

#### Funções

+ Contrutor passando uma série como parâmetro e outro vazio.
+ void menu(): Função que cria um menu, pede uma entrada de dados enquanto for diferente de 0. Em primeira instância, deve ser feita a escolha de qual série será trabalhada, depois, para cada valor entre 1 e 5, realizar certas funções de CRUD, opções como excluir deve chamar função visaoEpisodio.excluir() que faz a escolha entre excluir por nome ou por ID. (Lembrete: sempre quando voltar, zerar a serieAtual e temporadaAtual)Estética:
1 - Incluir Episódio
2 - Excluir Episódio
3 - Alterar Episódio
4 - Buscar Um Episódio
5 - Buscar Todos os Episódios
6 - Escolher Temporada
0 - Sair
```
PucFlix v
--------------------------
> Início > Episódios > "Nome da série"
1 - Incluir Episódio
2 - Excluir Episódio
3 - Alterar Episódio
4 - Buscar Um Episódio
5 - Buscar Todos os Episódios
6 - Escolher Temporada
0 - Sair
```
+ void menuTemporada( ): Função para controlar as opções da tela 'Início > Episódios > Nome da Série > Temporada x'
```
PUCFlix v
--------------------------
> Início > Episódios > "Nome da Série" > Temporada X

1 - Incluir Episódio
2 - Excluir Episódio
3 - Alterar Episódio
4 - Buscar Um Episódio
5 - Buscar Todos os Episódios
0 - Sair
```
+ void entrarTemporada(): Primeiramente, pede uma entrada de dados de um int temporada. Realiza a verificação se é uma temporada válida e mostra um submenu com todas as opções anteriores. Atualizar a variavel temporadaAtual e chamar menuTemporada(). 
+ void incluirEpisodio(): Chama a função ControleEpisodio.incluirEpisodio() passando o resultado de lerSerie como parâmetro. Mostrar mensagem de sucesso ou falha.
+ void alterarEpisodio(): Chamar buscarUmEpisodio() e utilizar retorno como parâmetro em ControleEpisodio.alterarEpisodio(). Mostrar mensagem de sucesso ou falha.
+ void excluirEpisodio( ): Chamar buscarUmEpisodio() e utilizar retorno como parâmetro em ControleEpisodio.excluirEpisodio(). Mostrar mensagem de sucesso ou falha.
+ Episodio lerEpisodio(): Função para ler uma entrada de dados com suas devidas verificações e gerar um objeto da Classe Episodio e retorná-lo. Caso objeto não seja válido, levantar exceção. Caso valor de temporadaAtual seja diferente de 0, ele será o valor atribuido à variável temporada do episódio. Caso contrário, perguntar a temporada no questionário. O questioário deve ter seguinte forma:
“Qual o/a (atributo) (tipo do atributo e/ou regras/formato)? ” para cada atributo. Ao final, pedir uma confirmação com (S/N).
+ Episodio lerEpisodio(Episodio antigo): Funcionamento parecido com o lerEpisodio sem parâmetro, porém caso o usuário aperte Enter em um atributo ele terá o mesmo valor que o atributo do Episodio antigo. Usado no método de alterarEpisodio().
+ Episodio buscarUmEpisodio(): Primeiro, buscar Episódios pelo nome com buscarEpisodioNome() e filtrar pela Série e pela Temporada (se estiver no MenuTemporada). Em seguida, criar um menu de seleção para escolher e mostrar apenas um.
+ void buscarEpisodios(): Mostrar todos os Episódios da Série selecionada e criar um menu de seleção para escolher e mostrar apenas um.
+ buscarUmEpisodioTemporada(int temporada): Primeiro, buscar Episódios pelo nome com buscarEpisodioNome() e filtrar pela Série e pela Temporada (se estiver no MenuTemporada). Em seguida, criar um menu de seleção para escolher e mostrar apenas um.
+ void buscarEpisodiosTemporada(int temporada): Mostrar todos os Episódios de uma temporada da Série selecionada e criar um menu de seleção para escolher e mostrar apenas um.
+ List<Episodio> buscarEpisodioNome(): Buscar todos os Episódios cujo nome se inicia com uma String lida do usuário.
+ void mostrarEpisodio(Episodio e): Chama o toString da classe Episodio

### VisaoAtor

#### Atributos

+ private static Scanner console = new Scanner(System.in);
+ ControleAtor controleAtor;

#### Funções

+ menu(): Função que cria um menu, pede uma entrada de dados enquanto for diferente de 0. Para cada valor entre 1 e 5, realizar certas funções de CRUD. Estética:
```
PucFlix v(versao)
--------------------------
> Início > Atores
1 - Incluir Ator
2 - Excluir Ator
3 - Alterar Ator
4 - Buscar Um Ator
5 - Buscar Atuações de Um Ator
0 - Sair
```
+ void incluirAtor(): Função para chamar ControleAtor.incluirAtor(), passando o resultado de lerAtor como parâmetro. Mostrar mensagem de sucesso ou falha.
+ void alterarAtor(): Função para chamar ControleAtor.alterarAtor(). Chamar buscarUmAtor() para encontrar um Ator existente, depois passar o resultado como parâmetro para lerAtor(atorAntigo). Depois utilizar retorno como parâmetro em ControleAtor.alterarAtor(). Mostrar mensagem de sucesso ou falha.
+ void excluirAtor(): Função para chamar ControleAtor.excluirAtor(). Chamar buscarUmAtor() para encontrar um Ator existente e utilizar retorno como parâmetro em ControleAtor.excluirAtor(). Mostrar mensagem de sucesso ou falha.
+ Ator lerAtor(): Função para ler uma entrada de dados com suas devidas verificações e gerar um objeto da Classe Ator e retorná-lo. Caso objeto não seja válido, levantar exceção. O questionário deve ter seguinte forma:
“Qual o/a (atributo) (tipo do atributo e/ou regras/formato)? ” para cada atributo. Ao final, pedir uma confirmação com (S/N).
+ Ator lerAtor(Ator antigo): Funcionamento parecido com o lerAtor sem parâmetro, porém caso o usuário aperte Enter em um atributo ele terá o mesmo valor que o atributo do Ator antigo. Usado no método de alterarAtor().
+ Ator buscarUmAtor(): Primeiro, buscar Atores pelo nome com buscarAtorNome(). Em seguida, criar um menu de seleção para escolher e mostrar apenas um.
+ void buscarAtuacoes(): Primeiro, buscar Atuações pelo buscarAtuacaoAtor(). Em seguida, criar um menu de seleção para escolehr e mostrar apenas um.
+ List<Ator> buscarAtorNome(): Ler uma String do usuário e passar como parâmetro para ControleAtor.buscarAtor(String entrada). O objeto retornado é uma Lista, podendo conter elementos ou não.
+ void buscarAtuacoes(): Ler um Ator com lerAtor() e buscar todas as Atuações vinculadas a ele com ControleAtuacao.buscarAtuacaoAtor(). Criar um menu de seleção e mostrar a escolhida pelo usuário.
+ mostrarAtor(Ator a): Chama o toString da classe Ator.

### VisaoAtuacao

#### Atributos

+ private static Scanner console = new Scanner(System.in);
+ ControleAtuacao controleAtuacao;

#### Funções

+ Construtor único criando a instância de ControleSerie.
+ menu(): Função que cria um menu, pede uma entrada de dados enquanto for diferente de 0. 
```
PucFlix v(versao)
--------------------------
> Início > Séries > Elenco

1 - Incluir Atuação
2 - Excluir Atuação
3 - Alterar Atuação
4 - Buscar Atuação
5 - Buscar Atuações de Uma Série
0 - Sair
```
+ void incluirAtuacao(): Função para chamar ControleAtuacao.incluirAtuacao(), passando o resultado de lerAtuacao como parâmetro. Mostrar mensagem de sucesso ou falha.
+ void excluirAtuacao(): Função para chamar ControleAtuacao.excluirAtuacao(). Chamar buscarUmaAtuacao() para encontrar uma Atuacao existente e utilizar retorno como parâmetro em Controleatuacao.excluirAtuacao(). Mostrar mensagem de sucesso ou falha.
+ void alterarAtuacao(): Chamar buscarUmaAtuacao() para encontrar uma Atuacao existente, depois passar o resultado como parâmetro para lerAtuacao(atuacaoAntiga). Em seguida, utilizar retorno como parâmetro em ControleAtuacao.alterarAtuacao(). Mostrar mensagem de sucesso ou falha.
+ Atuacao lerAtuacao(): Função para ler uma entrada de dados com suas devidas verificações e gerar um objeto da Classe Atuacao e retorná-lo. Caso objeto não seja válido, levantar exceção. O questionário deve ter seguinte forma:
“Qual o/a (atributo) (tipo do atributo e/ou regras/formato)? ” para cada atributo. Ao final, pedir uma confirmação com (S/N).
+ Atuacao lerAtuacao(Atuacao antiga): Funcionamento parecido com o lerAtuacao sem parâmetro, porém caso o usuário aperte Enter em um atributo ele terá o mesmo valor que o atributo da Atuacao antiga. Usado no método de alterarAtuacao().
+ Atuacao buscarUmaAtuacao(): Primeiro, buscar Atuações pelo nome com buscarAtuacaoNome(). Em seguida, criar um menu de seleção para escolher e mostrar apenas um.
+ List<Atuacao> buscarAtuacaoNome(): Ler uma String do usuário e passar como parâmetro para ControleAtuacao.buscarAtuacao(String entrada). O objeto retornado é uma Lista, podendo conter elementos ou não.
+ void buscarAtuacoesSerie(): Ler uma Série com VisaoSerie.buscarUmaSerie() e buscar todas as Atuações vinculadas a ela com ControleAtuacao.buscarAtuacaoSerie(). Criar um menu de seleção e mostrar a escolhida pelo usuário.
+ void mostrarAtuacao(Atuacao a): Chama o toString da classe Atuacao

# Experiências Individuais

## Augusto

<div align="justify">
</div>

## Lucas

<div align="justify">
    O TP2 contribuiu significativamente para o meu entendimento acerca da implementação física de relacionamentos N:N. Nesse sentido, pude explorar diferentes formas de implementar relacionamentos tão relevantes - seja com dois índices indiretos com Árvores B+ com os identificadores únicos, seja com a criação de uma nova entidade intermediária. Além disso, planejar as entregas e distribuir as responsabilidades dos meus colegas mostrou-se ser um desafio maior do que esperado, o que com certeza agrega à minha formação como programador e como colega de trabalho.
</div>

## João

<div align="justify">
    A realização do TP2 se mostrou muito importante para o meu desenvolvimento, a fim de compreender como funciona melhor o tratamento e armazenamento de dados em memória secundária. O processo requeriu estudos para ter melhor compreensão do conteúdo e foi necessário o uso de estruturas como Arvore B+ e Tabela Hash Extensível para organizar as informações de um banco de dados de séries, com seus respectivos episódios e atuações. No geral, demonstrou-se ser um processo tranquilo, graças ao apoio dos meus colegas de equipe e o conteúdo promovido dentro de sala de aula.  
</div>

# Planos futuros

# Materiais adicionais

Para o planejamento inicial, utilizamos o [google docs](https://docs.google.com/document/d/1C75ZqCawQ5OhWpXl3aonh7J_1SdRE6VenWqyXlGX72c/edit?tab=t.0), definindo regras gerais do processo, datas de reunião e datas de entrega.

# ✒️ Autores
+ *Augusto Stambassi Duarte* - Project Manager 🧑‍💼 - [Git Pessoal](https://github.com/stambassi)
+ *Lucas Carneiro Nassau Malta* - Desenvolvedor 👨‍💻 - [Git Pessoal](https://github.com/lucascarneiro1202)
+ *João Pedro Torres* - Desenvolvedor 👨‍💻 - [Git Pessoal](https://github.com/Towers444)
