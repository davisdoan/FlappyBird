package com.bignerdranch.android.flappybird;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PipeDrawingView extends View {

    private Paint mPipePaint;
    private Paint mTesstPaint;
    private Paint mSkyPaint;
    private Paint mFloorPaint;
    private Paint mGrassPaint;

    public PipeDrawingView(Context context){
        this(context, null);
    }

    public PipeDrawingView(Context context, AttributeSet attrs){
        super(context, attrs);

        mPipePaint = new Paint();
        int pipeColor = context.getResources().getColor(R.color.green_pipe);
        mPipePaint.setColor(pipeColor);

        mTesstPaint = new Paint();
        mTesstPaint.setColor(Color.RED);

        mSkyPaint = new Paint();
        int skyColor = context.getResources().getColor(R.color.blue_sky);
        mSkyPaint.setColor(skyColor);

        mFloorPaint = new Paint();
        int floorColor = context.getResources().getColor(R.color.tan_floor);
        mFloorPaint.setColor(floorColor);

        mGrassPaint = new Paint();
        int grassColor= context.getResources().getColor(R.color.light_green_grass);
        mGrassPaint.setColor(grassColor);
    }


    @Override
    protected void onDraw(Canvas canvas){
        /**
        int left = 0;
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        canvas.save();

        super.onDraw(canvas);

        canvas.drawColor(getContext().getResources().getColor(R.color.blue_sky));

        canvas.drawRect(width - (width / 3), 0, width - (width / 10), height - (height / 2), mPipePaint); // top pipe
        canvas.drawRect(width - (width / 3), (float) (height * .75), width - (width / 10), height - (height /10), mTesstPaint); // bottom pipe

        canvas.drawRect(left, (float) (height - (height * .10)),width,height,mFloorPaint ); // floot
        canvas.drawRect(left, (float) (height - (height * .10)),width,(float) (height - (height * .10)),mGrassPaint); // grass

        width += 10;
        invalidate();
         **/

    }

}
