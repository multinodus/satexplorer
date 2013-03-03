package satteliteExplorer.ui.scene;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import satteliteExplorer.scheduler.models.SatModel;
import satteliteExplorer.scheduler.transformations.PredictorDataElement;
import satteliteExplorer.scheduler.transformations.PredictorOfObservations;
import satteliteExplorer.scheduler.transformations.SI_Transform;
import satteliteExplorer.scheduler.util.DateTimeConstants;
import satteliteExplorer.scheduler.util.Pair;

import javax.swing.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 19.12.12
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleChart extends JFrame {
  public ScheduleChart() {
    final IntervalCategoryDataset dataset = createDataset();
    final JFreeChart chart = createChart(dataset);

    // add the chart to a panel...
    final ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    setContentPane(chartPanel);
  }

  private Date date(final int day, final int month, final int year) {

    final Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);
    final Date result = calendar.getTime();
    return result;

  }

  private JFreeChart createChart(final IntervalCategoryDataset dataset) {
    final JFreeChart chart = ChartFactory.createGanttChart(
        "Распределение задач",  // chart title
        "Задачи",              // domain axis label
        "Время",              // range axis label
        dataset,             // data
        true,                // include legend
        true,                // tooltips
        false                // urls
    );
//        chart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(10.0f);
    return chart;
  }

  private IntervalCategoryDataset createDataset() {
    Map<satteliteExplorer.db.entities.Task, Pair<SatModel, List<PredictorDataElement>>> schedule =
        new HashMap<satteliteExplorer.db.entities.Task, Pair<SatModel, List<PredictorDataElement>>>();
    for (satteliteExplorer.db.entities.Task task : PlanetSimpleTest.scene.getWorld().getTasks()) {
      PredictorOfObservations predictorOfObservations = PredictorOfObservations.getInstance();
      Pair<SatModel, List<PredictorDataElement>> data = predictorOfObservations.BestObservers(SI_Transform.INITIAL_TIME,
          new Date(SI_Transform.INITIAL_TIME.getTime() + 7 * DateTimeConstants.MSECS_IN_DAY), task.getRegion(), 0.01f,
          PlanetSimpleTest.scene.getWorld().getSatModels());
      schedule.put(task, data);
    }
    return createDataset(schedule);
  }

  private IntervalCategoryDataset createDataset(Map<satteliteExplorer.db.entities.Task, Pair<SatModel, List<PredictorDataElement>>> data) {
    final TaskSeriesCollection collection = new TaskSeriesCollection();

    for (satteliteExplorer.db.entities.Task task : data.keySet()) {
      Pair<SatModel, List<PredictorDataElement>> p = data.get(task);

      final TaskSeries s = new TaskSeries("Задача №" + task.getTaskId() + "/Исполнитель:Спутник №" + p.f.getSat().getSatId());

      Date startTime = null;
      Date finishTime = null;
      for (PredictorDataElement element : p.s) {
        if (element.angle < element.visibleAngle) {
          if (startTime == null) {
            startTime = element.date;
            finishTime = element.date;
          } else {
            finishTime = element.date;
          }
        } else {
          if (finishTime != null) {
            s.add(new Task("", new SimpleTimePeriod(startTime, finishTime)));
          }

          startTime = null;
          finishTime = null;
        }
      }

      collection.add(s);
    }

    return collection;
  }
}
