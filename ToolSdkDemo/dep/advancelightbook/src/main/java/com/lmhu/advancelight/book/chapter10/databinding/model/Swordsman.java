package com.lmhu.advancelight.book.chapter10.databinding.model;

/**
 * Created by hulimin on 2018/2/24.
 */

public class Swordsman {
    private String name;
    private String level;

    public Swordsman(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
