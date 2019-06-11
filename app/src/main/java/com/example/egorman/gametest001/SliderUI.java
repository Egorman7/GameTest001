package com.example.egorman.gametest001;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Egorman on 22.04.2017.
 */

public class SliderUI implements GameObject{
    public int x,y;
    public int width, height;
    private int sliderX, sliderY, sWidht, sHeight;
    public boolean interact;
    private Rect rect;

    public SliderUI(int x, int y, int w, int h)
    {
        this.x=x; this.y=y;
        width=w; height=h;
        sWidht = 20;
        sHeight = height+40;
        sliderX = (2*x+w)/2 - sWidht/2;
        sliderY = y-sHeight/2;
        interact = false;
        rect = new Rect(x,y,x+w,y+h);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(x,y,x+width,y+height,paint);
        paint.setColor(Color.BLUE);
        canvas.drawRect(sliderX, sliderY, sliderX+sWidht, sliderY+sHeight, paint);
    }

    @Override
    public void update()
    {

    }

    public float getAngle()
    {
        int sCx = sliderX+sWidht/2;
        int Cx = x+width/2;
        return -(float)(60f*(Cx-sCx)/(width/2));
    }

    public boolean isTouched(int x, int y)
    {
        return rect.contains(x,y);
    }

    public void update(int x)
    {
        sliderX=x;
        if(x<this.x-sWidht/2) sliderX=this.x-sWidht/2;
        if(x>this.x+width-sWidht/2) sliderX=this.x+width-sWidht/2;
    }
}
