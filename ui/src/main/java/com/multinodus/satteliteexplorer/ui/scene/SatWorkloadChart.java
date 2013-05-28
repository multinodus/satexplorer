package com.multinodus.satteliteexplorer.ui.scene;

import com.multinodus.satteliteexplorer.db.entities.Task;
import com.multinodus.satteliteexplorer.scheduler.models.SatModel;
import com.multinodus.satteliteexplorer.scheduler.transformations.PredictedDataElement;
import com.multinodus.satteliteexplorer.scheduler.util.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import java.awt.*;
import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 26.05.13
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class SatWorkloadChart {

  private ChartPanel chartPanel;
  private JFreeChart chart;

  public SatWorkloadChart(java.util.List<Pair<SatModel, java.util.List<Pair<Task, PredictedDataElement>>>> episodes,
                          int[] episodesTaskCount) {
    CategoryDataset dataset = createDataset(episodes, episodesTaskCount);
    chart = createChart(dataset);
    chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 350));
  }

  public void saveImage() {
    chartPanel.repaint();
    try {
      File outputfile = new File(System.getProperty("user.dir") + "/ui/target/classes/Textures/" + "satWorkload_chart.png");
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

  private CategoryDataset createDataset(java.util.List<Pair<SatModel, java.util.List<Pair<Task, PredictedDataElement>>>> episodes,
                                        int[] episodesTaskCount) {
    DefaultCategoryDataset result = new DefaultCategoryDataset();

    for (int i = 0; i < episodesTaskCount.length - 1; i++){
      SatModel satModel = episodes.get(i).f;
      double workload = satModel.getSat().getEquipment().getSnapshotVolume() * episodesTaskCount[i];
      double free = satModel.getSat().getEquipment().getStorageCapacity() - workload;
      result.addValue(workload, "Загруженно", String.format("Эпизод №%d, КА №%d", i, satModel.getSat().getSatId()));
      result.addValue(free, "Свободно", String.format("Эпизод №%d, КА №%d", i, satModel.getSat().getSatId()));
    }

    return result;
  }

  private JFreeChart createChart(final CategoryDataset dataset) {

    final JFreeChart chart = ChartFactory.createStackedBarChart(
        "Загруженность БЗУ КА", "", "МБ",
        dataset, PlotOrientation.VERTICAL, true, true, false);

    chart.setBackgroundPaint(new Color(255, 255, 255, 0));

    CategoryPlot plot = chart.getCategoryPlot();
    plot.setBackgroundPaint(new Color(255, 255, 255, 0));
    plot.setBackgroundImageAlpha(0.0f);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);
    plot.getRenderer().setSeriesPaint(0, new Color(128, 0, 0));
    plot.getRenderer().setSeriesPaint(1, new Color(0, 0, 255));

    plot.getRangeAxis().setTickLabelPaint(Color.white);
    plot.getRangeAxis().setLabelPaint(Color.white);
    plot.getDomainAxis().setTickLabelPaint(Color.white);
    plot.getDomainAxis().setLabelPaint(Color.white);

    chart.getTitle().setPaint(Color.white);

    return chart;
  }
}
