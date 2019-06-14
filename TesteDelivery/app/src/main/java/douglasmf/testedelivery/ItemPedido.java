package douglasmf.testedelivery;

public class ItemPedido {
    public ItemCardapio itemCardapio;
    public float subtotal;
    public String obs;
    public int quantidade = 1;
    public int posicao = 0;

    public ItemPedido(ItemCardapio itemCardapio, int quantidade, String obs, int posicao){
        this.itemCardapio = itemCardapio;
        this.obs = obs;
        this.quantidade = quantidade;
        this.subtotal = (float) quantidade * itemCardapio.preco;
        this.posicao = posicao;
    }
}
