package satteliteExplorer.ui.scene;

import com.google.common.collect.Multimap;
import com.jme3.math.FastMath;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import satteliteExplorer.db.entities.Task;
import satteliteExplorer.scheduler.models.SatModel;
import satteliteExplorer.scheduler.transformations.PredictedDataElement;
import satteliteExplorer.scheduler.transformations.PredictorOfObservations;
import satteliteExplorer.scheduler.transformations.SI_Transform;
import satteliteExplorer.scheduler.util.DateTimeConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
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
  private Image background;

  public QualityChart() {
    try {
      File file = new File(System.getProperty("user.dir") + "/assets/Textures/earth_mini.jpg");
      background = ImageIO.read(file);
    } catch (IOException e) {
      System.out.println(e.toString());
    }

    DefaultXYZDataset dataset = createDataset();
    JFreeChart chart = createChart(dataset);

    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    setContentPane(chartPanel);
  }

  private DefaultXYZDataset createDataset() {
    DefaultXYZDataset dataset = new DefaultXYZDataset();

    PredictorOfObservations predictorOfObservations = PredictorOfObservations.getInstance();
    Map<SatModel, Multimap<Task, PredictedDataElement>> allData = predictorOfObservations.observe(SI_Transform.INITIAL_TIME,
        new Date(SI_Transform.INITIAL_TIME.getTime() + DateTimeConstants.MSECS_IN_DAY), PlanetSimpleTest.scene.getWorld().getTasks(),
        PlanetSimpleTest.scene.getWorld().getSatModels(), 0.1f);

    int j = 0;
    for (SatModel sat : allData.keySet()){
      Multimap<Task, PredictedDataElement> observation = allData.get(sat);
      int i = 0;
      double[][] data = new double[3][observation.keys().size()];
      for (satteliteExplorer.db.entities.Task task : observation.keys()) {
        Collection<PredictedDataElement> elements = observation.get(task);

        data[0][i] = task.getRegion().getLongitude()* FastMath.RAD_TO_DEG;
        data[1][i] = task.getRegion().getLatitude()* FastMath.RAD_TO_DEG;

        boolean explored = false;
        for (PredictedDataElement element : elements) {
//          if (element.date.after(task.getStart()) && element.date.before(task.getFinish())){
            data[2][i] = 1;
            explored = true;
            break;
//          }
        }
        if (!explored) {
          data[2][i] = 0;
        }
        i++;
      }
      dataset.addSeries(j, data);
      j++;
      break;
    }

    return dataset;
  }

  private XYPlot createPlot(DefaultXYZDataset dataset) {
    NumberAxis xAxis = new NumberAxis("Долгота");
    xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    xAxis.setLowerMargin(0.0);
    xAxis.setUpperMargin(0.0);
    NumberAxis yAxis = new NumberAxis("Широта");
    yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    yAxis.setLowerMargin(0.0);
    yAxis.setUpperMargin(0.0);
    XYBlockRenderer renderer = new XYBlockRenderer();

    LookupPaintScale scale = new LookupPaintScale(0,1.0,Color.lightGray);
    scale.add(0, Color.red);
    scale.add(0.25, Color.orange);
    scale.add(0.5, Color.yellow);
    scale.add(0.75, new Color(125, 255, 0));
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

  private JFreeChart createChart(DefaultXYZDataset dataset) {
    XYPlot plot = createPlot(dataset);

    JFreeChart chart = new JFreeChart("Карта наблюдаемости", plot);
    chart.removeLegend();
    chart.setBackgroundPaint(Color.white);

    return chart;
  }
}
