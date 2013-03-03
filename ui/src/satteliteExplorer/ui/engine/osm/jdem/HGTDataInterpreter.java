package satteliteExplorer.ui.engine.osm.jdem;

import satteliteExplorer.ui.engine.osm.datasource.DataInterpreter;

import java.io.InputStream;

public class HGTDataInterpreter extends DEMSource implements DataInterpreter {

	public HGTDataInterpreter( int width,int height, double resolution, int endianness)
	{
		super(width,height,resolution,endianness);
	}
	
	public Object getData (InputStream in) throws Exception
	{
		return load(in);
	}
}
