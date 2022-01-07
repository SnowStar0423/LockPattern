package com.henleylee.lockpattern.style;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.henleylee.lockpattern.Cell;

import java.util.List;

/**
 * {@link Cell}绘制样式抽象接口
 *
 * @author Henley
 * @since 2019/8/26 16:53
 */
public interface ICellStyle {

    /**
     * 绘制正常情况下（即未设置的）每个图案的样式
     *
     * @param canvas
     * @param cells  the target cell view
     */
    void draw(@NonNull Canvas canvas, List<Cell> cells);

}
