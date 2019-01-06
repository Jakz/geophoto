package com.github.jakz.geophoto.tools;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CheckedThreadPoolExecutor extends ThreadPoolExecutor
{
  public CheckedThreadPoolExecutor(int n)
  {
    super(n, n, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
  }
  
  @Override
  protected void afterExecute(Runnable r, Throwable t) 
  {
    super.afterExecute(r, t);
    if (t == null && r instanceof Future<?>) {
      try {
        Object result = ((Future<?>) r).get();
      } catch (CancellationException ce) {
          t = ce;
      } catch (ExecutionException ee) {
          t = ee.getCause();
      } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
      }
    }
    
    if (t != null)
      t.printStackTrace();
  }
}
