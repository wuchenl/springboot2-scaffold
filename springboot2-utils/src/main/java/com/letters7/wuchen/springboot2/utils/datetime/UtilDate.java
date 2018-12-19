package com.letters7.wuchen.springboot2.utils.datetime;

import com.google.common.collect.Maps;
import com.letters7.wuchen.springboot2.utils.string.UtilString;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UtilDate {
    public static final String empty = "";

    /**
     * 时间字段常量，表示“秒”
     */
    public final static int SECOND = 0;

    /**
     * 时间字段常量，表示“分”
     */
    public final static int MINUTE = 1;

    /**
     * 时间字段常量，表示“时”
     */
    public final static int HOUR = 2;

    /**
     * 时间字段常量，表示“天”
     */
    public final static int DAY = 3;
    //默认
    public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //解决多线程并发时候DateFormat.parse的问题
    private static Map<String, DateTimeFormatter> dateFormatMap = null;
    static {
        dateFormatMap = Maps.newConcurrentMap();

        dateFormatMap.put("\\d{1,2}/\\d{1,2}/\\d{4}", DateTimeFormat.forPattern("MM/dd/yyyy"));
        dateFormatMap.put("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}", DateTimeFormat.forPattern("MM/dd/yyyy HH"));
        dateFormatMap.put("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}", DateTimeFormat.forPattern("MM/dd/yyyy HH:mm"));
        dateFormatMap.put("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}", DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss"));

        dateFormatMap.put("\\d{4}-\\d{1,2}-\\d{1,2}", DateTimeFormat.forPattern("yyyy-MM-dd"));
        dateFormatMap.put("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}", DateTimeFormat.forPattern("yyyy-MM-dd HH"));
        dateFormatMap.put("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
        dateFormatMap.put("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));

        dateFormatMap.put("\\d{4}/\\d{1,2}/\\d{1,2}", DateTimeFormat.forPattern("yyyy/MM/dd"));
        dateFormatMap.put("\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}", DateTimeFormat.forPattern("yyyy/MM/dd HH"));
        dateFormatMap.put("\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}", DateTimeFormat.forPattern("yyyy/MM/dd HH:mm"));
        dateFormatMap.put("\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}", DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss"));

        dateFormatMap.put("\\d{8}" , DateTimeFormat.forPattern("yyyyMMdd"));
        dateFormatMap.put("\\d{10}", DateTimeFormat.forPattern("yyyyMMddHH"));
        dateFormatMap.put("\\d{12}", DateTimeFormat.forPattern("yyyyMMddHHmm"));
        dateFormatMap.put("\\d{14}", DateTimeFormat.forPattern("yyyyMMddHHmmss"));
    }


    //==========================日期格式校验=========================
    //根据传入的日期字符串返回匹配的DateFormat
    private static DateTimeFormatter getDateFormatByPattern(String dateText) {
        if (UtilString.isBlank(dateText)) {
            return null;
        }
        Iterator<String> keyiter = dateFormatMap.keySet().iterator();
        while (keyiter.hasNext()) {
            String regExp = keyiter.next();

            Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(dateText);

            if (matcher.matches()) {
                return dateFormatMap.get(regExp);
            }
        }
        return null;
    }


    //日期格式校验
    public static boolean isValidDate(String dateText) {
        DateTimeFormatter format = getDateFormatByPattern(dateText);
        if (format == null) {
            return false;
        }

        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            //format.setLenient(false);
            format.parseLocalDateTime(dateText).toDate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }




    //===========================获取当前日期=========================
    public static Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(DateTime.now().toDate().getTime());
    }

    /**
     * 获取毫秒数据的字符串
     */
    public static String getMillisecondAsString() {
        return DateTime.now().toString("yyyyMMddHHmmssSSS");
    }

    /**
     * 返回当前日期时间数字<br>
     * 默认格式:yyyymmddhhmmss
     */
    public static BigDecimal getCurrentTimeAsNumber() {
        String returnStr = DateTime.now().toString("yyyyMMddHHmmss");
        return new BigDecimal(returnStr);
    }






    //===========================字符串和日期之间的解析=========================
    public static Date parseDateTime(String dateText) {
        return parseDateTime(dateText, null);
    }

    public static Date parseDateTime(String dateText, String format) {
        if (UtilString.isBlank(dateText)) {
            return null;
        }
        DateTimeFormatter dateFormat = null;
        if (UtilString.isNotEmpty(format)) {
            dateFormat = DateTimeFormat.forPattern(format);
        } else {
            dateFormat = getDateFormatByPattern(dateText);
        }
        if (dateFormat == null) {
            throw new RuntimeException(dateText + ":无法识别的日期格式");
        }

        try {
            return dateFormat.parseLocalDateTime(dateText).toDate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Timestamp parseTimestamp(String dateText, String format) {
        if (UtilString.isBlank(dateText)) {
            return null;
        }
        DateTimeFormatter dateFormat = null;
        if (UtilString.isNotEmpty(format)) {
            dateFormat = DateTimeFormat.forPattern(format);
        } else {
            dateFormat = getDateFormatByPattern(dateText);
        }
        if (dateFormat == null) {
            throw new RuntimeException(dateText + ":无法识别的日期格式");
        }

        try {
            Date date = dateFormat.parseLocalDateTime(dateText).toDate();
            return new Timestamp(date.getTime());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Timestamp parseTimestamp(String dateText) {
        return parseTimestamp(dateText, null);
    }


    /**
     * "yyyy-MM-dd HH:mm:ss"
     */
    public static String formatCurrentDateTime() {
        return formatCurrentDate(DATE_TIME_FORMAT);
    }

    /**
     * "yyyy-MM-dd"
     */
    public static String formatCurrentDate() {
        return formatCurrentDate(DEFAULT_DATE_FORMAT);
    }

    public static String formatCurrentDate(String format) {
        Date date = getCurrentDate();
        return formatDateTime(date, format);
    }


    public static String formatDateTime(Date time) {
        return formatDateTime(time, DEFAULT_DATE_FORMAT);
    }

    public static String formatDateTime(Date time, String format) {
        if (time == null) {
            return null;
        }
        DateTimeFormatter dateFormat = null;
        if (UtilString.isNotEmpty(format)) {
            dateFormat = DateTimeFormat.forPattern(format);
        } else {
            dateFormat = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT);
        }
        return new DateTime(time).toString(dateFormat);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis
     * @return
     */
    public static String formatTimeMillis(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    public static String formatTimestamp(Timestamp time) {
        return formatTimestamp(time, null);
    }

    public static String formatTimestamp(Timestamp time, String format) {
        if (time == null) {
            return null;
        }
        Date date = new Date(time.getTime());
        return formatDateTime(date, format);
    }


    public static String formatToFormat(String datetext, String targetFormat) {
        if (UtilString.isBlank(datetext)) {
            return null;
        }
        DateTimeFormatter dateFormat = getDateFormatByPattern(datetext);
        if (dateFormat == null) {
            throw new RuntimeException(datetext + ":无法识别的日期格式");
        }

        try {
            Date date1 = dateFormat.parseLocalDateTime(datetext).toDate();
            return new DateTime(date1).toString(targetFormat);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }








    //================================日期之间的间隔================================
    /**
     * 判断当前时间是否在开始和结束日期区间之间
     */
    public static boolean isBetweenDateRange(String startDate, String endDate) {
        String now = UtilDate.formatCurrentDateTime();
        return UtilDate.isBetweenDateRange(now, startDate, endDate);
    }

    public static boolean isBetweenDateRange(String dateText, String startDate, String endDate) {
        Timestamp startTime = null;
        if (UtilString.isNotEmpty(startDate)) {
            startTime = parseTimestamp(startDate);
        }

        Timestamp endTime = null;
        if (UtilString.isNotEmpty(endDate)) {
            endTime = parseTimestamp(endDate);
        }

        Timestamp inputTime = UtilDate.parseTimestamp(dateText);
        if (inputTime == null) {
            return false;
        }
        if (startTime != null && endTime == null) {
            return inputTime.compareTo(startTime) >= 0;
        } else if (startTime == null && endTime != null) {
            return inputTime.compareTo(endTime) <= 0;
        } else if (startTime == null && endTime == null) {
            return true;
        } else if (startTime != null && endTime != null) {
            return inputTime.compareTo(startTime) >= 0 && inputTime.compareTo(endTime) <= 0;
        }
        return false;
    }


    /**
     * 根据所给的起止时间来计算间隔的月数
     */
    public static int getIntervalMonths(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int startDateM = Calendar.MONTH;
        int startDateY = Calendar.YEAR;
        int enddatem = Calendar.MONTH;
        int enddatey = Calendar.YEAR;
        int interval = (enddatey * 12 + enddatem) - (startDateY * 12 + startDateM);
        return interval;
    }

    /**
     * 根据所给的起止时间来计算间隔的天数
     */
    public static int getIntervalDays(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate 和 endDate不能为空！");
        }
        long startdate = startDate.getTime();
        long enddate = endDate.getTime();
        long interval = enddate - startdate;
        int intervalday = (int) (interval / (1000 * 60 * 60 * 24));
        return intervalday;
    }






    //================================相对日期的获值================================
    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = System.currentTimeMillis()  - date.getTime();
        return t / (60 * 1000);
    }


    /**
     * 获取多少天之前的日期
     */
    public static String beforeDateAsFormat(String datestr, int dayNum) {
        if (UtilString.isEmpty(datestr)) {
            return datestr;
        }

        Date beforeDate = UtilDate.beforeDate(UtilDate.parseDateTime(datestr), dayNum);
        DateTimeFormatter format = UtilDate.getDateFormatByPattern(datestr);
        if (format == null) {
            throw new UnknownFormatConversionException("无法识别日期格式");
        }
        return new DateTime(beforeDate).toString(format);
    }

    public static Date beforeDate(int dayNum) {
        return beforeDate(new Date(), dayNum);
    }

    public static Date beforeDate(Date date, int dayNum) {
        if (date == null) {
            date = new Date();
        }
        return new DateTime(date).minusDays(dayNum).toDate();
    }


    /**
     * 获取多少天之后的日期
     */
    public static String afterDateAsFormat(String datestr, int dayNum) {
        if (UtilString.isEmpty(datestr)) {
            return datestr;
        }

        Date afterDate = UtilDate.afterDate(UtilDate.parseDateTime(datestr), dayNum);
        DateTimeFormatter format = UtilDate.getDateFormatByPattern(datestr);
        if (format == null) {
            throw new UnknownFormatConversionException("无法识别日期格式");
        }
        return new DateTime(afterDate).toString(format);
    }

    public static Date afterDate(int dayNum) {
        return afterDate(new Date(), dayNum);
    }

    public static Date afterDate(Date date, int dayNum) {
        if (date == null) {
            date = new Date();
        }
        return new DateTime(date).plusDays(dayNum).toDate();
    }


    /**
     * 获取多少月之前的日期
     */
    public static String beforeMonthAsFormat(String datestr, int monthNum) {
        if (UtilString.isEmpty(datestr)) {
            return datestr;
        }

        Date beforeDate = UtilDate.beforeMonth(UtilDate.parseDateTime(datestr), monthNum);
        DateTimeFormatter format = UtilDate.getDateFormatByPattern(datestr);
        if (format == null) {
            throw new UnknownFormatConversionException("无法识别日期格式");
        }
        return new DateTime(beforeDate).toString(format);
    }

    public static Date beforeMonth(int monthNum) {
        return beforeMonth(new Date(), monthNum);
    }

    public static Date beforeMonth(Date date, int monthNum) {
        if (date == null) {
            date = new Date();
        }
        return new DateTime(date).minusMonths(monthNum).toDate();
    }


    /**
     * 获取多少月之后的日期
     */
    public static String afterMonthAsFormat(String datestr, int monthNum) {
        if (UtilString.isEmpty(datestr)) {
            return datestr;
        }

        Date afterDate = UtilDate.afterMonth(UtilDate.parseDateTime(datestr), monthNum);
        DateTimeFormatter format = UtilDate.getDateFormatByPattern(datestr);
        if (format == null) {
            throw new UnknownFormatConversionException("无法识别日期格式");
        }
        return new DateTime(afterDate).toString(format);
    }

    public static Date afterMonth(int monthNum) {
        return afterMonth(new Date(), monthNum);
    }

    public static Date afterMonth(Date date, int monthNum) {
        if (date == null) {
            date = new Date();
        }
        return new DateTime(date).plusMonths(monthNum).toDate();
    }


    /**
     * 获取多少年之前的日期
     */
    public static String beforeYearAsFormat(String datestr, int yearNum) {
        if (UtilString.isEmpty(datestr)) {
            return datestr;
        }

        Date beforeDate = UtilDate.beforeYear(UtilDate.parseDateTime(datestr), yearNum);
        DateTimeFormatter format = UtilDate.getDateFormatByPattern(datestr);
        if (format == null) {
            throw new UnknownFormatConversionException("无法识别日期格式");
        }
        return new DateTime(beforeDate).toString(format);
    }

    public static Date beforeYear(int yearNum) {
        return beforeYear(new Date(), yearNum);
    }

    public static Date beforeYear(Date date, int yearNum) {
        if (date == null) {
            date = new Date();
        }
        return new DateTime(date).minusYears(yearNum).toDate();
    }


    /**
     * 获取多少年之前的日期
     */
    public static String afterYearAsFormat(String datestr, int yearNum) {
        if (UtilString.isEmpty(datestr)) {
            return datestr;
        }

        Date beforeDate = UtilDate.afterYear(UtilDate.parseDateTime(datestr), yearNum);
        DateTimeFormatter format = UtilDate.getDateFormatByPattern(datestr);
        if (format == null) {
            throw new UnknownFormatConversionException("无法识别日期格式");
        }
        return new DateTime(beforeDate).toString(format);
    }

    public static Date afterYear(int yearNum) {
        return afterYear(new Date(), yearNum);
    }

    public static Date afterYear(Date date, int yearNum) {
        if (date == null) {
            date = new Date();
        }
        return new DateTime(date).plusYears(yearNum).toDate();
    }







    //===========================日期的工具方法=========================
    /**
     * 根据日期获取星期
     */
    public static String getWeekDayByDate(String strdate) {
        final String[] dayNames = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) {
            dayOfWeek = 0;
        }
        return dayNames[dayOfWeek];
    }

    /**
     * 获取指定年份和月份对应的天数
     */
    public static int getDaysInMonth(int year, int month) {
        if ((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10)
                || (month == 12)) {
            return 31;
        } else if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
            return 30;
        } else {
            if (((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0)) {
                return 29;
            } else {
                return 28;
            }
        }
    }
    /**
     * 根据生日取年龄
     */
    public static String getAge(String birthDayStr) {
        if (UtilString.isBlank(birthDayStr)) {
            return null;
        }

        try {
            Date birthDay = UtilDate.parseDateTime(birthDayStr);
            Calendar cal = Calendar.getInstance();

            if (cal.before(birthDay)) {
                return null;
            }

            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

            cal.setTime(birthDay);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

            int age = yearNow - yearBirth;
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    //monthNow==monthBirth
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        age--;
                    }
                } else {
                    //monthNow>monthBirth
                    age--;
                }
            }

            return Integer.toString(age);
        } catch (Exception e) {}
        return "";
    }




    //===============================生成日期范围=============================
    /**
     * 根据输入的日期返回带上时间串的范围
     */
    public static Date[] buildNotEmptyDateTimeRange(String inputDateFormat) {
        if(UtilString.isEmpty(inputDateFormat)){
            throw new IllegalArgumentException("入参日期不能为空");
        }
        String inputDate =UtilDate.formatToFormat(inputDateFormat , UtilDate.DEFAULT_DATE_FORMAT);
        Date[] result = new Date[2];
        result[0] = UtilDate.parseDateTime(inputDate+" 00:00:00" , UtilDate.DATE_TIME_FORMAT);
        result[1] = UtilDate.parseDateTime(inputDate+" 23:59:59" , UtilDate.DATE_TIME_FORMAT);
        return result;
    }

    /**
     * 根据输入的日期返回带上时间串的范围
     */
    public static Date[] buildNotEmptyDateTimeRange(String inputDateStart , String inputDateEnd) {
        if(UtilString.isEmpty(inputDateStart) || UtilString.isEmpty(inputDateEnd)){
            throw new IllegalArgumentException("入参日期不能为空");
        }
        String dateStart = UtilDate.formatToFormat(inputDateStart , UtilDate.DEFAULT_DATE_FORMAT);
        String dateEnd = UtilDate.formatToFormat(inputDateEnd , UtilDate.DEFAULT_DATE_FORMAT);
        Date[] result = new Date[2];
        result[0] = UtilDate.parseDateTime(dateStart+" 00:00:00" , UtilDate.DATE_TIME_FORMAT);
        result[1] = UtilDate.parseDateTime(dateEnd+" 23:59:59" , UtilDate.DATE_TIME_FORMAT);
        return result;
    }






    //===============================main=============================
    public static void main(String[] args){
        System.out.println(UtilDate.formatToFormat("1991-04-14","MM/dd/yyyy"));
    }


}
