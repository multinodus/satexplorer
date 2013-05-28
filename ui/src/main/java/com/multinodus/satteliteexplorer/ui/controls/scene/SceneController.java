package com.multinodus.satteliteexplorer.ui.controls.scene;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.multinodus.satteliteexplorer.db.EntityContext;
import com.multinodus.satteliteexplorer.db.entities.DataCenter;
import com.multinodus.satteliteexplorer.db.entities.Sat;
import com.multinodus.satteliteexplorer.db.entities.Task;
import com.multinodus.satteliteexplorer.scheduler.models.RegionModel;
import com.multinodus.satteliteexplorer.scheduler.models.SatModel;
import com.multinodus.satteliteexplorer.scheduler.optimizations.IKnapsackData;
import com.multinodus.satteliteexplorer.scheduler.optimizations.OptimizationServer;
import com.multinodus.satteliteexplorer.scheduler.transformations.PredictedDataElement;
import com.multinodus.satteliteexplorer.scheduler.transformations.PredictorOfObservations;
import com.multinodus.satteliteexplorer.scheduler.util.Pair;
import com.multinodus.satteliteexplorer.ui.controls.analysis.AnalysisDefinition;
import com.multinodus.satteliteexplorer.ui.controls.chooseMethod.ChooseMethodDialogControlDefinition;
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

public class SceneController implements Controller {
  private Screen screen;
  private Nifty nifty;

  private Button okButton;
  private ListBox<JustAnExampleModelClass> dataCenterListBox;
  private ListBox<JustAnExampleModelClass> satListBox;
  private ListBox<JustAnExampleModelClass> selectedDataCenterListBox;
  private ListBox<JustAnExampleModelClass> selectedSatListBox;

  private List<Object> sats;
  private List<JustAnExampleModelClass> satsItems;

  private List<Object> dataCenters;
  private List<JustAnExampleModelClass> dataCentersItems;

  public SceneController(){
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

    okButton = screen.findNiftyControl("okButtonScene", Button.class);
    dataCenterListBox = screen.findNiftyControl("dataCenterListBox", ListBox.class);
    satListBox = screen.findNiftyControl("satListBox", ListBox.class);
    selectedDataCenterListBox = screen.findNiftyControl("selectedDataCenterListBox", ListBox.class);
    selectedSatListBox = screen.findNiftyControl("selectedSatListBox", ListBox.class);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    initSatListBox();
    initDataCenterListBox();
  }

  private void initSatListBox(){
    sats = EntityContext.get().getAllEntities(Sat.class);

    satsItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : sats) {
      satsItems.add(new JustAnExampleModelClass(((Sat) o).getSatId().toString()));
    }
    satListBox.clear();
    satListBox.addAllItems(satsItems);
    if (sats.size() > 0) {
      satListBox.selectItemByIndex(0);
    }
  }

  private void initDataCenterListBox(){
    dataCenters = EntityContext.get().getAllEntities(DataCenter.class);

    dataCentersItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : dataCenters) {
      dataCentersItems.add(new JustAnExampleModelClass(((DataCenter) o).getDataCenterId().toString()));
    }
    dataCenterListBox.clear();
    dataCenterListBox.addAllItems(dataCentersItems);
    if (dataCenters.size() > 0) {
      dataCenterListBox.selectItemByIndex(0);
    }
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

  @NiftyEventSubscriber(id = "okButtonScene")
  public void onOkButtonSceneClicked(final String id, final ButtonClickedEvent event) {
    List<JustAnExampleModelClass> selectedSatItems = selectedSatListBox.getItems();
    List<JustAnExampleModelClass> selectedDataCenterItems = selectedDataCenterListBox.getItems();

    List<Sat> selectedSats = Lists.newArrayList();
    List<DataCenter> selectedDataCenters = Lists.newArrayList();

    for (JustAnExampleModelClass sat : selectedSatItems){
      int key = Integer.parseInt(sat.toString());
      Sat selectedSat = null;
      for (Object so : sats){
        Sat s = (Sat) so;
        if (s.getSatId() == key){
          selectedSat = s;
          break;
        }
      }
      selectedSats.add(selectedSat);
    }

    for (JustAnExampleModelClass dataCenter : selectedDataCenterItems){
      int key = Integer.parseInt(dataCenter.toString());
      DataCenter selectedDataCenter = null;
      for (Object o : dataCenters){
        DataCenter d = (DataCenter) o;
        if (d.getDataCenterId() == key){
          selectedDataCenter = d;
          break;
        }
      }
      selectedDataCenters.add(selectedDataCenter);
    }

    UIApplication.app.scene.createWorld(selectedSats, selectedDataCenters);
    screen.findElementByName(SceneControlDefinition.NAME).hide();
  }

  @NiftyEventSubscriber(id = "dataCenterListBox")
  public void onDataCenterListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<JustAnExampleModelClass> event) {
    List<JustAnExampleModelClass> selection = event.getSelection();
    selectedDataCenterListBox.addAllItems(selection);
    dataCenterListBox.removeAllItems(selection);
  }

  @NiftyEventSubscriber(id = "satListBox")
  public void onSatListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<JustAnExampleModelClass> event) {
    List<JustAnExampleModelClass> selection = event.getSelection();
    selectedSatListBox.addAllItems(selection);
    satListBox.removeAllItems(selection);
  }

  @NiftyEventSubscriber(id = "selectedDataCenterListBox")
  public void onSelectedDataCenterListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<JustAnExampleModelClass> event) {
    List<JustAnExampleModelClass> selection = event.getSelection();
    dataCenterListBox.addAllItems(selection);
    selectedDataCenterListBox.removeAllItems(selection);
  }

  @NiftyEventSubscriber(id = "selectedSatListBox")
  public void onSelectedSatListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<JustAnExampleModelClass> event) {
    List<JustAnExampleModelClass> selection = event.getSelection();
    satListBox.addAllItems(selection);
    selectedSatListBox.removeAllItems(selection);
  }
}
