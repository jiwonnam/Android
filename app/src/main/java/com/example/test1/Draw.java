package com.example.test1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

class Draw extends View {

    public Draw(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ArrayList<Path> pathList = new ArrayList<Path>();
        Paint paint = new Paint();
        Matrix matrix1 = new Matrix();
        Matrix matrix2 = new Matrix();

        Path path1 = new Path();
        Path path2 = new Path();

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        path1.moveTo(0,height/2);
        path1.cubicTo(width/4,height/2-200,width-width/4,height/2+200, width,height/2);
        path1.cubicTo(width+ width/4,height/2-200,width*2-width/4,height/2+200, width*2,height/2);
        //path1.moveTo(0,height/2);
        //pathList.add(path1);
        pathList.add(path1);

        //path2.set(path1);
        //matrix.setRotate(10, width/2, height/2);
        //path2.transform(matrix);
        //pathList.add(path2);


        //path1.moveTo(0,height/2+100);
        //path1.cubicTo(width/4, height/2+200, width-width/4, height/2-200, width, height/2-100);

        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);

        for (Path path : pathList) {
            canvas.drawPath(path, paint);
        }

        //invalidate();
        //matrix1.setTranslate(10,0);
        //path1.transform(matrix1);
        matrix2.setRotate(10, 100,0);
        path1.transform(matrix2);
        canvas.drawPath(path1, paint);



        //canvas.translate(50,50);

        //path2.set(path1);
        //path2.transform(matrix);
        //canvas.drawPath(path2, paint);

    }
}
