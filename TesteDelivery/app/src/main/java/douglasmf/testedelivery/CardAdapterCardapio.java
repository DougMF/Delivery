package douglasmf.testedelivery;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CardAdapterCardapio extends RecyclerView.Adapter<CardAdapterCardapio.MyViewHolder> {
    private ArrayList<ItemCardapio> itens;
    static RecyclerView.Adapter mAdapter;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tVItem,
                tVDescricao,
                tVPreco;

        public MyViewHolder(final View vCard) {
            super(vCard);
            this.tVItem = vCard.findViewById(R.id.tVItem);
            this.tVDescricao = vCard.findViewById(R.id.tVDescricao);
            this.tVPreco = vCard.findViewById(R.id.tVPreco);

            Button bAdicionar = vCard.findViewById(R.id.bAdicionarPedido);

            //Listener do botão "Adicionar" do fragment "Cardápio"
            bAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupAdicionar(vCard);
                }
            });
        }

        //Popup "popup_pedido"
        public void popupAdicionar(final View vCard){
            final Dialog dialog = new Dialog(vCard.getContext());
            dialog.setContentView(R.layout.popup_pedido);
            dialog.show();

            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Button bAdicionar = dialog.findViewById(R.id.bAdicionarPedido);
            Button bMais = dialog.findViewById(R.id.bMais);
            Button bMenos = dialog.findViewById(R.id.bMenos);
            Button bCancelar = dialog.findViewById(R.id.bCancelar);

            //Listener do botão "Adicionar"
            bAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adicionarPedido(dialog, vCard);
                }
            });

            //Listener do botão "Cancelar"
            bCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            //Listener do botão "-"
            bMenos.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    final TextView tVQuantidade = dialog.findViewById(R.id.tVQuantidade);
                    int quantidade = Integer.parseInt(tVQuantidade.getText().toString());

                    if(quantidade > 1){
                        quantidade--;
                        tVQuantidade.setText(quantidade + "");
                    }

                }
            });

            //Listener do botão "+"
            bMais.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    final TextView tVQuantidade = dialog.findViewById(R.id.tVQuantidade);
                    int quantidade = Integer.parseInt(tVQuantidade.getText().toString());

                    quantidade++;

                    tVQuantidade.setText(quantidade + "");
                }
            });
        }

        public void adicionarPedido(Dialog dialog, View vCard){
            String obs = ((EditText)dialog.findViewById(R.id.tVObs)).getText().toString(),
                    nomePedido = ((TextView) vCard.findViewById(R.id.tVItem)).getText().toString();
            int quantidade = Integer.parseInt(((TextView)dialog.findViewById(R.id.tVQuantidade)).getText().toString());

            ItemCardapio itemCardapio = null;

            //Encontra o item do cardápio selecionado
            for(ItemCardapio item: AppRequest.getInstance().getCardapio()){
                if(item.item.equals(nomePedido)){
                    itemCardapio = item;
                    break;
                }
            }

            //Posição na qual o item foi inserido
            int posicao = AppRequest.getInstance().getMeusPedidos().size();

            //Adiciona aos meus pedidos
            AppRequest.getInstance().adicionarItemPedido(new ItemPedido(itemCardapio, quantidade, obs, posicao), posicao);
            dialog.dismiss();
        }
    }

    public CardAdapterCardapio(ArrayList<ItemCardapio> itens, RecyclerView.Adapter mAdapter) {
        this.itens = itens;
        this.mAdapter = mAdapter;
    }

    @Override
    public CardAdapterCardapio.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vCard = inflater.inflate(R.layout.card_item, parent, false);

        return new MyViewHolder(vCard);
    }

    //Substitui o conteúdo das views
    @Override
    public void onBindViewHolder(MyViewHolder holder, int posicao) {
        ItemCardapio item = itens.get(posicao);

        holder.tVItem.setText(item.item);
        holder.tVDescricao.setText(item.descricao);
        holder.tVPreco.setText(NumberFormat.getCurrencyInstance().format(item.preco));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }
}