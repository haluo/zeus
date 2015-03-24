package com.soa.zeus.web.util;


import com.soa.zeus.service.common.PageQuery;

/**
 * Created with IntelliJ IDEA.
 * User: shizhizhong
 * Date: 14-2-28
 * Time: 上午11:09
 * To change this template use File | Settings | File Templates.
 */
public class PageUtil {

    public static String page(PageQuery page, String url) {
        int il = 7;
        int sep = il / 2;
        if (page.getCount() < 1)
            return "共0条";
        int pageCount = page.getCount() % page.getPageSize() == 0 ? page.getCount() / page.getPageSize() : (page.getCount() / page.getPageSize() + 1);
        boolean showStart = true, showEnd = true;
        if (page.getP() == 1)
            showStart = false;
        if (page.getP() == pageCount)
            showEnd = false;
        int[] arr = new int[pageCount > il ? il : pageCount];
        int max = 0;
        if (pageCount <= il || page.getP() < sep + 2)
            for (int i = 0; i < arr.length; i++)
                arr[i] = i + 1;
        else {
            if (page.getP() + sep > pageCount)
                max = pageCount;
            else
                max = page.getP() + sep;
            for (int i = 0; i < arr.length; i++)
                arr[i] = max - arr.length + i + 1;
        }
        StringBuffer re = new StringBuffer();
        if (showStart)
            re.append(getPageFirst(url + "&p=1", "首页")).append(getPagePre(url + "&p=" + (page.getP() - 1), "上一页"));
        for (int i = 0; i < arr.length; i++)
            re.append(getPageItem(url + "&p=" + arr[i], arr[i] + "", (arr[i] == page.getP())));
        if (showEnd)
            re.append(getPageNext(url + "&p=" + (page.getP() + 1), "下一页")).append(getPageLast(url + "&p=" + pageCount, "末页"));
        return re.toString();
    }

    public static String getPageItem(String mark, String text, boolean cur) {
        return "<a class=\"" + (cur ? "pp-cur-item" : "pp-item") + "\" href=\"" + mark + "\" topage=\"" + mark + "\">" + text + "</a>";
    }

    public static String getPageFirst(String mark, String text) {
        return "<a class=\"pp-first\" href=\"" + mark + "\" topage=\"" + mark + "\">" + text + "</a>";
    }

    public static String getPageLast(String mark, String text) {
        return "<a class=\"pp-last\" href=\"" + mark + "\" topage=\"" + mark + "\">" + text + "</a>";
    }

    public static String getPagePre(String mark, String text) {
        return "<a class=\"pp-pre\" href=\"" + mark + "\" topage=\"" + mark + "\">" + text + "</a>";
    }

    public static String getPageNext(String mark, String text) {
        return "<a class=\"pp-next\" href=\"" + mark + "\" topage=\"" + mark + "\">" + text + "</a>";
    }


    public static void main(String[] args) {
        PageQuery page = new PageQuery();
        page.setPageSize(2);
        page.setCount(16);
        page.setP(5);
        System.out.println(PageUtil.page(page, "localhost/content/index.action?id=1"));
    }

}
