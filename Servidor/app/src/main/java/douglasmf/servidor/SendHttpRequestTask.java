package douglasmf.servidor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL("https://obconic-discount.000webhostapp.com/PHPDelivery/ImagensCardapio/Xis.jpeg");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }catch (Exception e){
            System.out.println("Erro!");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        AppRequest.getInstance().imagens.add(result);
    }
}
