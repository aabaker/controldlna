/*
Copyright (c) 2013, Felix Ableitner
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
	  notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
	  notice, this list of conditions and the following disclaimer in the
	  documentation and/or other materials provided with the distribution.
 * Neither the name of the <organization> nor the
	  names of its contributors may be used to endorse or promote products
	  derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.nutomic.controldlna.gui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.github.nutomic.controldlna.R;

public class PreferencesActivity extends PreferenceActivity
		implements SharedPreferences.OnSharedPreferenceChangeListener {

	public static final String KEY_ENABLE_WIFI_ON_START = "enable_wifi_on_start";
	public static final String KEY_INCOMING_PHONE_CALL_PAUSE = "incoming_phone_call_pause";
	private static final String KEY_CONTACT_DEV = "contact_dev";

	private ListPreference mEnableWifiOnStart;
	private Preference mContactDev;

	/**
	 * Initializes preferences from xml.
	 *
	 * Manual target API as we manually check if ActionBar is available (for ActionBar back button).
	 */
	@Override
	@TargetApi(11)
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Ideally this code should call getActionBar().setDisplayHomeAsUpEnabled(true) but
		// it isn't possible to do that with the current appcompat library without re-writing
		// preferences to use fragments

		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
		addPreferencesFromResource(R.xml.preferences);
		final PreferenceScreen screen = getPreferenceScreen();
		mEnableWifiOnStart = (ListPreference) screen.findPreference(KEY_ENABLE_WIFI_ON_START);
		mEnableWifiOnStart.setSummary(mEnableWifiOnStart.getEntry());
		mContactDev = screen.findPreference(KEY_CONTACT_DEV);
	}

	/**
	 * Navigates up from activity on ActionBar back click.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Sends mail intent on contact dev preference click.
	 */
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
										 Preference preference) {
		if (preference == mContactDev) {
			startActivity(new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
					"mailto", getString(R.string.contact_mail, "@", "."), null)));
			return true;
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	/**
	 * Updates summary of list preference (from current item).
	 */
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(KEY_ENABLE_WIFI_ON_START) && mEnableWifiOnStart != null) {
			mEnableWifiOnStart.setSummary(mEnableWifiOnStart.getEntry());
		}
	}

}
