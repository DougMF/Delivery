package douglasmf.testedelivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    private Button btnLogin;
    private Button btnTelaCadastro;
    private EditText eTEmail;
    private EditText eTSenha;
    private ProgressBar progresso;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eTEmail = findViewById(R.id.eTEmail);
        eTSenha = findViewById(R.id.eTSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnTelaCadastro = findViewById(R.id.btnTelaCadastro);
        progresso = findViewById(R.id.progresso);

        //Listener para o botão Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = eTEmail.getText().toString().trim(),
                        password = eTSenha.getText().toString().trim();

                fecharTeclado();

                //Verifica se os dados foram preenchidos
                //if (!email.isEmpty() && !password.isEmpty()) {
                    //Tenta efetuar o login
                    verificarLogin(email, password);
                /*} else {
                    Toast.makeText(getApplicationContext(),"Todos os campos devem ser preenchidos", Toast.LENGTH_LONG).show();
                }*/
            }
        });

        //Link para tela de cadastro
        btnTelaCadastro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void verificarLogin(final String email, final String senha) {
        AcessoSQL acesso = new AcessoSQL();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progresso.setVisibility(ProgressBar.VISIBLE);
        acesso.login(email, senha, progresso, this);
    }

    private void fecharTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
