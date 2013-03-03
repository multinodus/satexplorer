package satteliteExplorer.ui.engine.osm.data;

public abstract class Projectable {
	protected Projection proj;
	
	public abstract void reproject(Projection newProj);
}
