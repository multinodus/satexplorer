package satteliteExplorer.ui.scene;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import satteliteExplorer.scheduler.models.SatModel;
import satteliteExplorer.scheduler.transformations.PredictorDataElement;
import satteliteExplorer.scheduler.transformations.PredictorOfObservations;
import satteliteExplorer.scheduler.transformations.SI_Transform;
import satteliteExplorer.scheduler.util.DateTimeConstants;
import satteliteExplorer.scheduler.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 29.04.13
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
public class QualityChart extends JFrame {

  public QualityChart() {
    DefaultXYZDataset dataset = createDataset();
    JFreeChart chart = createChart(dataset);

    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    setContentPane(chartPanel);
  }

  private DefaultXYZDataset createDataset() {
    DefaultXYZDataset dataset = new DefaultXYZDataset();

    Map<satteliteExplorer.db.entities.Task, Pair<SatModel, java.util.List<PredictorDataElement>>> schedule =
        new HashMap<satteliteExplorer.db.entities.Task, Pair<SatModel, java.util.List<PredictorDataElement>>>();
    for (satteliteExplorer.db.entities.Task task : PlanetSimpleTest.scene.getWorld().getTasks()) {
      PredictorOfObservations predictorOfObservations = PredictorOfObservations.getInstance();
      Pair<SatModel, java.util.List<PredictorDataElement>> data = predictorOfObservations.bestObservers(SI_Transform.INITIAL_TIME,
          new Date(SI_Transform.INITIAL_TIME.getTime() + 7 * DateTimeConstants.MSECS_IN_DAY), task.getRegion(), 0.01f,
          PlanetSimpleTest.scene.getWorld().getSatModels());
      schedule.put(task, data);
    }

    int i = 0;
    for (satteliteExplorer.db.entities.Task task : schedule.keySet()) {
      Pair<SatModel, java.util.List<PredictorDataElement>> p = schedule.get(task);

      double[][] data = new double[3][schedule.keySet().size()];

      data[0][i] = task.getRegion().getLatitude();
      data[1][i] = task.getRegion().getLongitude();

      boolean explored = false;
      for (PredictorDataElement element : p.s) {
        if (element.angle < element.visibleAngle) {
          data[2][i] = 1;
          explored = true;
          break;
        }
      }
      if (!explored) {
        data[2][i] = 0;
      }
      dataset.addSeries(i, data);
      i++;
    }

    return dataset;
  }

  private XYPlot createPlot(DefaultXYZDataset dataset) {
    NumberAxis xAxis = new NumberAxis("Intensity");
    xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    xAxis.setLowerMargin(0.0);
    xAxis.setUpperMargin(0.0);
    NumberAxis yAxis = new NumberAxis("Distance to Closest Blood Vessel (um)");
    yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    yAxis.setLowerMargin(0.0);
    yAxis.setUpperMargin(0.0);
    XYBlockRenderer renderer = new XYBlockRenderer();
    PaintScale scale = new GrayPaintScale(0, 10000.0);
    renderer.setPaintScale(scale);
    renderer.setBlockHeight(1);
    renderer.setBlockWidth(1);
    XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinesVisible(false);
    plot.setRangeGridlinePaint(Color.white);

    return plot;
  }

  private JFreeChart createChart(DefaultXYZDataset dataset) {
    XYPlot plot = createPlot(dataset);

    JFreeChart chart = new JFreeChart("Surface Plot", plot);
    chart.removeLegend();
    chart.setBackgroundPaint(Color.white);

    return chart;
  }
}
