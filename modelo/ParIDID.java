package modelo;

import java.io.*;

import aeds3.RegistroArvoreBMais;

public class ParIDID implements RegistroArvoreBMais<ParIDID> {
    private int ID;
    private int ID_Dependente;
    private final short TAMANHO = 8;

    /**
     * Construtor padrão, inicializa com valores -1
     */
    public ParIDID() {
        this.ID = -1;
        this.ID_Dependente = -1;
    }

    /**
     * Construtor que recebe os IDs da série e do episódio
     * @param idS (int): ID da série
     * @param idEp (int): ID do episódio
     */
    public ParIDID(int idS, int idEp) {
        this.ID = idS;
        this.ID_Dependente = idEp;
    }

    /**
     * Retorna o ID da série
     * @return (int) ID da série
     */
    public int getID() {
        return ID;
    }

    /**
     * Retorna o ID do episódio
     * @return (int) ID do episódio
     */
    public int getID_Dependente() {
        return ID_Dependente;
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
        dos.writeInt(ID);
        dos.writeInt(ID_Dependente);
        return baos.toByteArray();
    }

    /**
     * Preenche o objeto a partir de um array de bytes
     * @param ba (byte[]): Array de bytes contendo os dados
     */
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.ID = dis.readInt();
        this.ID_Dependente = dis.readInt();
    }

    /**
     * Compara este objeto com outro ParIDID baseado no ID do Episódio
     * @param obj (ParIDID): Objeto a ser comparado
     * @return (int) Valor negativo, zero ou positivo conforme a ordem crescente de IDs
     */
    public int compareTo(ParIDID obj) {
        if (this.ID != obj.ID)
            return this.ID - obj.ID;
        else
        // Só compara os valores de id2, se o id2 da busca for diferente de -1
        // Isso é necessário para que seja possível a busca de lista
            return this.ID_Dependente == -1 ? 0 : this.ID_Dependente - obj.ID_Dependente;
    }

    /**
     * Retorna uma cópia do objeto
     * @return (ParIDID) Clone do objeto atual
     */
    public ParIDID clone() {
        return new ParIDID(this.ID, this.ID_Dependente);
    }

    /**
     * Representação textual do objeto
     * @return (String) Representação formatada do par de IDs
     */
    @Override
    public String toString() {
        return "(Série: " + ID + ", Episódio: " + ID_Dependente + ")";
    }
}
