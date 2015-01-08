package com.superball.hellowifi.Spectrogram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.wifi.ScanResult;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TIAN on 2015/1/7.
 */
public class SpectrogramView extends View {

    int[] xScaleParam_2G = {2412, 2472, 20, 5};
    ///
    int[] xScaleParam = xScaleParam_2G;
    int xFine = xScaleParam[2] / 2;
    ;
    int xCount = (xScaleParam[2] + xScaleParam[1] - xScaleParam[0]) / xScaleParam[3];
    float signalCount = xScaleParam[2] / xScaleParam[3];
    int[] xScaleParam_5G = {5000, 5900, 20, 5};
    ///
    Mode frequencyMode = Mode.Mode_2G;
    ///
    List<ScanResult> emptyList = new ArrayList<>();
    List<ScanResult> scanResults = emptyList;
    ///
    int yLabelWidth = 30;
    int xLabelHeight = 30;
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
    float chartRight = 240;
    float chartBottom = 360;
    ///
    int[] yScaleParam = {-100, 0, 0, 25};
    int yFine = yScaleParam[2] / 2;
    int yCount = (yScaleParam[2] + yScaleParam[1] - yScaleParam[0]) / yScaleParam[3];

    public SpectrogramView(Context context, AttributeSet attrs) {

        super(context, attrs);

        initParams();
    }

    public void setFrequencyMode(Mode mode) {

        frequencyMode = mode;

        initParams();
    }

    public void setScanResults(List<ScanResult> results) {

        if (results != null) {

            scanResults = results;

        } else {

            scanResults = emptyList;
        }
    }

    private void initParams() {

        if (frequencyMode == Mode.Mode_2G) {

            xScaleParam = xScaleParam_2G;
        }

        if (frequencyMode == Mode.Mode_5G) {

            xScaleParam = xScaleParam_5G;
        }

        ///
        xFine = xScaleParam[2] / 2;
        xCount = (xScaleParam[2] + xScaleParam[1] - xScaleParam[0]) / xScaleParam[3];
        signalCount = xScaleParam[2] / xScaleParam[3];
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
        for (ScanResult scanResult : scanResults) {

            if (frequencyMode == Mode.Mode_2G) {

                if (scanResult.frequency / 1000 != 2) {

                    continue;
                }
            }

            if (frequencyMode == Mode.Mode_5G) {

                if (scanResult.frequency / 1000 != 5) {

                    continue;
                }
            }

            drawSignal(canvas, scanResult);
        }
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

        for (int i = 1, iChannel = 0; i < xCount; i++) {

            int xValue = xScaleParam[0] - xFine + xScaleParam[3] * i;

            if (xValue < xScaleParam[0]) continue;
            if (xValue > xScaleParam[1]) continue;

            float xLine = chartLeft + xScale * i;

            iChannel++;

            canvas.rotate(30, xLine, xLabelTop);
            canvas.drawText(String.format("%d(%d)", xValue, iChannel), xLine, xLabelTop, paint);
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
            canvas.drawText(String.format("%d ", yValue), yLabelLeft, yLine, paint);
            canvas.rotate(30, yLabelLeft, yLine);
        }

        ///
        paint.setTextAlign(Paint.Align.RIGHT);

        canvas.drawText(" MHz ", chartRight, chartBottom + fontHeight, paint);

        ///
        paint.setTextAlign(Paint.Align.RIGHT);

        canvas.drawText(" dBm ", chartLeft, chartTop + fontHeight, paint);

        ///
        canvas.restore();
    }

    private void drawSignal(Canvas canvas, ScanResult scanResult) {

        ///
        canvas.save();

        ///
        int red = (int) (Math.random() * 160);
        int green = (int) (Math.random() * 160);
        int blue = (int) (Math.random() * 160);

        ///
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(red, green, blue));

        ///
        int rssi = Math.max(scanResult.level, yScaleParam[0]);

        ///
        float signalLeft = chartLeft + xScale * ((float) (scanResult.frequency - xScaleParam[0]) / xScaleParam[3]);

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

        canvas.rotate(-45, xLabel, yLabel);
        canvas.drawText(scanResult.SSID, xLabel, yLabel, paint);
        canvas.rotate(45, xLabel, yLabel);

        ///
        canvas.restore();
    }

    public enum Mode {
        Mode_2G(2),
        Mode_5G(5);
        final int nativeInt;

        Mode(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }
}

