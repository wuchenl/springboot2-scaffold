package com.letters7.wuchen.springboot2.utils.number;

/**
 * 转换为中文的数字和金额
 * @author zoubin02
 */
public class UtilMoneyConversion {

    private static String digitalCN[] = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

    private static String digitalUnitCN[] = new String[]{"", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿",
            "拾", "佰", "仟", "万", "拾", "佰", "仟"};


    // 转换为大写的中文数字
    // 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
    public static String integerToStringCN(String positiveNumberString) {
//        String RMBStr = "";
        StringBuffer rmbStr=new StringBuffer();
        boolean lastzero = false;
        boolean hasvalue = false;       // 亿、万进位前有数值标记
        int len, n;
        len = positiveNumberString.length();
        if (len > 15) {
            return "数值过大!";
        }
        for (int i = len - 1; i >= 0; i--) {
            if (positiveNumberString.charAt(len - i - 1) == ' ') {
                continue;
            }
            n = positiveNumberString.charAt(len - i - 1) - '0';
            if (n < 0 || n > 9) {
                return "输入含非数字字符!";
            }

            if (n != 0) {
                if (lastzero) {
                    rmbStr = rmbStr.append(digitalCN[0]);  // 若干零后若跟非零值，只显示一个零
                }
                // 除了亿万前的零不带到后面
                //if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) )    // 如十进位前有零也不发壹音用此行
                if (!(n == 1 && (i % 4) == 1 && i == len - 1))     // 十进位处于第一位不发壹音
                {
                    rmbStr =  rmbStr.append(digitalCN[n]);
                }
                rmbStr =  rmbStr.append(digitalUnitCN[i]);    // 非零值后加进位，个位为空
                hasvalue = true;                                    // 置万进位前有值标记

            } else {
                if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue))  // 亿万之间必须有非零值方显示万
                {
                    rmbStr =  rmbStr.append(digitalUnitCN[i]);   // “亿”或“万”
                }
            }
            if (i % 8 == 0) {
                hasvalue = false;      // 万进位前有值标记逢亿复位
            }
            lastzero = (n == 0) && (i % 4 != 0);
        }

        if (rmbStr.length() == 0) {
            return digitalCN[0];         // 输入空字符或"0"，返回"零"
        }
        return rmbStr.toString();
    }


    //转换为大写的中文数字+元角分格式
    public static String numberToRMBString(double numberToConvert) {
        String SignStr = "";
        String TailStr = "";
        long fraction, integer;
        int jiao, fen;

        if (numberToConvert < 0) {
            numberToConvert = -numberToConvert;
            SignStr = "负";
        }
        if (numberToConvert > 99999999999999.999 || numberToConvert < -99999999999999.999) {
            return "数值位数过大!";
        }
        // 四舍五入到分
        long temp = Math.round(numberToConvert * 100);
        integer = temp / 100;
        fraction = temp % 100;
        jiao = (int) fraction / 10;
        fen = (int) fraction % 10;
        if (jiao == 0 && fen == 0) {
            TailStr = "整";
        } else {
            TailStr = digitalCN[jiao];
            if (jiao != 0) {
                TailStr += "角";
            }
            if (integer == 0 && jiao == 0)                // 零元后不写零几分
            {
                TailStr = "";
            }
            if (fen != 0) {
                TailStr += digitalCN[fen] + "分";
            }
        }

        // 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
        //        if( !integer ) return  SignStr+TailStr;

        return  SignStr + integerToStringCN(String.valueOf(integer)) + "元" + TailStr;
    }


    public static void main(String[] args){
        //捌拾捌万叁仟叁佰贰拾贰元伍角肆分
        System.out.println(numberToRMBString(883322.54));
    }


}
