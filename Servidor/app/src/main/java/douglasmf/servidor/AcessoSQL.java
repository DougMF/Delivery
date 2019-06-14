package douglasmf.servidor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AcessoSQL{//Flag para controlar a execução das requisições

    private boolean rodar = true, //Flag para controlar a aquisição de pedidos em espera
                    continuar; //Flag para controlar a alteração do status do pedido
    Object lockGetPedidos = new Object(), lockAlterarStatus = new Object(); //Objects para sincronizar as threads

    public void login(final String usuario, final String senha, final ProgressBar progresso, final Context context){

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.urlLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progresso.setVisibility(ProgressBar.GONE);

                        if(!response.equals("ok")){
                            Toast.makeText(context,response, Toast.LENGTH_LONG).show();
                            ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }else{
                            Intent i = new Intent(context, PedidosActivity.class);
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
                //TODO: Remover usuário e senha
                String usua = "admin";
                String sen = "123";
                params.put("usuario", usua);
                params.put("senha", sen);

                return params;
            }
        };

        AppRequest.getInstance().getRequestQueue().add(postRequest);
    }

    public void atualizarCardapio(final RecyclerView.Adapter mAdapter, final ProgressBar progresso) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (AppUrls.urlCardapio, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject item = response.getJSONObject(i);

                                AppRequest.getInstance().cardapio.add(new ItemCardapio(item.getString("item"),
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
                        //TODO: Arrumar erro
                        System.out.println("Erro");
                    }
        });

        AppRequest.getInstance().getRequestQueue().add(jsonArrayRequest);
    }

    public void getPedidos() {
        new Thread(){
            public void run(){
                while (true) {
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                            (AppUrls.urlGetPedidos, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(final JSONArray response) {
                            new Thread() {
                                public void run() {
                                    try {
                                        ArrayList<Item> itens = new ArrayList<>();
                                        JSONArray jItens;
                                        JSONObject jItem;
                                        Pedido auxPedido;
                                        int total, usuario, quantidade;
                                        String nome, telefone, item, obs;

                                        //Conjunto de pedidos aguardando aprovação
                                        for (int i = 0; i < response.length(); i++, itens.clear()) {
                                            jItens = new JSONArray(response.getJSONObject(i).getString("itens")); //Itens do pedido

                                            //Insere os itens do pedido em uma lista
                                            for (int j = 0; j < jItens.length(); j++) {
                                                jItem = jItens.getJSONObject(j); //Item da lista de itens

                                                item = jItem.getString("item");
                                                obs = jItem.getString("obs");
                                                quantidade = jItem.getInt("quantidade");

                                                itens.add(new Item(item, obs, quantidade));
                                            }

                                            //Dados do pedido do usuário
                                            total = response.getJSONObject(i).getInt("total");
                                            usuario = response.getJSONObject(i).getInt("usuario");
                                            nome = response.getJSONObject(i).getString("nome");
                                            telefone = response.getJSONObject(i).getString("telefone");

                                            //Guarda o pedido temporariamente
                                            auxPedido = new Pedido(itens, total, usuario, nome, telefone);

                                            //Altera o status do pedido aceito para "E" (Em espera)
                                            alterarStatus(usuario, 'E', 0, auxPedido);

                                            //Aguarda a alteração do status deste pedido
                                            synchronized (lockAlterarStatus){
                                                lockAlterarStatus.wait();
                                            }
                                        }

                                        //Sinaliza que pode continuar pegando os pedidos do servidor
                                        synchronized (lockGetPedidos){
                                            lockGetPedidos.notify();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        throw new RuntimeException(e);
                                    }
                                }
                            }.start();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Erro: " + error);
                        }
                    });

                    AppRequest.getInstance().getRequestQueue().add(jsonArrayRequest);

                    //Aguarda a resposta
                    synchronized (lockGetPedidos){
                        try {
                            lockGetPedidos.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new RuntimeException(e);
                        }
                    }

                    //Pega os pedidos do sistema a cada 5 segundos
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    //Altera o status sem a necessidade de não haver erros na alteração
    public void alterarStatus(final int usuario, final char status, final Context context) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.urlAlterarStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("usuario", usuario + "");
                params.put("status", status + "");

                return params;
            }
        };

        AppRequest.getInstance().getRequestQueue().add(postRequest);
    }

    //Altera o status com precauções para que não ocorram erros
    public void alterarStatus(final int usuario, final char status, final int cont, final Pedido pedido) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.urlAlterarStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("erro")) {
                            //Se houver erro, tenta alterar o status mais 5 vezes
                            if(cont < 5) {
                                alterarStatus(usuario, status, cont + 1, pedido);
                            }else{
                                //Libera a execução e não adiciona o pedido à lista de pedidos em espera
                                synchronized (lockAlterarStatus){
                                    lockAlterarStatus.notify();
                                }
                            }
                        }else{
                            //Adiciona à lista de pedidos em espera se o status for alterado
                            new GerenciaPedido('E').adicionarPedido(pedido);

                            //Libera o sistema para tentar pegar o próximo pedido
                            synchronized (lockAlterarStatus){
                                lockAlterarStatus.notify();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Se houver erro, tenta alterar o status mais 5 vezes
                        if(cont < 5) {
                            alterarStatus(usuario, status, cont + 1, pedido);
                        }else{
                            //Libera a execução e não adiciona o pedido à lista de pedidos em espera
                            synchronized (lockAlterarStatus){
                                lockAlterarStatus.notify();
                            }
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("usuario", usuario + "");
                params.put("status", status + "");

                return params;
            }
        };

        AppRequest.getInstance().getRequestQueue().add(postRequest);
    }

    public void removerPedido(final Button bCancelar, final int usuario, final int posicao,
                              final Context context, final char tipo) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.urlRemoverPedido,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("erro")) {
                            Toast.makeText(context, "Não foi possível cancelar/finalizar o pedido!\nTente novamente", Toast.LENGTH_LONG);
                            bCancelar.setEnabled(true);
                        }else{
                            //Remove o pedido
                            new GerenciaPedido(tipo).removerPedido(posicao);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Não foi possível cancelar/finalizar o pedido!\nTente novamente", Toast.LENGTH_LONG);
                        bCancelar.setEnabled(true);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("usuario", usuario + "");

                return params;
            }
        };

        AppRequest.getInstance().getRequestQueue().add(postRequest);
    }

    public void alterarSenha(){

    }

    public void getClientes(RecyclerView.Adapter clientesAdapter, ProgressBar progressBar){

    }
}
