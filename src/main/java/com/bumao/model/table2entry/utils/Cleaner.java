package com.bumao.model.table2entry.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cleaner {
    public static String clean(String str){
        String pattern = "^(`|')(.*)(`|')$";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(str);
        if (m.find()) {
//            System.out.println("Found value: " + m.group(0) );
//            System.out.println("Found value: " + m.group(1) );
//            System.out.println("Found value: " + m.group(2) );
            return m.group(2);
        }
        return str;
    }

    public static void main(String[] args) {
        System.out.println( clean("`'a``s`s'`") );
    }
}
