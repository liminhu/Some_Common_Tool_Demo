package demo.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by hulimin on 2017/9/19.
 */

public class NewFileUtils {
    //  File f=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),   "/"+dirName);  //Camera"/gg_"+time+".jpg");      //
    public static void copyFile(InputStream inStream, String fileName) {
        deleteAllGitFile();
        File f=new File(fileName);
        String newPath=f.getAbsolutePath();
        try {
            MyLog.e("newPath : "+newPath);
            int bytesum = 0;
            int byteread = 0;
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[2048];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    //System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
               MyLog.e("file size --- "+bytesum);
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }





    public static void  deleteAllGitFile() {
        File f=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String[] str=f.list();
        for(int i=0; i<str.length; i++){
            if(str[i].contains(".gif")){
                MyLog.e("delete -- "+str[i]);
                File newFile=new File(f, str[i]);
                newFile.delete();
            }
        }
    }





    public static String getImagesDirPath() {
        File directory = new File("");// 设定为当前文件夹
        try {
            String path = directory.getCanonicalPath(); // 获取标准的路径
            String imgPath = "/hn_images";
            StringBuilder sb = new StringBuilder();
            sb.append(path);
            sb.append(imgPath);
            return sb.toString();
        } catch (Exception e) {

        }
        return null;
    }


    public static String[] getAllGifImagesPath() {
        String path = getImagesDirPath();
        File file = new File(path, "old_image");
        String[] filePath = file.list();
        String[] newFilePath = new String[filePath.length];
        for (int i = 0; i < filePath.length; i++) {
            newFilePath[i] = file.getAbsolutePath() + File.separator + filePath[i];
        }
        return newFilePath;
    }

    public static void main(String[] args) {
        String[] data = getAllGifImagesPath();
        for (int i = 0; i < data.length; i++) {
            if (data[i].contains(".gif")) {
                System.out.println(data[i] + " --- " + i + "\t" + data[i] + "\n");
                String md5 = getMd5ByFile(data[i]);
                System.out.println(md5);
                StringBuilder sb=new StringBuilder();
                sb.append(getImagesDirPath());
                sb.append(File.separator);
                sb.append(md5);
                sb.append(".gif");
                copyFile(data[i], sb.toString());
            }
        }
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            System.out.println("newPath : "+newPath);
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[2048];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    //System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                System.out.println("file size --- "+bytesum);
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }



    public static String getMd5ByFile(String path) {
        File file = new File(path);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            System.out.println("file length:" + file.length());
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            return StringUtils.byteArrayToHexString(md5.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
