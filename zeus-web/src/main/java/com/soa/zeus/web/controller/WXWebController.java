package com.soa.zeus.web.controller;

import com.soa.zeus.service.common.Cfg;
import com.soa.zeus.service.wx.sdk.WXCfg;
import com.soa.zeus.service.wx.sdk.WXClient;
import com.soa.zeus.service.wx.sdk.WXHttpHandle;
import com.soa.zeus.service.wx.sdk.vo.ToWXMsgText;
import com.soa.zeus.service.wx.sdk.vo.WXMsg;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by shizhizhong on 14-10-19.
 */
@Controller
public class WXWebController extends BaseController {

    public static Logger log = Logger.getLogger("wx-accept");

    @ResponseBody
    @RequestMapping(value = "/wx/accept", method = RequestMethod.GET)
    public String accept(String signature, String timestamp, String nonce, String echostr) {
        //微信验证
        if (WXClient.checkFormWX(signature, timestamp, nonce)) {
            return echostr;
        }
        return FAIL;
    }

    @RequestMapping(value = "/wx/accept", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public void accept(String signature, String timestamp, String nonce, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        log.info("/wx/accept:"+timestamp);
        log.info("/wx/accept:"+signature+"    "+timestamp+"    "+nonce);
        response.setContentType("text/html;charset=UTF-8");
        try {
            //微信验证
            if (WXClient.checkFormWX(signature, timestamp, nonce)) {
                Date date = new Date();
                WXMsg wxMsg = WXClient.getWXMsg(request);
//                log.info("/wx/accept wxMsg:"+ wxMsg.getMsgType());
                String content = "";
                //接收文字信息
//                if (wxMsg.getMsgType().equals(WXMsg.Type.Text.toString())) {
                content = "hi,小麦送马上就要上线了,敬请期待！";
//                }
                //接收图片信息
//                if (wxMsg.getMsgType().equals(WXMsg.Type.Image.toString())) {
////                    content = WXCfg.httpPre + "/user/photo/" + wxMsg.getFromUserName();
//                    content = WXCfg.httpPre + "/mobile/index/" + wxMsg.getFromUserName();
//                    UserPhoto userPhoto = new UserPhoto();
//                    userPhoto.setOpenid(wxMsg.getFromUserName());
//                    userPhoto.setPicUrl(wxMsg.getPicUrl());
//                    userPhoto.setType(0);
//                    userPhoto.setStatus(0);
//                    userPhoto.setModified(date);
//                    userPhoto.setCreated(date);
//                    Integer id = userPhotoService.save(userPhoto);
//                    if (id != null)
//                        FileHandle.up(FileHandle.fileName(wxMsg.getFromUserName(), id.toString(), ".jpg"), wxMsg.getPicUrl());
//                }
                //事件推送
                if (wxMsg.getMsgType().equals(WXMsg.Type.Event.toString())) {
                    //关注事件
                    if( wxMsg.getEvent().equalsIgnoreCase(WXMsg.EventType.Subscribe.toString()) ){

                    }
                    //点击菜单事件
                    if( wxMsg.getEvent().equalsIgnoreCase(WXMsg.EventType.Click.toString()) ){
                        if(wxMsg.getEventKey().equals("m_key_1")){
                            content = "您还未有历史订单，敬请期待！";
                        }
                        if(wxMsg.getEventKey().equals("m_key_2")){
                            content = "小麦侠闪电送！运费5到10元，营业时间：上午10点至深夜~";
                        }
                        if(wxMsg.getEventKey().equals("m_key_3")){
                            content = "亲，欢迎您随时向我们反馈意见和建议。我们一直在努力！您可以通过微信直接联系我们，也欢迎拨打：400-8888-888。";
                        }
                        if(wxMsg.getEventKey().equals("m_key_4")){
                            content = "APP即将上线，敬请期待！";
                        }

                    }
                }


                String xml = "";
//                if (wxMsg.getMsgType().equals(WXMsg.Type.Text.toString()) || wxMsg.getMsgType().equals(WXMsg.Type.Image.toString())) {
                //return
                ToWXMsgText toWXMsgText = new ToWXMsgText();
                toWXMsgText.setFromUserName(WXCfg.wxCode);
                toWXMsgText.setToUserName(wxMsg.getFromUserName());
                toWXMsgText.setContent(content);
                toWXMsgText.setMsgType(WXMsg.Type.Text.toString());
                toWXMsgText.setCreateTime(new Date().getTime());
                XStream xStream = WXClient.getXStream();
                xStream.alias("xml", ToWXMsgText.class);
                xml = xStream.toXML(toWXMsgText);
//                }
//                log.info("xml:"+xml);
                response.getWriter().write(xml);
            }
        } catch (Exception e) {
            log.error("wxAccept error", e);
        }
        //def
        response.getWriter().write("");
    }

    @RequestMapping(value = "/wx/index")
    public ModelAndView index(){
        ModelAndView mv  =new ModelAndView("/wx/index");

        return mv;
    }

    @RequestMapping(value = "/wx/coupon")
    public ModelAndView coupon(){
        ModelAndView mv  =new ModelAndView("/wx/coupon");

        return mv;
    }



    @RequestMapping(value = "/wx/check/link")
    public ModelAndView checkLink(String code, String state) {
        log.info("checkLink code:"+code);
        if (StringUtils.isNotBlank(code)) {

            Map<String, Object> re = getOpenIdFormOAuth(code);
            log.info("getOpenIdFormOAuth code:"+ re );
            if (re.get("openid") != null) {
                String openid = re.get("openid").toString();
                if ("1".equals(state)) {
                    log.info("checkLink goto vm" );
                    return new ModelAndView("redirect:/user/info/" + openid);
                }
            }
        }
        return new ModelAndView("redirect:/");
    }


    public Map<String, Object> getOpenIdFormOAuth(String code) {
        CloseableHttpResponse httpReponse = null;
        try {
            CloseableHttpClient httpClient = WXHttpHandle.Poll.client();
            HttpGet httpget = new HttpGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WXCfg.appId + "&secret=" + WXCfg.appSecret + "&code=" + code + "&grant_type=authorization_code");
            httpReponse = httpClient.execute(httpget);
            //获取状态行
            if (httpReponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpReponse.getEntity();
                return Cfg.OM.readValue(EntityUtils.toString(entity), Map.class);
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
        Date date = new Date();
        System.out.println(date.getTime());
    }

}
