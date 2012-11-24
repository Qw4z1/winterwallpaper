package se.kicksort.winterwallpaper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.kicksort.winterwallpaper.model.Tree;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

public class WinterWallpaper extends WallpaperService implements
		OnSharedPreferenceChangeListener {
	public static final String PREFERENCES = "se.kicksort.android.winterwallpaper";
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new ForestEngine();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}

	class ForestEngine extends Engine implements
			SharedPreferences.OnSharedPreferenceChangeListener {

		private final Handler mHandler = new Handler();

		private int width = 0;
		private int height = 0;

		private final Paint mPaint = new Paint();
		private float mOffset;
		private float mTouchX = -1;
		private float mTouchY = -1;
		private long mStartTime;
		private float mCenterX;
		private float mCenterY;

		/** Trees **/
		private List<Tree> trees = new ArrayList<Tree>();

		private final Runnable mForest = new Runnable() {
			public void run() {
				drawFrame();
			}
		};
		private boolean mVisible;

		ForestEngine() {
			// Create a Paint to draw the lines for our cube
			final Paint paint = mPaint;
			paint.setColor(Color.rgb(54, 255, 0));
			paint.setAntiAlias(true);
			paint.setStrokeWidth(2);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStyle(Paint.Style.STROKE);

			mStartTime = SystemClock.elapsedRealtime();
		}

		public void onSharedPreferenceChanged(SharedPreferences prefs,
				String key) {

		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mForest);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				drawFrame();
			} else {
				mHandler.removeCallbacks(mForest);
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);

			Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();

			this.width = display.getWidth();
			this.height = display.getHeight();

			drawFrame();
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mForest);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
				float yStep, int xPixels, int yPixels) {
			mOffset = xOffset;
			drawFrame();
		}

		/*
		 * Store the position of the touch event so we can use it for drawing
		 * later
		 */
		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				mTouchX = event.getX();
				mTouchY = event.getY();
			} else {
				mTouchX = -1;
				mTouchY = -1;
			}
			super.onTouchEvent(event);
		}

		/*
		 * Draw one frame of the animation. This method gets called repeatedly
		 */
		private void drawFrame() {
			final SurfaceHolder holder = getSurfaceHolder();
			// Create the initial trees
			if (trees.size() == 0) {
				for (int i = 0; i < 60; i++) {
					float yPos = this.height
							- ((float) Math.random() * (this.height - 190));

					float xPos = (float) Math.random() * this.width;
					if (xPos > this.width) {
						xPos = this.width - 10;
					}
					trees.add(new Tree(xPos, yPos, this.width));
				}
			}
			Collections.sort(trees, new Comparator<Tree>() {
				@Override
				public int compare(Tree tree1, Tree tree2) {
					Float change1 = Float.valueOf(tree1.getY());
					Float change2 = Float.valueOf(tree2.getY());
					return change1.compareTo(change2);
				}

			});

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					// draw something
					drawForest(c);
					for (Tree tree : trees) {
						tree.draw(c, mOffset);
					}
					drawTouchPoint(c);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}

			mHandler.removeCallbacks(mForest);
			if (mVisible) {
				mHandler.postDelayed(mForest, 1000 / 15);
			}
		}

		private void drawForest(Canvas c) {
			// Draw the background
			Paint paint = new Paint();
			paint.setColor(Color.rgb(180, 210, 230));
			c.drawRect(0, 0, this.width, 220, paint);
			paint.setColor(Color.rgb(200, 123, 26));
			c.drawRect(0, 220, this.width, this.height, paint);
		}

		private void drawTouchPoint(Canvas c) {
			// TODO do something cool when touched
		}
	}

}
