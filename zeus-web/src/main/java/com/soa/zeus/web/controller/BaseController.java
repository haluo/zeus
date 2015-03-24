package com.soa.zeus.web.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by shizhizhong on 15/2/10.
 */
public class BaseController {
    public static Logger log = Logger.getLogger(BaseController.class);
    public static ObjectMapper om = new ObjectMapper();

    //公用分页大小
    public static int PAGE2 = 2;
    public static int PAGE5 = 5;
    public static int PAGE10 = 10;
    public static int PAGE20 = 20;
    //默认的cookie过期时间
    private static final int COOKIE_TIMES = 365 * 24 * 60 * 60;
    private static final int DEF_COOKIE_TIMES = 60 * 60;

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

//    //根据拦截器cookie获得当前用户
//    public User getCurUser(HttpServletRequest request){
//        if (request.getAttribute(CommonInterceptor.ATTR_CUR_USER) == null)
//            return null;
//        return (User) request.getAttribute(CommonInterceptor.ATTR_CUR_USER);
//    }


    public ModelAndView to404() {
        return new ModelAndView("/error/404.vm");
    }

    public ModelAndView to404(ModelAndView mv) {
        mv.setViewName("/error/404.vm");
        return mv;
    }

    public static String formatStr(String value) {
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
//        return StringEscapeUtils.escapeSql(StringEscapeUtils.escapeJavaScript(StringEscapeUtils.escapeHtml(value)));
        return value;
    }

    public static void addCookiItem(String key, String value, HttpServletRequest request, HttpServletResponse response) {
        addCookiItem(key, value, DEF_COOKIE_TIMES, request, response);
    }

    /**
     * 全站范围内添加缓存
     *
     * @param key
     * @param value
     * @param time
     * @param request
     * @param response
     */
    public static void addCookiItem(String key, String value, int time, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (value != null) {
                value = URLEncoder.encode(value, "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(time);
        String path = request.getContextPath();
        if (StringUtils.isEmpty(request.getContextPath())) {
            path = "/";
        }
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static void removeCookiItem(String key, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        String path = request.getContextPath();
        if (StringUtils.isEmpty(request.getContextPath())) {
            path = "/";
        }
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    /**
     * 根据cookie key获得value
     *
     * @param cookies
     * @param key
     * @return
     */
    public static String getCookieValue(Cookie[] cookies, String key) {
        String value = null,
                name = "";
        if (cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            name = cookie.getName();
            if (key.equals(name)) {
                try {
                    value = cookie.getValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return value;
    }

    public void setCurrentNav( ModelAndView mv, String navKey){
        mv.addObject("_current_nav", navKey);
    }

    public static void main(String[] args) {

    }
}
