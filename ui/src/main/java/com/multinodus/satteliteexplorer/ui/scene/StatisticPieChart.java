package com.multinodus.satteliteexplorer.ui.scene;

import java.awt.Font;

import com.multinodus.satteliteexplorer.db.entities.Task;
import com.multinodus.satteliteexplorer.scheduler.optimizations.IKnapsackData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.util.TableOrder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 26.05.13
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class StatisticPieChart {
  private JFreeChart chart;
  private ChartPanel chartPanel;

  public StatisticPieChart(IKnapsackData knapsackData, int[] schedule, java.util.List<Object> taskList) {
    final CategoryDataset dataset = createDataset(knapsackData, schedule, taskList);
    chart = createChart(dataset);
    chartPanel = new ChartPanel(chart, true, true, true, false, true);
    chartPanel.setPreferredSize(new java.awt.Dimension(600, 380));
  }

  public void saveImage() {
    chartPanel.repaint();
    try {
      File outputfile = new File(System.getProperty("user.dir") + "/ui/target/classes/Textures/" + "statisticPie_chart.png");
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

  private CategoryDataset createDataset(IKnapsackData knapsackData, int[] schedule, java.util.List<Object> taskList) {

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

    final double[][] data = new double[][] {
        {costSum, costTotal - costSum},
        {number, schedule.length - number}
    };

    final CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
        "Доля выполненных задач",
        "Выполнено",
        data
    );

    return dataset;
  }

  private JFreeChart createChart(final CategoryDataset dataset) {
    final JFreeChart chart = ChartFactory.createMultiplePieChart(
        "Multiple Pie Chart",  // chart title
        dataset,               // dataset
        TableOrder.BY_ROW,
        true,                  // include legend
        true,
        false
    );
    final MultiplePiePlot plot = (MultiplePiePlot) chart.getPlot();
    final JFreeChart subchart = plot.getPieChart();
    final PiePlot p = (PiePlot) subchart.getPlot();
    p.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
    p.setLabelFont(new Font("SansSerif", Font.PLAIN, 8));
    p.setInteriorGap(0.30);
    p.setBackgroundPaint(new Color(255, 255, 255, 0));
    p.setBackgroundImageAlpha(0.0f);

    chart.getTitle().setPaint(Color.white);
    chart.setBackgroundPaint(new Color(255, 255, 255, 0));

    return chart;
  }
}
