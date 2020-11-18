package com.jian.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: MD5Util
 * @Description:MD5 ���ܹ�����
 * @author: jianlinwei
 * @date: 2018��2��4�� ����3:27:00
 */
public class MD5Util {

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};

    private final static String[] saltChar = {"Q", "B", "T", "D",
            "E", "Y", "G", "H", "I", "J"};

    /**
     * @param change_befer
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException
     */
    public String md5(String change_befer, String encoding) throws UnsupportedEncodingException {
        String result = null;


        try {

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            if (StringUtils.isNotEmpty(change_befer))
                result = byteArrayToHexString(messageDigest.digest(change_befer.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return result;
    }

    public String byteArrayToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte tem : bytes) {
            stringBuilder.append(byteToHexString(tem));
        }
        return stringBuilder.toString();
    }

    private String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    /**
     * @throws
     * @Title: getSalt
     * @Description: TODO
     * @param: @return
     * @author: jianlinwei
     * @return: String
     */
    public static String getSalt() {
        String str = null;
        for (int i = 0; i < 6; i++) {
            String s = saltChar[(int) (Math.random() * 10)];
            if (str == null) {
                str = s;
            } else {
                str = str + s;
            }
        }

        return str;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        MD5Util md5Util = new MD5Util();
        System.out.println(md5Util.md5("admin123", "utf-8"));
    }
}
