package satteliteExplorer.ui.engine.osm.jdem;

import satteliteExplorer.ui.engine.osm.data.Point;
import satteliteExplorer.ui.engine.osm.data.Projection;
import satteliteExplorer.ui.engine.osm.datasource.DataInterpreter;
import satteliteExplorer.ui.engine.osm.datasource.DataSource;
import satteliteExplorer.ui.engine.osm.datasource.TiledData;

public class HGTTileDeliverer extends satteliteExplorer.ui.engine.osm.datasource.TileDeliverer {

  int demWidth, demHeight;
  double demRes;

  public HGTTileDeliverer(String name, DataSource ds, DataInterpreter interpreter, int tileWidth, int tileHeight,
                          Projection proj, int demWidth, int demHeight, double demRes, String cachedir) {
    super(name, ds, interpreter, tileWidth, tileHeight, proj, cachedir);
    this.demWidth = demWidth;
    this.demHeight = demHeight;
    this.demRes = demRes;
  }


  protected TiledData dataWrap(Point origin, Object rawData) {
    if (rawData != null) {

      DEM dem = new DEM(origin, demWidth, demHeight, demRes, proj);

      dem.setHeights((int[]) rawData);

      return dem;
    }
    return null;
  }

}
