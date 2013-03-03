package satteliteExplorer.ui.engine.osm.datasource;

import satteliteExplorer.ui.engine.osm.data.Projection;

import java.io.IOException;

public interface TiledData {

	public void save(String filename) throws IOException;
	public void reproject(Projection proj);
	public void merge(TiledData otherData);
}
