package com.example.egorman.gametest001;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Pair;

public class Wheel implements GameObject {
    public float x,y;
    public float angle;
    public float pts[];
    public float pts2[];
    public float centerX, centerY;

    public Wheel(float x, float y, float angle){
        this.x=x; this.y=y; this.angle=angle;
        pts=new float[8];
        pts2=new float[8];
        updateWheel();
    }

    public double getCenterX(){return centerX;}
    public double getCenterY(){return centerY;}

    public void updateWheel()
    {
        pts2[0]=pts[0]=x; pts2[1]=pts[1]=y;
        pts2[2]=pts[2]=x; pts2[3]=pts[3]=y+20;
        pts2[4]=pts[4]=x+5; pts2[5]=pts[5]=y+20;
        pts2[6]=pts[6]=x+5; pts2[7]=pts[7]=y;
        centerX=x+2.5f; centerY=y+10f;
    }

    public void updateCenter()
    {
        /*float dangle = 180f - (float)Math.toDegrees(Math.atan(2.5/10.0));
        float dist = (float)Math.sqrt(2.5*2.5+10*10);
        Vector2d dir = new Vector2d((float) Math.cos(Math.toRadians(dangle)), (float) Math.sin(Math.toRadians(dangle)));
        centerX = pts2[0]+dist*dir.getX();
        centerY = pts2[1]+dist*dir.getY();*/
        centerX=x+2.5f; centerY=y+10f;
        //System.out.println(pts2[0] + "  " + pts2[1] + "  " + centerX + "  " +centerY);
    }

    private Pair<Double,Double> calculateAngle(double x, double y, double angle, double xc, double yc)
    {
        double newX = xc+(x-xc)*Math.cos(Math.toRadians(angle))+(yc-y)*Math.sin(Math.toRadians(angle));
        double newY = yc+(x-xc)*Math.sin(Math.toRadians(angle))+(y-yc)*Math.cos(Math.toRadians(angle));
        return new Pair<>(newX, newY);
    }

    public void rotate(float angle)
    {
        {
            this.angle = angle;
            for (int i = 0; i < 8; i += 2) {
                Pair<Double, Double> p = calculateAngle(pts[i], pts[i+1], angle, getCenterX(), getCenterY());
                pts2[i] = p.first.floatValue();
                pts2[i + 1] = p.second.floatValue();
            }
        }
    }

    @Override
    public void draw(Canvas canvas){
        //canvas.rotate(angle);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(pts2[0],pts2[1]);
        path.lineTo(pts2[2],pts2[3]);
        path.lineTo(pts2[4],pts2[5]);
        path.lineTo(pts2[6],pts2[7]);
        path.lineTo(pts2[0],pts2[1]);
        canvas.drawPath(path, paint);
        //canvas.rotate(-angle);
    }

    @Override
    public void update(){

    }
}
