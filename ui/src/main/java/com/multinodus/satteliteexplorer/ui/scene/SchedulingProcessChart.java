package com.multinodus.satteliteexplorer.ui.scene;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 18.05.13
 * Time: 22:00
 * To change this template use File | Settings | File Templates.
 */
public class SchedulingProcessChart {
  private XYSeries series;
  private int x = 0;
  private JFreeChart chart;
  private ChartPanel chartPanel;

  public SchedulingProcessChart() {
    XYDataset dataset = createDataset();
    chart = createChart(dataset);
    chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(700, 300));
  }

  public void addValue(float value){
    series.add(x, value);
    x++;
  }

  public void saveImage(){
    chartPanel.repaint();
    try {
      File outputfile = new File(System.getProperty("user.dir") + "/ui/target/classes/Textures/" + "new_chart.png");
      ChartUtilities.saveChartAsPNG(outputfile,
          chart,
          700, 300,
          null,
          true,    // encodeAlpha
          0);
    } catch (IOException exc) {
      System.out.println(exc.toString());
    }
  }

  private XYDataset createDataset() {
    series = new XYSeries("");

    final XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series);

    return dataset;
  }

  private JFreeChart createChart(final XYDataset dataset) {
    JFreeChart chart = ChartFactory.createXYLineChart(
        "История изменения целевой функции",   // chart title
        "Итерации",                     // x axis label
        "Целевая функция",               // y axis label
        dataset,                  // data
        PlotOrientation.VERTICAL,
        false,                     // include legend
        false,                     // tooltips
        false                     // urls
    );

    chart.setBackgroundPaint(new Color(255, 255, 255, 0));

    XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(new Color(255, 255, 255, 0));
    plot.setBackgroundImageAlpha(0.0f);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);

    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesLinesVisible(1, false);
    renderer.setSeriesShapesVisible(0, false);
    renderer.setSeriesStroke(0, new BasicStroke(5));
    plot.setRenderer(renderer);

    // change the auto tick unit selection to integer units only...
     NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    rangeAxis.setTickLabelPaint(Color.white);
    rangeAxis.setLabelPaint(Color.white);

    plot.getDomainAxis().setTickLabelPaint(Color.white);
    plot.getDomainAxis().setLabelPaint(Color.white);

    chart.getTitle().setPaint(Color.white);
    // OPTIONAL CUSTOMISATION COMPLETED.

    return chart;
  }
}
