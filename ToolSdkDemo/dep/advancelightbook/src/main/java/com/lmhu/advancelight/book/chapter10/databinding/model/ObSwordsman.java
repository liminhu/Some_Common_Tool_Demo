package com.lmhu.advancelight.book.chapter10.databinding.model;

import android.databinding.BaseObservable;

/**
 * Created by hulimin on 2018/2/24.
 */

public class ObSwordsman extends BaseObservable{
    private String name;
    private String level;

    public ObSwordsman(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChange();
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
