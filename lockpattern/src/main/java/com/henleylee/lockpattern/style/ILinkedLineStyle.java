package com.henleylee.lockpattern.style;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.henleylee.lockpattern.Cell;

import java.util.List;

/**
 * {@link Cell}连接线绘制样式抽象接口
 *
 * @author Henley
 * @since 2019/8/26 16:51
 */
public interface ILinkedLineStyle {

    /**
     * 绘制图案密码连接线
     *
     * @param canvas
     * @param cells
     * @param endX
     * @param endY
     */
    void draw(@NonNull Canvas canvas, List<Cell> cells, float endX, float endY);

}