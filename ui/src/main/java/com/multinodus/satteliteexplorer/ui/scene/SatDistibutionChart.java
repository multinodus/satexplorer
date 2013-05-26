package com.multinodus.satteliteexplorer.ui.scene;

import com.google.common.collect.Maps;
import com.multinodus.satteliteexplorer.db.entities.Task;
import com.multinodus.satteliteexplorer.scheduler.models.SatModel;
import com.multinodus.satteliteexplorer.scheduler.optimizations.IKnapsackData;
import com.multinodus.satteliteexplorer.scheduler.transformations.PredictedDataElement;
import com.multinodus.satteliteexplorer.scheduler.util.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.TableOrder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 26.05.13
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class SatDistibutionChart {
  private JFreeChart chart;
  private ChartPanel chartPanel;

  public SatDistibutionChart(java.util.List<Pair<SatModel, java.util.List<Pair<Task, PredictedDataElement>>>> episodes,
                             int[] episodesTaskCount) {
    PieDataset dataset = createDataset(episodes, episodesTaskCount);
    chart = createChart(dataset);
    chartPanel = new ChartPanel(chart, true, true, true, false, true);
    chartPanel.setPreferredSize(new Dimension(600, 380));
  }

  public void saveImage() {
    chartPanel.repaint();
    try {
      File outputfile = new File(System.getProperty("user.dir") + "/ui/target/classes/Textures/" + "satDistribution_chart.png");
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

  private PieDataset createDataset(java.util.List<Pair<SatModel, java.util.List<Pair<Task, PredictedDataElement>>>> episodes,
                                   int[] episodesTaskCount) {
    DefaultPieDataset dataset = new DefaultPieDataset();

    Map<SatModel, Integer> distibution = Maps.newHashMap();

    for (int i = 0; i < episodesTaskCount.length - 1; i++){
      SatModel satModel = episodes.get(i).f;
      Integer value = null;
      if (distibution.containsKey(satModel)){
        value = distibution.get(satModel);
        value += episodesTaskCount[i];
      } else {
        value = episodesTaskCount[i];
      }
      distibution.put(satModel, value);
    }

    for (SatModel satModel : distibution.keySet()){
      dataset.setValue(satModel.getSat().getSatId().toString(), distibution.get(satModel));
    }

    return dataset;
  }

  private JFreeChart createChart(final PieDataset dataset) {
    final JFreeChart chart = ChartFactory.createPieChart(
        "Распределение задач по КА",  // chart title
        dataset,               // dataset
        true,                  // include legend
        true,
        false
    );

    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
    plot.setNoDataMessage("No data available");
    plot.setCircular(false);
    plot.setLabelGap(0.02);
    plot.setBackgroundPaint(new Color(255, 255, 255, 0));
    plot.setBackgroundImageAlpha(0.0f);

    chart.getTitle().setPaint(Color.white);
    chart.setBackgroundPaint(new Color(255, 255, 255, 0));

    return chart;
  }
}
