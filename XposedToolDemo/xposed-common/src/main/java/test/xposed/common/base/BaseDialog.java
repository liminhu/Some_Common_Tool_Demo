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

package test.xposed.common.base;

import android.content.SharedPreferences;

import com.hlm.xposed.common.ui.base.BaseDialogFragment;

import test.xposed.common.Constant;

public abstract class BaseDialog extends BaseDialogFragment {

    @Override
    public SharedPreferences getDefaultSharedPreferences() {
        return getSharedPreferences(Constant.Name.XPOSED);
    }
}
