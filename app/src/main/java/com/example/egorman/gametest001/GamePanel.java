package com.example.egorman.gametest001;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Egorman on 21.04.2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    private MainThread thread;
    private SliderUI slider;
    private VerticalSliderUI vslider;
    private PlayerCar pcar;
    private ButtonF buttonF;
    private ButtonB buttonB;
    public GamePanel(Context context)
    {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        pcar = new PlayerCar(250,250, 100, 200, Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.car),100,200, true));
        slider = new SliderUI(100, Constants.SCREEN_HEIGHT-80, 400, 20);
        vslider = new VerticalSliderUI(Constants.SCREEN_WIDTH-80, 300, 20, 400);
        buttonF = new ButtonF(Constants.SCREEN_WIDTH-340, Constants.SCREEN_HEIGHT-100, 80, 80,
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.fbtnon),80,80, true),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.fbtnoff),80,80, true));
        buttonB = new ButtonB(Constants.SCREEN_WIDTH-220, Constants.SCREEN_HEIGHT-100, 80, 80,
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bbtnon),80,80, true),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bbtnoff),80,80, true));

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(buttonF.isTouched((int)event.getX(), (int)event.getY()) && !buttonF.getState()){
                    pcar.setSpeedMul(1);
                    pcar.setSpeed(vslider.getSpeed());
                    buttonB.changeState();
                    buttonF.changeState();
                } else if(buttonB.isTouched((int)event.getX(), (int)event.getY()) && !buttonB.getState()){
                    pcar.setSpeedMul(-0.7f);
                    pcar.setSpeed(vslider.getSpeed());
                    buttonF.changeState();
                    buttonB.changeState();
                }
            case MotionEvent.ACTION_MOVE:
                if(slider.isTouched((int)event.getX(), (int)event.getY()))
                {
                    slider.interact=true;
                    slider.update((int)event.getX());
                }
                else slider.interact=false;
                if(vslider.isTouched((int)event.getX(), (int)event.getY()))
                {
                    vslider.interact=true;
                    vslider.update((int)event.getY());
                }
                else vslider.interact=false;
        }
        //return super.onTouchEvent(event);
        return true;
    }

    public void update()
    {
        //pcar.rotate(1);
        if(slider.getAngle()<0) pcar.left=true; else pcar.left=false;
        pcar.rotate(slider.getAngle());
        pcar.setSpeed(vslider.getSpeed());
        //pcar.move(5);
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        canvas.drawColor(Color.GRAY);
        pcar.draw(canvas);
        slider.draw(canvas);
        vslider.draw(canvas);
        buttonF.draw(canvas);
        buttonB.draw(canvas);
    }
}

