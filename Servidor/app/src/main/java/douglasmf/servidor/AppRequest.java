package douglasmf.servidor;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class AppRequest extends Application {
    private RequestQueue queue = null;
    private static AppRequest instance;
    public ArrayList<ItemCardapio> cardapio = new ArrayList<>();
    public ArrayList<Pedido> pedidosEspera = new ArrayList<>();
    public ArrayList<Pedido> pedidosPreparo = new ArrayList<>();
    public ArrayList<Bitmap> imagens = new ArrayList<>();
    public ArrayList<Cliente> clientes = new ArrayList<>();
    public RecyclerView.Adapter emEsperaAdapter;
    public RecyclerView.Adapter emPreparoAdapter;
    public boolean threadGetPedidos = false; //Flag para evitar que o programa execute mais de uma thread para pegar os pedidos
    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized AppRequest getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }

        return queue;
    }
}