package satteliteExplorer.ui.engine.osm.data;


public interface Projection {
	public  Point project(Point lonLat);
	public   Point unproject(Point projected);
	public  String getID();
	

}
