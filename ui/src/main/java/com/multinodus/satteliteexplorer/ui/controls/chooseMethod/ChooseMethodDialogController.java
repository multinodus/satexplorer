package com.multinodus.satteliteexplorer.ui.controls.chooseMethod;

import com.multinodus.satteliteexplorer.scheduler.optimizations.IKnapsackData;
import com.multinodus.satteliteexplorer.scheduler.optimizations.OptimizationServer;
import com.multinodus.satteliteexplorer.scheduler.transformations.PredictorOfObservations;
import com.multinodus.satteliteexplorer.ui.controls.common.ImagePanelDefinition;
import com.multinodus.satteliteexplorer.ui.controls.common.JustAnExampleModelClass;
import com.multinodus.satteliteexplorer.ui.controls.schedulingProcess.SchedulingProcessDefinition;
import com.multinodus.satteliteexplorer.ui.scene.SchedulingProcessChart;
import com.multinodus.satteliteexplorer.ui.scene.UIApplication;
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
      IKnapsackData knapsackData = PredictorOfObservations.getInstance().getKnapsackData(UIApplication.app.scene.getWorld(),
          UIApplication.app.getHoursHorizont());
      optimizationServer.solve(knapsackData, UIApplication.app.getMethod());

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
      changeImage(schedulingProcessElement);
    } catch (Exception exc) {
      System.out.println(exc.toString());
    }
  }

  public void changeImage(Element schedulingProcessElement) {
    NiftyImage newImage = nifty.getRenderEngine().createImage("Textures/new_chart.png", false);

    Element element = schedulingProcessElement.findElementByName("schedulingProcessImg");

    element.getRenderer(ImageRenderer.class).setImage(newImage);
  }
}
