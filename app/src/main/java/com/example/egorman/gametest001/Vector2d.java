package com.example.egorman.gametest001;

/**
 * Created by Egorman on 22.04.2017.
 */

public class Vector2d {
    private float x, y;

    public float getX(){return x;}
    public float getY(){return y;}

    public Vector2d(float x, float y)
    {
        this.x=x; this.y=y;
    }

    public double getScalar(Vector2d targetVector){return (double)(targetVector.getX()*getX()+targetVector.getY()+getY());}
    public double getModule() {return Math.sqrt(x*x+y*y);}

    public double getAngle(Vector2d targetVector)
    {
        double cos = (targetVector.getScalar(targetVector))/(getModule()/targetVector.getModule());
        return Math.acos(cos);
    }
}
