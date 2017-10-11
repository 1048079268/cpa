package com.todaysoft.cpa.thread;

import com.todaysoft.cpa.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 12:52
 */
public class ContentManagerThread extends Thread {
    private static Logger logger= LoggerFactory.getLogger(ContentManagerThread.class);
    private List<Thread> contentThreads;
    private Thread exceptionThread;
    private ContentService contentService;

    public ContentManagerThread(int maxContentThreadNum,ContentService contentService) {
        this.contentService=contentService;
        contentThreads=new ArrayList<>(maxContentThreadNum);
        for (int i=0;i<maxContentThreadNum;i++){
            contentThreads.add(new Thread(new ContentThread(contentService)));
        }
        exceptionThread=new Thread(new ExceptionThread(contentService));
    }

    @Override
    public void run() {
        contentThreads.stream().forEach((thread -> thread.start()));
        exceptionThread.start();
    }

    /**
     * 获取全部线程的状态，当全部线处于等待状态时返回true
     * @return
     */
    public boolean isAllWaiting(){
        boolean contents=contentThreads.stream().allMatch(thread -> thread.getState().equals(Thread.State.WAITING));
        boolean exception=exceptionThread.getState().equals(Thread.State.WAITING);
        return exception&&contents;
    }

    /**
     * 检查线程是否存活，不存活的重启
     */
    public void checkAndRestart(){
        for (int i=0;i<contentThreads.size();i++){
            if (!contentThreads.get(i).isAlive()){
                logger.info("【contentManager】检查到结束的线程，开始重启...");
                contentThreads.set(i,new Thread(new ContentThread(contentService)));
                contentThreads.get(i).start();
                logger.info("【contentManager】重启完成，state:"+contentThreads.get(i).getState());
            }
        }
        if (!exceptionThread.isAlive()){
            logger.info("【contentManager】检查到结束的线程，开始重启...");
            exceptionThread=new Thread(new ExceptionThread(contentService));
            exceptionThread.start();
            logger.info("【contentManager】重启完成，state:"+exceptionThread.getState());
        }
    }

    /**
     * 关闭所有线程
     */
    public void stopAll(){
        logger.info("【contentManager】关闭所有线程...");
        contentThreads.stream().forEach(thread -> thread.interrupt());
        exceptionThread.interrupt();
    }
}
