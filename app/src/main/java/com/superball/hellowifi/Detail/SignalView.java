package com.superball.hellowifi.Detail;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TIAN on 2015/1/7.
 */
public class SignalView extends View {
    ///
    public final static int MIN_VALUE = -100;
    ///
    int[] yScaleParam = {MIN_VALUE, 0, 10};
    int yCount = (yScaleParam[1] - yScaleParam[0]) / yScaleParam[2];
    ///
    List<Integer> mRSSIList = new ArrayList<>();
    ///
    int yLabelWidth = 25;
    int xLabelHeight = 15;
    ///
    float xScale = 50;
    float yScale = 100;
    ///
    int paddingLeft = 10;
    ///
    float chartLeft = paddingLeft + yLabelWidth;
    int paddingRight = 10;
    int paddingTop = 10;
    float chartTop = paddingTop;
    int paddingBottom = 10;
    int xCount = 16;
    float chartRight = 240;
    float chartBottom = 360;

    public SignalView(Context context) {
        super(context);
    }

    public SignalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SignalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void clearSignalData() {

        mRSSIList.clear();
    }

    public void addSignalData(int rssi) {

        mRSSIList.add(rssi);

        if (mRSSIList.size() > xCount + 1) {

            mRSSIList.remove(0);
        }
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
        drawSignal(canvas);
    }

    private void render(Canvas canvas) {

        canvas.save();

        ///
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        ///
        Paint.FontMetrics fm = paint.getFontMetrics();

        float fontHeight = fm.descent - fm.ascent;

        float fontHeight_half = fontHeight / 2;

        /*
        * <1>
        */

        ///
        paint.setColor(Color.DKGRAY);

        ///
        canvas.drawRect(chartLeft, chartTop, chartRight, chartBottom, paint);

        /*
        * <2>
        */

        ///
        paint.setColor(Color.LTGRAY);

        ///
        for (int i = 1; i < xCount; i++) {

            float xLine = chartLeft + xScale * i;

            canvas.drawLine(xLine, chartTop, xLine, chartBottom, paint);
        }

        ///
        for (int i = 1; i < yCount; i++) {

            float yLine = chartTop + yScale * i;

            canvas.drawLine(chartLeft, yLine, chartRight, yLine, paint);
        }

        /*
        * <3>
        */

        ///
        paint.setColor(Color.GRAY);
        paint.setTextAlign(Paint.Align.CENTER);

        float xLabelTop = chartBottom + fontHeight;

        for (int i = 1; i < xCount; i++) {

            float xLine = chartLeft + xScale * i;

            canvas.drawText(String.format("%d", i), xLine, xLabelTop, paint);
        }

        ///
        paint.setTextAlign(Paint.Align.RIGHT);

        float yLabelLeft = chartLeft;

        for (int i = 1; i < yCount; i++) {

            int yValue = yScaleParam[0] + yScaleParam[2] * i;

            if (yValue < yScaleParam[0]) continue;
            if (yValue > yScaleParam[1]) continue;

            float yLine = chartBottom - yScale * i + fontHeight_half;

            canvas.drawText(String.format("%d ", yValue), yLabelLeft, yLine, paint);
        }

        ///
        paint.setTextAlign(Paint.Align.RIGHT);

        canvas.drawText("dBm ", chartLeft, chartTop + fontHeight_half, paint);

        ///
        canvas.restore();
    }

    private void drawSignal(Canvas canvas) {

        ///
        float xLast = paddingLeft;

        ///
        canvas.save();

        ///
        Paint linePaint = new Paint();

        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.rgb(0, 128, 0));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(4);

        ///
        Paint fillPaint = new Paint();

        fillPaint.setAntiAlias(true);
        fillPaint.setColor(Color.argb(128, 224, 192, 128));

        ///
        Path linePath = new Path();

        ///
        Path fillPath = new Path();

        fillPath.moveTo(chartLeft, chartBottom);

        ///
        for (int i = 0; i < mRSSIList.size(); i++) {

            if (i <= xCount) {

                ///
                float mark = (float) (mRSSIList.get(i) - yScaleParam[0]) / (yScaleParam[1] - yScaleParam[0]);

                float signalHeight = (chartBottom - chartTop) * mark;

                ///
                float xCoords = chartLeft + xScale * i;

                float yCoords = chartBottom - signalHeight;

                ///
                xLast = xCoords;

                ///
                if (i == 0) {

                    linePath.moveTo(xCoords, yCoords);

                } else {

                    linePath.lineTo(xCoords, yCoords);
                }

                ///
                fillPath.lineTo(xCoords, yCoords);
            }

        }

        fillPath.lineTo(xLast, chartBottom);

        ///
        canvas.drawPath(linePath, linePaint);

        canvas.drawPath(fillPath, fillPaint);

        ///
        canvas.restore();
    }
}

