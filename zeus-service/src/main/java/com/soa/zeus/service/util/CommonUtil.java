package com.soa.zeus.service.util;

/**
 * Created by shizhizhong on 14-10-2.
 */
public class CommonUtil {

    public static String join(Object[] o, String flag) {
        StringBuffer str_buff = new StringBuffer();

        for (int i = 0, len = o.length; i < len; i++) {
            str_buff.append(String.valueOf(o[i]));
            if (i < len - 1) str_buff.append(flag);
        }

        return str_buff.toString();
    }

}
