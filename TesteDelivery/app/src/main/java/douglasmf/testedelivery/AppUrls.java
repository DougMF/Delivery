package douglasmf.testedelivery;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppUrls {
    //Url de login
    public static String urlLogin = "https://obconic-discount.000webhostapp.com/PHPDelivery/Cliente/Login.php";

    //Url de cadastro
    public static String urlCadastro = "https://obconic-discount.000webhostapp.com/PHPDelivery/Cliente/Cadastro.php";

    //Url para extrair o cardápio do banco
    public static String urlGetCardapio = "https://obconic-discount.000webhostapp.com/PHPDelivery/Cliente/GetCardapio.php";

    //Url para extrair o cardápio de bebidas do banco
    public static String urlGetBebidas = "https://obconic-discount.000webhostapp.com/PHPDelivery/Cliente/GetBebidas.php";

    //Url para enviar o pedido ao banco
    public static String urlEnviarPedido = "https://obconic-discount.000webhostapp.com/PHPDelivery/Cliente/EnviarPedido.php";

    //Url para extrair o status do banco
    public static String urlVerificarStatus = "https://obconic-discount.000webhostapp.com/PHPDelivery/Cliente/VerificarStatus.php";
}