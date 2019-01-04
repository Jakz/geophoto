package com.github.jakz.geophoto.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class StatusBar extends JPanel
{
  private final JProgressBar progress;
  
  private AtomicInteger totalTasks, doneTasks;

  public StatusBar()
  {
    progress = new JProgressBar();
    progress.setMaximum(100);
    progress.setMinimum(0);
    
    setLayout(new FlowLayout(FlowLayout.RIGHT));
    add(progress);
    
    totalTasks = new AtomicInteger();
    doneTasks = new AtomicInteger();
  }
  
  public void updateProgressForTasks()
  {
    int value = (int)((doneTasks.get() / (float)totalTasks.get())*100);
    progress.setValue(value);
    System.out.println(doneTasks.get()+" "+totalTasks.get());
  }
  
  public void taskAdd()
  {
    totalTasks.incrementAndGet();
    updateProgressForTasks();
  }
  
  public void taskDone()
  {
    doneTasks.incrementAndGet();
    updateProgressForTasks();
  }
  
}
