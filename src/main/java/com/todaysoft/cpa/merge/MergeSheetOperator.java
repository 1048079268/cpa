package com.todaysoft.cpa.merge;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/17 10:21
 */
@FunctionalInterface
public interface MergeSheetOperator {
    boolean operate(MergeInfo mergeInfo);
}
