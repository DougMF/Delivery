package douglasmf.testedelivery;

public class ItemCardapio {
    public String item,
                  descricao;
    public float preco;

    public ItemCardapio(String item, String descricao, String preco){
        this.item = item;
        this.descricao = descricao;
        this.preco = Float.parseFloat(preco);
    }
}
