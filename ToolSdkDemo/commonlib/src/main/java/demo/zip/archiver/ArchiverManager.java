package demo.zip.archiver;

import android.os.Handler;
import android.text.TextUtils;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2017/12/24.
 */

public class ArchiverManager extends BaseArchiver {
    private volatile static ArchiverManager mInstance;
    private  BaseArchiver mCurrentArchiver;
    private Executor mThreadPool;
    private Handler handler;

    public static ArchiverManager getInstance(Handler handler){
        if(mInstance==null){
            synchronized (ArchiverManager.class){
                mInstance=new ArchiverManager(handler);
            }
        }
        return mInstance;
    }


    protected ArchiverManager(Handler handler){
        mThreadPool= Executors.newSingleThreadExecutor();
        this.handler=handler;
    }

    @Override
    public void doZipArchiver(final String src, final String dest, final boolean isCreateDir, final String passwd) {
        mCurrentArchiver=getCorrectArchiver("zip");
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                MyLog.e("doZipArchiver ... execute 压缩 1111 ");
                mCurrentArchiver.doZipArchiver(src, dest, isCreateDir, passwd);
            }
        });
        MyLog.e("doZipArchiver ... execute 压缩 222");
    }

    @Override
    public void doUnArchiver(final String srcFile, final String unArchiverPath, final String password, final IArchiverListener listener) {
            mCurrentArchiver=getCorrectArchiver(getFileType(srcFile));
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    MyLog.e("doUnArchiver ... execute  1111 ");
                    mCurrentArchiver.doUnArchiver(srcFile,unArchiverPath,password,listener);
                }
            });
            MyLog.e("doUnArchiver ... execute");
    }


    private String getFileType(String fileName){
        String type=null;
        if(TextUtils.isEmpty(fileName)){
            return type;
        }
        String[] temp=fileName.split("\\.");
        type=temp[temp.length-1];
        MyLog.e("type:%s, 获取文件类型。。。"+fileName, type);
        return type;
    }


    private BaseArchiver getCorrectArchiver(String type){
        if(type==null){
            return null;
        }
        switch (type){
            case ArchiverType._ZIP:
                return new ZipArchiver(handler);
        }
        return null;
    }


    public static class ArchiverType{
        public final static String _ZIP="zip";
    }
}
