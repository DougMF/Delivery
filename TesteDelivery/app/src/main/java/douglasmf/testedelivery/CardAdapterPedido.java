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

public class CardAdapterPedido extends RecyclerView.Adapter<CardAdapterPedido.MyViewHolder> {
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

            Button bExcluir = vCard.findViewById(R.id.bExcluirPedido);
            Button bObs = vCard.findViewById(R.id.bObs);

            //Listener do botão "Excluir"
            bExcluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(vCard.getContext()).create();
                    alertDialog.setMessage("Tem certeza que deseja excluir o item do pedido?");

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    AppRequest.getInstance().removerPedido(getAdapterPosition());
                                }
                            });

                    alertDialog.show();

                    Button bPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button bNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bPositive.getLayoutParams();
                    layoutParams.weight = 10;
                    bPositive.setLayoutParams(layoutParams);
                    bNegative.setLayoutParams(layoutParams);
                }
            });

            //Listener do botão "Observação"
            bObs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupObs(vCard);
                }
            });
        }

        //Popup "popup_obs"
        public void popupObs(final View vCard){
            final Dialog dialog = new Dialog(vCard.getContext());
            dialog.setContentView(R.layout.popup_obs);
            dialog.show();

            final EditText eTObs = dialog.findViewById(R.id.tVObs);
            eTObs.setText(tVObs.getText());
            eTObs.requestFocus();

            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Button bCancelar = dialog.findViewById(R.id.bCancelar);
            Button bSalvar = dialog.findViewById(R.id.bSalvar);

            //Listener do botão "Cancelar"
            bCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            //Listener do botão "Salvar"
            bSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String obs = eTObs.getText().toString();

                    //Remove obs do pedido selecionado
                    AppRequest.getInstance().getMeusPedidos().get(getAdapterPosition()).obs = obs;
                    AppRequest.getInstance().meuPedidoAdapter.notifyItemChanged(getAdapterPosition());
                    dialog.dismiss();
                }
            });
        }
    }

    public CardAdapterPedido(ArrayList<ItemPedido> itens) {
        this.itens = itens;
    }

    @Override
    public CardAdapterPedido.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vCard = inflater.inflate(R.layout.card_cardapio, parent, false);

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