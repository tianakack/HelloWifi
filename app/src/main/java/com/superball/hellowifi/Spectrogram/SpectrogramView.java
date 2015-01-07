package com.superball.hellowifi.Spectrogram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by TIAN on 2015/1/7.
 */
public class SpectrogramView extends View {

    ///
    float fontHeight = 16;

    ///
    int yLabelWidth = 20;
    int xLabelHeight = 40;

    ///
    int paddingLeft = 10;
    ///
    float chartLeft = paddingLeft + yLabelWidth;
    int paddingRight = 10;
    int paddingTop = 10;
    float chartTop = paddingTop;
    int paddingBottom = 10;
    float chartRight = 240;
    float chartBottom = 360;

    ///
    int[] xScaleParam = {2412, 2472, 20, 5};
    ///
    int xFine = xScaleParam[2] / 2;
    ///
    int xCount = (xScaleParam[2] + xScaleParam[1] - xScaleParam[0]) / xScaleParam[3];
    ///
    float signalCount = xScaleParam[2] / xScaleParam[3];
    int[] yScaleParam = {-200, 0, 0, 50};
    int yFine = yScaleParam[2] / 2;
    int yCount = (yScaleParam[2] + yScaleParam[1] - yScaleParam[0]) / yScaleParam[3];
    ///
    float xScale = 50;
    float yScale = 100;
    /* 声明Paint对象 */
    private Paint mPaint = null;

    public SpectrogramView(Context context) {
        super(context);

        ///
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        ///
        Paint.FontMetrics fm = mPaint.getFontMetrics();

        fontHeight = fm.descent - fm.ascent;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ///
        chartRight = getWidth() - paddingRight;
        chartBottom = getHeight() - xLabelHeight - paddingBottom;

        ///
        xScale = (chartRight - chartLeft) / xCount;
        yScale = (chartBottom - chartTop) / yCount;

        ///
        render(canvas);

        ///
        drawSignal(canvas, new int[]{2437, -50});
    }

    private void drawSignal(Canvas canvas, int[] data) {

        ///
        float signalLeft = chartLeft + xScale * ((float) (data[0] - xScaleParam[0]) / xScaleParam[3]);

        float signalRight = signalLeft + xScale * signalCount;

        ///
        float mark = (float) (data[1] - yScaleParam[0]) / (yScaleParam[1] - yScaleParam[0]);

        float signalHeight = (chartBottom - chartTop) * mark;

        ///
        RectF rectf = new RectF(signalLeft, chartBottom - signalHeight, signalRight, chartBottom + signalHeight);

        canvas.drawArc(rectf, 180, 180, false, mPaint);
    }

    private void render(Canvas canvas) {

        canvas.save();

        /*
        * <1>
        */

        ///
        mPaint.setColor(Color.DKGRAY);

        ///
        canvas.drawRect(chartLeft, chartTop, chartRight, chartBottom, mPaint);

        /*
        * <2>
        */

        ///
        mPaint.setColor(Color.LTGRAY);

        ///
        for (int i = 1; i < xCount; i++) {

            float xLine = chartLeft + xScale * i;

            canvas.drawLine(xLine, chartTop, xLine, chartBottom, mPaint);
        }

        ///
        for (int i = 1; i < yCount; i++) {

            float yLine = chartTop + yScale * i;

            canvas.drawLine(chartLeft, yLine, chartRight, yLine, mPaint);
        }

        /*
        * <3>
        */

        ///
        mPaint.setColor(Color.GRAY);
        mPaint.setTextAlign(Paint.Align.LEFT);

        float xLabelTop = chartBottom + fontHeight;

        for (int i = 1; i < xCount; i++) {

            int xValue = xScaleParam[0] - xFine + xScaleParam[3] * i;

            if (xValue < xScaleParam[0]) continue;
            if (xValue > xScaleParam[1]) continue;

            float xLine = chartLeft + xScale * i;

            canvas.rotate(30, xLine, xLabelTop);
            canvas.drawText(Integer.toString(xValue), xLine, xLabelTop, mPaint);
            canvas.rotate(-30, xLine, xLabelTop);
        }

        ///
        mPaint.setTextAlign(Paint.Align.RIGHT);

        float yLabelLeft = chartLeft;

        for (int i = 1; i < yCount; i++) {

            int yValue = yScaleParam[0] - yFine + yScaleParam[3] * i;

            if (yValue < yScaleParam[0]) continue;
            if (yValue > yScaleParam[1]) continue;

            float yLine = chartBottom - yScale * i;

            canvas.rotate(-30, yLabelLeft, yLine);
            canvas.drawText(Integer.toString(yValue), yLabelLeft, yLine, mPaint);
            canvas.rotate(30, yLabelLeft, yLine);
        }

        ///
        canvas.restore();
    }
}

