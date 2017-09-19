package com.lyj.bannerview.Banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yu on 2017/9/18.
 */

public class DotIndicatorView extends View {
    private Drawable mDrawable;

    public DotIndicatorView(Context context) {
        this(context,null);
    }

    public DotIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DotIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mDrawable != null){
//            mDrawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
//            mDrawable.draw(canvas);
            //绘制圆形
            Bitmap bitmap = drawable2Bitmap(mDrawable);
            Bitmap circleBitmap = getCircleBitmap(bitmap);
            canvas.drawBitmap(circleBitmap,0,0,null);
        }
    }

    /**
     * 把drawable转换成bitmap
     * @param drawable
     * @return
     */
    private Bitmap drawable2Bitmap(Drawable drawable) {
        if(drawable instanceof BitmapDrawable){
            return  (Bitmap) ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap outBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        drawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
        drawable.draw(canvas);
        return outBitmap;
    }

    /**
     * 获取圆形bitmap
     * @param bitmap
     * @return
     */
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        //防抖动
        paint.setDither(true);
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,getMeasuredHeight()/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,0,0,paint);

        return circleBitmap;
    }

    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        invalidate();
    }
}
