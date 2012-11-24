package se.kicksort.winterwallpaper.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.FloatMath;

public class Tree {
	public static final int TRUNK_COLOR = Color.BLACK;
	public static final int SHADOW_COLOR = Color.rgb(165, 96, 7);
	public static final int RED_BASE = 240;
	public static final int GREEN_BASE = 230;
	public static final int BLUE_BASE = 220;
	private static final int CANOPY_CIRCLES = 20;

	private float x;
	private float y;
	private float pointRadius;

	private float[] randomCanopyPoints;

	private int screenWidth;
	private float currentOffset;

	private Paint trunkPaint;
	private Paint shadowPaint;
	private Paint canopyPaint;

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Tree(float x, float y, int screenWidth) {
		this.x = x;
		this.setY(y);

		this.screenWidth = screenWidth;

		this.pointRadius = (float) Math.random() * 16 + 5;

		trunkPaint = new Paint();
		trunkPaint.setColor(TRUNK_COLOR);

		shadowPaint = new Paint();
		shadowPaint.setColor(SHADOW_COLOR);

		canopyPaint = new Paint();
		canopyPaint.setColor(Color.rgb(
				RED_BASE + (int) Math.floor(Math.random() * 10), GREEN_BASE
						+ (int) Math.floor(Math.random() * 20), BLUE_BASE
						+ (int) Math.floor(Math.random() * 30)));

		for (int i = 0; i < CANOPY_CIRCLES; i++) {
			this.randomCanopyPoints[i] = (float) (Math.random()
					* this.pointRadius / 2) + 3;
		}

	}

	public void draw(Canvas c, float offset) {
		if (offset != currentOffset) {

			if (offset > currentOffset) {
				x -= getY() / 100;
			} else {
				x += getY() / 100;
			}

			currentOffset = offset;
		}

		if (x <= -20) {
			x = screenWidth + 20;
		} else if (x > screenWidth) {
			x = -20;
		}

		if (x > 0 & x < screenWidth) {
			Path path = new Path();

			// Shadow movement
			path.moveTo(x, getY());
			path.lineTo(x, getY() + 32);
			path.lineTo(x + 22, getY() + 38);
			path.lineTo(x, getY() + 35);
			path.close();
			c.drawPath(path, shadowPaint);

			// Trunk movement
			path.reset();
			path.moveTo(x, getY());
			path.lineTo(x + pointRadius / 4, getY() + 34);
			path.lineTo(x, getY() + 35);
			path.lineTo(x - pointRadius / 4, getY() + 34);
			path.close();
			c.drawPath(path, trunkPaint);

			// Set up canopy circle
			path.reset();
			path.addCircle(x, getY(), pointRadius, Path.Direction.CW);
			path.close();
			c.drawPath(path, canopyPaint);

			for (int i = 0; i < CANOPY_CIRCLES; i++) {
				float angle = (float) ((float) i / 10 * Math.PI);
				float x1 = (float) (this.x + FloatMath.cos(angle) * pointRadius);
				float y1 = (float) (this.getY() + FloatMath.sin(angle) * pointRadius);

				path.reset();
				path.addCircle(x1, y1, this.randomCanopyPoints[i],
						Path.Direction.CW);
				path.close();
				c.drawPath(path, canopyPaint);
			}

		}
	}

}
