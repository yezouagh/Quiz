package ezouagh.quiz;

/**
 * Created by Younes on 04-Jul-15.
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class  JSONParser {
     static String TAG_UTF = "utf-8";
    public static JSONObject makeHttpRequest(String url, List<NameValuePair> params) {
        JSONObject jObj = null;
        String json = "";
        try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params,TAG_UTF );
                String ParamsURL=url+"?" + paramString;
                HttpGet httpGet = new HttpGet(ParamsURL);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                json = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try
        {
            if(!json.trim().equals(""))
            jObj = new JSONObject(json);
            else
                return makeHttpRequest(url,params);
        }
        catch (JSONException e) {
            e.printStackTrace();
           }catch (Exception e) {
            e.printStackTrace();
        }
        return jObj;
    }
}
