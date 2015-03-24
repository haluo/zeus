package com.soa.zeus.service.wx.sdk.util;

import com.soa.zeus.service.common.Cfg;
import com.soa.zeus.service.wx.sdk.WXHttpHandle;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by shizhizhong on 14-10-13.
 */
public class Menu {

    public static String AT = "JSTasX6ScIj0MVzAg9e8o0WDg8_Jwk5kp1jh-NwIeR_E0e7bXjKBTqldFN12ChCkc0W5XKjxMv_nz8TXo_9SqLdekNFbHxp_jwI9El6pzeY";

    public static String createMenus(){
        CloseableHttpResponse httpReponse = null;
        try {
            CloseableHttpClient httpClient = WXHttpHandle.Poll.client();
            HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+AT );
            String path = Class.class.getClass().getResource("/").getPath();

//            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//            nvps.add(new BasicNameValuePair("access_token", AT));
//            nvps.add(new BasicNameValuePair("body", Cfg.OM.writeValueAsString(Cfg.OM.readValue(new File(path + "wx/menu.json"), Map.class))));
//            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
            httpPost.setEntity(new StringEntity(Cfg.OM.writeValueAsString(Cfg.OM.readValue(new File(path + "wx/menu.json"), Map.class)), Consts.UTF_8));
            httpReponse = httpClient.execute(httpPost);
            //获取状态行
            if (httpReponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpReponse.getEntity();
                return EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpReponse != null)
                try {
                    httpReponse.close();
                } catch (IOException e) {
                }
        }
        return null;

    }


    public static void main(String[] args) {
//        try{
//            String path = Class.class.getClass().getResource("/").getPath();
//            System.out.println(Cfg.OM.writeValueAsString(Cfg.OM.readValue(new File(path+"wx/menu.json"), Map.class)));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        System.out.println(Menu.createMenus());
//                "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx1b43986b12be48f1&redirect_uri=http%3A%2F%2Fwww.1wenwan.com%2Fwx%2Fcheck%2Flink&response_type=code&scope=snsapi_base&state=1#wechat_redirect"
//        System.out.println(URLEncoder.encode("http://1wenwan.com/wx/check/link"));
    }
}
