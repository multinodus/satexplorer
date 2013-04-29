package satteliteExplorer.ui.engine.osm.datasource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class XMLDataInterpreter implements DataInterpreter {

  XMLDataHandler handler;

  public XMLDataInterpreter(XMLDataHandler handler) {
    this.handler = handler;

  }

  public Object getData(InputStream in) throws IOException, SAXException {
    handler.reset();
    XMLReader reader = getReader();
    if (reader != null) {
      reader.parse(new InputSource(in));
      return handler.getData();
    }
    return null;
  }

  public Object getData(String xml) throws IOException, SAXException {
    handler.reset();
    XMLReader reader = getReader();
    if (reader != null) {
      reader.parse(new InputSource(new StringReader(xml)));
      return handler.getData();
    }
    return null;
  }

  private XMLReader getReader() throws SAXException {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser parser = factory.newSAXParser();
      XMLReader reader = parser.getXMLReader();
      reader.setContentHandler(handler);
      return reader;
    }
    // wtf is a "ParserConfigurationException"?
    // A "serious configuration error" ???
    // Some internal thing which the library should probably handle itself.
    catch (ParserConfigurationException e) {
      System.out.println("WTF: " + e);
      return null;
    }
  }
}
