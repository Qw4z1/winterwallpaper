package se.kicksort.winterwallpaper.model;

import android.graphics.Paint;

public class Item {
	private float x;
	private float y;
	private float radius;
	
	private float[] randomCanopyPoints;
	
	private int screenWidth;
	private float curOffset;
	
	private Paint trunkPaint;
	private Paint shadowPaint;
	private Paint canopyPaint;
	
	public Item(float x, float y ,int screenWidth){
		this.x = x;
		this.y = y;
		this.screenWidth = screenWidth;
		
		trunkPaint = new Paint();
		shadowPaint.setColor(color)
	}
	
}
