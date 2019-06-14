package douglasmf.servidor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CardAdapterPedidoEspera extends RecyclerView.Adapter<CardAdapterPedidoEspera.MyViewHolder> {
    private ArrayList<Pedido> pedidos;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tVCliente,
                tVTelefone,
                tVPedido;

        public MyViewHolder(final View vCard) {
            super(vCard);

            this.tVCliente = vCard.findViewById(R.id.tVCliente);
            this.tVTelefone = vCard.findViewById(R.id.tVTelefone);
            this.tVPedido = vCard.findViewById(R.id.tVPedido);

            Button bAceitar = vCard.findViewById(R.id.bAceitar);
            final Button bCancelar = vCard.findViewById(R.id.bCancelar);
            Button bLigar = vCard.findViewById(R.id.bLigar);

            //Listener do botão "Aceitar"
            bAceitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int posicao = getAdapterPosition();
                    Pedido pedido = AppRequest.getInstance().pedidosEspera.get(posicao);
                    pedido.status = 'P';

                    //Altera o status do pedido para "Em preparo" (P)
                    new AcessoSQL().alterarStatus(AppRequest.getInstance().pedidosEspera.get(posicao)
                                    .usuario, 'P', vCard.getContext());

                    //Adiciona o pedido à lista de preparo e remove da lista de espera
                    new GerenciaPedido('E').removerPedido(posicao);
                    new GerenciaPedido('P').adicionarPedido(pedido);
                }
            });

            //Listener do botão "Cancelar"
            bCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(vCard.getContext()).create();
                    alertDialog.setMessage("Tem certeza que deseja cancelar o pedido?");

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int usuario = AppRequest.getInstance().pedidosEspera.get(getAdapterPosition()).usuario;
                                    bCancelar.setEnabled(false);
                                    new AcessoSQL().removerPedido(bCancelar, usuario, getAdapterPosition(), vCard.getContext(), 'E');
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

            //Listener do botão "Ligar"
            bLigar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String telefone = AppRequest.getInstance().pedidosEspera.get(getAdapterPosition()).telefone;

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + telefone));
                    vCard.getContext().startActivity(intent);
                }
            });
        }
    }

    public CardAdapterPedidoEspera(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public CardAdapterPedidoEspera.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vCard = inflater.inflate(R.layout.card_pedido_espera, parent, false);

        return new MyViewHolder(vCard);
    }

    //Substitui o conteúdo das views
    @Override
    public void onBindViewHolder(MyViewHolder holder, int posicao) {
        Pedido pedido = pedidos.get(posicao);

        //String para armazenar todos os itens do pedido
        String aux = "";

        for(Item item: pedido.itens){
            aux += "\bItem: " + item.item + "\n";
            aux += "\bObs.:" + item.obs + "\n";
            aux += "\bQuantidade: " + item.quantidade + "\n\n";
        }

        holder.tVCliente.setText(pedido.nome);
        holder.tVTelefone.setText(pedido.telefone);
        holder.tVPedido.setText(aux.trim());
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }
}