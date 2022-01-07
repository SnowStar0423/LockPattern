package com.henleylee.lockpattern;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link Cell}状态
 *
 * @author Henley
 * @since 2019/8/26 19:52
 */
@IntDef({
        CellStatus.NORMAL,
        CellStatus.CHECK,
        CellStatus.ERROR
})
@Retention(RetentionPolicy.SOURCE)
public @interface CellStatus {

    /**
     * default status
     */
    int NORMAL = 0;
    /**
     * checked status
     */
    int CHECK = 1;
    /**
     * checked error status
     */
    int ERROR = 2;

}
