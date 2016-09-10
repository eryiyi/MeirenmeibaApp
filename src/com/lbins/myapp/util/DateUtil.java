package com.lbins.myapp.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhl on 2015/2/1.
 */
public class DateUtil {

    /**
     * 根据日期获得毫秒值
     * @param str
     * @return
     */
    public static long getMs(String str, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDate(String time, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(Long.parseLong(time));
        return sdf.format(date);
    }



    public static String getDateAndTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//可以方便地修改日期格式
        return dateFormat.format( new Date() );
    }

    public static String getNoteDateline(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");//可以方便地修改日期格式
        return dateFormat.format( new Date() );
    }

    public static String getDateAndTimeByDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//可以方便地修改日期格式
        return dateFormat.format( date );
    }

    public static String getDateAndTimeTwo(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        return  dateFormat.format(new Date());
    }

    //获得年
    public static String getYear(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
        return dateFormat.format(new Date());
    }
    //获得月
    public static String getMonth(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");//可以方便地修改日期格式
        return dateFormat.format(new Date());
    }

    public static String getDay(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");//可以方便地修改日期格式
        return dateFormat.format(new Date());
    }
    //获得年和月
    public static String getYearAndMonth(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");//可以方便地修改日期格式
        return dateFormat.format(new Date());
    }

    //获得月和日
    public static String getMonthAndDay(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");//可以方便地修改日期格式
        return dateFormat.format(date);
    }

    // 获取当前时间所在年的周数
    public static int getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

//    // 获取某年的第几周的开始日期
//    public static Date getFirstDayOfWeek(int year, int week) {
//        Calendar c = new GregorianCalendar();
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, Calendar.JANUARY);
//        c.set(Calendar.DATE, 1);
//
//        Calendar cal = (GregorianCalendar) c.clone();
//        cal.add(Calendar.DATE, week * 7);
//        return getFirstDayOfWeek(cal.getTime());
//    }
//
//    // 获取某年的第几周的结束日期
//    public static Date getLastDayOfWeek(int year, int week) {
//        Calendar c = new GregorianCalendar();
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, Calendar.JANUARY);
//        c.set(Calendar.DATE, 1);
//        Calendar cal = (GregorianCalendar) c.clone();
//        cal.add(Calendar.DATE, week * 7);
//        return getLastDayOfWeek(cal.getTime());
//    }

//    // 获取当前时间所在周的开始日期
//    public static Date getFirstDayOfWeek(Date date) {
//        Calendar c = new GregorianCalendar();
//        c.setFirstDayOfWeek(Calendar.MONDAY);
//        c.setTime(date);
//        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
//        return c.getTime();
//    }
//
//    // 获取当前时间所在周的结束日期
//    public static Date getLastDayOfWeek(Date date) {
//        Calendar c = new GregorianCalendar();
//        c.setFirstDayOfWeek(Calendar.SUNDAY);
//        c.setTime(date);
//        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
//        return c.getTime();
//    }

    // 获得当前周- 周一的日期
//    public  static   String getCurrentMonday() {
//        int mondayPlus = getMondayPlus();
//        GregorianCalendar currentDate = new GregorianCalendar();
//        currentDate.add(GregorianCalendar.DATE, mondayPlus);
//        Date monday = currentDate.getTime();
////        DateFormat df = DateFormat.getDateInstance();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");//可以方便地修改日期格式
//        String preMonday = dateFormat.format(monday);
//        return preMonday;
//    }

    // 获得当前周- 周日  的日期
//    public  static  String getPreviousSunday() {
//        int mondayPlus = getMondayPlus();
//        GregorianCalendar currentDate = new GregorianCalendar();
//        currentDate.add(GregorianCalendar.DATE, mondayPlus +6);
//        Date monday = currentDate.getTime();
////        DateFormat df = DateFormat.getDateInstance();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");//可以方便地修改日期格式
//        String preMonday = dateFormat.format(monday);
//        return preMonday;
//    }

    // 获得当前日期与本周一相差的天数
    public  static   int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }



    /**
     * get Calendar of given year
     * @param year
     * @return
     */
    private static Calendar getCalendarFormYear(int year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        return cal;
    }


    /**
     * get start date of given week no of a year
     * @param year
     * @param weekNo
     * @return
     */
    public static String getStartDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
//        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
//                cal.get(Calendar.DAY_OF_MONTH);
        return  (cal.get(Calendar.MONTH) + 1) + "." +
                cal.get(Calendar.DAY_OF_MONTH);

    }

    /**
     * get the end day of given week no of a year.
     * @param year
     * @param weekNo
     * @return
     */
    public static String getEndDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
//        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
//                cal.get(Calendar.DAY_OF_MONTH);
        return   (cal.get(Calendar.MONTH) + 1) + "." +
                cal.get(Calendar.DAY_OF_MONTH);
    }

    //dateline专用
    public static String getEndDayOfWeekNo2(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
//        yyyy-MM-dd HH:mm:ss.SSS

        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH) + " 00:00:00.000";

    }

    //这个月最后一天
    public static int getLastMonthDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int index = calendar.get(Calendar.MONTH);
        int[][] days = new int[][] {
                { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
                { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } };
        int year = calendar.get(Calendar.YEAR);
        boolean leap = year % 4 == 0 && (year % 400 == 0 || year % 100 != 0);
        int curLast = days[leap?0:1][index];
//        int preLast = days[leap?0:1][index-1];
       return curLast;
    }

    //上个月最后一天
    public static int getPreLastMonthDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int index = calendar.get(Calendar.MONTH);
        int[][] days = new int[][] {
                { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
                { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } };
        int year = calendar.get(Calendar.YEAR);
        boolean leap = year % 4 == 0 && (year % 400 == 0 || year % 100 != 0);
        int curLast = days[leap?0:1][index];
        int preLast = days[leap?0:1][index-1];
        return preLast;
    }

    //根据月份查询属于第几季度
    public static int getQuarterByMonth(int month){
        if(month == 1 || month == 2 || month == 3){
            return 1;
        }
        if(month == 4 || month == 5 || month == 6){
            return 2;
        }
        if(month == 7 || month == 8 || month == 9){
            return 3;
        }
        if(month == 10 || month == 11 || month == 12){
            return 4;
        }
        return 1;
    }

}
