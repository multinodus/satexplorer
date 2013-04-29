package satteliteExplorer.ui.engine.osm.datasource;

import satteliteExplorer.ui.engine.osm.data.Point;

import java.io.IOException;
import java.io.InputStream;


public abstract class DataSource {
  protected FileFormatter formatter;
  String basePath;


  public DataSource(String basePath, FileFormatter formatter) {
    System.out.println("DataSource constructor: basePath=" + basePath);
    this.basePath = basePath;
    this.formatter = formatter;
  }

  public DataSource(String filename) {
    this.basePath = filename;
  }

  public Object getData(Point bottomLeft, DataInterpreter interpreter) throws Exception {
    InputStream in = getInputStream(bottomLeft);

    Object data = interpreter.getData(in);
    in.close();
    return data;
  }

  public Object getData(DataInterpreter interpreter) throws Exception {
    return interpreter.getData(getInputStream(basePath));
  }

  protected InputStream getInputStream(Point bottomLeft) throws IOException {

    String filename = basePath + "/" + formatter.format(bottomLeft);
    return getInputStream(filename);
  }

  public FileFormatter getFormatter() {
    return formatter;
  }

  protected abstract InputStream getInputStream(String filename) throws IOException;
}



