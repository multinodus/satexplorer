package satteliteExplorer.ui.engine.osm.datasource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class WebDataSource extends DataSource
{
	
	
	public WebDataSource(String basePath,FileFormatter formatter)
	{
		super(basePath,formatter);
	}

	
	protected InputStream getInputStream(String url)throws IOException
	{
		System.out.println("downloading from : " + url);
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		HttpEntity entity=response.getEntity();
		InputStream in = null;
		in=entity.getContent();
		return in;
	}
}
