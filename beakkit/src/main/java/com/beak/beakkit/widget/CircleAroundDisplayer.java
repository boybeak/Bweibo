package com.beak.beakkit.widget;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class CircleAroundDisplayer implements BitmapDisplayer {

    private static final String TAG = CircleAroundDisplayer.class.getSimpleName();

	private int mSize, mRound = 0;

    private Canvas mCanvas = null;
    private Paint mPaint = null;
    private Rect mRect = null;
    private RectF mRectF = null;

    public CircleAroundDisplayer(int size) {
        this (size, size / 2);
    }

	public CircleAroundDisplayer(int size, int round) {
		mSize = size;
		mRound = round;
        mCanvas = new Canvas();
        mPaint = new Paint();
        mRect = new Rect();
        mRectF = new RectF();
	}

	@Override
	public void display(Bitmap bmp, ImageAware aware, LoadedFrom from) {
		final int wid = bmp.getWidth();
		final int hei = bmp.getHeight();
		
		float scale = Math.min((float)wid / mSize, (float)hei / mSize);
		final int scaleSize = (int)(mSize * scale);
		Bitmap square = Bitmap.createBitmap(bmp, (wid - scaleSize) / 2, (hei - scaleSize) / 2, scaleSize, scaleSize);
        //Log.e(TAG, "display " + mSize + " ori bmp = " + square);
        Bitmap scaledBmp = Bitmap.createScaledBitmap(square, mSize, mSize, true);
        //square.recycle();
		
		aware.setImageBitmap(toRoundCorner(scaledBmp, mRound));
	}
	
	public  Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        Bitmap roundCornerBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        mCanvas.setBitmap(roundCornerBitmap);
        int color = 0xff424242;//int color = 0xff424242;
        mPaint.setColor(color);
        //防止锯齿
        mPaint.setAntiAlias(true);
        mRect.set(0, 0, width, height);
        mRectF.set(mRect);
        float roundPx = pixels;
        //相当于清屏
        mCanvas.drawARGB(0, 0, 0, 0);

        //先画了一个带圆角的矩形
        mCanvas.drawRoundRect(mRectF, roundPx, roundPx, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        //再把原来的bitmap画到现在的bitmap！！！注意这个理解
        mCanvas.drawBitmap(bitmap, mRect, mRect, mPaint);
        mPaint.setStrokeWidth(4);
        mCanvas.drawOval(mRectF, mPaint);
        return roundCornerBitmap;
    }

}
