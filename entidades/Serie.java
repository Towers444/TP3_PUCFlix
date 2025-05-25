package entidades;

import java.io.*;
import java.util.ArrayList;

import aeds3.EntidadeArquivo;

public class Serie implements EntidadeArquivo {
    private int ID;
    private String nome;
    private int anoLancamento;
    private String sinopse;
    private String streaming;
    private int nota;
    private ArrayList<String> criadores;
    private String genero;

    /**
     * Construtor padrão: inicializa os atributos com valores padrão
     */
    public Serie() {
        this(-1, "", 0, "", "", 0, new ArrayList<String>(), "");
    }

    /**
     * Construtor sem ID: define o ID como -1 e chama o construtor principal
     */
    public Serie(String nome, int anoLancamento, String sinopse, String streaming, int nota, ArrayList<String> criadores, String genero) {
        this(-1, nome, anoLancamento, sinopse, streaming, nota, criadores, genero);
    }

    /**
     * Construtor principal: inicializa todos os atributos e valida os valores
     */
    public Serie(int ID, String nome, int anoLancamento, String sinopse, String streaming, int nota, ArrayList<String> criadores, String genero) {
        this.ID = ID;
        this.nome = nome;
        this.anoLancamento = (anoLancamento >= 1000 && anoLancamento <= 9999) ? anoLancamento : 0;
        this.sinopse = sinopse;
        this.streaming = streaming;
        this.nota = (nota >= 0 && nota <= 10) ? nota : 0;
        this.criadores = criadores;
        this.genero = genero;
    }

    // Métodos getters e setters
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getAnoLancamento() { return anoLancamento; }
    public void setAnoLancamento(int anoLancamento) { this.anoLancamento = (anoLancamento >= 1000 && anoLancamento <= 9999) ? anoLancamento : this.anoLancamento; }

    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }

    public String getStreaming() { return streaming; }
    public void setStreaming(String streaming) { this.streaming = streaming; }

    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = (nota >= 0 && nota <= 10) ? nota : this.nota; }

    public ArrayList<String> getCriadores() { return criadores; }
    public void setCriadores(ArrayList<String> criadores) { this.criadores = criadores; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    /**
     * Método toString: retorna a representação formatada da série
     */
    public String toString() {
        // Definir dados
        StringBuilder str = new StringBuilder();
        int limite = 120;
        String sinopseTrim = sinopse;

        // Encontrando o maior comprimento dos valores para alinhar
        int maxLength = Math.max(20, sinopse.length()); 

        // Testar se a sinopse é grande demais
        if (maxLength > limite) {
            maxLength = limite;
        }

        // Testar se a sinopse ultrapassa o limite
        if (sinopse.length() > limite) {
            sinopseTrim = sinopse.substring(0, limite - 3) + "...";
        }

        // Cabeçalho
        str.append("\n+" + "-".repeat(maxLength + 21) + "+\n");

        // Conteúdo formatado
        str.append(String.format("| ID...............: %-" + maxLength + "d |\n", ID));
        str.append(String.format("| Nome.............: %-" + maxLength + "s |\n", nome));
        str.append(String.format("| Ano de Lançamento: %-" + maxLength + "d |\n", anoLancamento));
        str.append(String.format("| Sinopse..........: %-" + maxLength + "s |\n", sinopseTrim));
        str.append(String.format("| Streaming........: %-" + maxLength + "s |\n", streaming));
        str.append(String.format("| Nota.............: %-" + maxLength + "d |\n", nota));
        str.append(String.format("| Gênero...........: %-" + maxLength + "s |\n", genero));


        for (int i = 0; i < criadores.size(); i++) 
            str.append(String.format("| Criador (%d)......: %-"+maxLength+"s |\n", i + 1, criadores.get(i)));

        // Rodapé
        str.append("+" + "-".repeat(maxLength + 21) + "+");

        // Retornar
        return str.toString();
    }

    /**
     * Método para converter a série para um array de bytes
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(ID);
        dos.writeUTF(nome);
        dos.writeInt(anoLancamento);
        dos.writeUTF(sinopse);
        dos.writeUTF(streaming);
        dos.writeInt(nota);
        dos.writeInt(criadores.size());
        for (String criador : criadores) {
            dos.writeUTF(criador);
        }
        dos.writeUTF(genero);

        return baos.toByteArray();
    }

    /**
     * Método para preencher os atributos da série a partir de um array de bytes
     */
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        ID = dis.readInt();
        nome = dis.readUTF();
        anoLancamento = dis.readInt();
        sinopse = dis.readUTF();
        streaming = dis.readUTF();
        nota = dis.readInt();
        
        int tamanhoLista = dis.readInt();
        criadores = new ArrayList<>();
        for (int i = 0; i < tamanhoLista; i++) {
            criadores.add(dis.readUTF());
        }
        
        genero = dis.readUTF();
    }
}
