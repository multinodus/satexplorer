package satteliteExplorer.ui.engine.svg;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jfree.chart.JFreeChart;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 04.03.13
 * Time: 4:07
 * To change this template use File | Settings | File Templates.
 */
public class JFreeChartExporter {
  /**
   * Exports a JFreeChart to a SVG file.
   *
   * @param chart   JFreeChart to export
   * @param bounds  the dimensions of the viewport
   * @param svgFile the output file.
   * @throws IOException if writing the svgFile fails.
   */
  void exportChartAsSVG(JFreeChart chart, Rectangle bounds, File svgFile) throws IOException {
    // Get a DOMImplementation and create an XML document
    DOMImplementation domImpl =
        GenericDOMImplementation.getDOMImplementation();
    Document document = domImpl.createDocument(null, "svg", null);

    // Create an instance of the SVG Generator
    SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

    // draw the chart in the SVG generator
    chart.draw(svgGenerator, bounds);

    // Write svg file
    OutputStream outputStream = new FileOutputStream(svgFile);
    Writer out = new OutputStreamWriter(outputStream, "UTF-8");
    svgGenerator.stream(out, true /* use css */);
    outputStream.flush();
    outputStream.close();
  }
}
