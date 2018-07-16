package com.thxy.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 */

public class DateTimeUtil {
    public static final SimpleDateFormat SIMPLE_FORMAT =new SimpleDateFormat("yy-MM-dd", Locale.ENGLISH);

    public static final SimpleDateFormat FORMAT =new SimpleDateFormat("MM-dd HH:mm", Locale.ENGLISH);

    public static String getSimpleDate(Date date){
        return SIMPLE_FORMAT.format(date);
    }

    public static String getDate(Date date){
        return FORMAT.format(date);
    }
}
