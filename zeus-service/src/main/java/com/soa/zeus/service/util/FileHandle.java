package com.soa.zeus.service.util;

import com.soa.zeus.service.common.Cfg;
import com.soa.zeus.service.wx.sdk.WXHttpHandle;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.PutPolicy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONException;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by shizhizhong on 14-10-10.
 */
public class FileHandle {

    static {
        Config.ACCESS_KEY = Cfg.QN_ACCESS_KEY;
        Config.SECRET_KEY = Cfg.QN_SECRET_KEY;
    }

    public static final String bucket_name = Cfg.QN_NS_NAME;
    public static final String ROOT = "1wenwan";
    public static final String SPLIT = "_";

    public static String fileName(String openId, String photoId, String ex) {
        return ROOT + SPLIT + openId + SPLIT + photoId + ex;
    }

    public static String getFileName(String userId) {
        return ROOT + SPLIT + userId + SPLIT;
    }



    /**
     * 根据url上传
     * @param name
     * @param outUrl
     * @return
     * @throws Exception
     */
    public static String up(String name, String outUrl) throws Exception {
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在
        PutPolicy putPolicy = new PutPolicy(bucket_name);
        String uptoken = putPolicy.token(mac);

        PutExtra extra = new PutExtra();
//        extra.mimeType = "multipart/form-data";
        String key = name;
        PutRet putRet = copyFileFromUrl(outUrl, uptoken, key, extra);
        return putRet.getKey();
    }

    /**
     * 获取上传token
     * @return
     */
    public static String getToken(){
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在
        PutPolicy putPolicy = new PutPolicy(bucket_name);
        putPolicy.scope = bucket_name;
        //秒  一年  31536000
        putPolicy.expires = 3600;
        //仅能新增
        //putPolicy.insertOnly = 1;
        //putPolicy.endUser = "1wenwan_pc";
//        putPolicy.callbackUrl = "";
//        putPolicy.callbackBody = "key=$(key)";
        putPolicy.mimeLimit="image/*";
        //20m
        putPolicy.fsizeLimit = 1048576*20;
//        putPolicy.saveKey = saveKey;


        try {
            return  putPolicy.token(mac);
        } catch (AuthException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }


    private static PutRet copyFileFromUrl(String url, String uptoken, String key, PutExtra extra) {
        CloseableHttpResponse httpReponse = null;
        FileOutputStream outputStream = null;
        try {
            CloseableHttpClient httpClient = WXHttpHandle.Poll.client();
            HttpGet httpget = new HttpGet(url);
            httpReponse = httpClient.execute(httpget);
            //获取状态行
            if (httpReponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpReponse.getEntity();
                System.out.println(entity.getContentLength());
                return IoApi.Put(uptoken, key, entity.getContent(), extra, entity.getContentLength());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpReponse != null)
                try {
                    httpReponse.close();
                } catch (IOException e) {
                }
            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
//        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
//        // 请确保该bucket已经存在
//        String bucketName = "wenwan";
//        PutPolicy putPolicy = new PutPolicy(bucketName);
//        String uptoken = putPolicy.token(mac);
//        PutExtra extra = new PutExtra();
//        String key = "test/p1.jpg";
//        String localFile = "/Users/shizhizhong/myhome/ceshi.jpg";
//        PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
//        System.out.println(FileHandle.up("test1", "http://mmbiz.qpic.cn/mmbiz/WKXD62qbiajskBufuK5APCj4TL5GEHXibn0nxV3icvczz7UZ5hiaibs6xcXAPZdSwGeWQ5q6deERcyMqfuJ8Fn5pY4w/0"));
        System.out.println(System.currentTimeMillis()/1000);
        System.out.println(System.currentTimeMillis());
        System.out.println(FileHandle.getToken());
    }


}
