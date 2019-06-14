package douglasmf.servidor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CardAdapterCliente extends RecyclerView.Adapter<CardAdapterCliente.MyViewHolder> {
    private ArrayList<Cliente> clientes;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tVNome,
                tVTelefone,
                tVEmail,
                tVEndereco;

        public MyViewHolder(final View vCard) {
            super(vCard);

            this.tVNome = vCard.findViewById(R.id.tVNome);
            this.tVTelefone = vCard.findViewById(R.id.tVTelefone);
            this.tVEmail = vCard.findViewById(R.id.tVEmail);
            this.tVEndereco = vCard.findViewById(R.id.tVEndereco);

            Button bExcluir = vCard.findViewById(R.id.bExcluir);

            //Listener do botão "Aceitar"
            bExcluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: Excluir cliente
                }
            });
        }
    }

        public CardAdapterCliente(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }

    @Override
    public CardAdapterCliente.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vCard = inflater.inflate(R.layout.card_cliente, parent, false);

        return new MyViewHolder(vCard);
    }

    //Substitui o conteúdo das views
    @Override
    public void onBindViewHolder(MyViewHolder holder, int posicao) {
        holder.tVNome.setText(clientes.get(posicao).nome);
        holder.tVTelefone.setText(clientes.get(posicao).telefone);
        holder.tVEmail.setText(clientes.get(posicao).email);
        holder.tVEndereco.setText(clientes.get(posicao).endereco); //TODO: Arrumar endereço
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }
}