package com.soa.zeus.service.common;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by shizhizhong on 15/2/2.
 */
public class Cfg {

    public static String QN_ACCESS_KEY = "3VqtW8b4AGU6bkU3PQLf7K9HuiNpzpzfOKP-Gp3I";
    public static String QN_SECRET_KEY = "Ab0Mje7NflzgKHp7ainKdAOiHeqP0KGzypDdBbnA";


    public static String QN_NS_NAME = "dbww";
    public static String QN_NS_HTTP_PRE = "http://dbww.qiniudn.com";

    public static ObjectMapper OM = new ObjectMapper();

    public void setQN_NS_NAME(String QN_NS_NAME) {
        Cfg.QN_NS_NAME = QN_NS_NAME;
    }

    public void setQN_NS_HTTP_PRE(String QN_NS_HTTP_PRE) {
        Cfg.QN_NS_HTTP_PRE = QN_NS_HTTP_PRE;
    }
}
