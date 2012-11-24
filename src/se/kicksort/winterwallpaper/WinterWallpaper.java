package se.kicksort.winterwallpaper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.kicksort.winterwallpaper.model.Tree;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

public class WinterWallpaper extends WallpaperService {
	public static final String PREFERENCES = "se.kicksort.android.winterwallpaper";

	@Override
	public Engine onCreateEngine() {
		return new ForestEngine();
	}

	
	

	class ForestEngine extends Engine implements
			SharedPreferences.OnSharedPreferenceChangeListener {

		private final Handler mHandler = new Handler();

		private boolean mVisible = true;
		
		private int mWidth;
		private int mHeight;
		
		private int numberOfTrees;

		private float mOffset;
		private float mTouchX = -1;
		private float mTouchY = -1;
		private long mStartTime;
		private float mCenterX;
		private float mCenterY;

		/** Trees **/
		private List<Tree> trees = new ArrayList<Tree>();

		private final Runnable runner = new Runnable() {
			public void run() {
				drawFrame();
			}
		};
		
		public ForestEngine() {
			SharedPreferences prefs = PreferenceManager
			          .getDefaultSharedPreferences(WinterWallpaper.this);
			
			numberOfTrees = Integer.valueOf(prefs.getString("numberOfTrees", "20"));
			
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
			mHandler.removeCallbacks(runner);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				mHandler.postDelayed(runner, 1000 / 15);
			} else {
				mHandler.removeCallbacks(runner);
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);

			Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();

			setScreenSize();
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
			mHandler.removeCallbacks(runner);
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
			if (trees.isEmpty()) {
				for (int i = 0; i < numberOfTrees; i++) {
					float yPos = this.mHeight
							- ((float) Math.random() * (this.mHeight - 190));

					float xPos = (float) Math.random() * this.mWidth;
					if (xPos > this.mWidth) {
						xPos = this.mWidth - 10;
					}
					trees.add(new Tree(xPos, yPos, this.mWidth));
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

			mHandler.removeCallbacks(runner);
			if (mVisible) {
				mHandler.postDelayed(runner, 1000 / 15);
			}
		}

		private void drawForest(Canvas c) {
			// Draw the background
			Paint paint = new Paint();
			paint.setColor(Color.rgb(180, 210, 230));
			c.drawRect(0, 0, mWidth, mHeight/4, paint);
			
			paint.setColor(Color.rgb(200, 123, 26));
			c.drawRect(0, mHeight/4, mWidth, mHeight, paint);
		}

		private void drawTouchPoint(Canvas c) {
			// TODO do something cool when touched
		}
		
		private void setScreenSize(){
		    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
		        size_old(((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay());
		    }
		    else {
		        size_new(((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay());
		    }
		}
		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		private void size_new(Display display) {
		    Point point = new Point();
		    display.getSize(point);
		    mHeight = point.x;
		    mWidth = point.y;
		}

		@SuppressWarnings("deprecation")
		private void size_old(Display display) {
		    mHeight = display.getHeight();
		    mWidth = display.getWidth();
		}
		
		
	}

}
