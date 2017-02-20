package com.jack.geophoto.tools;

@FunctionalInterface
public interface BiCallback<T,V>
{
  void callback(T t, V v);
}
