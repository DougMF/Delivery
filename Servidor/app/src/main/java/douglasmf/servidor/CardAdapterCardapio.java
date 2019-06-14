package douglasmf.servidor;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CardAdapterCardapio extends RecyclerView.Adapter<CardAdapterCardapio.MyViewHolder> {
    private ArrayList<ItemCardapio> itens;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tVItem,
                tVDescricao,
                tVPreco;
        public ImageView iVFoto;

        public MyViewHolder(final View vCard) {
            super(vCard);
            this.tVItem = vCard.findViewById(R.id.tVItem);
            this.tVDescricao = vCard.findViewById(R.id.tVDescricao);
            this.tVPreco = vCard.findViewById(R.id.tVPreco);
            this.iVFoto = vCard.findViewById(R.id.iVFoto);

            Button bEditar = vCard.findViewById(R.id.bEditar);
            Button bExcluir = vCard.findViewById(R.id.bExcluir);

            //Listener do botão "Editar"
            bEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupDadosItem(vCard);
                }
            });

            //Listener do botão "Excluir"
            bExcluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: Excluir item do cardápio
                }
            });
        }

        //Popup "popup_dados_item"
        public void popupDadosItem(final View vCard){
            final Dialog dialog = new Dialog(vCard.getContext());
            dialog.setContentView(R.layout.popup_dados_item);
            dialog.show();

            final Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Button bAlterar = dialog.findViewById(R.id.bAdicionar);
            Button bCancelar = dialog.findViewById(R.id.bCancelar);

            bAlterar.setText("Alterar");

            //Listener do botão "Alterar"
            bAlterar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText eTNome = dialog.findViewById(R.id.eTNome);
                    EditText eTDescricao = dialog.findViewById(R.id.eTDescricao);
                    EditText eTPreco = dialog.findViewById(R.id.eTPreco);

                    //TODO: Alterar dados
                }
            });

            //Listener do botão "Cancelar"
            bCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }

    public CardAdapterCardapio(ArrayList<ItemCardapio> itens) {
        this.itens = itens;
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
        holder.iVFoto.setImageBitmap(AppRequest.getInstance().imagens.get(0));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }
}