package com.github.jakz.geophoto.ui.map.builders;

import com.github.jakz.geophoto.ui.map.GpsTrackLine;
import com.teamdev.jxmaps.Map;

public class GpxTrackLineBuilder extends MapElementBuilder<GpsTrackLine>
{
  public GpxTrackLineBuilder(Map map) { super(map); }
  @Override public GpsTrackLine build() { return new GpsTrackLine(map); }
}
