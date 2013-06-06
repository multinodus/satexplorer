package com.multinodus.satteliteexplorer.ui.scene;

import com.google.common.collect.Multimap;
import com.multinodus.satteliteexplorer.db.entities.Task;
import com.multinodus.satteliteexplorer.scheduler.models.SatModel;
import com.multinodus.satteliteexplorer.scheduler.transformations.PredictedDataElement;
import com.multinodus.satteliteexplorer.scheduler.util.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYInterval;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
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
public class SatExlorationChart extends JFrame {
  private ChartPanel chartPanel;
  private JFreeChart chart;

  public SatExlorationChart(java.util.List<Pair<SatModel, java.util.List<Pair<Task, PredictedDataElement>>>> episodes,
                            Multimap<Integer, Task> exploredTasks) {
    java.util.List<SatModel> satModels = UIApplication.app.scene.getWorld().getSatModels();

    IntervalXYDataset data1 = createDataset(episodes, exploredTasks, satModels);

    XYShapeRenderer renderer1 = new XYShapeRenderer();
//    XYBarRenderer renderer1 = new XYBarRenderer();
    DateAxis domainAxis = new DateAxis("Время");

    String[] labels = new String[satModels.size()];
    int i = 0;
    for (SatModel satModel : satModels){
      labels[i] = "КА №" + satModel.getSat().getSatId().toString();
      i++;
    }

    SymbolAxis rangeAxis = new SymbolAxis("Съемка", labels);
    rangeAxis.setGridBandsVisible(false);

//    renderer1.getUseYInterval();

    XYPlot plot = new XYPlot(data1, domainAxis, rangeAxis, renderer1);
    plot.setBackgroundPaint(new Color(255, 255, 255));
//    plot.setBackgroundImageAlpha(0.0f);
//    plot.setDomainGridlinePaint(Color.white);
//    plot.setRangeGridlinePaint(Color.white);

    chart = new JFreeChart("Интервалы съемки", plot);
    chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(1280, 760));
//    chartPanel.setMouseZoomable(true, false);

//    rangeAxis.setTickLabelPaint(Color.white);
//    rangeAxis.setLabelPaint(Color.white);
//
//    domainAxis.setTickLabelPaint(Color.white);
//    domainAxis.setLabelPaint(Color.white);
//
//    chart.getTitle().setPaint(Color.white);
    chart.setBackgroundPaint(new Color(255, 255, 255));

    add(chartPanel);
  }

  public void saveImage() {
    chartPanel.repaint();
    try {
      File outputfile = new File(System.getProperty("user.dir") + "/ui/target/classes/Textures/" + "satExploration_chart.png");
      ChartUtilities.saveChartAsPNG(outputfile,
          chart,
          1280, 760,
          null,
          true,    // encodeAlpha
          0);
    } catch (IOException exc) {
      System.out.println(exc.toString());
    }
  }

  private IntervalXYDataset createDataset(java.util.List<Pair<SatModel, java.util.List<Pair<Task, PredictedDataElement>>>> episodes,
                                  Multimap<Integer, Task> exploredTasks, java.util.List<SatModel> satModels) {
    TaskSeriesCollection taskSeriesCollection = new TaskSeriesCollection();

    for (SatModel selectedSat : satModels){
      TaskSeries s1 = new TaskSeries("КА №"+selectedSat.getSat().getSatId().toString());

      int i = 0;
      for (Pair<SatModel, java.util.List<Pair<Task, PredictedDataElement>>> episode : episodes){
        if (episode.f == selectedSat){
          Collection<Task> exlored = exploredTasks.get(i);
          for (Pair<Task, PredictedDataElement> p : episode.s){
            if (exlored.contains(p.f)){
              s1.add(new org.jfree.data.gantt.Task("Задача №" + p.f.getTaskId(), new SimpleTimePeriod(p.s.date.getTime(), p.s.date.getTime() + selectedSat.getSat().getEquipment().getSnapshotTime())));
            }
          }
        }
        i++;
      }

      taskSeriesCollection.add(s1);
    }

    final XYTaskDataset dataset = new XYTaskDataset(taskSeriesCollection);

    dataset.setTransposed(true);
    dataset.setSeriesWidth(0.6D);
    return dataset;
  }
}
