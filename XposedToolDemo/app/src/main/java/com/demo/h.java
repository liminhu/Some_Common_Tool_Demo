package com.demo;

import android.text.TextUtils;

import com.my.utils.tool.MyLog;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class h {
    public static void a(String arg3, String arg4) {
        FileOutputStream v1;
        FileOutputStream v2 = null;
        try {
            v1 = new FileOutputStream(arg3);
            v1.write(arg4.getBytes());
            v1.flush();
            if(v1 == null) {
                return;
            }

        }
        catch(Exception v0) {

        }finally {
            try {
                if(v2!=null){
                    v2.close();
                }
            }catch (Exception e){

            }

        }
        return;
    }

    public static String a(String arg5) {
        String v0_2=null;
        FileInputStream v2=null;
        String v1 = null;
        try {
            v2 = new FileInputStream(arg5);
            byte[] v3 = new byte[v2.available()];
            v2.read(v3);
            v0_2 = new String(v3, "UTF-8");
            if(v2 == null) {
                return v0_2;
            }
        }catch (Exception e){

        }finally {
            try {
                if(v2!=null){
                    v2.close();
                }
            }catch (Exception e){

            }

        }
        return v0_2;
    }

    public static String b(String arg20) {
        String v3_1;
        int v3 = arg20.indexOf(".BOTTLE.bodyDepth");
        MyLog.e("JumpHook  --- %s ", "TTLE.bodyDepth ... " + v3);
        if(v3 >= 1) {
            v3_1 = arg20.substring(v3 - 1, v3);
            MyLog.d("JumpHook  ---- %s ", "confuseClass ..." + v3_1 + "...");
            if(TextUtils.isEmpty(((CharSequence)v3_1))) {
                v3_1 = null;
            }
            else {
                String v4 = "this.combo=new ";
                int v5 = arg20.indexOf(v4);
                String v6 = v3_1 + ".GAME";
                int v7 = arg20.indexOf(v6);
                int v8 = arg20.indexOf("scene.add");
                String v9 = "this.renderer.render(this.scene,this.camera)";
                int v10 = arg20.indexOf(v9);
                int v11 = arg20.indexOf("bottle.status");
                int v12 = arg20.indexOf("mouseDownTime");
                String v13 = v3_1 + ".BOTTLE";
                int v14 = arg20.indexOf(v13);
                String v15 = v3_1 + ".BLOCK";
                int v16 = arg20.indexOf(v15);
                CharSequence v3_2 = null;
                if(v5 != -1) {
                    int v17 = arg20.indexOf(".", v4.length() + v5);
                    if(v17 != -1) {
                        v3_1 = arg20.substring(v4.length() + v5, v17);
                        MyLog.d("JumpHook  ---- %s ", "hookConfusedJs className ..." + v3_1 + "...");
                        v3_1.trim();
                    }
                }

                int v4_1 = -1;
                if(!TextUtils.isEmpty(v3_2)) {
                    v4_1 = arg20.indexOf((((String)v3_2)) + ".Mesh");
                }
                MyLog.d("JumpHook  ---- %s ", "hookConfusedJs  ---index1 " + v5 + ", " + v7 + ", " + v8 + ", " + v10 + ", " + v11 + ", " + v12 + ", " + v14 + ", " + v4_1 + ", " + v16 + ", ..." + (((String)v3_2)) + "...");
                if(!TextUtils.isEmpty(v3_2) && v5 != -1 && v7 != -1 && v8 != -1 && v10 != -1 && v11 != -1 && v12 != -1 && v14 != -1 && v4_1 != -1 && v16 != -1) {
                    v3_1 = arg20.substring(0, v5) + ("this.vectorHelperOne=new " + (((String)v3_2)) + ".Vector2(0, 0);this.vectorHelperTwo=new " + (((String)v3_2)) + ".Vector2(0, 0);this.helperLine=new " + (((String)v3_2)) + ".Line();this.helperLine.material.color.setHex(0x0000ff);var pointsOfHelperLine=new Float32Array(6);this.helperLine.geometry.addAttribute(\"position\",new " + (((String)v3_2)) + ".BufferAttribute(pointsOfHelperLine,3));this.helperLine.geometry.attributes.position.setDynamic(true);this.helperLine.name=\"helper_line\";this.scene.add(this.helperLine);this.helperArrow=new " + (((String)v3_2)) + ".Mesh(new " + (((String)v3_2)) + ".CircleGeometry(.6, 50),new " + (((String)v3_2)) + ".MeshBasicMaterial({color: 255}));this.helperArrow.name=\"helper_arrow\";this.helperArrow.position.x=-500;this.helperArrow.rotation.x=-Math.PI/2;this.scene.add(this.helperArrow);") + arg20.substring(v5, arg20.length());
                    v4_1 = v3_1.indexOf(v9);
                    return v3_1.substring(0, v4_1) + ("if(\"prepare\" == this.bottle.status) {var i=(Date.now()-this.mouseDownTime)/1e3;var vz=Math.min(i*" + v13 + ".velocityZIncrement,150);vz=+vz.toFixed(2);var vy=Math.min(" + v13 + ".velocityY+i*" + v13 + ".velocityYIncrement,180);vy=+vy.toFixed(2);this.vectorHelperOne.set(this.nextBlock.obj.position.x-this.bottle.obj.position.x,this.nextBlock.obj.position.z-this.bottle.obj.position.z);this.vectorHelperOne.x=+this.vectorHelperOne.x.toFixed(2);this.vectorHelperOne.y=+this.vectorHelperOne.y.toFixed(2);var r=vy/" + v6 + ".gravity*2;var n=this.bottle.obj.position.y.toFixed(2);var a=" + v15 + ".height/2-n;r=+(r-=+((-vy+Math.sqrt(Math.pow(vy,2)-2*" + v6 + ".gravity*a))/-" + v6 + ".gravity).toFixed(2)).toFixed(2);var s=[];this.vectorHelperTwo.set(this.bottle.obj.position.x,this.bottle.obj.position.z);var l=this.vectorHelperOne.setLength(vz * r);this.vectorHelperTwo.add(l);s.push(+this.vectorHelperTwo.x.toFixed(2),+this.vectorHelperTwo.y.toFixed(2));this.helperArrow.position.set(s[0],this.nextBlock.obj.position.y+" + v15 + ".height/2+.15,s[1]);var array=this.helperLine.geometry.attributes.position.array;array[0]=this.currentBlock.obj.position.x;array[1]=this.currentBlock.obj.position.y+" + v15 + ".height/2+.15;array[2]=this.currentBlock.obj.position.z;array[3]=s[0];array[4]=this.nextBlock.obj.position.y+" + v15 + ".height/2+.15;array[5]=s[1];this.helperLine.geometry.computeBoundingSphere();this.helperLine.geometry.attributes.position.needsUpdate=true;}else{this.helperArrow.position.set(-300,0,0);var array=this.helperLine.geometry.attributes.position.array;array[0]=-300;array[1]=0;array[2]=0;array[3]=-500;array[4]=0;array[5]=0;this.helperLine.geometry.computeBoundingSphere();this.helperLine.geometry.attributes.position.needsUpdate = true;}") + v3_1.substring(v4_1, v3_1.length());
                }

                v3_1 = null;
            }
        }
        else {
            v3_1 = null;
        }

        return v3_1;
    }
}
