package com.henleylee.lockpattern;

import java.util.List;

/**
 * 手势解锁监听
 *
 * @author Henley
 * @since 2019/8/26 16:54
 */
public interface OnPatternChangedListener {

    /**
     * 开始绘制图案时（即手指按下触碰到绘画区域时）会调用该方法
     */
    void onPatternStart();

    /**
     * 图案绘制改变时（即手指在绘画区域移动时）会调用该方法，请注意只有 @param hitList改变了才会触发此方法
     *
     * @param view
     * @param cells
     */
    void onPatternChange(PatternLockerView view, List<Cell> cells);

    /**
     * 图案绘制完成时（即手指抬起离开绘画区域时）会调用该方法
     *
     * @param view
     * @param cells
     */
    void onPatternComplete(PatternLockerView view, List<Cell> cells);

    /**
     * 已绘制的图案被清除时会调用该方法
     */
    void onPatternClear();

}
