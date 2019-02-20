package com.github.jakz.geophoto.ui.tree;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.PhotoEnumeration;

public class TreeBuilder
{
  public static enum DateOrder
  {
    NEWEST_FIRST,
    OLDEST_FIRST
  };
  
  public static PhotoTreeNode<PhotoEnumerationNode> ofFlatList(Collection<PhotoEnumeration> nodes)
  {
    PhotoEnumerationNode root = new PhotoEnumerationNode(null, null, null);
    root.setChildren(nodes.stream().map(e -> new PhotoEnumerationNode(root, e, null)).collect(Collectors.toList()));
    return root;
  }
  
  public static PhotoTreeNode<PhotoEnumerationNode> byDay(Stream<Photo> photos, DateOrder order)
  {
    final LocalDate unknownDate = LocalDate.of(1900, 1, 1);
    
    Map<LocalDate, List<Photo>> groups = photos.collect(Collectors.groupingBy(photo -> {
      LocalDateTime date = photo.dateTimeOriginal();
      return date != null ? date.toLocalDate() : unknownDate;
    }));
    
    /* sort by date, default is oldest first */
    Comparator<Map.Entry<LocalDate, List<Photo>>> sorter = (e1, e2) -> e1.getKey().compareTo(e2.getKey());
    
    if (order == DateOrder.NEWEST_FIRST)
      sorter = sorter.reversed();
        
    PhotoEnumerationNode root = new PhotoEnumerationNode(null, null, null);
    root.setChildren(
        groups.entrySet().stream().sorted(sorter)
          .map(e -> new PhotoEnumerationNode(
              root, 
              PhotoEnumeration.of(e.getValue(), e.getKey() != unknownDate ? e.getKey().toString() : "Unknown"),
              null)
          )
          .collect(Collectors.toList())
    );
    
    return root;
  }
}
