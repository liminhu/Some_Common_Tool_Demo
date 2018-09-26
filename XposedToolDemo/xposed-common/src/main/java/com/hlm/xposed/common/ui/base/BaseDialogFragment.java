/*
 * Copyright (c) 2018 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hlm.xposed.common.ui.base;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hlm.xposed.common.Constant;
import com.hlm.xposed.common.helper.ReceiverHelper;
import com.hlm.xposed.common.ui.interfaces.TrackViewStatus;
import com.hlm.xposed.common.util.Pair;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return createView(inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化View
        initView(view, getArguments());
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initView(View view, Bundle args);

    public Context getContext() {
        return getActivity();
    }

    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    public SharedPreferences getSharedPreferences(String name) {
        return getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public abstract SharedPreferences getDefaultSharedPreferences();

    public <T> T trackBind(TrackViewStatus<T> track, String key, T defValue, TrackViewStatus.StatusChangeListener<T> listener) {
        return track.bind(getDefaultSharedPreferences(), key, defValue, listener);
    }

    public void sendRefreshPreferenceBroadcast(String key, Object value) {

        List<Pair<String, Object>> data = Arrays.asList(
                new Pair<String, Object> (key, value));

        Intent intent = new Intent(Constant.Action.REFRESH_PREFERENCE);
        intent.putExtra(Constant.Key.DATA, (Serializable) data);

        // 发送广播
        ReceiverHelper.sendBroadcastReceiver(getActivity(), intent);
    }
}
