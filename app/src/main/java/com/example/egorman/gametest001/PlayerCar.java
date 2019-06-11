package com.example.egorman.gametest001;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.Pair;
import android.view.MotionEvent;

/**
 * Created by Egorman on 22.04.2017.
 */

public class PlayerCar implements GameObject {
    //private Rect rectangle;
    private float angle;
    private int x,y,w,h;
    private float speed, speedMul;
    //private Point backWheels;
    public boolean left;
    public Wheel flWheel, frWheel, blWheel, brWheel;
    private Bitmap texture;
    private double L;
    private float pts[];
    private Matrix mx;

    public PlayerCar(int x, int y, int w, int h, Bitmap texture){
        this.x=x; this.y=y; this.w=w; this.h=h;
        this.texture = texture;
        pts = new float[8];
        pts[0]=x; pts[1]=y;
        pts[2]=x+w; pts[3]=y;
        pts[4]=x+w; pts[5]=y+h;
        pts[6]=x; pts[7]=y+h;
        flWheel = new Wheel(x+6,y+30,0);
        frWheel = new Wheel(x+w-11,y+30,0);
        blWheel = new Wheel(x+6,y+h-45,0);
        brWheel = new Wheel(x+w-11,y+h-45,0);
        speed=0f;
        speedMul = 1f;
        mx = new Matrix();
        mx.postTranslate(x,y);
        L = h-40;
        //rectangle = new Rect(x,y,x+w,y+h);
        angle = -90f;
        //speed = 5f;
        //backWheels = new Point(x+w/2, y+(h/8));
        left=true;

        //this.texture=texture;
    }
    public void setSpeed(float speed){
        this.speed=speed*speedMul;
    }
    public void setSpeedMul(float speedMul){
        this.speedMul=speedMul;
    }
    private void updateCoord(int x, int y)
    {
        this.x=x; this.y=y;
        //rectangle = new Rect(x,y,x+w,y+h);
        //backWheels = new Point(x+w/2, y+(h/8));
    }

    public Vector2d getRotationVector()
    {
        return new Vector2d((float)(Math.cos(Math.toRadians(angle))),(float)(Math.sin(Math.toRadians(angle))));
    }

    public void move(float speed)
    {
        updateCoord((int)(x+speed*getRotationVector().getX()), (int)(y+speed*getRotationVector().getY()));
    }

    private Pair<Double,Double> calculateAngle(double x, double y, double angle, double xc, double yc)
    {
        double newX = xc+(x-xc)*Math.cos(Math.toRadians(angle))+(yc-y)*Math.sin(Math.toRadians(angle));
        double newY = yc+(x-xc)*Math.sin(Math.toRadians(angle))+(y-yc)*Math.cos(Math.toRadians(angle));
        return new Pair<>(newX, newY);
    }

    private void DrawLine(float x1, float y1, float x2, float y2, Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawLine(x1,y1,x2,y2,paint);
    }

    double rCx, rCy;

    public void rotate(float angle)
    {
        if(angle>=3) {
            // ** Расчет центра окружности, относительно которой выполняется поворот машины **

            double Rf = L / Math.sin(Math.toRadians(angle));
            double Rb = L / Math.tan(Math.toRadians(angle));
            double fi = (180.0 * speed) / (Math.PI * Rf);
            this.angle += fi;
            if (this.angle > 270) this.angle -= 360;
            if (this.angle < -90) this.angle += 360;

            Vector2d dir = new Vector2d((float) Math.cos(Math.toRadians(this.angle+90)), (float) Math.sin(Math.toRadians(this.angle+90)));
            double pointX = rCx = brWheel.getCenterX() + Rb * dir.getX();
            double pointY = rCy = brWheel.getCenterY() + Rb * dir.getY();
            // ** Car rotating **
            float ddx=0, ddy=0;
            for (int i = 0; i < 8; i += 2) {
                Pair<Double, Double> p = calculateAngle(pts[i], pts[i + 1], fi, pointX, pointY);
                if(i==0)
                {
                    ddx=p.first.floatValue()-pts[i];
                    ddy=p.second.floatValue()-pts[i+1];
                }
                pts[i] = p.first.floatValue();
                pts[i + 1] = p.second.floatValue();
            }
            //mx.postTranslate(ddx,ddy);
            mx.postRotate((float)fi,(float)rCx,(float)rCy);
            // ** Wheel rotating **
            Wheel whls[] = new Wheel[4];
            whls[0]=flWheel; whls[1]=frWheel; whls[2]=brWheel; whls[3]=blWheel;
            for(int j=0; j<4; j++) {
                Pair<Double, Double> p2 = calculateAngle(whls[j].centerX, whls[j].centerY, fi, pointX, pointY);
                whls[j].centerX=p2.first.floatValue();
                whls[j].centerY=p2.second.floatValue();
                for (int i = 0; i < 8; i += 2) {
                    Pair<Double, Double> p = calculateAngle(whls[j].pts[i], whls[j].pts[i + 1], fi, pointX, pointY);
                    whls[j].pts[i] = p.first.floatValue();
                    whls[j].pts[i + 1] = p.second.floatValue();
                    whls[j].pts2 = whls[j].pts.clone();
                    //whls[j].updateCenter();
                }
            }
            flWheel.rotate(angle);
            frWheel.rotate(angle);
        }
        else
        {
            if(angle<=-3) {
                // ** Расчет центра окружности, относительно которой выполняется поворот машины **

                double Rf = L / Math.sin(Math.toRadians(angle));
                double Rb = L / Math.tan(Math.toRadians(angle));
                double fi = (180.0 * -speed) / (Math.PI * Rf);
                this.angle -= fi;
                if (this.angle > 270) this.angle -= 360;
                if (this.angle < -90) this.angle += 360;

                Vector2d dir = new Vector2d((float) Math.cos(Math.toRadians(this.angle - 90)), (float) Math.sin(Math.toRadians(this.angle - 90)));
                double pointX = rCx = blWheel.getCenterX() - Rb * dir.getX();
                double pointY = rCy = blWheel.getCenterY() - Rb * dir.getY();
                // ** Car rotating **
                float ddx=0, ddy=0;
                for (int i = 0; i < 8; i += 2) {
                    Pair<Double, Double> p = calculateAngle(pts[i], pts[i + 1], -fi, pointX, pointY);
                    if(i==0)
                    {
                        ddx=p.first.floatValue()-pts[i];
                        ddy=p.second.floatValue()-pts[i+1];
                    }
                    pts[i] = p.first.floatValue();
                    pts[i + 1] = p.second.floatValue();
                }
                //mx.postTranslate(ddx,ddy);
                mx.postRotate((float)-fi,(float)rCx,(float)rCy);
                // ** Wheel rotating **
                Wheel whls[] = new Wheel[4];
                whls[0] = flWheel;
                whls[1] = frWheel;
                whls[2] = brWheel;
                whls[3] = blWheel;
                for (int j = 0; j < 4; j++) {
                    Pair<Double, Double> p2 = calculateAngle(whls[j].centerX, whls[j].centerY, -fi, pointX, pointY);
                    whls[j].centerX=p2.first.floatValue();
                    whls[j].centerY=p2.second.floatValue();
                    //whls[j].updateCenter();
                    for (int i = 0; i < 8; i += 2) {
                        Pair<Double, Double> p = calculateAngle(whls[j].pts[i], whls[j].pts[i + 1], -fi, pointX, pointY);
                        whls[j].pts[i] = p.first.floatValue();
                        whls[j].pts[i + 1] = p.second.floatValue();
                        whls[j].pts2 = whls[j].pts.clone();
                        //whls[j].updateCenter();
                    }
                }
                flWheel.rotate(angle);
                frWheel.rotate(angle);
            }
            else {
                float dx = speed * getRotationVector().getX();
                float dy = speed * getRotationVector().getY();
                Wheel whls[] = new Wheel[4];
                whls[0] = flWheel;
                whls[1] = frWheel;
                whls[2] = brWheel;
                whls[3] = blWheel;
                for (int i = 0; i < 8; i += 2) {
                    pts[i] += dx;
                    pts[i + 1] += dy;
                }
                mx.postTranslate(dx,dy);
                for (int j = 0; j < 4; j++) {
                    whls[j].centerX+=dx;
                    whls[j].centerY+=dy;
                    for (int i = 0; i < 8; i += 2) {
                        whls[j].pts[i] += dx;
                        whls[j].pts[i + 1] += dy;
                        whls[j].pts2 = whls[j].pts.clone();
                        //whls[j].updateCenter();
                    }
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        /*Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(pts[0],pts[1]);
        path.lineTo(pts[2],pts[3]);
        path.lineTo(pts[4],pts[5]);
        path.lineTo(pts[6],pts[7]);
        path.lineTo(pts[0],pts[1]);
        canvas.drawPath(path, paint);*/
        //mx.postRotate(angle+90,(float)rCx,(float)rCy);
        /*Matrix mtr = new Matrix();
        mtr.postTranslate(pts[0],x);
        mtr.postRotate(this.angle-90,(float)rCx,(float)rCy);*/

       // DrawLine((float)rCx, (float)rCy, (float)brWheel.getCenterX(), (float)brWheel.getCenterY(), canvas);
       // DrawLine((float)rCx, (float)rCy, (float)blWheel.getCenterX(), (float)blWheel.getCenterY(), canvas);
        flWheel.draw(canvas);
        frWheel.draw(canvas);
        blWheel.draw(canvas);
        brWheel.draw(canvas);
        canvas.drawBitmap(texture, mx, paint);
        //canvas.drawBitmap(texture, );*/
    }

    @Override
    public void update() {

    }
}
