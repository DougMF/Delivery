package douglasmf.servidor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    ProgressBar progresso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button bLogin = findViewById(R.id.btnLogin);
        final EditText eTUsuario = findViewById(R.id.eTUsuario);
        final EditText eTSenha = findViewById(R.id.eTNovaSenha);
        progresso = findViewById(R.id.progresso);
        final Context context = this;

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = eTUsuario.getText().toString().trim(),
                        senha = eTSenha.getText().toString().trim();

                fecharTeclado();

                verificarLogin(usuario, senha, context);
            }
        });
    }

    private void verificarLogin(final String usuario, final String senha, Context context) {
        AcessoSQL acessoSQL = new AcessoSQL();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progresso.setVisibility(ProgressBar.VISIBLE);

        System.out.println("Usuario: " + usuario + "\nSenha: " + senha);

        acessoSQL.login(usuario, senha, progresso, context);
    }

    private void fecharTeclado() {
        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
