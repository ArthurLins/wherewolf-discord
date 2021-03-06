package com.arthurl.wolfbot.threading;

import java.util.concurrent.*;

public class ThreadPool {
    private final ThreadPoolExecutor threadPool;
    private final ScheduledExecutorService executorService;

    private boolean started;

    public ThreadPool() {
        final RejectedHandler rejectedHandler = new RejectedHandler();
        final ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(3,
                25,
                1,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory,
                rejectedHandler
        );
        this.executorService = Executors.newScheduledThreadPool(10);
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

    public ScheduledFuture run(Runnable rn, int delay){
        try {
            if (this.isStarted()) {
                return this.executorService.schedule(rn, delay, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ScheduledFuture periodic(Runnable runnable, int delay){
        return this.executorService.scheduleWithFixedDelay(runnable, 0,delay, TimeUnit.MILLISECONDS);
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
