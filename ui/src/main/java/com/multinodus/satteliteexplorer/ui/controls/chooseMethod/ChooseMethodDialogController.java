package com.multinodus.satteliteexplorer.ui.controls.chooseMethod;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.multinodus.satteliteexplorer.db.EntityContext;
import com.multinodus.satteliteexplorer.db.entities.Task;
import com.multinodus.satteliteexplorer.scheduler.models.SatModel;
import com.multinodus.satteliteexplorer.scheduler.optimizations.IKnapsackData;
import com.multinodus.satteliteexplorer.scheduler.optimizations.OptimizationServer;
import com.multinodus.satteliteexplorer.scheduler.transformations.PredictedDataElement;
import com.multinodus.satteliteexplorer.scheduler.transformations.PredictorOfObservations;
import com.multinodus.satteliteexplorer.scheduler.util.Pair;
import com.multinodus.satteliteexplorer.scheduler.util.Triple;
import com.multinodus.satteliteexplorer.ui.controls.analysis.AnalysisDefinition;
import com.multinodus.satteliteexplorer.ui.controls.common.ImagePanelDefinition;
import com.multinodus.satteliteexplorer.ui.controls.common.JustAnExampleModelClass;
import com.multinodus.satteliteexplorer.ui.controls.schedulingProcess.SchedulingProcessDefinition;
import com.multinodus.satteliteexplorer.ui.scene.*;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ChooseMethodDialogController implements Controller {
  private Screen screen;
  private TextField timeoutTextField;
  private TextField horizontTextField;
  private Nifty nifty;

  private Button okButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private static String[] methods = {"Genetic algorithm", "Branch&Bound", "Adopt"};
  private static String[] nativeMethods = {"genetic", "ilp", "adopt"};
  private List<JustAnExampleModelClass> dropDownItems;

  public ChooseMethodDialogController(){
  }

  @SuppressWarnings("unchecked")
  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.screen = screen;
    this.nifty = nifty;
    dropDown = screen.findNiftyControl("dropDownChooseMethod", DropDown.class);
    timeoutTextField = screen.findNiftyControl("timeout", TextField.class);
    horizontTextField = screen.findNiftyControl("horizont", TextField.class);

    okButton = screen.findNiftyControl("okButtonOrbit", Button.class);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (String method : methods) {
      dropDownItems.add(new JustAnExampleModelClass(method));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    dropDown.selectItemByIndex(0);

    timeoutTextField.setText("60");
    horizontTextField.setText("40");
  }

  @Override
  public void onStartScreen() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void onFocus(boolean b) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean inputEvent(NiftyInputEvent niftyInputEvent) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @NiftyEventSubscriber(id = "okButtonOrbit")
  public void onOkButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    String method = nativeMethods[index];

    int horizont = Integer.parseInt(horizontTextField.getText());
    int timeout = Integer.parseInt(timeoutTextField.getText());

    UIApplication.app.setHoursHorizont(horizont);
    UIApplication.app.setTimeout(timeout);
    UIApplication.app.setMethod(method);

    this.screen.findElementByName(ChooseMethodDialogControlDefinition.NAME).hide();
    Element schedulingProcessElement = this.screen.findElementByName(SchedulingProcessDefinition.NAME);
    schedulingProcessElement.show();

    startScheduling(schedulingProcessElement);
  }

  public void startScheduling(Element schedulingProcessElement) {
    OptimizationServer optimizationServer = UIApplication.app.optimizationServer;
    int[] result = null;
    try {
      Pair<IKnapsackData, List<Pair<SatModel, List<Pair<Task, PredictedDataElement>>>>>  knapsackData = PredictorOfObservations.getInstance().getKnapsackData(UIApplication.app.scene.getWorld(),
          UIApplication.app.getHoursHorizont());
      int[] schedule = optimizationServer.solve(knapsackData.f, UIApplication.app.getMethod());

      List<Object> taskList = EntityContext.get().getAllEntities(Task.class);
      Map<Task, Pair<SatModel, PredictedDataElement>> detailedSchedule = Maps.newHashMap();
      for (int i = 0; i < schedule.length; i++){
        Task task = (Task)taskList.get(i);
        SatModel satModel = null;
        PredictedDataElement predictedDataElement = null;
        Pair<SatModel, List<Pair<Task, PredictedDataElement>>> p = schedule[i] < knapsackData.s.size() ? knapsackData.s.get(schedule[i]) : null;
        if (p!=null){
          satModel = p.f;
          for (Pair<Task, PredictedDataElement> exploration : p.s){
            if (exploration.f == task){
              predictedDataElement = exploration.s;
              break;
            }
          }
        }
        detailedSchedule.put(task, new Pair<SatModel, PredictedDataElement>(satModel, predictedDataElement));
      }

      Multimap<Integer, Task> exploredTasks = ArrayListMultimap.create();

      int[] episodeTaskCount = new int[knapsackData.s.size() + 1];
      for (int i = 0; i < schedule.length; i++){
        episodeTaskCount[schedule[i]] += 1;
        exploredTasks.put(schedule[i], (Task)taskList.get(i));
      }

      updateCharts(knapsackData.s, exploredTasks, episodeTaskCount, taskList, schedule, knapsackData.f);
    } catch (Exception exc) {
      System.out.println(exc.toString());
    }
  }

  public void updateCharts(List<Pair<SatModel, List<Pair<Task, PredictedDataElement>>>> episodes, Multimap<Integer, Task> exploredTasks,
                           int[] episodeTaskCount, List<Object> taskList, int[] schedule, IKnapsackData knapsackData) {
    SchedulingProcessChart chart = new SchedulingProcessChart();
    chart.addValue(1);
    chart.addValue(3);
    chart.addValue(13);
    chart.addValue(56);
    chart.addValue(79);
    chart.addValue(92);
    chart.addValue(132);
    chart.addValue(157);
    chart.addValue(182);
    chart.addValue(193);
    chart.addValue(199);
    chart.addValue(200);
    chart.addValue(203);
    chart.saveImage();

    SatDistibutionChart satDistibutionChart = new SatDistibutionChart(episodes, episodeTaskCount);
    satDistibutionChart.saveImage();

//    SatExlorationChart satExlorationChart = new SatExlorationChart(episodes, exploredTasks, selectedSat)
//    satExlorationChart.saveImage();

    SatWorkloadChart satWorkloadChart = new SatWorkloadChart(episodes, episodeTaskCount);
    satWorkloadChart.saveImage();

    CostPieChart costPieChart = new CostPieChart(knapsackData, schedule, taskList);
    costPieChart.saveImage();

    NumberPieChart numberPieChart = new NumberPieChart(knapsackData, schedule, taskList);
    numberPieChart.saveImage();

    changeImage();
  }

  public void changeImage() {
    changeImage(SchedulingProcessDefinition.NAME, "Textures/new_chart.png", "schedulingProcessImg");
    changeImage(AnalysisDefinition.NAME, "Textures/satDistribution_chart.png", "satDistributionImg");
//    changeImage(AnalysisDefinition.NAME, "Textures/satExploration_chart.png", "satExplorationImg");
    changeImage(AnalysisDefinition.NAME, "Textures/satWorkload_chart.png", "satWorkloadImg");
    changeImage(AnalysisDefinition.NAME, "Textures/numberPie_chart.png", "numberPieImg");
    changeImage(AnalysisDefinition.NAME, "Textures/costPie_chart.png", "costPieImg");
  }

  public void changeImage(String elementName, String imagePath, String imageElementName){
    Element schedulingProcessElement = this.screen.findElementByName(elementName);
    NiftyImage newImage = nifty.getRenderEngine().createImage(imagePath, false);

    Element element = schedulingProcessElement.findElementByName(imageElementName);

    element.getRenderer(ImageRenderer.class).setImage(newImage);
  }
}
