package se.kicksort.winterwallpaper;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class WinterPreferenceActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencescreen);
        
        Preference circlePreference = getPreferenceScreen().findPreference("numberOfTrees");
        
        circlePreference.setOnPreferenceChangeListener(numberCheckListener);
    }

    
    /**
     * Checks that a preference is a valid numerical value
     */

    Preference.OnPreferenceChangeListener numberCheckListener = new OnPreferenceChangeListener() {

      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
        // Check that the string is an integer
        if (newValue != null && newValue.toString().length() > 0
            && newValue.toString().matches("\\d*")) {
          return true;
        }
        // If now create a message to the user
        Toast.makeText(WinterPreferenceActivity.this, "Invalid Input",
            Toast.LENGTH_SHORT).show();
        return false;
      }
    };
}
