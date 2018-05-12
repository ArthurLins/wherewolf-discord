package com.arthurl.wolfbot.threading;


import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RejectedHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        //LoggerUtil.debug(r.toString()+" is rejected.");
    }
}
