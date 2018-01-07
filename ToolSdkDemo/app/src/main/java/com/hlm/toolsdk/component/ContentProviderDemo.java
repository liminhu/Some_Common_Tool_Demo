package com.hlm.toolsdk.component;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.Toast;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/1/7.
 */

public class ContentProviderDemo extends ContentProvider {
    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        MyLog.e("B is call A test call method ... "+method);
        //Toast.makeText(getContext(), "call method "+method, Toast.LENGTH_SHORT).show();
        Bundle bundle=new Bundle();
        bundle.putString("data", "method name ... "+method);
        return bundle;
    }





    @Override
    public boolean onCreate() {
        MyLog.e("this is a false ...");
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
