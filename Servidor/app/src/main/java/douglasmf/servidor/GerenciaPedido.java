package douglasmf.servidor;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class GerenciaPedido {

    private RecyclerView.Adapter adapter;
    private ArrayList<Pedido> pedidos;

    public GerenciaPedido(char tipo){
        if(tipo == 'E'){ //Em espera
            adapter = AppRequest.getInstance().emEsperaAdapter;
            pedidos = AppRequest.getInstance().pedidosEspera;
        }else{ //Em preparo
            adapter = AppRequest.getInstance().emPreparoAdapter;
            pedidos = AppRequest.getInstance().pedidosPreparo;
        }
    }

    public void adicionarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        //Atualiza a ui da aba "Meu pedido" com novo item inserido
        this.adapter.notifyItemInserted(pedidos.size() - 1);
    }

    public void removerPedido(int posicao) {
        this.pedidos.remove(posicao);
        //Atualiza a ui da aba "" com o item removido
        this.adapter.notifyItemRemoved(posicao);
    }
}