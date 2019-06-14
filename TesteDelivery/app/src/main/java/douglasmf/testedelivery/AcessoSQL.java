package douglasmf.testedelivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AcessoSQL{

    public void login(final String email, final String senha, final ProgressBar progresso, final Context context){

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.urlLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progresso.setVisibility(ProgressBar.GONE);

                        if(response.equals("Erro")){
                            Toast.makeText(context,"E-mail ou senha inválido(s)!", Toast.LENGTH_LONG).show();
                            ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }else{
                            AppRequest.getInstance().usuario = response;
                            Intent i = new Intent(context, Pedidos.class);
                            context.startActivity(i);
                            ((Activity)context).finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Erro
                        progresso.setVisibility(ProgressBar.GONE);
                        ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(context,"Erro no login! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("senha", senha);

                return params;
            }
        };

        AppRequest.getInstance().getRequestQueue().add(postRequest);
    }

    public void cadastro(final String nome,
                         final String email, final String senha, final String telefone,
                         final String logradouro,  final String bairro, final String numero,
                         final String cep, final ProgressBar progresso, final Context context) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.urlCadastro,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progresso.setVisibility(ProgressBar.GONE);

                        ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        if(response.equals("erro1")) Toast.makeText(context,"O e-mail já está sendo utilizado!", Toast.LENGTH_LONG).show();
                        else if (response.equals("erro2")) Toast.makeText(context,"Erro no cadastro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        else Toast.makeText(context,"Cadastro efetuado com sucesso!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progresso.setVisibility(ProgressBar.GONE);
                        Toast.makeText(context,"Erro no cadastro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nome", nome);
                params.put("email", email);
                params.put("senha", senha);
                params.put("telefone", telefone);
                params.put("logradouro", logradouro);
                params.put("bairro", bairro);
                params.put("numero", numero);
                params.put("cep", cep);

                return params;
            }
        };

        AppRequest.getInstance().getRequestQueue().add(postRequest);
    }

    public void atualizarCardapio(final RecyclerView.Adapter mAdapter, final ProgressBar progresso) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (AppUrls.urlGetCardapio, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject item = response.getJSONObject(i);

                                AppRequest.getInstance().getCardapio().add(new ItemCardapio(item.getString("item"),
                                        item.getString("descricao"),
                                        item.getString("preco")));

                                progresso.setVisibility(ProgressBar.GONE);
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Erro");
                    }
        });

        AppRequest.getInstance().getRequestQueue().add(jsonArrayRequest);
    }

    public void finalizarPedido(final Context context, final Button bfinalizar){
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.urlEnviarPedido,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("ok")){
                            bfinalizar.setText("Aguardando aprovação");
                            bfinalizar.setBackgroundColor(Color.RED);
                            bfinalizar.setTextColor(Color.WHITE);

                            //Troca o layout do adapter para exibir os itens do pedido finalizado
                            AppRequest.getInstance().meuPedidoAdapter = new CardAdapterPedidoFinal(AppRequest.getInstance().getMeusPedidos());
                            AppRequest.getInstance().recyclerView.setAdapter(AppRequest.getInstance().meuPedidoAdapter);

                            Timer timer = new Timer();

                            timer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    verificarStatus(context, bfinalizar, this);
                                }
                            },0,2000);

                        }else if (response.equals("erro")){
                            Toast.makeText(context,"Erro no sistema. Tente novamente em alguns minutos", Toast.LENGTH_LONG).show();
                            bfinalizar.setEnabled(true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Erro no cadastro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                JSONArray array = new JSONArray();

                for(int i = 0; i < AppRequest.getInstance().getMeusPedidos().size(); i++){

                    JSONObject obj = new JSONObject();

                    try {
                        obj.put("item",AppRequest.getInstance().getMeusPedidos().get(i).itemCardapio.item);
                        obj.put("obs",AppRequest.getInstance().getMeusPedidos().get(i).obs);
                        obj.put("quantidade",AppRequest.getInstance().getMeusPedidos().get(i).quantidade);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array.put(obj);
                }

                params.put("itens", array.toString());
                params.put("total", AppRequest.getInstance().total + "");
                params.put("usuario", AppRequest.getInstance().usuario);

                return params;
            }
        };

        AppRequest.getInstance().getRequestQueue().add(postRequest);
    }

    public void verificarStatus(final Context context, final Button bFinalizar, final TimerTask timerTask){
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.urlVerificarStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("erro")){ //Erro de comunicação
                        }else if (response.equals("fim")){ //Pedido encerrado
                            AppRequest.getInstance().limparDados(bFinalizar);
                            timerTask.cancel();
                        }else{ //Status recebido
                            //Status diferente do status atual
                            if (!response.trim().equals(AppRequest.getInstance().status)){
                                AppRequest.getInstance().status = response.trim().charAt(0);

                                if(response.trim().equals("P"))
                                    bFinalizar.setText("Em preparo");
                                else if(response.trim().equals("D"))
                                    bFinalizar.setText("A caminho");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Erro no cadastro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("usuario", AppRequest.getInstance().usuario);

                return params;
            }
        };

        AppRequest.getInstance().getRequestQueue().add(postRequest);
    }
}
