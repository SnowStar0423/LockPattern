package com.henleylee.lockpattern.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.NonNull;

import com.henleylee.lockpattern.Cell;
import com.henleylee.lockpattern.DecoratorStyle;
import com.henleylee.lockpattern.PatternHelper;

import java.util.List;

/**
 * @author Henley
 * @since 2019/8/29 15:29
 */
public class ArrowLockerLinkedLineStyle implements ILinkedLineStyle {

    private Paint paint;
    private Paint arrowPaint;
    private DecoratorStyle style;

    public ArrowLockerLinkedLineStyle(DecoratorStyle style) {
        this.style = style;
        this.paint = StyleHelper.createPaint();
        this.paint.setStyle(Paint.Style.STROKE);
        this.arrowPaint = new Paint();
        this.arrowPaint.setDither(true);
        this.arrowPaint.setAntiAlias(true);
        this.arrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void draw(@NonNull Canvas canvas, List<Cell> cells, float endX, float endY) {
        if (cells == null || cells.isEmpty()) {
            return;
        }
        int saveCount = canvas.save();

        paint.setColor(style.getColor(cells.get(0).getStatus()));
        paint.setStrokeWidth(style.getLineWidth());
        arrowPaint.setColor(style.getColor(cells.get(0).getStatus()));
        arrowPaint.setStrokeWidth(style.getLineWidth());

        Path path = new Path();
        for (int i = 1; i < cells.size(); i++) {
            Cell start = cells.get(i - 1);
            Cell end = cells.get(i);
            path.reset();
            path.moveTo(start.getX(), start.getY());
            path.lineTo(end.getX(), end.getY());
            canvas.drawPath(path, paint);

            path.reset();
            int arrowHeight = 8;
            double d = PatternHelper.distance(start.getX(), start.getY(), end.getX(), end.getY());
            float cosB = (float) ((end.getX() - start.getX()) / d);
            float sinB = (float) ((end.getY() - start.getY()) / d);
            float tanC = (float) Math.tan(Math.PI / 4);// 箭头尖锐程度，默认为直角
            float h = start.getRadius() / 2f + arrowHeight;
            float l = arrowHeight * tanC;
            float a = l * cosB;
            float b = l * sinB;
            float x0 = h * cosB;
            float y0 = h * sinB;
            float x1 = start.getX() + (h + arrowHeight) * cosB;
            float y1 = start.getY() + (h + arrowHeight) * sinB;
            float x2 = start.getX() + x0 - b;
            float y2 = start.getY() + y0 + a;
            float x3 = start.getX() + x0 + b;
            float y3 = start.getY() + y0 - a;
            path.moveTo(x1, y1);
            path.lineTo(x2, y2);
            path.lineTo(x3, y3);
            path.close();
            canvas.drawPath(path, arrowPaint);
        }

        if ((endX != 0f || endY != 0f) && cells.size() < 9) {
            path.reset();
            Cell cell = cells.get(cells.size() - 1);
            path.moveTo(cell.getX(), cell.getY());
            path.lineTo(endX, endY);
            canvas.drawPath(path, paint);
        }

        canvas.restoreToCount(saveCount);
    }

}
