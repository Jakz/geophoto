package com.github.jakz.geophoto.data.attr;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.github.jakz.geophoto.data.Rational;
import com.pixbits.lib.io.xml.gpx.Coordinate;

public interface AttrHandler<T>
{
   public T parse(java.lang.String[] tags);
   public byte[] toBytes(T value);
   public T fromBytes(byte[] bytes);
   
   default byte[] toBytesGeneric(Object value) { return toBytes((T)value); }
   
   public static class StringHandler implements AttrHandler<String>
   {
     @Override public String parse(String[] strings) { return strings[0]; }
     @Override public byte[] toBytes(String value) { return value.getBytes(); }
     @Override public String fromBytes(byte[] bytes) { return new java.lang.String(bytes); }
   }
   
   public static class IntHandler implements AttrHandler<java.lang.Integer>
   {
     @Override public java.lang.Integer parse(java.lang.String[] strings) { return java.lang.Integer.valueOf(strings[0]); }
     @Override public byte[] toBytes(java.lang.Integer value) { return ByteBuffer.allocate(java.lang.Integer.BYTES).putInt(value).array(); }
     @Override public java.lang.Integer fromBytes(byte[] bytes) { return ByteBuffer.wrap(bytes).getInt(); }
   }
   
   public static class DoubleHandler implements AttrHandler<Double>
   {
     @Override public Double parse(java.lang.String[] strings) { return Double.valueOf(strings[0]); }
     @Override public byte[] toBytes(java.lang.Double value) { return ByteBuffer.allocate(Double.BYTES).putDouble(value).array(); }
     @Override public Double fromBytes(byte[] bytes) { return ByteBuffer.wrap(bytes).getDouble(); }
   }
   
   public static class RationalHandler implements AttrHandler<Rational>
   {

    @Override public Rational parse(java.lang.String[] strings) { return new Rational(strings[0]); }
    @Override public byte[] toBytes(Rational value) { return ByteBuffer.allocate(Long.BYTES*2).putLong(value.num).putLong(value.den).array(); }
    @Override public Rational fromBytes(byte[] bytes)
    { 
      ByteBuffer buffer = ByteBuffer.wrap(bytes);
      long num = buffer.getLong();
      long den = buffer.getLong();
      return new Rational(num, den);
    }
   }
   
   public static class DateHandler implements AttrHandler<LocalDateTime>
   {
      private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

      @Override public LocalDateTime parse(java.lang.String[] strings) { return LocalDateTime.parse(strings[0], dateFormatter); }
      @Override public byte[] toBytes(LocalDateTime value) { return value.format(dateFormatter).getBytes(); }
      @Override public LocalDateTime fromBytes(byte[] bytes) { return LocalDateTime.parse(new String(bytes), dateFormatter); }
   }
   
   public static class CoordinateHandler implements AttrHandler<Coordinate>
   {
    @Override public Coordinate parse(java.lang.String[] strings)
    {
      double lat = strings[0] != null ? Double.parseDouble(strings[0]) : Double.NaN;
      double lng = strings[1] != null ? Double.parseDouble(strings[1]) : Double.NaN;
      double alt = strings[2] != null ? Double.parseDouble(strings[2]) : Double.NaN;
      
      if (Double.isNaN(lat) || Double.isNaN(lng))
        return new Coordinate(Double.NaN, Double.NaN);
      else
        return new Coordinate(lat, lng, alt);
    }
    
    @Override public byte[] toBytes(Coordinate value) { return value.toByteArray(); }
    @Override public Coordinate fromBytes(byte[] bytes) { return Coordinate.of(bytes); }   
   }
   
   public static class EnumHandler<T extends Enum<?>> implements AttrHandler<T>
   {
     private final Class<T> type;
     public EnumHandler(Class<T> type) { this.type = type; }
     
     @Override public T parse(java.lang.String[] tags)
     { 
       int ordinal = Integer.valueOf(tags[0]);
       return type.getEnumConstants()[ordinal];
     }
     
     @Override public byte[] toBytes(T value) { return ByteBuffer.allocate(4).putInt(value.ordinal()).array(); }
     @Override public T fromBytes(byte[] bytes) { return type.getEnumConstants()[ByteBuffer.wrap(bytes).getInt()]; }
   }
}
