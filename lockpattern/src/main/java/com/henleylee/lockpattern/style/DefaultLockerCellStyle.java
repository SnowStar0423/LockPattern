package com.henleylee.lockpattern.style;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.henleylee.lockpattern.Cell;
import com.henleylee.lockpattern.CellStatus;
import com.henleylee.lockpattern.DecoratorStyle;

import java.util.List;

/**
 * 手势解锁{@link Cell}默认绘制样式
 *
 * @author Henley
 * @since 2019/8/26 19:39
 */
public class DefaultLockerCellStyle implements ICellStyle {

    private Paint paint;
    private DecoratorStyle style;

    public DefaultLockerCellStyle(DecoratorStyle style) {
        this.style = style;
        this.paint = StyleHelper.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(@NonNull Canvas canvas, List<Cell> cells) {
        if (cells == null || cells.isEmpty()) {
            return;
        }

        int saveCount = canvas.save();

        for (Cell cell : cells) {
            int status = cell.getStatus();

            // draw outer circle
            paint.setColor(style.getColor(status));
            canvas.drawCircle(cell.getX(), cell.getY(), cell.getRadius(), paint);

            // draw fill circle
            paint.setColor(style.getFillColor());
            canvas.drawCircle(cell.getX(), cell.getY(), cell.getRadius() - style.getStrokeWidth(), paint);

            // draw inner circle
            if (status != CellStatus.NORMAL) {
                paint.setColor(style.getColor(status));
                canvas.drawCircle(cell.getX(), cell.getY(), cell.getRadius() / 3f, paint);
            }
        }

        canvas.restoreToCount(saveCount);
    }

}