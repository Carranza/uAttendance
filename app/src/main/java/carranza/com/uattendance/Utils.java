package carranza.com.uattendance;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Francisco on 5/9/2015.
 */
public class Utils {
    private static String host = "http://www.makingapps.es/upi/";

    public static int flag = 0;

    public static String getHost() {
        return host;
    }

    public static JSONArray documents;

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;
    }

    public static String requestGet(String url) {
        HttpClient httpClient = null;
        HttpGet request = null;
        HttpResponse httpResponse = null;
        InputStream inputStream = null;
        String result = "";

        try {
            httpClient = new DefaultHttpClient();

            request = new HttpGet(url);
            // request.addHeader("X_UPI_PASSWORD", UserModel.getUser().getPassword());
            // request.addHeader("X_UPI_USERNAME", UserModel.getUser().getEmail());
            request.addHeader("X_PASSWORD", "caracola");
            request.addHeader("X_USERNAME", "carranzafr@gmail.com");

            httpResponse = httpClient.execute(request);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "error";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String requestPost(String url, List<NameValuePair> values) {
        HttpClient httpClient = null;
        HttpPost request = null;
        HttpResponse httpResponse = null;
        InputStream inputStream = null;
        String result = "";

        try {
            httpClient = new DefaultHttpClient();

            request = new HttpPost(url);
            // request.addHeader("X_UPI_PASSWORD", UserModel.getUser().getPassword());
            // request.addHeader("X_UPI_USERNAME", UserModel.getUser().getEmail());
            request.addHeader("X_PASSWORD", "caracola");
            request.addHeader("X_USERNAME", "carranzafr@gmail.com");

            if (!values.isEmpty())
                request.setEntity(new UrlEncodedFormEntity(values));

            httpResponse = httpClient.execute(request);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "error";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
