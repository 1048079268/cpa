package com.todaysoft.cpa.thread;

import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.Page;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 11:38
 */
public class MutationStatisticThread implements Runnable {
    private Page page;
    private ContentParam contentParam;

    public MutationStatisticThread(Page page, ContentParam contentParam) {
        this.page = page;
        this.contentParam = contentParam;
    }

    @Override
    public void run() {

    }
}
