package douglasmf.servidor;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DadosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados);

        Button bSalvar = findViewById(R.id.bSalvar);

        bSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_senha, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        popupAlterarSenha(item);

        return true;
    }

    //Popup "popup_pedido"
    public void popupAlterarSenha(final MenuItem item) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_alterar_senha);
        dialog.show();

        final Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final Button bSalvar = dialog.findViewById(R.id.bSalvar);
        final Button bCancelar = dialog.findViewById(R.id.bCancelar);
        final EditText eTNovaSenha = dialog.findViewById(R.id.eTNovaSenha);
        final EditText eTNovaSenha1 = dialog.findViewById(R.id.eTNovaSenha1);
        final EditText eTSenhaAtual = dialog.findViewById(R.id.eTSenhaAtual);

        //Listener do botão Salvar
        bSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!eTNovaSenha1.getText().toString().isEmpty() && !eTSenhaAtual.getText().
                        toString().isEmpty()){
                    if(eTNovaSenha.getText().equals(eTNovaSenha1.getText())){
                        bSalvar.setEnabled(false);
                        bCancelar.setEnabled(false);

                        new AcessoSQL().alterarSenha();
                    }else{
                        Toast.makeText(getApplicationContext(), "As senhas digitadas não correspondem!", Toast.LENGTH_LONG);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Todos os campos devem ser preenchidos!", Toast.LENGTH_LONG);
                }
            }
        });

        //Listener do botão Cancelar
        bCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Limpa os campos e fecha a janela
                eTNovaSenha.setText("");
                eTNovaSenha1.setText("");
                eTSenhaAtual.setText("");
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),
                PedidosActivity.class);
        startActivity(i);
        finish();
    }
}
