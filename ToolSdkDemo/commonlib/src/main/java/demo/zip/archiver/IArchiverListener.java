package demo.zip.archiver;

/**
 * Created by hulimin on 2017/12/24.
 */

/**
 * 解压进度接口
 */
public interface IArchiverListener {
    void onStartArchiver();

    void onProgressArchiver(int current, int total);

    void onEndArchiver();
}
