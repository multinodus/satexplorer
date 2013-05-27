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
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

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
public class CostPieChart {
  private JFreeChart chart;
  private ChartPanel chartPanel;

  public CostPieChart(IKnapsackData knapsackData, int[] schedule, java.util.List<Object> taskList) {
    PieDataset dataset = createDataset(knapsackData, schedule, taskList);
    chart = createChart(dataset);
    chartPanel = new ChartPanel(chart, true, true, true, false, true);
    chartPanel.setPreferredSize(new Dimension(600, 380));
  }

  public void saveImage() {
    chartPanel.repaint();
    try {
      File outputfile = new File(System.getProperty("user.dir") + "/ui/target/classes/Textures/" + "costPie_chart.png");
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

  private PieDataset createDataset(IKnapsackData knapsackData, int[] schedule, java.util.List<Object> taskList) {
    DefaultPieDataset dataset = new DefaultPieDataset();
    double costTotal = 0;
    double costSum = 0;
    int number = 0;
    int garbageIndex = knapsackData.getM() - 1;
    for (int i = 0; i < schedule.length; i++){
      if (schedule[i] != garbageIndex){
        costSum += knapsackData.getProfit()[i][schedule[i]];
        number++;
      }
      costTotal += ((Task)taskList.get(i)).getCost();
    }

    dataset.setValue("Выполнено", costSum);
    dataset.setValue("Не выполнено", costTotal - costSum);

    return dataset;
  }


  private JFreeChart createChart(final PieDataset dataset) {
    final JFreeChart chart = ChartFactory.createPieChart(
        "Доля выполненных задач по стоимости",  // chart title
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

    plot.setLabelGenerator(new CustomLabelGenerator());

    chart.getTitle().setPaint(Color.white);
    chart.setBackgroundPaint(new Color(255, 255, 255, 0));

    return chart;
  }
}
