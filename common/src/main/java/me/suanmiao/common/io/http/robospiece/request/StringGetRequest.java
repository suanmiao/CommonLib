package me.suanmiao.common.io.http.robospiece.request;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by suanmiao on 14/12/15.
 */
public class StringGetRequest extends BaseOkhttpRequest<String> {

    private Map<String, String> params;
    private String url;

    public StringGetRequest(String url, Map<String, String> params) {
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
        try {

            Set<Map.Entry<String,String>> entrys = params.entrySet();
            List<Map.Entry<String,String>> entryList = new ArrayList<>();
            String targetUrl = url;
            entryList.addAll(entrys);
            for (int i = 0;i<entryList.size();i++) {
                Map.Entry<String,String> entry = entryList.get(i);
                String key = entry.getKey();
                String value = entry.getValue();
                try {

                    value = URLEncoder.encode(value, "UTF-8");
                } catch (Exception e) {

                }

                if (i == 0) {
                    targetUrl += ("?" + key + "=" + value);
                } else {
                    targetUrl += ("&" + key + "=" + value);
                }
            }

            HttpGet httpget = new HttpGet(targetUrl);

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            return IOUtils.toString(response.getEntity().getContent());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}
