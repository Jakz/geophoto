package com.github.jakz.geophoto.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.github.jakz.geophoto.Mediator;

public class StatusBar extends JPanel
{
  private final Mediator mediator;
  
  private final JProgressBar progress;
  private final JLabel text;
  
  private AtomicInteger totalTasks, doneTasks;

  public StatusBar(Mediator mediator)
  {
    this.mediator = mediator;
    
    progress = new JProgressBar();
    progress.setMaximum(100);
    progress.setMinimum(0);
    progress.setVisible(false);
    
    text = new JLabel();
    
    setLayout(new FlowLayout(FlowLayout.RIGHT));
    add(progress);
    add(text);
    add(mediator.ui().quickInfo());
    
    totalTasks = new AtomicInteger();
    doneTasks = new AtomicInteger();
    
    
  }
  
  public void setText(String text)
  {
    this.text.setText(text);
  }
  
  private void updateProgressForTasks()
  {
    int value = (int)((doneTasks.get() / (float)totalTasks.get())*100);
    progress.setValue(value);
    //System.out.println(doneTasks.get()+" "+totalTasks.get());
  }
  
  public void taskAdd()
  {
    totalTasks.incrementAndGet();
    updateProgressForTasks();
    progress.setVisible(true);
  }
  
  public void taskDone()
  {
    doneTasks.incrementAndGet();
    updateProgressForTasks();
    
    if (doneTasks.get() == totalTasks.get())
      progress.setVisible(false);
  }
  
}
