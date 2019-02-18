package com.github.jakz.geophoto.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Database
{
  final Set<PhotoFolder> folders;
  final Set<Photo> photo;
  final Map<PhotoFolder, TreeSet<Photo>> photoMap; 
  
  public Database()
  {
    folders = new HashSet<>();
    photo = new HashSet<>();
    photoMap = new HashMap<>();
  }
  
  public void addFolder(PhotoSet folder)
  {
    
  }
}
