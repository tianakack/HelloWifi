package com.superball.hellowifi.Spectrogram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.superball.hellowifi.ScanList.ScanList;

/**
 * Created by TIAN on 2015/1/7.
 */
public class SpectrogramView extends View {

    ///
    int yLabelWidth = 20;
    int xLabelHeight = 40;

    ///
    int paddingLeft = 10;
    ///
    float chartLeft = paddingLeft + yLabelWidth;
    ///
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
    float signalCount = xScaleParam[2] / xScaleParam[3];
    ///
    int xCount = (xScaleParam[2] + xScaleParam[1] - xScaleParam[0]) / xScaleParam[3];
    int[] yScaleParam = {-100, 0, 0, 25};
    int yFine = yScaleParam[2] / 2;
    int yCount = (yScaleParam[2] + yScaleParam[1] - yScaleParam[0]) / yScaleParam[3];

    ///
    float xScale = 50;
    float yScale = 100;

    public SpectrogramView(Context context) {
        super(context);
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
        for (ScanList.ScanItem scanItem : ScanList.ITEMS) {

            drawSignal(canvas, scanItem);
        }

    }

    private void drawSignal(Canvas canvas, ScanList.ScanItem scanItem) {

        int red = (int) (Math.random() * 160);
        int green = (int) (Math.random() * 160);
        int blue = (int) (Math.random() * 160);

        ///
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(red, green, blue));

        ///
        int rssi = Math.max(scanItem.content.level, yScaleParam[0]);

        ///
        float signalLeft = chartLeft + xScale * ((float) (scanItem.content.frequency - xScaleParam[0]) / xScaleParam[3]);

        float signalRight = signalLeft + xScale * signalCount;

        ///
        float mark = (float) (rssi - yScaleParam[0]) / (yScaleParam[1] - yScaleParam[0]);

        float signalHeight = (chartBottom - chartTop) * mark;

        ///
        RectF rectf = new RectF(signalLeft, chartBottom - signalHeight, signalRight, chartBottom + signalHeight);

        canvas.drawArc(rectf, 180, 180, false, paint);

        ///
        float xLabel = (signalLeft + signalRight) / 2;
        float yLabel = chartBottom - signalHeight;

        canvas.rotate(-30, xLabel, yLabel);
        canvas.drawText(scanItem.content.SSID, xLabel, yLabel, paint);
        canvas.rotate(30, xLabel, yLabel);
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
        paint.setTextAlign(Paint.Align.LEFT);

        float xLabelTop = chartBottom + fontHeight;

        for (int i = 1; i < xCount; i++) {

            int xValue = xScaleParam[0] - xFine + xScaleParam[3] * i;

            if (xValue < xScaleParam[0]) continue;
            if (xValue > xScaleParam[1]) continue;

            float xLine = chartLeft + xScale * i;

            canvas.rotate(30, xLine, xLabelTop);
            canvas.drawText(Integer.toString(xValue), xLine, xLabelTop, paint);
            canvas.rotate(-30, xLine, xLabelTop);
        }

        ///
        paint.setTextAlign(Paint.Align.RIGHT);

        float yLabelLeft = chartLeft;

        for (int i = 1; i < yCount; i++) {

            int yValue = yScaleParam[0] - yFine + yScaleParam[3] * i;

            if (yValue < yScaleParam[0]) continue;
            if (yValue > yScaleParam[1]) continue;

            float yLine = chartBottom - yScale * i;

            canvas.rotate(-30, yLabelLeft, yLine);
            canvas.drawText(Integer.toString(yValue), yLabelLeft, yLine, paint);
            canvas.rotate(30, yLabelLeft, yLine);
        }

        ///
        canvas.restore();
    }
}

