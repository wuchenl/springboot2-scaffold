package com.letters7.wuchen.springboot2.utils.number;

import com.letters7.wuchen.springboot2.utils.string.UtilString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by zoubin02 on 2017/9/5.
 */
public class UtilBigDecimal {

    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 提供精确的加法运算。
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */

    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 把数字型对象转换为BigDecimal,2位小数的
     * @param obj
     * @return
     */
    public static  BigDecimal parseToBigDecimal2Point(Object obj) {
        String val = String.valueOf(obj);
        if (val == null) {
            return new BigDecimal("0.00");
        }

        val = val.replaceAll(",", "");
        if (!isNumber(val)) {
            return new BigDecimal("0.00");
        }

        BigDecimal decimal = new BigDecimal(val);
        decimal = decimal.setScale(2, RoundingMode.HALF_UP);
        return decimal;
    }
    private static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }
    private static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }




    /**
     * 格式化为，隔开和两位小数的格式
     * @param obj
     * @return
     */
    public static String formatByCommaAnd2Point(Object obj) {
        BigDecimal decimal = parseToBigDecimal2Point(obj);

        DecimalFormat df = new DecimalFormat("#,###.00");
        String decimalStr = df.format(decimal).equals(".00")?"0.00":df.format(decimal);
        if(decimalStr.startsWith(".")){
            decimalStr = "0"+decimalStr;
        }
        return decimalStr;
    }


    /**
     * 格式化两位小数的格式
     * @param obj
     * @return
     */
    public static String formatBy2Point(Object obj) {
        String decimalStr = formatByCommaAnd2Point(obj);
        decimalStr = decimalStr.replaceAll(",", "");
        return decimalStr;
    }


    /**
     * 格式化没有后缀0的格式
     * @param obj
     * @return
     */
    public static String formatBySolid(Object obj) {
        String decimalStr = formatBy2Point(obj);
        decimalStr = UtilString.removeEnd(decimalStr , "00");
        decimalStr = UtilString.removeEnd(decimalStr , "0");
        decimalStr = UtilString.removeEnd(decimalStr , ".");
        return decimalStr;
    }


    public static void main(String[] args){
        String str = "123,123.01";
        String bd = UtilBigDecimal.formatBySolid(str);
        System.out.println(bd);
    }

}
