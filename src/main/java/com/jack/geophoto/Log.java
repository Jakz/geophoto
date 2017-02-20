package com.jack.geophoto;

public class Log
{
  public static void d(String string, Object... args)
  {
    System.out.printf("%s\n", String.format(string, args));
  }
}
