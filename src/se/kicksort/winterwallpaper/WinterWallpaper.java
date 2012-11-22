package se.kicksort.winterwallpaper;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.service.wallpaper.WallpaperService;

public class WinterWallpaper extends WallpaperService implements
			OnSharedPreferenceChangeListener{
	public static final String PREFERENCES = "se.kicksort.android.winterwallpaper";
	@Override
	public Engine onCreateEngine() {
		return null;
		
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
		
	}

}
