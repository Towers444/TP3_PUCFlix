package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

import aeds3.EntidadeArquivo;
import controle.ControleSerie;

public class Episodio implements EntidadeArquivo {

    private int ID;
    private int IDSerie;
    private String nome;
    private byte temporada;
    private LocalDate dataLancamento;
    private float duracao; // em minutos
    private byte nota; // 0 a 10
    private List<String> diretores;

    public Episodio() throws Exception {
        this(-1, -1, "", 0, LocalDate.now(), 0F, -1, null);
    }

    public Episodio(int IDSerie, String nome, int temporada, LocalDate dataLancamento, float duracao, int nota, List<String> diretores) throws Exception {
        this(-1, IDSerie, nome, temporada, dataLancamento, duracao, nota, diretores);
    }

    public Episodio(int ID, int IDSerie, String nome, int temporada, LocalDate dataLancamento, float duracao, int nota, List<String> diretores) throws Exception {
        this.ID = ID;
        if (ControleSerie.validarSerie(IDSerie) || IDSerie == -1)
            this.IDSerie = IDSerie;
        else 
            throw new Exception("IDSérie inválido!");
        this.nome = nome;
        if (temporada >= 0)
            this.temporada = (byte) temporada;
        else
            throw new Exception("Temporada menor que zero!");
        this.dataLancamento = dataLancamento;
        if (duracao >= 0)
            this.duracao = duracao;
        else
            throw new Exception("Duração menor que zero!");    
        if ((0 <= nota && nota <= 10) || (nota == -1))        
            this.nota = (byte) nota;
        else
            throw new Exception("Nota fora do intervalo permitido! (0:10)");
        this.diretores = diretores;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIDSerie() {
        return this.IDSerie;
    }

    public void setIdSerie(int idSerie) {
        this.IDSerie = idSerie;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    } 

    public byte getTemporada() {
        return this.temporada;
    }

    public void setTemporada(byte temporada) {
        this.temporada = temporada;
    }

    public LocalDate getDataLancamento() {
        return this.dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public float getDuracao() {
        return this.duracao;
    }

    public void setDuracao(float duracao) {
        this.duracao = duracao;
    }

    public byte getNota() {
        return this.nota;
    }

    public void setNota(byte nota) {
        this.nota = nota;
    }

    public List<String> getDiretores() {
        return this.diretores;
    }

    public void setDiretores(List<String> diretores) {
        this.diretores = diretores;
    }

    @Override
    public String toString() {
        // Definir dados
        StringBuilder str = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Encontrando o maior comprimento dos valores para alinhar
        int maxLength = Math.max(20, nome.length()); 

        // Cabeçalho
        str.append("\n+" + "-".repeat(maxLength + 22) + "+\n");

        // Encontrar tamanho do espaçamento em "Duração"
        int len = Math.max(0, maxLength - 8 - String.format("%.2f", duracao).length());

        // Conteúdo formatado
        str.append(String.format("| ID................: %-" + maxLength + "d |\n", ID));
        str.append(String.format("| IDSerie...........: %-" + maxLength + "d |\n", IDSerie));
        str.append(String.format("| Nome..............: %-" + maxLength + "s |\n", nome));
        str.append(String.format("| Temporada.........: %-" + maxLength + "d |\n", temporada));
        str.append(String.format("| Data de lançamento: %-" + maxLength + "s |\n", dataLancamento.format(formatter)));
        str.append(String.format("| Duração...........: %.2f minutos%-" + len + "s |\n", duracao, ""));
        str.append(String.format("| Nota..............: %-" + maxLength + "d |\n", nota));

        if(diretores != null){
            for (int i = 0; i < diretores.size(); i++) 
                str.append(String.format("| Diretor(%d)........: %-"+maxLength+"s |\n", i + 1, diretores.get(i)));
        }

        // Rodapé
        str.append("+" + "-".repeat(maxLength + 22) + "+\n");

        // Retornar
        return str.toString();
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(ID);
        dos.writeInt(IDSerie);
        dos.writeUTF(nome);
        dos.writeByte(temporada);
        dos.writeInt( (int) dataLancamento.toEpochDay() );
        dos.writeFloat(duracao);
        dos.writeByte(nota);

        if (diretores == null) diretores = new ArrayList<String>();
        dos.writeByte(diretores.size());   
        for (String diretor : diretores) {
            dos.writeUTF(diretor);
        }

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);

        ID = dis.readInt();
        IDSerie = dis.readInt();
        nome = dis.readUTF();
        temporada = dis.readByte();
        dataLancamento = LocalDate.ofEpochDay(dis.readInt());
        duracao = dis.readFloat();
        nota = dis.readByte();

        if (diretores == null) diretores = new ArrayList<String>();
        byte length = dis.readByte();
        for (byte i = 0; i < length; i++) {
            diretores.add(dis.readUTF());
        }
    }
}