package com.example.egorman.gametest001;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Egorman on 04.06.2017.
 */

public class ButtonF implements GameObject{
    private int x, y, w, h;
    private boolean pressed=true;
    private int color;
    private Bitmap texture1, texture2; // 1 - on, 2 - off
    private Rect rect;

    public ButtonF(int x, int y, int w, int h, Bitmap t1, Bitmap t2){
        this.x=x; this.y=y; this.w=w; this.h=h;
        texture1=t1; texture2=t2;
        color = Color.GREEN;
        rect = new Rect(x,y,x+w,y+h);
    }

    public boolean isTouched(int x, int y)
    {
        return rect.contains(x,y);
    }

    public boolean getState(){return pressed;}

    public void changeState(){
        if(pressed) color=Color.RED; else color=Color.GREEN;
        pressed=!pressed;
    }

    @Override
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(color);
        //canvas.drawRect(x,y,x+w,y+h, paint);
        canvas.drawBitmap(pressed ? texture1 : texture2, x,y, paint);
    }

    @Override
    public void update(){

    }
}
