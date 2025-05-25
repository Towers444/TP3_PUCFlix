package entidades;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import aeds3.EntidadeArquivo;

public class Ator implements EntidadeArquivo {
    private int ID;
    private String nome;
    private char genero;
    private LocalDate dataNascimento;
    private String nacionalidade;

    /**
     * Construtor padrão: inicializa com valores vazios convencionados
     */
    public Ator() {
        this(-1, "", 'I', LocalDate.now(), "");
    }

    /**
     * Construtor sem ID: define ID = -1
     */
    public Ator(String nome, char genero, LocalDate dataNascimento, String nacionalidade) {
        this(-1, nome, genero, dataNascimento, nacionalidade);
    }

    /**
     * Construtor principal: inicializa todos os atributos com validação
     */
    public Ator(int ID, String nome, char genero, LocalDate dataNascimento, String nacionalidade) {
        this.ID = ID;
        this.nome = (nome != null) ? nome : "";
        this.genero = (genero == 'M' || genero == 'F' || genero == 'I') ? genero : 'I';
        this.dataNascimento = (dataNascimento != null) ? dataNascimento : LocalDate.now();
        this.nacionalidade = (nacionalidade != null) ? nacionalidade : "";
    }

    // Getters e Setters
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public char getGenero() { return genero; }
    public void setGenero(char genero) {
        if (genero == 'M' || genero == 'F' || genero == 'I') {
            this.genero = genero;
        }
    }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento != null)
            this.dataNascimento = dataNascimento;
    }

    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

    /**
     * Representação formatada do ator
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int maxLength = Math.max(20, nome.length()); 

        // Cabeçalho
        sb.append("\n+" + "-".repeat(maxLength + 22) + "+\n");
        sb.append(String.format("| ID................: %-" + maxLength + "d |\n", ID));
        sb.append(String.format("| Nome..............: %-" + maxLength + "s |\n", nome));
        sb.append(String.format("| Gênero............: %-" + maxLength + "c |\n", genero));
        sb.append(String.format("| Data de Nascimento: %-" + maxLength + "s |\n", dataNascimento.format(formatter)));
        sb.append(String.format("| Nacionalidade.....: %-" + maxLength + "s |\n", nacionalidade));
        sb.append("+" + "-".repeat(maxLength + 22) + "+\n");

        return sb.toString();
    }

    /**
     * Converte o objeto em um array de bytes
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(ID);
        dos.writeUTF(nome);
        dos.writeChar(genero);
        dos.writeUTF(dataNascimento.toString());
        dos.writeUTF(nacionalidade);

        return baos.toByteArray();
    }

    /**
     * Preenche os atributos do objeto a partir de um array de bytes
     */
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        ID = dis.readInt();
        nome = dis.readUTF();
        genero = dis.readChar();
        dataNascimento = LocalDate.parse(dis.readUTF());
        nacionalidade = dis.readUTF();
    }
}
