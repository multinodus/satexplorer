package satteliteExplorer.ui.scene;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.jme3.math.FastMath;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import satteliteExplorer.db.EntityContext;
import satteliteExplorer.db.entities.Sat;
import satteliteExplorer.db.entities.Task;
import satteliteExplorer.scheduler.models.SatModel;
import satteliteExplorer.scheduler.optimizations.OptimizationServer;
import satteliteExplorer.scheduler.optimizations.genetic.GeneticSolver;
import satteliteExplorer.scheduler.transformations.PredictedDataElement;
import satteliteExplorer.scheduler.transformations.PredictorOfObservations;
import satteliteExplorer.scheduler.transformations.SI_Transform;
import satteliteExplorer.scheduler.util.DateTimeConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 29.04.13
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
public class QualityChart extends JFrame {
  private Image background;
  private JFreeChart chart;
  private UIApplication app;

  public QualityChart(UIApplication app) {
    this.app = app;

    try {
      File file = new File(System.getProperty("user.dir") + "/assets/Textures/earth_mini.jpg");
      background = ImageIO.read(file);
    } catch (IOException e) {
      System.out.println(e.toString());
    }

    PredictorOfObservations predictorOfObservations = PredictorOfObservations.getInstance();
    Map<SatModel, Multimap<Task, PredictedDataElement>> allData = predictorOfObservations.observe(SI_Transform.INITIAL_TIME,
        new Date(SI_Transform.INITIAL_TIME.getTime() + 40 * DateTimeConstants.MSECS_IN_HOUR/*DateTimeConstants.DAYS_IN_WEEK*DateTimeConstants.MSECS_IN_DAY*/), app.scene.getWorld().getTasks(),
        app.scene.getWorld().getSatModels(), 0.05f);

//    DefaultXYZDataset dataset = createDataset(allData);
    XYDataset dataset = solve(allData);
    JFreeChart chart = createChart(dataset);

    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    setContentPane(chartPanel);

    BufferedImage bufferedImage = chart.createBufferedImage(800, 600);
    try {
      File outputfile = new File(System.getProperty("user.dir") + "/out/production/ui/Textures/" + "chart.png");
      ImageIO.write(bufferedImage, "png", outputfile);
    } catch (IOException exc) {
      System.out.println(exc.toString());
    }
  }

  private DefaultXYZDataset createDataset(Map<SatModel, Multimap<Task, PredictedDataElement>> allData) {
    DefaultXYZDataset dataset = new DefaultXYZDataset();

    int j = 0;
    for (SatModel sat : allData.keySet()) {
      Multimap<Task, PredictedDataElement> observation = allData.get(sat);
      int i = 0;
      double[][] data = new double[3][observation.keys().size()];
      for (satteliteExplorer.db.entities.Task task : observation.keys()) {
        Collection<PredictedDataElement> elements = observation.get(task);

        data[0][i] = task.getRegion().getLongitude() * FastMath.RAD_TO_DEG;
        data[1][i] = task.getRegion().getLatitude() * FastMath.RAD_TO_DEG;

        boolean explored = false;
        for (PredictedDataElement element : elements) {
          if (element.date.after(task.getStart()) && element.date.before(task.getFinish())) {
            data[2][i] = task.getCost();
            explored = true;
            break;
          }
        }
        if (!explored) {
          data[2][i] = 0;
        }
        i++;
      }
      dataset.addSeries(j, data);
      j++;
//      break;
    }

    return dataset;
  }

  private XYDataset solve(Map<SatModel, Multimap<Task, PredictedDataElement>> data) {
    List<Object> satList = EntityContext.get().getAllEntities(Sat.class);
    List<Object> taskList = EntityContext.get().getAllEntities(Task.class);

    int satSize = satList.size();
    int taskSize = taskList.size();

    Map<Object, Integer> satIndexes = Maps.newHashMap();
    Map<Object, Integer> taskIndexes = Maps.newHashMap();

    int i = 0;
    for (Object sat : satList) {
      satIndexes.put(sat, i);
      i++;
    }

    i = 0;
    for (Object task : taskList) {
      taskIndexes.put(task, i);
      i++;
    }

    double[][] explorationCost = new double[taskSize][];
    for (int taskIndex = 0; taskIndex < taskSize; taskIndex++) {
      explorationCost[taskIndex] = new double[satSize + 1];
    }

    for (SatModel sat : data.keySet()) {
      Multimap<Task, PredictedDataElement> observation = data.get(sat);
      for (satteliteExplorer.db.entities.Task task : observation.keys()) {
        Collection<PredictedDataElement> elements = observation.get(task);

        double cost = 0;
        for (PredictedDataElement element : elements) {
          if (element.date.after(task.getStart()) && element.date.before(task.getFinish())) {
            cost = task.getCost();
            break;
          } else {
            cost = task.getCost() / 4;
          }
        }
        explorationCost[taskIndexes.get(task)][satIndexes.get(sat.getSat())] = cost;
      }
    }

    for (int j = 0; j < explorationCost.length; j++) {
      double sum = 0;
      for (int k = 0; k < explorationCost[j].length; k++) {
        sum += explorationCost[j][k];
      }
      if (sum < 0.00001) {
        explorationCost[j][satSize] = 1;
      }
    }

    OptimizationServer optimizationServer = app.optimizationServer;
    int[] result = null;
    try {
      result = optimizationServer.solve(satSize, taskSize, explorationCost, "genetic");
    } catch (Exception exc) {
      System.out.println(exc.toString());
    }

    XYSeriesCollection dataset = new XYSeriesCollection();
    XYSeries[] serieses = new XYSeries[satSize + 1];
    for (int j = 0; j < serieses.length; j++) {
      serieses[j] = new XYSeries(j);
    }

    double[][] d = new double[3][result.length];
    for (int j = 0; j < result.length; j++) {
      Task task = (Task) taskList.get(j);
      serieses[result[j]].add(task.getRegion().getLongitude() * FastMath.RAD_TO_DEG,
          task.getRegion().getLatitude() * FastMath.RAD_TO_DEG);
    }

    for (XYSeries series : serieses) {
      dataset.addSeries(series);
    }
    return dataset;
  }

  private XYPlot createPlot(XYDataset dataset) {
    NumberAxis xAxis = new NumberAxis("Долгота");
    xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    xAxis.setLowerMargin(0.0);
    xAxis.setUpperMargin(0.0);
    NumberAxis yAxis = new NumberAxis("Широта");
    yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    yAxis.setLowerMargin(0.0);
    yAxis.setUpperMargin(0.0);
    XYShapeRenderer renderer = new XYShapeRenderer();

    XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
    plot.setBackgroundImage(background);
    plot.setDomainGridlinesVisible(false);
    plot.setRangeGridlinePaint(Color.white);

    return plot;
  }

  private XYPlot createPlot2(DefaultXYZDataset dataset) {
    NumberAxis xAxis = new NumberAxis("Долгота");
    xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    xAxis.setLowerMargin(0.0);
    xAxis.setUpperMargin(0.0);
    NumberAxis yAxis = new NumberAxis("Широта");
    yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    yAxis.setLowerMargin(0.0);
    yAxis.setUpperMargin(0.0);
    XYBlockRenderer renderer = new XYBlockRenderer();

    LookupPaintScale scale = new LookupPaintScale(0, 1.0, Color.lightGray);
    scale.add(0, Color.red);
    scale.add(0.25, Color.orange);
    scale.add(0.5, Color.yellow);
    scale.add(0.75, new Color(200, 255, 0));
    scale.add(1.0, Color.green);
    renderer.setPaintScale(scale);
    renderer.setBlockHeight(1);
    renderer.setBlockWidth(1);
    XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
    plot.setBackgroundImage(background);
//    plot.setBackgroundPaint(Color.white);
    plot.setDomainGridlinesVisible(false);
    plot.setRangeGridlinePaint(Color.white);

    return plot;
  }

  private JFreeChart createChart(XYDataset dataset) {
    XYPlot plot = createPlot(dataset);

    JFreeChart chart = new JFreeChart("Карта наблюдаемости", plot);
//    chart.removeLegend();
    chart.setBackgroundPaint(Color.white);

    return chart;
  }

  private JFreeChart createChart(DefaultXYZDataset dataset) {
    XYPlot plot = createPlot2(dataset);

    JFreeChart chart = new JFreeChart("Карта наблюдаемости", plot);
    chart.removeLegend();
    chart.setBackgroundPaint(Color.white);

    return chart;
  }
}
