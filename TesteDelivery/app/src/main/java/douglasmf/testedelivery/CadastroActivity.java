package douglasmf.testedelivery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CadastroActivity extends Activity {
    private static final String TAG = CadastroActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private ProgressBar progresso;
    private String nome, email, senha, telefone;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Views que serão carregadas na activity
        final View vCadastroDados = getLayoutInflater().inflate(R.layout.activity_cadastro, null),
              vCadastroEndereco = getLayoutInflater().inflate(R.layout.activity_cadastro_endereco, null);

        //Inicia com a tela de cadastro de dados
        setContentView(vCadastroDados);

        //Views da tela de dados
        final EditText eTNome = vCadastroDados.findViewById(R.id.eTNome),
                 eTEmail = vCadastroDados.findViewById(R.id.eTEmail),
                 eTSenha = vCadastroDados.findViewById(R.id.eTSenha),
                 eTelefone = vCadastroDados.findViewById(R.id.eTTelefone),
                 bProximo = vCadastroDados.findViewById(R.id.bCadastrar),
                 btnTelaLogin = vCadastroDados.findViewById(R.id.btnTelaLogin);

        //Views da tela de endereço
        final EditText eTLogradouro = vCadastroEndereco.findViewById(R.id.eTLogradouro),
                 eTBairro = vCadastroEndereco.findViewById(R.id.eTBairro),
                 eTNumero = vCadastroEndereco.findViewById(R.id.eTNumero),
                 eTCep = vCadastroEndereco.findViewById(R.id.eTCep);
        Button bVoltar = vCadastroEndereco.findViewById(R.id.bVoltar);
        Button bCadastrar = vCadastroEndereco.findViewById(R.id.bCadastrar);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        progresso = vCadastroEndereco.findViewById(R.id.progresso);

        bProximo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                 nome = eTNome.getText().toString().trim();
                 email = eTEmail.getText().toString().trim();
                 senha = eTSenha.getText().toString().trim();
                 telefone = eTelefone.getText().toString().trim();

                fecharTeclado();

                //Verifica se todos os campos foram preenchidos
                if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty() && !telefone.isEmpty()) {
                    inflarLayoutEndereco(vCadastroDados, vCadastroEndereco);
                } else {
                    //Usuário não preencheu todos os campos
                    Toast.makeText(getApplicationContext(),
                            "Todos os campos devem ser preechidos!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        //Abre a tela de login
        btnTelaLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        //Cadastra os dados do usuário
        bCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String logradouro = eTLogradouro.getText().toString(),
                        bairro = eTBairro.getText().toString(),
                        numero = eTNumero.getText().toString(),
                        cep = eTCep.getText().toString();

                if(!logradouro.isEmpty() && !bairro.isEmpty() && !numero.isEmpty() && !cep.isEmpty()) {
                    cadastrarUsuario(logradouro, bairro, numero, cep);
                }else{
                    //Usuário não preencheu todos os campos
                    Toast.makeText(getApplicationContext(),
                            "Todos os campos devem ser preechidos!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        //Volta para o cadastro de dados
        bVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflarLayoutDadosPessoais(vCadastroEndereco, vCadastroDados);
            }
        });

    }

    private void inflarLayoutDadosPessoais(View vCadastroEndereco, View vCadastroDados){
        vCadastroEndereco.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_right));
        setContentView(vCadastroDados);
        vCadastroDados.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
    }

    private void inflarLayoutEndereco(View vCadastroDados, View vCadastroEndereco){
        vCadastroDados.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
        setContentView(vCadastroEndereco);
        vCadastroEndereco.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
    }

    //Método para inserir o usuário no banco
    private void cadastrarUsuario(final String logradouro, final String bairro, final String numero,
                                  final String cep) {
        progresso.setVisibility(ProgressBar.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        AcessoSQL acesso = new AcessoSQL();
        acesso.cadastro(nome, email, senha, telefone, logradouro, bairro, numero, cep, progresso, this);
    }

    private void fecharTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
