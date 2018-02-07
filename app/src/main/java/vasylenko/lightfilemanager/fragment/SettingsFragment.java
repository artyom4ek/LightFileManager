package vasylenko.lightfilemanager.fragment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import vasylenko.lightfilemanager.R;
import vasylenko.lightfilemanager.local.LocaleManager;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        //TODO: Need to add recreate() MainActivity for change language in Runtime.
        final ListPreference listPreference = (ListPreference) findPreference("pref_key_languages_list");
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String currentAppLanguage = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_key_languages_list",
                        getResources().getString(R.string.default_language));
                LocaleManager.changeLocale(getResources(), currentAppLanguage);
                return true;
            }
        });

    }

}
