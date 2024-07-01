package com.iotsmartaliv.utils.faceenroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class FocusView extends View {
    private Paint mPaint;
    private Paint mStrokePaint;
    private Path mPath = new Path();

    public FocusView(Context context) {
        super(context);
        initPaints();
    }

    public FocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public FocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#2ab122"));

        mStrokePaint = new Paint();
        mStrokePaint.setColor(Color.YELLOW);
        mStrokePaint.setStrokeWidth(30);
        mStrokePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();

        float radius = 0;
        float strokeWidth = 0;
        if (getWidth() < getHeight()) {
            radius = getWidth() / 2 - 10;
            strokeWidth = (getHeight() - getWidth()) / 2;
        } else {
            radius = getHeight() / 2 - 10;
            strokeWidth = (getWidth() - getHeight()) / 2;
        }

        mPaint.setStrokeWidth(strokeWidth);

        mPath.addCircle(getWidth() / 2, getHeight() / 2, radius, Path.Direction.CW);
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mStrokePaint);

        canvas.drawPath(mPath, mPaint);
    }
}