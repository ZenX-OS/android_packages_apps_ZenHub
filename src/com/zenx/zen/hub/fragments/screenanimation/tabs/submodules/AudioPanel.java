/*
 * Copyright (C) 2020 Zenx-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zenx.zen.hub.fragments.screenanimation.tabs.submodules;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.preference.*;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class AudioPanel extends SettingsPreferenceFragment  implements Preference.OnPreferenceChangeListener {

    private ListPreference mVolumePanelTheme;
    private ListPreference mVolumeAlignment;
    private static final String SYSTEMUI_PLUGIN_VOLUME = "systemui_plugin_volume";
    private static final String VOLUME_PANEL_ALIGNMENT = "volume_panel_alignment";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.audio_panel);
        final PreferenceScreen prefScreen = getPreferenceScreen();

        ContentResolver resolver = getActivity().getContentResolver();
        Resources resources = getResources();

        // set volume panel theme
        mVolumePanelTheme = (ListPreference) findPreference(SYSTEMUI_PLUGIN_VOLUME);
        String style = Settings.System.getString(resolver, Settings.System.SYSTEMUI_PLUGIN_VOLUME);
        mVolumePanelTheme.setValue(style);
        mVolumePanelTheme.setSummary(mVolumePanelTheme.getEntry());
        mVolumePanelTheme.setOnPreferenceChangeListener(this);

        // set volume alignment
        mVolumeAlignment = (ListPreference) findPreference(VOLUME_PANEL_ALIGNMENT);
        int align = Settings.System.getInt(resolver,
                Settings.System.VOLUME_PANEL_ALIGNMENT, 1);
        mVolumeAlignment.setValue(String.valueOf(align));
        mVolumeAlignment.setSummary(mVolumeAlignment.getEntry());
        mVolumeAlignment.setOnPreferenceChangeListener(this);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mVolumePanelTheme) {
            String style = (String) newValue;
            int index = mVolumePanelTheme.findIndexOfValue((String) newValue);
            Settings.System.putString(getActivity().getContentResolver(),
                    Settings.System.SYSTEMUI_PLUGIN_VOLUME, style);
            mVolumePanelTheme.setSummary(mVolumePanelTheme.getEntries()[index]);
            return true;
        } else if (preference == mVolumeAlignment) {
            int align = Integer.valueOf((String) newValue);
            int index = mVolumeAlignment.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.VOLUME_PANEL_ALIGNMENT, align);
            mVolumeAlignment.setSummary(mVolumeAlignment.getEntries()[index]);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.ZENX_SETTINGS;
    }
}