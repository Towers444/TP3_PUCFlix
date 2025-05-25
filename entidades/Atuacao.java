package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import aeds3.EntidadeArquivo;
import controle.ControleSerie;

public class Atuacao implements EntidadeArquivo {
    private int IDAtuacao;
    private int IDAtor;
    private int IDSerie;
    private String personagem;
    

    public Atuacao(int IDAtuacao, int IDAtor, int IDSerie, String personagem) throws Exception{
        if (IDAtuacao > 0 || IDAtuacao == -1)
            setID(IDAtuacao);
        else
            throw new Exception("ID de Atuação inválido");
        if (IDAtor > 0 || IDAtor == -1) // Substituir por metodo ControleAtor.validarAtor()
            setIDAtor(IDAtor);
        else
            throw new Exception("ID de Ator inválido");
        if (ControleSerie.validarSerie(IDSerie) || IDSerie == -1)
            setIDSerie(IDSerie);
        else
            throw new Exception("ID de Série inválida");
        setPersonagem(personagem);
    }

    public Atuacao(int IDAtor, int IDSerie, String personagem) throws Exception{
        this(-1,IDAtor, IDSerie, personagem);
    }

    public Atuacao() throws Exception{
        this(-1,-1,-1,"");
    }

    public int getID(){
        return  this.IDAtuacao;
    }
    
    public void setID(int id){
        this.IDAtuacao = id;
    }

    public int getIDAtor(){
        return  this.IDAtor;
    }
    
    public void setIDAtor(int id){
        this.IDAtor = id;
    }

    public int getIDSerie(){
        return  this.IDSerie;
    }
    
    public void setIDSerie(int id){
        this.IDSerie = id;
    }

    public String getPersonagem(){
        return  this.personagem;
    }
    
    public void setPersonagem(String p){
        this.personagem = p;
    }



    @Override
    public String toString() {
        // Definir dados
        StringBuilder str = new StringBuilder();

        // Encontrando o maior comprimento dos valores para alinhar
        int maxLength = Math.max(20, personagem.length()); 

        // Cabeçalho
        str.append("\n+" + "-".repeat(maxLength + 22) + "+\n");

        // Encontrar tamanho do espaçamento em "Duração"

        // Conteúdo formatado
        str.append(String.format("| ID........: %-" + maxLength + "d |\n", IDAtuacao));
        str.append(String.format("| IDAtor....: %-" + maxLength + "d |\n", IDAtor));
        str.append(String.format("| IDSerie...: %-" + maxLength + "d |\n", IDSerie));
        str.append(String.format("| Personagem: %-" + maxLength + "s |\n", personagem));

        // Rodapé
        str.append("+" + "-".repeat(maxLength + 22) + "+\n");

        // Retornar
        return str.toString();
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(IDAtuacao);
        dos.writeInt(IDAtor);
        dos.writeInt(IDSerie);
        dos.writeUTF(personagem);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);

        setID(dis.readInt());
        setIDAtor(dis.readInt());
        setIDSerie(dis.readInt());
        setPersonagem(dis.readUTF());
        
    }


}