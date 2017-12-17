package demo.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by hulimin on 2017/9/6.
 */


// TODO: 2017/9/7 :待优化，得通过id找到对象

/**
 * 2131756235  --- 相册的按钮id
  */
public class ViewUtils {


    public static void testClick(View view,  float x, float y){
        MyLog.e("childview--testClick---x:"+x+";\ty:"+y);
        MyLog.e("childview--testClick---"+view.getClass().getName()+"---id:"+view.getId());
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        dispatchTouchEvent(view, event);
//		view.dispatchTouchEvent(event);
        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        dispatchTouchEvent(view, event);
    }


    public static void testClick(View view, int shift){
        float x=view.getWidth()/2;
        float y=view.getHeight()/2;
        MyLog.e("childview--testClick_shift---"+view.getClass().getName()+"---id:"+view.getId() +"y:"+y);
        testClick(view,x,y-shift);
    }


    public static void testClick(View view){
        MyLog.e("childview--testClick_1111---"+view.getClass().getName()+"---id:"+view.getId());
        float x=view.getWidth()/2;
        float y=view.getHeight()/2;
        testClick(view,x,y);
    }

    private static void dispatchTouchEvent(final View view, final MotionEvent event) {
        try {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.dispatchTouchEvent(event);
                }
            }, 10);
        } catch (Throwable e) {
            throw new RuntimeException("Dispatching touch event failed", e);
        }
    }



    public static View getImgViewByViewId(ViewGroup vg, String realTextName){
        String viewName="android.widget.TextView";
        View view=getViewByRealTextName(vg, viewName,realTextName,1);
        return view;
    }


     public static View getImgViewByViewId(ViewGroup vg){
         String viewName="android.widget.TextView";
         String realTextName="相册";
         View view=getViewByRealTextName(vg, viewName,realTextName,1);
         return view;
    }



    public static View getLinearLayoutIma(ViewGroup vg, int id){
        View result=null;
        try{
            if(vg.getChildCount() == 0){
                return null;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                    View childView=vg.getChildAt(i);
                   // MyLog.e("childview--getLinearLayoutIma---"+childView.getClass().getName()+"---id:"+childView.getId());
                    if (vg.getChildAt(i).getId() == id) {
                        return childView;
                    }
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        result = getLinearLayoutIma((ViewGroup) vg.getChildAt(i),id);
                        if (result == null) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }
                return result;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }





    public static View getLinearLayoutIma(ViewGroup vg){
        //2131756235
        View edit= ViewUtils.getEditText(vg);
        return getViewFromViewGroup(edit, 2131756235, 2131756236);
    }






    public static View getViewFromViewGroup(View edit, int parent_id, int child_id){
        View result=null;
        ViewGroup viewGroup=(ViewGroup) edit.getParent().getParent();
        for(int i=0; i<viewGroup.getChildCount(); i++) {
            final View childView = viewGroup.getChildAt(i);
            if(childView.getId()==parent_id){
             //   MyLog.e("childview-----"+childView.getClass().getName()+"---id:"+childView.getId());

        /*        ViewGroup vg=(ViewGroup)childView;
                MyLog.e("childview----1111-"+childView.getClass().getName()+"---id:"+childView.getId());
                for(int j=0; j<vg.getChildCount(); j++) {
                    final View ch = vg.getChildAt(j);
                    MyLog.e("111--childview-----"+ch.getClass().getName()+"---id:"+ch.getId());

                    if(ch.getId() == child_id){
                        return ch;
                    }
                }*/
               // MyLog.e("0000--childview----- size:"+vg.getChildCount());
                return childView;
            }
        }
        return result;
    }



    private static View getViewByName(ViewGroup vg, String viewName){
        View result=null;
        try{
            if(vg.getChildCount() == 0){
                return null;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                  //  MyLog.e(vg.getChildAt(i).getClass().getName());
                    if (vg.getChildAt(i).getClass().getName().contains(viewName)) {
                        return vg.getChildAt(i);
                    }
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        result = getEditText((ViewGroup) vg.getChildAt(i));
                        if (result == null) {
                            continue;
                        } else {
                            break;
                        }

                    }
                }
                return result;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static View getEditText(ViewGroup vg){
        String viewName="EditText";
        return getViewByName(vg, viewName);
    }



    //1:表示textview, 2:表示button
    private static View getViewByRealTextName(ViewGroup vg, String viewName, String realTextName, int type){
        View result=null;
        try{
            if(vg.getChildCount() == 0){
                return null;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                  //  MyLog.e(vg.getChildAt(i).getClass().getName());
                    if (vg.getChildAt(i).getClass().getName().equals(viewName)) {
                        if(type==2){
                            Button button=(Button) vg.getChildAt(i);
                           // MyLog.e("button :\t"+button.getText().toString());
                            if(button.getText().equals(realTextName)) {
                                return button;
                            }
                        }else if(type==1){
                            TextView textView=(TextView) vg.getChildAt(i);
                            //MyLog.e("textView :\t"+textView.getText().toString());
                            if(textView.getText().equals(realTextName)) {
                                return textView;
                            }
                        }
                    }
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        result = getViewByRealTextName((ViewGroup) vg.getChildAt(i), viewName, realTextName,type);
                        if (result == null) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }
                return result;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }



    public static View getSendButton(ViewGroup vg){
        String viewName="android.widget.Button";
        String realTextName="发送";
        return getViewByRealTextName(vg, viewName,realTextName,2);
    }



    static int num=0;
    public static  void printfView(ViewGroup vg){
        try{
            if(vg.getChildCount() == 0){
                return;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                    String name=vg.getChildAt(i).getClass().getName();
                     MyLog.e("hook_"+num+":"+vg.getChildAt(i).getClass().getName());
             /*       if(name.contains("android.widget.Button")) {
                        Button button=(Button) vg.getChildAt(i);
                        MyLog.e("hook_Button:"+num+":"+"\tid:"+button.getId()+"\t"+button.getText().toString());
                        num++;
                    }else if(name.contains("TextView")){
                        TextView button=(TextView) vg.getChildAt(i);
                        MyLog.e("hook_TextView"+num+":"+button.getText().toString());
                        num++;
                    }*/
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        num++;
                        printfView((ViewGroup) vg.getChildAt(i));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }




    public static void testSaveImage(ViewGroup viewGroup, Handler mainHandler){
        for(int i=0; i<viewGroup.getChildCount(); i++){
            final View childView = viewGroup.getChildAt(i);
            MyLog.e("childview-----"+childView.toString());
              if(childView.getClass().getName().contains("Button")  || childView.getClass().getName().contains("Image")  ||  childView.getClass().getName().contains("TextView")  ||  childView.getClass().getName().contains("Layout") ){
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    MyLog.e("childview-----"+childView.getClass().getName()+"---id:"+childView.getId());
                    Bitmap bitmap = copyByDrawingCache(childView);
                    if(null != bitmap ){
                        saveBitmap(bitmap,childView.getClass().getName()+"-"+childView.getId());
                    }
                    else{
                        MyLog.e("childview-----"+"获取图片失败");
                    }

                }
               });
              }

            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                testSaveImage((ViewGroup) viewGroup.getChildAt(i), mainHandler);
            }
        }
    }




    public static void testSaveImage(View view, Handler mainHandler){
        ViewGroup viewGroup=(ViewGroup) view.getParent().getParent();
        for(int i=0; i<viewGroup.getChildCount(); i++){
            final View childView = viewGroup.getChildAt(i);
            MyLog.e("childview-----"+childView.toString());
          //  if(childView.getClass().getName().contains("Button")){
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyLog.e("childview-----"+childView.getClass().getName()+"---id:"+childView.getId());
                        Bitmap bitmap = copyByDrawingCache(childView);
                        if(null != bitmap ){
                            saveBitmap(bitmap,childView.getClass().getName()+"-"+childView.getId());
                        }
                        else{
                            MyLog.e("childview-----"+"获取图片失败");
                        }

                    }
                });
        //    }


        }
    }







    // TODO: 2017/9/7  ---- 保存view的视图到sdcard  --- 注意路径被重定向

    /**
     * 通过drawingCache获取bitmap
     */
    public static Bitmap copyByDrawingCache(View mOriginImageView) {
        mOriginImageView.setDrawingCacheEnabled(true);
        mOriginImageView.buildDrawingCache(true);
        final Bitmap bp = mOriginImageView.getDrawingCache();
        if(bp != null){
            Bitmap finalBp = Bitmap.createBitmap(bp);
            mOriginImageView.setDrawingCacheEnabled(false);
            return finalBp;
        }
        return null;
    }

    public static  void saveBitmap(Bitmap bm,String name) {
        MyLog.e("view--  saveBitmap:"+bm.toString());
        File f = new File(Environment.getExternalStorageDirectory(), "/aaa/"+name+".png");
        MyLog.e("view--  name:"+f.getAbsolutePath());
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            MyLog.e("view--  saveBitmap: succ"+name);
        } catch (Exception e) {
            MyLog.e("view--  1111: succ"+       e.getMessage());

        }

    }





}
