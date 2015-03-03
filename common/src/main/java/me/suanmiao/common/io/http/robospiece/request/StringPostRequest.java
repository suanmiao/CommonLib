package me.suanmiao.common.io.http.robospiece.request;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by suanmiao on 14/12/15.
 */
public class StringPostRequest extends BaseOkhttpRequest<String> {

    private Map<String, String> params;
    private String url;

    public StringPostRequest(String url,Map<String, String> params) {
        super(String.class);
        this.params = params;
        this.url = url;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        return post();
    }

    private String post() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            Set<String> keys = params.keySet();
            for (String key : keys) {
                String value = params.get(key);
                nameValuePairs.add(new BasicNameValuePair(key, value));
            }

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            return IOUtils.toString(response.getEntity().getContent());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}
