package com.jack.geophoto;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

/**
 * Hello world!
 *
 */
public class App 
{
  public static void main( String[] args )
  {
    Browser browser = new Browser();
    BrowserView view = new BrowserView(browser);

    JFrame frame = new JFrame("JxBrowser Google Maps");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.add(view, BorderLayout.CENTER);
    frame.setSize(700, 500);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    browser.loadURL("http://maps.google.com");
  }
}
