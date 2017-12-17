package demo.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by hulimin on 2017/9/4.
 */

public class StringUtils {

    public static String hexStringToString(String hex){
        return new String(hexStringtoByteArray(hex));
    }

    private static byte[] hexStringtoByteArray(String hexString) {
        if (hexString == null)
            System.out.println("this hexString must not be empty");
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff); // Character.digit
            // 在指定的基数返回指定字符的数值
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }




    public static void test(){
        Exception e = new Exception("this is PluginManagerView log");
        StackTraceElement[]  elements=e.getStackTrace();
        StringBuilder sb=new StringBuilder();
        for(int i=0; i<elements.length; i++){
            sb.append(elements[i]);
            sb.append("\n");
        }
        MyLog.e( "hook_Exception"+sb.toString());
    }


    public static String myUrlDecodedata(String srcData) {

        try {
            String urlDecodeData = URLDecoder.decode(srcData,"utf-8");
            return urlDecodeData;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String myUrlEncodedata(String srcData) {
        try {
            String urlCodeData = URLEncoder.encode(srcData, "utf-8");
            return urlCodeData;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String myUrlEncodedata(String srcData,String encode) {
        try {
            String urlCodeData = URLEncoder.encode(srcData, encode);
            return urlCodeData;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static  String  getDateTime() {
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
        return time;
    }

    private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String byteArrayToHexString(byte[] data) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // 取出字节的高四位，作为索引得到相应的十六进制标识符，注意 >>> 无符号右移
            strBuilder.append(HEX_CHAR[(data[i] & 0xF0) >>> 4]);
            // 取出字节的低四位，作为索引得到相应的十六进制标识符
            strBuilder.append(HEX_CHAR[(data[i] & 0x0F)]);
            if (i < data.length - 1) {
                //strBuilder.append(' ');
            }
        }
        return strBuilder.toString();
    }




    public static  String getImei(Context context){
        //File f=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),   "/Camera");  //Camera"/gg_"+time+".jpg");      //
        //MyLog.e(f.getAbsolutePath());
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        MyLog.e(tm.getDeviceId());
        return tm.getDeviceId();
    }

}
