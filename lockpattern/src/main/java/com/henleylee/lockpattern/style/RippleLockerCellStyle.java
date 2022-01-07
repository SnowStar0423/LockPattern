package com.henleylee.lockpattern.style;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.henleylee.lockpattern.Cell;
import com.henleylee.lockpattern.CellStatus;
import com.henleylee.lockpattern.DecoratorStyle;

import java.util.List;

/**
 * 手势解锁{@link Cell}水波纹绘制样式
 *
 * @author Henley
 * @since 2019/8/29 16:15
 */
public class RippleLockerCellStyle implements ICellStyle {

    private Paint paint;
    private DecoratorStyle style;

    public RippleLockerCellStyle(DecoratorStyle style) {
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

            if (status == CellStatus.NORMAL) {
                // draw outer circle
                paint.setColor(style.getColor(status));
                paint.setStrokeWidth(style.getLineWidth() / 2f);
                canvas.drawCircle(cell.getX(), cell.getY(), cell.getRadius(), paint);

                // draw fill circle
                paint.setColor(style.getFillColor());
                canvas.drawCircle(cell.getX(), cell.getY(), cell.getRadius() - style.getLineWidth() / 2f, paint);
            } else {
                // draw outer circle
                paint.setColor(style.getColor(status) & 0x14FFFFFF);
                canvas.drawCircle(cell.getX(), cell.getY(), cell.getRadius(), paint);

                // draw inner circle
                paint.setColor(style.getColor(status) & 0x43FFFFFF);
                canvas.drawCircle(cell.getX(), cell.getY(), cell.getRadius() * 2f / 3f, paint);

                // draw inner circle
                paint.setColor(style.getColor(status));
                canvas.drawCircle(cell.getX(), cell.getY(), cell.getRadius() / 3f, paint);
            }
        }

        canvas.restoreToCount(saveCount);
    }

}