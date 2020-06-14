/*
 * Copyright (C) 2020 ZenX-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zenx.zen.hub.fragments.screenanimation.tabs;

import android.app.settings.SettingsEnums;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import androidx.preference.*;
import android.provider.Settings;
import com.android.internal.util.zenx.Utils;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.display.NightModePreferenceController;
import com.android.settings.display.ThemePreferenceController;
import com.android.settings.search.Indexable;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.search.SearchIndexable;

import com.zenx.support.preferences.SystemSettingSwitchPreference;

import java.util.ArrayList;
import java.util.List;

public class Ui extends DashboardFragment implements Preference.OnPreferenceChangeListener {
    private static final String TAG = "DisplaySettings";
    private static final String STATUSBAR_DUAL_ROW = "statusbar_dual_row";

    private SystemSettingSwitchPreference mStatusbarDualRow;

    @Override
    public int getMetricsCategory() {
        return SettingsEnums.DISPLAY;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.zen_hub_ui;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mStatusbarDualRow = (SystemSettingSwitchPreference) findPreference(STATUSBAR_DUAL_ROW);
        mStatusbarDualRow.setChecked((Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.STATUSBAR_DUAL_ROW, 0) == 1));
        mStatusbarDualRow.setOnPreferenceChangeListener(this);
    }

    @Override
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle());
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(
            Context context, Lifecycle lifecycle) {
        final List<AbstractPreferenceController> controllers = new ArrayList<>();
        controllers.add(new NightModePreferenceController(context));
        controllers.add(new ThemePreferenceController(context));
        return controllers;
    }

        @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mStatusbarDualRow) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_DUAL_ROW, value ? 1 : 0);
            Utils.showSystemUiRestartDialog(getContext());
            return true;
        }
        return false;
    }


}
