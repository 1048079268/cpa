package com.todaysoft.cpa.merge;

/**
 * @desc: 操作excel的sheet的函数时接口
 * @author: 鱼唇的人类
 * @date: 2017/11/17 10:21
 */
@FunctionalInterface
public interface MergeSheetOperator {
    boolean operate(MergeInfo mergeInfo);
}
