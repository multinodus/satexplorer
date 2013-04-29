package satteliteExplorer.ui.engine.osm.datasource;

import satteliteExplorer.ui.engine.osm.data.Point;


public abstract class FileFormatter {

  public abstract String format(Point bottomLeft);
}

