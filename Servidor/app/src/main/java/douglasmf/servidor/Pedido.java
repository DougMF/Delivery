package douglasmf.servidor;

import java.util.ArrayList;

public class Pedido {
    public ArrayList<Item> itens;
    public float total;
    public int usuario;
    public String nome;
    public String telefone;
    public char status = 'E';

    public Pedido(ArrayList<Item> itens, float total, int usuario, String nome, String telefone){
        this.itens = new ArrayList<>(itens);
        this.total = total;
        this.usuario = usuario;
        this.nome = nome;
        this.telefone = telefone;
    }
}
