package demo.zip.archiver;

import android.os.Handler;
import android.text.TextUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.nio.channels.FileLockInterruptionException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import demo.utils.FileUtils;
import demo.utils.MyLog;

/**
 * Created by hulimin on 2017/12/24.
 */

public class ZipArchiver extends BaseArchiver {
    private Handler mHandler;

    public ZipArchiver(Handler mHandler) {
        MyLog.e(" zip init...");
        this.mHandler = mHandler;
    }



    @Override
    public void doZipArchiver(String src, String dest, boolean isCreateDir, String passwd){
        File srcFile=new File(src);
        String destName= FileUtils.buildDestFileName(srcFile, dest);
        ZipParameters parameters=new ZipParameters();
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        if(!TextUtils.isEmpty(passwd)){
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(passwd.toCharArray());
        }
        try {
            MyLog.e("正在压缩中。。。");
            ZipFile zipFile=new ZipFile(destName);
            if(srcFile.isDirectory()){
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                if(!isCreateDir){
                    File[] listFiles=srcFile.listFiles();
                    ArrayList<File> temp=new ArrayList<File>();
                    Collections.addAll(temp,listFiles);
                    zipFile.addFiles(temp, parameters);
                }
                zipFile.addFolder(srcFile, parameters);
            }else{
                zipFile.addFile(srcFile, parameters);
            }
        } catch (ZipException e) {
            e.printStackTrace();
            MyLog.e(e.getMessage());
            mHandler.obtainMessage(1,"压缩异常了。。。").sendToTarget();
        }
        MyLog.e("压缩完成。。。");
        mHandler.obtainMessage(1,"压缩完成。。。").sendToTarget();
    }




    @Override
    public void doUnArchiver(String srcFile, String unArchiverPath, String password, final IArchiverListener listener) {
        MyLog.e("srcFile "+srcFile);
        if(TextUtils.isEmpty(srcFile) || TextUtils.isEmpty(unArchiverPath)){
            return;
        }
        File src=new File(srcFile);
        if(!src.exists()){
            String[] temp=srcFile.split("\\/");
            String data=temp[temp.length-1];
            MyLog.e("解压文件不存在。。。"+data);
            mHandler.obtainMessage(1,data+"解压文件不存在。。。").sendToTarget();
            return;
        }

        try {
            ZipFile zipFile=new ZipFile(srcFile);
            zipFile.setFileNameCharset("GBK");
            if(!zipFile.isValidZipFile()){
                mHandler.obtainMessage(1,"文件格式非法，无法解压。。。").sendToTarget();
                throw  new ZipException("文件格式非法，无法解压。。。");
            }
            File destDir=new File(unArchiverPath);
            if(destDir.exists() && destDir.isFile()){
                destDir.delete();
            }
            if(!destDir.exists()){
                destDir.mkdirs();
            }
            if(zipFile.isEncrypted()){
                zipFile.setPassword(password.toCharArray());
            }
            if(listener != null && mHandler!=null){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onStartArchiver();
                    }
                });
            }

            FileHeader fh=null;
            final  int total=zipFile.getFileHeaders().size();
            for(int i=0; i<zipFile.getFileHeaders().size(); i++){
                fh=(FileHeader)zipFile.getFileHeaders().get(i);
                zipFile.extractFile(fh,unArchiverPath);
                if(listener!=null){
                    final int progress=i;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onProgressArchiver(progress+1, total);
                        }
                    });
                }
            }

        } catch (Exception e) {
            MyLog.e("解压时异常："+e.getMessage());
        }
        MyLog.e("解压完成。。。。");
        if(listener!=null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onEndArchiver();
                }
            });
        }
    }
}
