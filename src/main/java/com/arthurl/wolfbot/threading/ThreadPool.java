package com.arthurl.wolfbot.threading;

import java.util.concurrent.*;

public class ThreadPool {
    private final ThreadPoolExecutor threadPool;
    private final ScheduledExecutorService executorService;

    private boolean started;

    public ThreadPool() {
        RejectedHandler rejectedHandler = new RejectedHandler();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(3,
                10,
                10L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory,
                rejectedHandler
        );
        this.executorService = Executors.newScheduledThreadPool(2);
        this.started = true;
    }

    public void run(Runnable rn){
        try{
            if (this.isStarted()){
                threadPool.execute(rn);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void run(Runnable rn, int delay){
        try {
            if (this.isStarted()) {
                this.executorService.schedule(rn, delay, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isStarted() {
        return started;
    }

    private void dispose(){
        this.threadPool.shutdownNow();
        this.started = false;
        while (!this.threadPool.isTerminated());
        this.executorService.shutdownNow();
        while (!this.executorService.isTerminated());
    }

}
