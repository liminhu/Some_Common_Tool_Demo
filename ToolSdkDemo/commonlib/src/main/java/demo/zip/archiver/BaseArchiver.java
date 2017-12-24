package demo.zip.archiver;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.logging.LogRecord;

/**
 * Created by hulimin on 2017/12/24.
 */

public abstract class BaseArchiver {

    /**
     * 压缩文件
     */
    public   abstract void doZipArchiver(String src, String dest, boolean isCreateDir, String passwd);


    /**
     * 解压文件
     * @param srcFile
     * @param unArchiverPath
     * @param password
     * @param listener
     */
    public abstract void doUnArchiver(String srcFile, String unArchiverPath, String password, IArchiverListener listener);
}
