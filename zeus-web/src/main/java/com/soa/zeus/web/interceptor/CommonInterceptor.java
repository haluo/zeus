/**
 *
 */
package com.soa.zeus.web.interceptor;

import com.soa.zeus.service.domain.User;
import com.soa.zeus.service.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shi Zhizhong
 * @ClassName: CommonInterceptor
 * @Description: TODO
 * @date 2011-7-18 下午05:32:47
 */
public class CommonInterceptor extends HandlerInterceptorAdapter {

    private static Logger log = Logger.getLogger(CommonInterceptor.class);
    public static String FIT_PATH_MARK = "/app";
    public static final String COOKI_KEY_WEB = "dbww.web";
    public static final String CHECK_URL_PRE = "/u/.*";
    public static final String USER_CHECK_URL = "/login";
    public static final String USER_COOKIE_KEY = "_user_id";
    public static final String ATTR_CUR_USER = "currentUser";

    public static final String MENU_KEY = "m_cur";
    public static final String MENU_SHOW_PRE = "/content/";
    public static final String MENU_TRADE_PRE = "/trade/";
    public static final String MENU_SHOW = "m_content";
    public static final String MENU_TRADE = "m_trade";

    /**
     * 方法执行前的拦截方法
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        String url = request.getRequestURI();
        String controllerUrl = getControllerPath(url, request.getContextPath());
        Pattern pattern = Pattern.compile(CHECK_URL_PRE);
        Matcher matcher = pattern.matcher(controllerUrl);
        User user = null;
        String userId = getCookieValue(request.getCookies(), USER_COOKIE_KEY);
        if (StringUtils.isNotEmpty(userId))
            user = userService.getById(Integer.valueOf(userId));
        if (matcher.matches()) {
            if (user == null) {
                response.sendRedirect(request.getContextPath() + USER_CHECK_URL);
                return false;
            }
        }
        if (user != null)
            request.setAttribute(ATTR_CUR_USER, user);
        return true;
    }

    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }


    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        String url = request.getRequestURI();
        String p = "/app", u = "/u";
        if (StringUtils.indexOf(url, p + MENU_SHOW_PRE) >= 0 || StringUtils.indexOf(url, p + u + MENU_SHOW_PRE) >= 0)
            request.setAttribute(MENU_KEY, MENU_SHOW);
        if (StringUtils.indexOf(url, p + MENU_TRADE_PRE) >= 0 || StringUtils.indexOf(url, p + u + MENU_TRADE_PRE) >= 0)
            request.setAttribute(MENU_KEY, MENU_TRADE);
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
                    log.error("get value from cookie error：", e);
                }
                break;
            }
        }
        return value;
    }

    /**
     * 获得controller url
     *
     * @param url
     * @param contextPath
     * @return
     */
    private String getControllerPath(String url, String contextPath) {
        return StringUtils.substringAfter(StringUtils.substringBefore(
                StringUtils.substringBefore(url, "?"), "."), contextPath + FIT_PATH_MARK);
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


    @Resource
    private IUserService userService;
}
