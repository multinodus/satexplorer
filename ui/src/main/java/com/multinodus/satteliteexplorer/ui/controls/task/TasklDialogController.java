package com.multinodus.satteliteexplorer.ui.controls.task;

import com.multinodus.satteliteexplorer.db.EntityContext;
import com.multinodus.satteliteexplorer.db.entities.Equipment;
import com.multinodus.satteliteexplorer.db.entities.Region;
import com.multinodus.satteliteexplorer.db.entities.Task;
import com.multinodus.satteliteexplorer.ui.controls.common.JustAnExampleModelClass;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The ChatControlDialogController registers a new control with Nifty
 * that represents the whole Dialog. This gives us later an appropriate
 * ControlBuilder to actual construct the Dialog (as a control).
 *
 * @author void
 */
public class TasklDialogController implements Controller {
  private Screen screen;

  private Button addButton;
  private Button removeButton;
  private Button updateButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private List<Object> tasks;
  private List<JustAnExampleModelClass> dropDownItems;

  private DropDown<JustAnExampleModelClass> regionDropDown;
  private List<Object> region;
  private List<JustAnExampleModelClass> dropDownItemsRegion;

  private DropDown<JustAnExampleModelClass> equipmentDropDown;
  private List<Object> equipment;
  private List<JustAnExampleModelClass> dropDownItemsEquipment;


  @SuppressWarnings("unchecked")
  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.screen = screen;
    dropDown = screen.findNiftyControl("dropDownTask", DropDown.class);
    regionDropDown = screen.findNiftyControl("dropDownRegionForeign", DropDown.class);
    regionDropDown = screen.findNiftyControl("dropDownEquipmentForeign", DropDown.class);

    addButton = screen.findNiftyControl("addButtonTask", Button.class);
    removeButton = screen.findNiftyControl("removeButtonTask", Button.class);
    updateButton = screen.findNiftyControl("updateButtonTask", Button.class);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    update();
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

  @NiftyEventSubscriber(id = "dropDownTask")
  public void onDropDownSelectionChanged(final String id, final DropDownSelectionChangedEvent<JustAnExampleModelClass> event) {
    if (event.getSelection() == null) {
      clearTextField();
    } else {
      fillTextField((Task) tasks.get(event.getSelectionItemIndex()));
    }
  }

  @NiftyEventSubscriber(id = "addButtonTask")
  public void onAddButtonClicked(final String id, final ButtonClickedEvent event) {
    Task task = getTask();
    if (task != null) {
      EntityContext.get().createEntity(task, Task.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "removeButtonTask")
  public void onRemoveButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    if (index >= 0) {
      EntityContext.get().deleteEntity(tasks.get(index), Task.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "updateButtonTask")
  public void onUpdateButtonClicked(final String id, final ButtonClickedEvent event) {
    Task task = getTask();
    if (task != null) {
      EntityContext.get().updateEntity(task, Task.class);
    }
    update();
  }

  private void update() {
    tasks = EntityContext.get().getAllEntities(Task.class);

    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : tasks) {
      dropDownItems.add(new JustAnExampleModelClass(((Task) o).getTaskId().toString()));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    if (tasks.size() > 0) {
      dropDown.selectItemByIndex(0);
      fillTextField((Task) tasks.get(0));
    }
  }

  private void updateRegion() {
    region = EntityContext.get().getAllEntities(Region.class);

    dropDownItemsRegion = new ArrayList<JustAnExampleModelClass>();
    for (Object o : region) {
      dropDownItemsRegion.add(new JustAnExampleModelClass(((Region) o).getRegionId().toString()));
    }
    regionDropDown.clear();
    regionDropDown.addAllItems(dropDownItemsRegion);
    if (region.size() > 0) {
      regionDropDown.selectItemByIndex(0);
    }
  }

  private void updateEquipment() {
    equipment = EntityContext.get().getAllEntities(Equipment.class);

    dropDownItemsEquipment = new ArrayList<JustAnExampleModelClass>();
    for (Object o : equipment) {
      dropDownItemsEquipment.add(new JustAnExampleModelClass(((Equipment) o).getEquipmentId().toString()));
    }
    equipmentDropDown.clear();
    equipmentDropDown.addAllItems(dropDownItemsEquipment);
    if (equipment.size() > 0) {
      equipmentDropDown.selectItemByIndex(0);
    }
  }

  private void clearTextField() {

  }

  private void fillTextField(Task task) {

  }

  private Task getTask() {
    try {
      Task task = dropDown.getSelectedIndex() > -1 ? (Task) tasks.get(dropDown.getSelectedIndex()) : new Task();
      return task;
    } catch (Exception exc) {
      return null;
    }
  }
}
