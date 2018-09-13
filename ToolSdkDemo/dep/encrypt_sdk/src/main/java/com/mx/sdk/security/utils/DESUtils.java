package com.mx.sdk.security.utils;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import demo.utils.StringUtils;

public class DESUtils {
    private static String strDefaultKey = "e^3^d#5w";
    public static final String bytesToHexString(byte[] bArray) {
        if(bArray == null )
        {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }




    public static byte[] encryptDES(String encryptString, String encryptKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(getKey(encryptKey), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        return encryptedData;
    }



    /**
     * 自定义一个key
     * @param string
     */
    public static byte[] getKey(String keyRule) {
        Key key = null;
        byte[] keyByte = keyRule.getBytes();
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        key = new SecretKeySpec(byteTemp, "DES");
        return key.getEncoded();
    }



    /***
     * 解密数据
     * @param decryptString
     * @param decryptKey
     * @return
     * @throws Exception
     */
    public static String decryptDES(byte[] decryptString, String decryptKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(getKey(decryptKey), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte decryptedData[] = cipher.doFinal(decryptString);
        return new String(decryptedData);
    }


    public static void main(String[] args) {
        String data="{\"2018-04-12\":[{\"id\":\"4719\",\"pos\":\"1\",\"type\":\"3\",\"count\":\"1\"},{\"id\":\"14\",\"pos\":\"2\",\"type\":\"14\",\"count\":\"1\"},{\"id\":\"614\",\"pos\":\"3\",\"type\":\"1\",\"count\":\"1\"},{\"id\":\"451\",\"pos\":\"4\",\"type\":\"1\",\"count\":\"1\"},{\"id\":\"455\",\"pos\":\"5\",\"type\":\"1\",\"count\":\"1\"},{\"id\":\"6\",\"pos\":\"6\",\"type\":\"14\",\"count\":\"1\"},{\"id\":\"18\",\"pos\":\"7\",\"type\":\"14\",\"count\":\"1\"}]}";

        try {
            byte[] ret=encryptDES(data, strDefaultKey);
            System.out.println(StringUtils.byteArrayToHexString(ret));    //有个无符号bug


            System.out.println("************** \n 解密结果 **************** \n ");
            byte[]  test=ret;
            System.out.println(decryptDES(test, strDefaultKey));


        }catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

    }




}
