package com.soa.zeus.service.wx.sdk;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by shizhizhong on 14-10-2.
 */
public enum WXHttpHandle {
    Poll(1);

    private int type;
    private CloseableHttpClient closeableHttpClient;

    WXHttpHandle(int type) {
        this.type = type;
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // Increase max total connection to 200
        cm.setMaxTotal(600);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(200);
        // Increase max connections for localhost:80 to 50
        //        HttpHost localhost = new HttpHost("locahost", 80);
        //        cm.setMaxPerRoute(new HttpRoute(localhost), 50);

        this.closeableHttpClient = HttpClients.custom()
                .setConnectionManager(cm)
        //                .setUserAgent("Up-Cluster/Club/Common")
                .build();
    }



    public CloseableHttpClient client(){
        return closeableHttpClient;
    }


}
