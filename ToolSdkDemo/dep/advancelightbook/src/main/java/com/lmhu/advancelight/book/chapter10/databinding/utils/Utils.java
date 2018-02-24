package com.lmhu.advancelight.book.chapter10.databinding.utils;

import android.databinding.BindingConversion;

import com.lmhu.advancelight.book.chapter10.databinding.model.Swordsman;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hulimin on 2018/2/24.
 */

public class Utils {
    public static String getName(Swordsman swordsman){
        return  swordsman.getName();
    }


    @BindingConversion
    public static String convertDate(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        return sdf.format(date);
    }

}
