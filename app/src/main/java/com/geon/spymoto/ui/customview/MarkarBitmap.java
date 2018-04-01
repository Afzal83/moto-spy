package com.geon.spymoto.ui.customview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.Log;

/**
 * Created by Babu on 7/20/2017.
 */

public class MarkarBitmap {

    double speed=0.0;
    String vehicleNumberPlate ="";
    public static Bitmap getRoundedCroppedBitmap(double speed, String vehicleNumberPlate) {

        Log.e("...SPEED...",""+speed);
        int colorHex = 0;//Color.parseColor("#ff0000");

        if(speed == 0.0){colorHex = Color.parseColor("#000000");}
        else if( speed >0.0 && speed<=10.0){colorHex = Color.parseColor("#4d4d4d");}
        else if( speed >10.0 && speed<=20.0){colorHex =  Color.parseColor("#99b3ff");}
        else if( speed>20.0 && speed <=30.0){colorHex =  Color.parseColor("#3366ff");}
        else if(speed >30.0 && speed<=40.0){colorHex = Color.parseColor("#0040ff");}
        else if(speed >40.0 && speed<=50.0){colorHex = Color.parseColor("#0033cc");}
        else if(speed >50 && speed<=60){colorHex = Color.parseColor("#339966");}
        else if(speed >60 && speed<=70){colorHex = Color.parseColor("#006600");}
        else if(speed >70 && speed<=80){colorHex = Color.parseColor("#ff6600");}
        else if(speed >80 && speed<=90){colorHex = Color.parseColor("#ff471a");}
        else if( speed >90 && speed<=100){colorHex = Color.parseColor("#ff3300");}
        else if(speed<100){colorHex = Color.parseColor("#cc0000");}
        else {colorHex = Color.parseColor("#000000");}

        Bitmap output = Bitmap.createBitmap(280,160, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        drawPoly(canvas,colorHex,
                new Point[]{
                        new Point(0,0),
                        new Point(0,50),
                        new Point(115,50),
                        new Point(125,80),
                        new Point(135,50),
                        new Point(300,50),
                        new Point(300,0)
                });

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
        canvas.drawText(vehicleNumberPlate, 10,30, paint);
//        if(speed < 0.0){
//            canvas.drawText("ENGINE STOP", 10,70, paint);
//        }else{
//            canvas.drawText("Speed: " +Double.toString(speed)+" Km/h", 10,70, paint);
//        }
        return output;

    }
    static void drawPoly(Canvas canvas, int color, Point[] points) {
        // line at minimum...
        if (points.length < 2) {
            return;
        }
        Paint polyPaint = new Paint();
        polyPaint.setColor(color);
        polyPaint.setStyle(Style.FILL);
        Path polyPath = new Path();
        polyPath.moveTo(points[0].x, points[0].y);
        int i, len;
        len = points.length;
        for (i = 0; i < len; i++) {
            polyPath.lineTo(points[i].x, points[i].y);

        }
        polyPath.lineTo(points[0].x, points[0].y);
        canvas.drawPath(polyPath, polyPaint);
    }
    static  class Point {
        public float x = 0;
        public float y = 0;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
