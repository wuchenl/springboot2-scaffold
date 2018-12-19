package com.letters7.wuchen.springboot2.utils.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class UtilMd5 {



    /**
     * 把字符串进行MD5加密
     *
     * @param string 字符串
     * @return MD5加密后的字符串
     */
    public static String encode(String string) {
        StringBuilder sb = new StringBuilder(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashValue = md.digest(string.getBytes());
            for (int i = 0; i < hashValue.length; i++) {
                sb.append(Integer.toHexString((hashValue[i] & 0xf0) >> 4));
                sb.append(Integer.toHexString(hashValue[i] & 0x0f));
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return sb.toString();
    }

    /**
     * 获取Md5加密
     *
     * @param text
     * @return
     */
    /*public static String encode(String text) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes(Charset.forName("utf-8")));
            byte[] b = md.digest();
            int i;

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }*/

}
