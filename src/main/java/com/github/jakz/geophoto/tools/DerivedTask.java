package com.github.jakz.geophoto.tools;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class DerivedTask<T,V> implements Callable<V>
{
  Function<T, V> transformer;
  Callable<T> supplier;
  
  DerivedTask(Callable<T> supplier, Function<T, V> transformer)
  {
    this.supplier = supplier;
    this.transformer = transformer;
  }
  
  @Override
  public V call() throws Exception
  {
    return transformer.apply(supplier.call());
  }
}
