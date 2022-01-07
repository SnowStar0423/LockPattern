package com.henleylee.lockpattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.henleylee.lockpattern.style.DefaultLockerCellStyle;
import com.henleylee.lockpattern.style.DefaultLockerLinkedLineStyle;
import com.henleylee.lockpattern.style.ICellStyle;
import com.henleylee.lockpattern.style.ILinkedLineStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势解锁控件(默认为3*3九空格，支持N*N宫格扩展)
 *
 * @author Henley
 * @since 2019/8/26 16:55
 */
public class PatternLockerView extends View {

    /** 每条边有几个点，默认为{@code 3} */
    private int side = 3;
    /** 绘制完成清除路径的延迟时间(单位ms)，只有在{@link #enableAutoClean}为{@code true}时有效 */
    private long freezeDuration = 1000L;
    /** 绘制完成后是否自动清除标志位，如果开启了该标志位，延时{@link #freezeDuration}毫秒后自动清除已绘制图案 */
    private boolean enableAutoClean = true;
    /** 是否开启跳过中间点模式，如果开启了该标志，则可以不用连续中间点 */
    private boolean enableSkipMode = false;
    /** 是否开启绘制路径隐藏模式，如果开启了该标志位，则隐藏绘制路径 */
    private boolean enableInStealthMode = false;
    /** 是否开启触摸反馈，如果开启了该标志，则每连接一个{@link Cell}则会震动 */
    private boolean enableHapticFeedback = false;

    /** 绘制{@link Cell}样式 */
    private ICellStyle cellStyle = null;
    /** 绘制{@link Cell}连接线样式 */
    private ILinkedLineStyle linkedLineStyle = null;
    /** 绘制样式(颜色、描边等信息) */
    private DecoratorStyle decoratorStyle;

    /** 终点X坐标 */
    private float endX = 0f;
    /** 终点Y坐标 */
    private float endY = 0f;
    /** 记录绘制多少个{@link Cell}，用于判断是否调用{@link OnPatternChangedListener} */
    private int hitSize = 0;

    /** 真正的{@link Cell}集合 */
    private List<Cell> mCells = new ArrayList<>();
    /** 选中的{@link Cell}集合 */
    private List<Cell> mSelectedCells = new ArrayList<>();
    /** 手势解锁监听 */
    private OnPatternChangedListener mPatternChangedListener = null;
    /** 清除绘制路径的{@link Runnable} */
    private Runnable mClearPatternRunnable = new Runnable() {
        @Override
        public void run() {
            clearPattern();
        }
    };

    public PatternLockerView(Context context) {
        this(context, null);
    }

    public PatternLockerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PatternLockerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PatternLockerView, defStyleAttr, 0);

        int normalColor = ta.getColor(R.styleable.PatternLockerView_pattern_normalColor, Config.DEFAULT_NORMAL_COLOR);
        int checkColor = ta.getColor(R.styleable.PatternLockerView_pattern_checkColor, Config.DEFAULT_CHECK_COLOR);
        int errorColor = ta.getColor(R.styleable.PatternLockerView_pattern_errorColor, Config.DEFAULT_ERROR_COLOR);
        int fillColor = ta.getColor(R.styleable.PatternLockerView_pattern_fillColor, Config.DEFAULT_FILL_COLOR);
        float strokeWidth = ta.getDimension(R.styleable.PatternLockerView_pattern_strokeWidth, 4f);
        float lineWidth = ta.getDimension(R.styleable.PatternLockerView_pattern_lineWidth, 8f);

        this.side = ta.getInt(R.styleable.PatternLockerView_pattern_side, Config.DEFAULT_SIDE);
        this.enableSkipMode = ta.getBoolean(R.styleable.PatternLockerView_pattern_enableSkipMode, false);
        this.enableInStealthMode = ta.getBoolean(R.styleable.PatternLockerView_pattern_enableInStealthMode, false);
        this.freezeDuration = ta.getInteger(R.styleable.PatternLockerView_pattern_freezeDuration, Config.DEFAULT_FREEZE_DURATION);
        this.enableAutoClean = ta.getBoolean(R.styleable.PatternLockerView_pattern_enableAutoClean, true);
        this.enableHapticFeedback = ta.getBoolean(R.styleable.PatternLockerView_pattern_enableHapticFeedback, false);

        ta.recycle();

        // style
        this.decoratorStyle = new DecoratorStyle(normalColor, checkColor, errorColor, fillColor, strokeWidth, lineWidth);
        this.cellStyle = new DefaultLockerCellStyle(decoratorStyle);
        this.linkedLineStyle = new DefaultLockerLinkedLineStyle(decoratorStyle);
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
     * 返回绘制完成清除路径的延迟时间(单位：ms)
     */
    public long getFreezeDuration() {
        return freezeDuration;
    }

    /**
     * 设置绘制完成清除路径的延迟时间
     *
     * @param freezeDuration 绘制完成清除路径的延迟时间(单位：ms)
     */
    public void setFreezeDuration(long freezeDuration) {
        this.freezeDuration = freezeDuration;
    }

    /**
     * 返回绘制完后是否自动清除路径(默认为true)
     */
    public boolean isEnableAutoClean() {
        return enableAutoClean;
    }

    /**
     * 设置绘制完成后是否自动清除路径
     *
     * @param enableAutoClean 绘制完成后是否自动清除路径(默认为true)
     */
    public void setEnableAutoClean(boolean enableAutoClean) {
        this.enableAutoClean = enableAutoClean;
    }

    /**
     * 返回是否开启跳过中间点模式(默认为false)
     */
    public boolean isEnableSkipMode() {
        return enableSkipMode;
    }

    /**
     * 设置是否开启跳过中间点模式
     *
     * @param enableSkipMode 是否开启跳过中间点模式(默认为false)
     */
    public void setEnableSkipMode(boolean enableSkipMode) {
        this.enableSkipMode = enableSkipMode;
    }

    /**
     * 返回是否开启绘制路径隐藏模式(默认为false)
     */
    public boolean isEnableInStealthMode() {
        return enableInStealthMode;
    }

    /**
     * 设置是否开启绘制路径隐藏模式(默认为false)
     *
     * @param enableInStealthMode 是否开启绘制路径隐藏模式(默认为false)
     */
    public void setEnableInStealthMode(boolean enableInStealthMode) {
        this.enableInStealthMode = enableInStealthMode;
    }

    /**
     * 返回是否开启触摸反馈(默认为false)
     */
    public boolean isEnableHapticFeedback() {
        return enableHapticFeedback;
    }

    /**
     * 设置是否开启触摸反馈
     *
     * @param enableHapticFeedback 是否开启触摸反馈(默认为false)
     */
    public void setEnableHapticFeedback(boolean enableHapticFeedback) {
        this.enableHapticFeedback = enableHapticFeedback;
    }

    /**
     * 返回绘制{@link Cell}样式
     *
     * @see ICellStyle
     * @see DefaultLockerCellStyle
     */
    public ICellStyle getCellStyle() {
        return cellStyle;
    }

    /**
     * 设置绘制{@link Cell}样式
     *
     * @param cellStyle 绘制{@link Cell}样式
     * @see ICellStyle
     * @see DefaultLockerCellStyle
     */
    public void setCellStyle(ICellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    /**
     * 返回绘制连接线样式
     *
     * @see ILinkedLineStyle
     * @see DefaultLockerLinkedLineStyle
     */
    public ILinkedLineStyle getLinkedLineStyle() {
        return linkedLineStyle;
    }

    /**
     * 返回绘制连接线样式
     *
     * @param linkedLineStyle 绘制连接线样式
     * @see ILinkedLineStyle
     * @see DefaultLockerLinkedLineStyle
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
     * 返回手势解锁监听
     */
    public OnPatternChangedListener getOnPatternChangedListener() {
        return mPatternChangedListener;
    }

    /**
     * 设置手势解锁监听
     *
     * @param listener 手势解锁监听
     */
    public void setOnPatternChangedListener(OnPatternChangedListener listener) {
        this.mPatternChangedListener = listener;
    }

    /**
     * 返回选中的{@link Cell}集合
     */
    public List<Cell> getSelectedCells() {
        return mSelectedCells;
    }

    /**
     * 设置绘制状态
     *
     * @param status 绘制状态
     * @see CellStatus
     */
    public void setPatternStatus(@CellStatus int status) {
        for (Cell cell : mSelectedCells) {
            cell.setStatus(status);
        }
        if (status == CellStatus.NORMAL) {
            mSelectedCells.clear();
        }
        handleStealthMode(status);
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
            linkedLineStyle.draw(canvas, mSelectedCells, endX, endY);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.setOnPatternChangedListener(null);
        this.removeCallbacks(mClearPatternRunnable);
    }

    /**
     * init cells
     */
    private void initCells() {
        mCells.clear();
        mSelectedCells.clear();
        int offsetX = getPaddingLeft();
        int offsetY = getPaddingTop();
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        mCells.addAll(PatternHelper.getCellList(side, width, height, offsetX, offsetY));
    }

    /**
     * handle {@link MotionEvent#ACTION_DOWN} event
     */
    private void handleActionDown(MotionEvent event) {
        // 1. reset to default state
        setPatternStatus(CellStatus.NORMAL);
        // 2. check selected cell
        checkSelectedCell(event);
        // 3. notify listener
        if (mPatternChangedListener != null) {
            mPatternChangedListener.onPatternStart();
        }
    }

    /**
     * handle {@link MotionEvent#ACTION_MOVE} event
     */
    private void handleActionMove(MotionEvent event) {
        // 1. check selected cell
        checkSelectedCell(event);
        // 2. update end point
        endX = event.getX();
        endY = event.getY();
        // 3. notify listener if needed
        int size = mSelectedCells.size();
        if (hitSize != size) {
            hitSize = size;
            if (mPatternChangedListener != null) {
                mPatternChangedListener.onPatternChange(this, mSelectedCells);
            }
        }

    }

    /**
     * handle {@link MotionEvent#ACTION_UP} event
     */
    private void handleActionUp() {
        // 1. set pattern status
        setPatternStatus(CellStatus.CHECK);
        // 2. update end point
        endX = 0f;
        endY = 0f;
        // 3. notify listener
        if (mPatternChangedListener != null) {
            mPatternChangedListener.onPatternComplete(this, mSelectedCells);
        }
        // 4. post clear pattern runnable if needed
        if (enableAutoClean && !mSelectedCells.isEmpty()) {
            postClearPatternRunnable();
        }
    }

    /**
     * clear pattern
     */
    private void clearPattern() {
        // 1. set pattern status
        setPatternStatus(CellStatus.NORMAL);
        // 2. invalidate view
        if (enableInStealthMode) {
            invalidate();
        }
        // 3. notify listener
        if (mPatternChangedListener != null) {
            mPatternChangedListener.onPatternClear();
        }
    }

    /**
     * post clear pattern runnable
     */
    private void postClearPatternRunnable() {
        removeCallbacks(mClearPatternRunnable);
        postDelayed(mClearPatternRunnable, freezeDuration);
    }

    /**
     * check user's touch moving is or not in the area of cells
     */
    private void checkSelectedCell(MotionEvent event) {
        for (Cell cell : mCells) {
            if (PatternHelper.isInRound(event.getX(), event.getY(), cell.getX(), cell.getY(), cell.getRadius())) {
                if (!enableSkipMode && !mSelectedCells.isEmpty()) {
                    Cell last = mSelectedCells.get(mSelectedCells.size() - 1);
                    if (last.getIndex() < cell.getIndex()) {
                        for (int i = last.getIndex() + 1; i < cell.getIndex(); i++) {
                            Cell center = mCells.get(i);
                            if (PatternHelper.isInLine(last.getX(), last.getY(), center.getX(), center.getY(), cell.getX(), cell.getY())) {
                                addSelectedCell(center);
                            }
                        }
                    } else if (last.getIndex() > cell.getIndex()) {
                        for (int i = last.getIndex() - 1; i > cell.getIndex(); i--) {
                            Cell center = mCells.get(i);
                            if (PatternHelper.isInLine(last.getX(), last.getY(), center.getX(), center.getY(), cell.getX(), cell.getY())) {
                                addSelectedCell(center);
                            }
                        }
                    }
                }
                addSelectedCell(cell);
            } else if (!enableInStealthMode) {
                invalidate();
            }
        }
    }

    /**
     * add selected cell
     */
    private void addSelectedCell(Cell cell) {
        if (!mSelectedCells.contains(cell)) {
            mSelectedCells.add(cell);
            handleHapticFeedback();
        }
        setPatternStatus(CellStatus.CHECK);
    }

    /**
     * handle the stealth mode (if true: do not post invalidate; false: post invalidate)
     */
    private void handleStealthMode(@CellStatus int status) {
        if (!enableInStealthMode || status == CellStatus.ERROR) {
            this.postInvalidate();
        }
    }

    /**
     * handle haptic feedback
     */
    private void handleHapticFeedback() {
        if (enableHapticFeedback) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING |
                            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        }
    }

}
