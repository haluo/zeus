package com.soa.zeus.service.wx.sdk;

import com.soa.zeus.service.common.Cfg;
import com.soa.zeus.service.util.CommonUtil;
import com.soa.zeus.service.wx.sdk.vo.WXMsg;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by shizhizhong on 14-10-2.
 */
public class WXClient {

    public static Logger log = Logger.getLogger("WXClient");

    /**
     * 验证微信推送
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkFormWX(String signature, String timestamp, String nonce) {
        try {
            if (StringUtils.isBlank(signature) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)) {
                return false;
            }
            List<String> list = new ArrayList<String>();
            list.add(WXCfg.TOKEN);
            list.add(timestamp);
            list.add(nonce);
            Collections.sort(list);
            String re = DigestUtils.shaHex(CommonUtil.join(list.toArray(), ""));
            if (re.equals(signature)) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static WXMsg getWXMsg(HttpServletRequest request) {
        try {
            //处理接收消息
            ServletInputStream in = request.getInputStream();
            //将POST流转换为XStream对象
            XStream xs = new XStream(new DomDriver());
            //将指定节点下的xml节点数据映射为对象
            xs.alias("xml", WXMsg.class);
            //将流转换为字符串
            StringBuilder xmlMsg = new StringBuilder();
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) != -1; ) {
                xmlMsg.append(new String(b, 0, n, "UTF-8"));
            }
            //将xml内容转换为InputMessage对象
            return (WXMsg) xs.fromXML(xmlMsg.toString());

        } catch (Exception e) {

        }
        return null;
    }

    public static String getAccessToken() {
        long index = System.currentTimeMillis();
        CloseableHttpResponse httpReponse = null;
        boolean bool = false;
        try {
            CloseableHttpClient httpClient = WXHttpHandle.Poll.client();
            String url = WXCfg.access_token_url + "&appid=" + WXCfg.appId + "&secret=" + WXCfg.appSecret;
            HttpGet httpGet = new HttpGet(url);
            httpReponse = httpClient.execute(httpGet);
            //获取状态行
            if (httpReponse.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK) {
                HttpEntity entity = httpReponse.getEntity();
                String str = EntityUtils.toString(entity);
                Map<String, String> map = Cfg.OM.readValue(str, Map.class);
                return map.get("access_token");
            }
        } catch (Exception e) {
            log.error("handleCdn error", e);
        } finally {
            if (httpReponse != null)
                try {
                    httpReponse.close();
                } catch (IOException e) {
                    log.error(e);
                }
        }
        return null;
    }

    public static XStream getXStream() {
        return new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    @Override
                    protected void writeText(QuickWriter writer,
                                             String text) {
                        if (!text.startsWith("<![CDATA[")) {
                            text = "<![CDATA[" + text + "]]>";
                        }
                        writer.write(text);
                    }
                };
            }
        });
    }


    public static void main(String[] args) {
        System.out.println(WXClient.getAccessToken());
//        System.out.println(WXClient.checkFormWX("b5b06610420a4c506b4160b4f66033742bf3bf1c","1417083254","1824799744"));
//        https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx1b43986b12be48f1&redirect_uri=http://www.1wenwan.com/wx/check/link&response_type=code&scope=snsapi_base&state=3#wechat_redirect
    }
}
