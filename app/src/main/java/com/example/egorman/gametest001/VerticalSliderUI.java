package com.example.egorman.gametest001;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Egorman on 04.06.2017.
 */

public class VerticalSliderUI implements GameObject {
    public int x,y;
    public int width, height;
    private int sliderX, sliderY, sWidht, sHeight;
    private Rect rect;
    public boolean interact;

    public VerticalSliderUI(int x, int y, int w, int h)
    {
        this.x=x; this.y=y;
        width=w; height=h;
        sWidht = width+40;
        sHeight = 20;
        sliderX = (2*x+w)/2 - sWidht/2;
        sliderY = y+height;
        interact = false;
        rect = new Rect(x,y,x+w,y+h);
    }

    public boolean isTouched(int x, int y)
    {
        return rect.contains(x,y);
    }

    public float getSpeed()
    {
        float sCy = sliderY+sHeight/2;
        float Cy = y+height-height*0.2f;
        float res=0;
        if(Cy-sCy > 0){
            res = 5f*(Cy-sCy)/(height);
        }
        return res;
    }

    @Override
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(x,y,x+width,y+height,paint);
        paint.setColor(Color.BLUE);
        canvas.drawRect(sliderX, sliderY, sliderX+sWidht, sliderY+sHeight, paint);
    }

    public void update(int y){
        sliderY=y;
        if(y<this.y-sHeight/2) sliderY=this.y-sHeight/2;
        if(y>this.y+height-sHeight/2) sliderY=this.y+height-sHeight/2;
    }

    @Override
    public void update() {
    }
}
