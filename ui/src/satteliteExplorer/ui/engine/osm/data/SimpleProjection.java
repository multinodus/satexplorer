package satteliteExplorer.ui.engine.osm.data;

public abstract class SimpleProjection implements Projection {
	
	public abstract String getID();
	public boolean equals(SimpleProjection other)
	{
		return getID().equals(other.getID());
	}
}
