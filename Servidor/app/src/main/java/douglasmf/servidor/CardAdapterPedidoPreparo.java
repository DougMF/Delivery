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

public class CardAdapterPedidoPreparo extends RecyclerView.Adapter<CardAdapterPedidoPreparo.MyViewHolder> {
    private ArrayList<Pedido> pedidos;
    public int pos;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tVCliente,
                tVTelefone,
                tVEndereco,
                tVPedido,
                tVStatus;

        public MyViewHolder(final View vCard) {
            super(vCard);

            this.tVCliente = vCard.findViewById(R.id.tVCliente);
            this.tVTelefone = vCard.findViewById(R.id.tVTelefone);
            this.tVEndereco = vCard.findViewById(R.id.tVEndereco);
            this.tVPedido = vCard.findViewById(R.id.tVPedido);
            this.tVStatus = vCard.findViewById(R.id.tVStatus);

            final Button bAlterarStatus = vCard.findViewById(R.id.bAlteraStatus);
            final Button bCancelar = vCard.findViewById(R.id.bCancelar);
            Button bLigar = vCard.findViewById(R.id.bLigar);

            //Listener do botão "Altera status"
            bAlterarStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Pedido está em preparo, passará para em entrega (D)
                    if(AppRequest.getInstance().pedidosPreparo.get(getAdapterPosition()).status == 'P'){
                        new AcessoSQL().alterarStatus(AppRequest.getInstance().pedidosPreparo.
                                get(getAdapterPosition()).usuario, 'D', vCard.getContext());
                        AppRequest.getInstance().pedidosPreparo.get(getAdapterPosition()).status = 'D';
                        tVStatus.setText("A caminho");
                        bAlterarStatus.setText("Finalizar");
                    //Pedido está em entrega, será finalizado
                    }else{
                        bAlterarStatus.setEnabled(false);
                        int posicao = getAdapterPosition();

                        new AcessoSQL().removerPedido(bAlterarStatus, AppRequest.getInstance().
                                pedidosPreparo.get(posicao).usuario, posicao, vCard.getContext(), 'P');
                    }
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
                                    int usuario = AppRequest.getInstance().pedidosPreparo.get(getAdapterPosition()).usuario;
                                    bCancelar.setEnabled(false);
                                    new AcessoSQL().removerPedido(bCancelar, usuario, getAdapterPosition(), vCard.getContext(), 'P');
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
                    String telefone = AppRequest.getInstance().pedidosPreparo.get(getAdapterPosition()).telefone;

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + telefone));
                    vCard.getContext().startActivity(intent);
                }
            });
        }
    }

    public CardAdapterPedidoPreparo(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public CardAdapterPedidoPreparo.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vCard = inflater.inflate(R.layout.card_pedido_preparo, parent, false);

        return new MyViewHolder(vCard);
    }

    //Substitui o conteúdo das views
    @Override
    public void onBindViewHolder(MyViewHolder holder, int posicao) {
        //String para armazenar todos os itens do pedido
        String aux = "";

        for(Item item: pedidos.get(posicao).itens){
            aux += "\bItem: " + item.item + "\n";
            aux += "\bObs.:" + item.obs + "\n";
            aux += "\bQuantidade: " + item.quantidade + "\n\n";
        }

        //TODO: Descomentar endereço
        holder.tVCliente.setText(pedidos.get(posicao).nome);
        holder.tVTelefone.setText(pedidos.get(posicao).telefone);
        //holder.tVEndereco.setText(itens.get(posicao).endereco);
        holder.tVPedido.setText(aux.trim());

        if(pedidos.get(posicao).status == 'P')
            holder.tVStatus.setText("Em preparo");
        else holder.tVStatus.setText("A caminho");
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }
}