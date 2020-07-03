package com.rainbow.superschedule.utils;

import java.util.Calendar;

/**
 * 时间日期操作类(内容不全待补充)
 * 避免使用全局静态Calendar变量,因为会有set的问题出现
 * Created By Rainbow on 2019/4/30.
 */
public class DateUtils {
    public static int getDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1;
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getDayOfWeek() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取某年某月的天数
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String[] parseTime(String times) {
        String[] t = times.split(" ");
        String[] date = parseDate(t[0]);
        String[] hourMin = parseHourAndMin(t[1]);
        return new String[]{date[0], date[1], date[2], hourMin[0], hourMin[1]};
    }

    public static String[] parseDate(String times) {
        return times.split("-");
    }

    public static String[] parseHourAndMin(String times) {
        return times.split(":");
    }
}
