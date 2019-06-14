package douglasmf.testedelivery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CardAdapterPedidoFinal extends RecyclerView.Adapter<CardAdapterPedidoFinal.MyViewHolder> {
    private ArrayList<ItemPedido> itens;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tVItem,
                tVPreco,
                tVObs,
                tVQuantidade;

        public MyViewHolder(final View vCard) {
            super(vCard);

            this.tVItem = vCard.findViewById(R.id.tVItem);
            this.tVPreco = vCard.findViewById(R.id.tVPreco);
            this.tVObs = vCard.findViewById(R.id.tVObs);
            this.tVQuantidade = vCard.findViewById(R.id.tVQuantidade);
        }
    }

    public CardAdapterPedidoFinal(ArrayList<ItemPedido> itens) {
        this.itens = itens;
    }

    @Override
    public CardAdapterPedidoFinal.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vCard = inflater.inflate(R.layout.card_pedido_final, parent, false);

        return new MyViewHolder(vCard);
    }

    //Substitui o conteúdo das views
    @Override
    public void onBindViewHolder(MyViewHolder holder, int posicao) {
        ItemPedido item = itens.get(posicao);

        holder.tVItem.setText(item.itemCardapio.item);
        holder.tVPreco.setText(NumberFormat.getCurrencyInstance().format(item.subtotal));
        holder.tVObs.setText(item.obs);
        holder.tVQuantidade.setText("Quantidade: " + item.quantidade);
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }
}