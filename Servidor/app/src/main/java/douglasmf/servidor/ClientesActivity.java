package douglasmf.servidor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

public class ClientesActivity extends AppCompatActivity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        final ImageButton iBConfig = findViewById(R.id.iBConfig);

        context = this;

        iBConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, iBConfig);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_dados:
                                Intent iDados = new Intent(getApplicationContext(),
                                        DadosActivity.class);
                                startActivity(iDados);
                                finish();
                                return true;
                            case R.id.action_cardapio:
                                Intent iCardapio = new Intent(getApplicationContext(),
                                        CardapioActivity.class);
                                startActivity(iCardapio);
                                finish();
                                return true;
                            case R.id.action_clientes:
                                Intent iPedidos = new Intent(getApplicationContext(),
                                        PedidosActivity.class);
                                startActivity(iPedidos);
                                finish();
                                return true;
                            case R.id.action_relatorio:
                                Intent iRelatorio = new Intent(getApplicationContext(),
                                        RelatorioActivity.class);
                                startActivity(iRelatorio);
                                finish();
                                return true;
                        }

                        return false;
                    }
                });
                popup.show();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new CardAdapterCliente(AppRequest.getInstance().clientes);

        ProgressBar progresso = findViewById(R.id.progresso);
        progresso.setVisibility(ProgressBar.VISIBLE);
        new AcessoSQL().getClientes(mAdapter, progresso);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }
}
