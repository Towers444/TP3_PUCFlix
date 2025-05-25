package modelo;

import java.io.*;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.regex.Pattern;

import aeds3.RegistroArvoreBMais;

public class ParNomeID implements RegistroArvoreBMais<ParNomeID> {
    private String nome;
    private int ID;
    private final short TAMANHO = 34;
    private final short TAMANHO_NOME = 30;

    /**
     * Construtor padrão, inicializa com valores vazios
     */
    public ParNomeID() {
        this.nome = "";
        this.ID = -1;
    }

    /**
     * Construtor que recebe nome e ID, truncando o nome se necessário
     * @param nome (String): Nome da série (máx. 30 caracteres)
     * @param ID (int): Identificador único
     */
    public ParNomeID(String nome, int ID) {
        this.nome = (nome.length() > TAMANHO_NOME) ? nome.substring(0, TAMANHO_NOME) : nome;
        this.ID = ID;
    }

    public String getNome() {
        return this.nome;
    }

    public int getID() {
        return this.ID;
    }

    /**
     * Retorna o tamanho do registro
     * @return (short) Tamanho em bytes
     */
    public short size() {
        return TAMANHO;
    }

    /**
     * Converte o objeto para um array de bytes
     * @return (byte[]) Representação serializada do objeto
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBytes(String.format("%-30s", nome)); // Garante que o nome tenha 30 bytes
        dos.writeInt(ID);
        return baos.toByteArray();
    }

    /**
     * Preenche o objeto a partir de um array de bytes
     * @param ba (byte[]): Array de bytes contendo os dados
     */
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] b = new byte[TAMANHO_NOME];
        dis.readFully(b);
        this.nome = new String(b).trim();
        this.ID = dis.readInt();
    }

    /**
     * Compara este objeto com outro ParNomeID baseado no nome
     * @param obj (ParNomeID): Objeto a ser comparado
     * @return (int) Valor negativo, zero ou positivo conforme a ordem lexicográfica
     */
    public int compareTo(ParNomeID obj) {
        String var2 = transforma(this.nome);
        String var3 = transforma(obj.nome);
        if (var3.length() > var2.length()) {
           var3 = var3.substring(0, var2.length());
        }
  
        if (var2.compareTo(var3) == 0) {
           return this.ID == -1 ? 0 : this.ID - obj.ID;
        } else {
           return var2.compareTo(var3);
        }
    }

    /**
     * Retorna uma cópia do objeto
     * @return (ParNomeID) Clone do objeto atual
     */
    public ParNomeID clone() {
        return new ParNomeID(this.nome, this.ID);
    }

    /**
     * Representação textual do objeto
     * @return (String) Nome e ID formatados
     */
    @Override
    public String toString() {
        return "(" + this.nome + "; " + this.ID + ")";
    }

    public static String transforma(String var0) {
      String var1 = Normalizer.normalize(var0, Form.NFD);
      Pattern var2 = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
      return var2.matcher(var1).replaceAll("").toLowerCase();
   }
}
