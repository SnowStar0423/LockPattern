package com.henleylee.lockpattern.style;

import android.graphics.Paint;

/**
 * 绘制样式辅助类
 *
 * @author Henley
 * @since 2019/8/26 16:50
 */
class StyleHelper {

    static Paint createPaint() {
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

}
