package com.multinodus.satteliteexplorer.ui.scene;

import com.google.common.collect.Multimap;
import com.multinodus.satteliteexplorer.db.entities.Task;
import com.multinodus.satteliteexplorer.scheduler.models.SatModel;
import com.multinodus.satteliteexplorer.scheduler.transformations.PredictedDataElement;
import com.multinodus.satteliteexplorer.scheduler.util.Pair;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 26.05.13
 * Time: 18:22
 * To change this template use File | Settings | File Templates.
 */
public class SatExlorationChart {
  private ChartPanel chartPanel;
  private JFreeChart chart;

  public SatExlorationChart(java.util.List<Pair<SatModel, java.util.List<Pair<Task, PredictedDataElement>>>> episodes,
                            Multimap<Integer, Task> exploredTasks, SatModel selectedSat) {
    XYDataset data1 = createDataset(episodes, exploredTasks, selectedSat);
    XYItemRenderer renderer1 = new XYBarRenderer();
    DateAxis domainAxis = new DateAxis("Время");
    ValueAxis rangeAxis = new NumberAxis("Съемка");

    XYPlot plot = new XYPlot(data1, domainAxis, rangeAxis, renderer1);
    plot.setBackgroundPaint(new Color(255, 255, 255, 0));
    plot.setBackgroundImageAlpha(0.0f);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);

    chart = new JFreeChart("Интервалы съемки", plot);
    chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    chartPanel.setMouseZoomable(true, false);

    rangeAxis.setTickLabelPaint(Color.white);
    rangeAxis.setLabelPaint(Color.white);

    domainAxis.setTickLabelPaint(Color.white);
    domainAxis.setLabelPaint(Color.white);

    chart.getTitle().setPaint(Color.white);
    chart.setBackgroundPaint(new Color(255, 255, 255, 0));
  }

  public void saveImage() {
    chartPanel.repaint();
    try {
      File outputfile = new File(System.getProperty("user.dir") + "/ui/target/classes/Textures/" + "satExploration_chart.png");
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

  private XYDataset createDataset(java.util.List<Pair<SatModel, java.util.List<Pair<Task, PredictedDataElement>>>> episodes,
                                  Multimap<Integer, Task> exploredTasks, SatModel selectedSat) {
    TimePeriodValues s1 = new TimePeriodValues("Series 1");

    int i = 0;
    for (Pair<SatModel, java.util.List<Pair<Task, PredictedDataElement>>> episode : episodes){
      if (episode.f == selectedSat){
        Collection<Task> exlored = exploredTasks.get(i);
        for (Pair<Task, PredictedDataElement> p : episode.s){
          if (exlored.contains(p.f)){
            s1.add(new SimpleTimePeriod(p.s.date.getTime(), selectedSat.getSat().getEquipment().getSnapshotTime()), 1);
          }
        }
      }
      i++;
    }

    final TimePeriodValuesCollection dataset = new TimePeriodValuesCollection();
    dataset.addSeries(s1);
    dataset.setDomainIsPointsInTime(false);

    return dataset;

  }
}
