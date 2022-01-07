package com.henleylee.lockpattern;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.henleylee.lockpattern.style.DefaultIndicatorCellStyle;
import com.henleylee.lockpattern.style.DefaultIndicatorLinkedLineStyle;
import com.henleylee.lockpattern.style.ICellStyle;
import com.henleylee.lockpattern.style.ILinkedLineStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势解锁指示器控件(用于展示绘制路径)
 *
 * @author Henley
 * @since 2019/8/26 17:43
 */
public class PatternIndicatorView extends View {

    /** 每条边有几个点，默认为{@code 3} */
    private int side = 3;
    /** 绘制{@link Cell}样式 */
    private ICellStyle cellStyle = null;
    /** 绘制{@link Cell}连接线样式 */
    private ILinkedLineStyle linkedLineStyle = null;
    /** 真正的{@link Cell}集合 */
    private List<Cell> mCells = new ArrayList<>();
    /** 选中的{@link Cell}集合 */
    private List<Cell> mSelectedCells = new ArrayList<>();
    /** 绘制样式(颜色、描边等信息) */
    private DecoratorStyle decoratorStyle;

    public PatternIndicatorView(Context context) {
        this(context, null);
    }

    public PatternIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PatternIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PatternIndicatorView, defStyleAttr, 0);

        this.side = ta.getInt(R.styleable.PatternIndicatorView_pattern_side, Config.DEFAULT_SIDE);
        int normalColor = ta.getColor(R.styleable.PatternIndicatorView_pattern_normalColor, Config.DEFAULT_NORMAL_COLOR);
        int checkColor = ta.getColor(R.styleable.PatternIndicatorView_pattern_checkColor, Config.DEFAULT_CHECK_COLOR);
        int errorColor = ta.getColor(R.styleable.PatternIndicatorView_pattern_errorColor, Config.DEFAULT_ERROR_COLOR);
        int fillColor = ta.getColor(R.styleable.PatternIndicatorView_pattern_fillColor, Config.DEFAULT_FILL_COLOR);
        float strokeWidth = ta.getDimension(R.styleable.PatternIndicatorView_pattern_strokeWidth, 2.5f);
        float lineWidth = ta.getDimension(R.styleable.PatternIndicatorView_pattern_lineWidth, 5f);

        ta.recycle();

        // style
        this.decoratorStyle = new DecoratorStyle(normalColor, checkColor, errorColor, fillColor, strokeWidth, lineWidth);
        this.cellStyle = new DefaultIndicatorCellStyle(decoratorStyle);
        this.linkedLineStyle = new DefaultIndicatorLinkedLineStyle(decoratorStyle);
    }

    /**
     * 返回每条边有几个点(默认为3)
     */
    public int getSide() {
        return side;
    }

    /**
     * 设置每条边有几个点
     *
     * @param side 每条边有几个点(默认为3)
     */
    public void setSide(int side) {
        this.side = side;
    }

    /**
     * 返回绘制{@link Cell}样式
     *
     * @see ICellStyle
     * @see DefaultIndicatorCellStyle
     */
    public ICellStyle getCellStyle() {
        return cellStyle;
    }

    /**
     * 设置绘制{@link Cell}样式
     *
     * @param cellStyle 绘制{@link Cell}样式
     * @see ICellStyle
     * @see DefaultIndicatorCellStyle
     */
    public void setCellStyle(ICellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    /**
     * 返回绘制连接线样式
     *
     * @see ILinkedLineStyle
     * @see DefaultIndicatorLinkedLineStyle
     */
    public ILinkedLineStyle getLinkedLineStyle() {
        return linkedLineStyle;
    }

    /**
     * 返回绘制连接线样式
     *
     * @param linkedLineStyle 绘制连接线样式
     * @see ILinkedLineStyle
     * @see DefaultIndicatorLinkedLineStyle
     */
    public void setLinkedLineStyle(ILinkedLineStyle linkedLineStyle) {
        this.linkedLineStyle = linkedLineStyle;
    }

    /**
     * 返回Style(颜色、描边等信息)
     */
    public DecoratorStyle getDecoratorStyle() {
        return decoratorStyle;
    }

    /**
     * 设置Style(颜色、描边等信息)
     *
     * @param decoratorStyle Style(颜色、描边等信息)
     */
    public void setDecoratorStyle(DecoratorStyle decoratorStyle) {
        this.decoratorStyle = decoratorStyle;
    }

    /**
     * 返回选中的{@link Cell}集合
     */
    public List<Cell> getSelectedCells() {
        return mSelectedCells;
    }

    /**
     * 设置选中的{@link Cell}集合
     */
    public void setSelectedCells(List<Cell> selectedCells) {
        // 1. clear pre state
        mSelectedCells.clear();
        setPatternStatus(CellStatus.NORMAL);

        // 2. record new state
        if (selectedCells != null) {
            for (Cell selected : selectedCells) {
                for (Cell cell : mCells) {
                    if (selected.getIndex() == cell.getIndex()) {
                        cell.setStatus(selected.getStatus());
                        mSelectedCells.add(cell);
                    }
                }
            }
        }

        // 3. invalidate view
        postInvalidate();
    }

    /**
     * 设置绘制状态
     *
     * @param status 绘制状态
     * @see CellStatus
     */
    public void setPatternStatus(@CellStatus int status) {
        for (Cell cell : mCells) {
            cell.setStatus(status);
        }
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureSpec = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(measureSpec, measureSpec);
        initCells();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (cellStyle != null && !mCells.isEmpty()) {
            cellStyle.draw(canvas, mCells);
        }
        if (linkedLineStyle != null && !mSelectedCells.isEmpty()) {
            linkedLineStyle.draw(canvas, mSelectedCells, 0, 0);
        }
    }

    private void initCells() {
        mCells.clear();
        mSelectedCells.clear();
        int offsetX = getPaddingLeft();
        int offsetY = getPaddingTop();
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        mCells.addAll(PatternHelper.getCellList(side, width, height, offsetX, offsetY));
    }

}
