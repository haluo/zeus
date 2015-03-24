package com.soa.zeus.web.util;


import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class HtmlUtil {

    private static final String ENC = "UTF-8";

    private static final String DOT = ".";

    private static final String URLPREFIX = "/app/";

    private static final int ON_EACH_SIDE = 1;

    private static final int ON_ENDS = 1;

    public static final int HIGHEST_SPECIAL = '>';

    public static char[][] specialCharactersRepresentation = new char[HIGHEST_SPECIAL + 1][];

    static {
        specialCharactersRepresentation['&'] = "&amp;".toCharArray();
        specialCharactersRepresentation['<'] = "&lt;".toCharArray();
        specialCharactersRepresentation['>'] = "&gt;".toCharArray();
        specialCharactersRepresentation['"'] = "&#034;".toCharArray();
        specialCharactersRepresentation['\''] = "&#039;".toCharArray();
    }

    private HtmlUtil() {
    }

    public static int page(int count, int per_page) {
        int num_pages = count / per_page;
        if (count % per_page > 0) {
            num_pages++;
        }
        return num_pages;
    }

    @SuppressWarnings("unchecked")
    public static String pager(HttpServletRequest request, int count, int page_num, int per_page) throws Exception {
        String pageName = "p";
        int num_pages = count / per_page;
        if (count % per_page > 0) {
            num_pages++;
        }

        List page_range;

        if (num_pages <= 10) {
            page_range = range(0, num_pages);
        } else {
            page_range = new ArrayList();
            if (page_num > (ON_EACH_SIDE + ON_ENDS)) {
                if( ON_EACH_SIDE - 1 < 1 )
                    page_range.addAll(range(0, 1));
                else
                    page_range.addAll(range(0, ON_EACH_SIDE - 1));
                page_range.add(DOT);
                page_range.addAll(range(page_num - ON_EACH_SIDE, page_num + 1));
            } else {
                page_range.addAll(range(0, page_num + 1));
            }
            if (page_num < (num_pages - ON_EACH_SIDE - ON_ENDS - 1)) {
                page_range.addAll(range(page_num + 1, page_num + ON_EACH_SIDE + 1));
                page_range.add(DOT);
                page_range.addAll(range(num_pages - ON_ENDS, num_pages));
            } else {
                page_range.addAll(range(page_num + 1, num_pages));
            }
        }

        StringBuilder result = new StringBuilder();
        if (page_num - 1 >= 0) {
            result.append(String.format("<a class=\"page-item\" href=\"%s\">上一页</a>", escapeXml(getQueryUri(request, pageName, new String[]{String.valueOf(page_num - 1)})), page_num - 1));
        }
        for (Object p : page_range) {
            result.append(paginator_number(request, pageName, page_num, p));
        }
        if (page_num + 1 < num_pages) {
            result.append(String.format("<a class=\"page-item\" href=\"%s\">下一页</a>", escapeXml(getQueryUri(request, pageName, new String[]{String.valueOf(page_num + 1)})), page_num + 1));
        }
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    public static String jsonPager(Object outMark, int count, int page_num, int per_page) throws Exception {
        String pageName = "p";
        int num_pages = count / per_page;
        if (count % per_page > 0) {
            num_pages++;
        }

        List page_range;

        if (num_pages <= 10) {
            page_range = range(0, num_pages);
        } else {
            page_range = new ArrayList();
            if (page_num > (ON_EACH_SIDE + ON_ENDS)) {
                page_range.addAll(range(0, ON_EACH_SIDE - 1));
                page_range.add(DOT);
                page_range.addAll(range(page_num - ON_EACH_SIDE, page_num + 1));
            } else {
                page_range.addAll(range(0, page_num + 1));
            }
            if (page_num < (num_pages - ON_EACH_SIDE - ON_ENDS - 1)) {
                page_range.addAll(range(page_num + 1, page_num + ON_EACH_SIDE + 1));
                page_range.add(DOT);
                page_range.addAll(range(num_pages - ON_ENDS, num_pages));
            } else {
                page_range.addAll(range(page_num + 1, num_pages));
            }
        }

        StringBuilder result = new StringBuilder();

        for (Object p : page_range) {
            String pStr = "";
            if (p.equals(DOT)) {
                pStr = "<span>...</span>";
            }
            int i = (Integer) p;
            if (i == page_num)
                pStr = String.format("<a  class=\"page-curr-item\"  jpMark=\"" + outMark.toString() + "\" href=\"javascript:;\" >%d</a>", i + 1);
            else
                pStr = String.format("<a class=\"page-item\" jpMark=\"" + outMark.toString() + "\" href=\"javascript:;\" >%d</a>", i + 1);
            result.append(pStr);
        }

        return result.toString();
    }

    @SuppressWarnings("unchecked")
    public static String sortUri(HttpServletRequest request, String property, String sp, String so) throws Exception {
        String spName = "sp";
        String soName = "so";
        Map newParams = new HashMap();
        newParams.put(spName, new String[]{property});
        if (property.equals(sp)) {
            newParams.put(soName, new String[]{"desc".equals(so) ? "asc" : "desc"});
        } else if (StringUtils.isNotEmpty(so)) {
            newParams.put(soName, new String[]{"desc"});
        }
        return escapeXml(getQueryUri(request, newParams));
    }

    @SuppressWarnings("unchecked")
    public static String sortUri(HttpServletRequest request, String property, String sp, String so, String sortKey) throws Exception {
        String spName = "sp";
        String soName = "so";
        Map newParams = new HashMap();
        newParams.put(spName, new String[]{property});
        if (property.equals(sp) && property.equals(sortKey)) {
            newParams.put(soName, new String[]{"desc".equals(so) ? "asc" : "desc"});
        } else if (StringUtils.isNotEmpty(sortKey)) {
            newParams.put(soName, new String[]{"asc"});
        }
        return escapeXml(getQueryUri(request, newParams));
    }

    @SuppressWarnings("unchecked")
    public static String currentUri(HttpServletRequest request, Map newParams) throws Exception {
        return escapeXml(getQueryUri(request, newParams));
    }

    @SuppressWarnings("unchecked")
    public static String currentUri(HttpServletRequest request) throws Exception {
        String url = request.getRequestURI();
        String ctx = request.getContextPath();
        int p = (ctx + URLPREFIX).length();
        //去掉spring监听的前缀
        if (url.substring(0, p).equals(ctx + URLPREFIX)) {
            url = "/" + url.substring(p, url.length());
        }
        StringBuilder uri = new StringBuilder(url);

        int count = 0;
        Map params = new HashMap();
        params.putAll(request.getParameterMap());

        Set<String> keys = params.keySet();
        for (String key : keys) {
            String[] values = (String[]) params.get(key);
            String keyEnc = URLEncoder.encode(key, ENC);
            for (String value : values) {
                String valueEnc = URLEncoder.encode(value, ENC);
                uri.append(count == 0 ? '?' : '&').append(keyEnc).append('=').append(valueEnc);
                count++;
            }
        }
        return uri.toString();
    }

    @SuppressWarnings("unchecked")
    public static String currentUri(HttpServletRequest request, String porperty, String porpertyValue) {

        String url = request.getRequestURI();
        String ctx = request.getContextPath();
        int p = (ctx + URLPREFIX).length();
        //去掉spring监听的前缀
        if (url.substring(0, p).equals(ctx + URLPREFIX)) {
            url = ctx + "/" + url.substring(p, url.length());
        }
        StringBuilder uri = new StringBuilder(url);

        int count = 0;
        Map params = new HashMap();
        params.putAll(request.getParameterMap());
        if (StringUtils.isNotEmpty(porperty)) {
            if (StringUtils.isNotEmpty(porpertyValue)) {
                params.put(porperty, new String[]{porpertyValue});
            } else {
                params.put(porperty, new String[]{});
            }

        }

        Set<String> keys = params.keySet();
        for (String key : keys) {
            String[] values = (String[]) params.get(key);
            String keyEnc = null;
            try {
                keyEnc = URLEncoder.encode(key, ENC);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (String value : values) {
                String valueEnc = null;
                try {
                    valueEnc = URLEncoder.encode(value, ENC);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                uri.append(count == 0 ? '?' : '&').append(keyEnc).append('=').append(valueEnc);
                count++;
            }
        }
        return uri.toString();
    }


    @SuppressWarnings("unchecked")
    private static List range(int start, int end) {
        List items = new ArrayList();
        for (int i = start; i < end; i++) {
            items.add(i);
        }
        return items;
    }


    private static String paginator_number(HttpServletRequest request, String pageName, int page_num, Object p) throws Exception {
        if (p.equals(DOT)) {
            return "<span>...</span>";
        }
        int i = (Integer) p;
        if (i == page_num) {
            return String.format("<a  class=\"page-curr-item\" href=\"javascript:;\">%d</a>", i + 1);
        }
        return String.format("<a class=\"page-item\" href=\"%s\">%d</a>", escapeXml(getQueryUri(request, pageName, new String[]{p.toString()})), i + 1);
    }

    private static String getQueryUri(HttpServletRequest request, String paramName, String[] paramValue) throws UnsupportedEncodingException {
        Map<String, String[]> newParams = new HashMap<String, String[]>();
        newParams.put(paramName, paramValue);
        return getQueryUri(request, newParams);
    }

    @SuppressWarnings("unchecked")
    private static String getQueryUri(HttpServletRequest request, final Map<String, String[]> newParams) throws UnsupportedEncodingException {
        String url = request.getRequestURI();
        String ctx = request.getContextPath();
        int p = (ctx + URLPREFIX).length();
        //去掉spring监听的前缀
        if (url.substring(0, p).equals(ctx + URLPREFIX)) {
            url = ctx + "/" + url.substring(p, url.length());
        }
        StringBuilder uri = new StringBuilder(url);

        int count = 0;
        Map params = new HashMap();
        params.putAll(request.getParameterMap());
        if (newParams != null) {
            Set<String> nks = newParams.keySet();
            for (String nk : nks) {
                params.put(nk, newParams.get(nk));
            }
        }


        Set<String> keys = params.keySet();
        for (String key : keys) {
            String[] values = (String[]) params.get(key);
            String keyEnc = URLEncoder.encode(key, ENC);
            for (String value : values) {
                String valueEnc = URLEncoder.encode(value, ENC);
                uri.append(count == 0 ? '?' : '&').append(keyEnc).append('=').append(valueEnc);
                count++;
            }
        }
        return uri.toString();
    }

    /**
     * Performs the following substring replacements
     * (to facilitate output to XML/HTML pages):
     * <p/>
     * & -> &amp;
     * < -> &lt;
     * > -> &gt;
     * " -> &#034;
     * ' -> &#039;
     * <p/>
     * See also OutSupport.writeEscapedXml().
     */
    public static String escapeXml(String buffer) {
        int start = 0;
        int length = buffer.length();
        char[] arrayBuffer = buffer.toCharArray();
        StringBuffer escapedBuffer = null;

        for (int i = 0; i < length; i++) {
            char c = arrayBuffer[i];
            if (c <= HIGHEST_SPECIAL) {
                char[] escaped = specialCharactersRepresentation[c];
                if (escaped != null) {
                    // create StringBuffer to hold escaped xml string
                    if (start == 0) {
                        escapedBuffer = new StringBuffer(length + 5);
                    }
                    // add unescaped portion
                    if (start < i) {
                        escapedBuffer.append(arrayBuffer, start, i - start);
                    }
                    start = i + 1;
                    // add escaped xml
                    escapedBuffer.append(escaped);
                }
            }
        }
        // no xml escaping was necessary
        if (start == 0) {
            return buffer;
        }
        // add rest of unescaped portion
        if (start < length) {
            escapedBuffer.append(arrayBuffer, start, length - start);
        }
        return escapedBuffer.toString();
    }
}
