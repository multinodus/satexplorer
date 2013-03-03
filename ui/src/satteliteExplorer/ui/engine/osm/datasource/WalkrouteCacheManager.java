package satteliteExplorer.ui.engine.osm.datasource;

import satteliteExplorer.ui.engine.osm.data.Walkroute;
import org.xml.sax.SAXException;

import java.io.*;

// manage caching walkroutes
public class WalkrouteCacheManager {

	String cacheDir;
	
	public WalkrouteCacheManager(String cacheDir)
	{
		this.cacheDir = cacheDir;
		File recDir = new File(cacheDir+"/rec");
		if(!recDir.exists())
			recDir.mkdirs();
	}
	
	public void addWalkrouteToCache(Walkroute wr) throws IOException
	{
		doAddWalkrouteToCache(wr,cacheDir+"/"+wr.getId()+".xml");
	}
	
	public void addWalkrouteToCache(Walkroute wr, String filename) throws IOException
	{
		doAddWalkrouteToCache(wr,cacheDir+"/rec/"+filename);
	}
	
	public void addRecordingWalkroute(Walkroute wr) throws IOException
	{
		doAddWalkrouteToCache(wr,cacheDir+"/rec/tmp.xml");
	}
	
	private void doAddWalkrouteToCache(Walkroute wr, String filename) throws IOException
	{
		String xml = wr.toXML();
		DataOutputStream out = new DataOutputStream(new FileOutputStream(filename));
		out.writeBytes(xml);
		out.close();
	}
	
	public boolean isInCache(int wrId)
	{
		return new File(cacheDir+"/"+ wrId + ".xml").exists();
	}
	
	public Walkroute getWalkrouteFromCache(int id) throws IOException, SAXException
	{
		return getWalkroute(cacheDir+"/"+ id + ".xml");
	}
	
	public Walkroute getRecordedWalkroute(String file) throws IOException, SAXException
	{
		return getWalkroute(cacheDir+"/rec/"+file);
	}
	
	public Walkroute getRecordingWalkroute() throws IOException, SAXException
	{
		return getRecordedWalkroute("tmp.xml");
	}
	
	public boolean deleteRecordingWalkroute()
	{
		File f = new File(cacheDir+"/rec/tmp.xml");
		if(f.exists())
			return f.delete();
		return false;
	}
	
	private Walkroute getWalkroute(String file) throws IOException, SAXException
	{
		if(new File(file).exists())
		{
			FileInputStream in = new FileInputStream(file);
			XMLDataInterpreter interpreter = new XMLDataInterpreter(new WalkrouteHandler());
			Walkroute wr = (Walkroute)interpreter.getData(in);
			in.close();
			return wr;
		}
		return null;
	}
}
