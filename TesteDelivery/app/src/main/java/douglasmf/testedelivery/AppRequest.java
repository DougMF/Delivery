package douglasmf.testedelivery;

import android.app.Application;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.NumberFormat;
import java.util.ArrayList;

public class AppRequest extends Application {
    private RequestQueue queue;
    private static AppRequest instance;
    private static ArrayList<ItemCardapio> cardapio = new ArrayList<>();
    private static ArrayList<ItemPedido> meusPedidos = new ArrayList<>();
    public RecyclerView.Adapter meuPedidoAdapter;
    public RecyclerView recyclerView;
    public TextView tVTotal;
    public Button bFinalizar;
    public float total = 0;
    public String usuario;
    public char status = 'A';

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized AppRequest getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }

        return queue;
    }

    public ArrayList<ItemCardapio> getCardapio(){
        return cardapio;
    }

    public ArrayList<ItemPedido> getMeusPedidos(){
        return meusPedidos;
    }

    public void adicionarItemPedido(ItemPedido itemPedido, int posicao){
        meusPedidos.add(itemPedido);
        total += itemPedido.subtotal;
        //Atualiza o valor total do pedido
        atualizarTotal();
        //Atualiza a ui da aba "Meu pedido" com novo item inserido
        meuPedidoAdapter.notifyItemInserted(posicao);
    }

    public void removerPedido(int posicao){
        total -= meusPedidos.get(posicao).subtotal;
        meusPedidos.remove(posicao);
        atualizarTotal();
        //Atualiza a ui da aba "Meu pedido" com o item removido
        meuPedidoAdapter.notifyItemRemoved(posicao);
    }

    public void atualizarTotal(){
        tVTotal.setText("Total: " + NumberFormat.getCurrencyInstance().format(total));
        if (total > 0) bFinalizar.setVisibility(Button.VISIBLE);
        else bFinalizar.setVisibility(Button.INVISIBLE);
    }

    public void limparDados(Button bFinalizar){
        bFinalizar.setText("Finalizar");
        bFinalizar.setBackgroundColor(Color.GREEN);
        bFinalizar.setTextColor(Color.WHITE);
        bFinalizar.setEnabled(true);

        meusPedidos.clear();
        meuPedidoAdapter = new CardAdapterPedido(AppRequest.getInstance().getMeusPedidos());
        recyclerView.setAdapter(meuPedidoAdapter);
        total = 0;
        status = 'A';

        atualizarTotal();
    }
}