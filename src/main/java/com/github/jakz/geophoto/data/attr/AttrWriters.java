package com.github.jakz.geophoto.data.attr;

import java.nio.ByteBuffer;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.pixbits.lib.io.xml.gpx.Coordinate;

public class AttrWriters
{
  public static final Function<Integer, byte[]> IntWriter = o -> ByteBuffer.allocate(4).putInt(o).array();
  public static final Function<Coordinate, byte[]> CoordinateWriter = o -> o.toByteArray();
  

  public static final Function<byte[], Integer> IntReader = o -> ByteBuffer.wrap(o).getInt();
  public static final Function<byte[], Coordinate> CoordinateReader = o -> Coordinate.of(o);

}
