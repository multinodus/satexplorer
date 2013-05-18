package com.multinodus.satteliteexplorer.ui.controls.sat;

import com.multinodus.satteliteexplorer.db.EntityContext;
import com.multinodus.satteliteexplorer.db.entities.Equipment;
import com.multinodus.satteliteexplorer.db.entities.Orbit;
import com.multinodus.satteliteexplorer.db.entities.Sat;
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
 * The SatController to show off the new Slider and Scrollbar Controls and a couple of more new Nifty 1.3 things.
 *
 * @author void
 */
public class SatController implements Controller {
  private Screen screen;

  private Button addButton;
  private Button removeButton;
  private Button updateButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private List<Object> sats;
  private List<JustAnExampleModelClass> dropDownItems;

  private DropDown<JustAnExampleModelClass> orbitDropDown;
  private List<Object> orbit;
  private List<JustAnExampleModelClass> dropDownItemsOrbit;

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
    dropDown = screen.findNiftyControl("dropDownSat", DropDown.class);
    orbitDropDown = screen.findNiftyControl("dropDownOrbitForeign", DropDown.class);
    orbitDropDown = screen.findNiftyControl("dropDownEquipmentSatForeign", DropDown.class);

    addButton = screen.findNiftyControl("addButtonSat", Button.class);
    removeButton = screen.findNiftyControl("removeButtonSat", Button.class);
    updateButton = screen.findNiftyControl("updateButtonSat", Button.class);
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

  @NiftyEventSubscriber(id = "dropDownSat")
  public void onDropDownSelectionChanged(final String id, final DropDownSelectionChangedEvent<JustAnExampleModelClass> event) {
    if (event.getSelection() == null) {
      clearTextField();
    } else {
      fillTextField((Sat) sats.get(event.getSelectionItemIndex()));
    }
  }

  @NiftyEventSubscriber(id = "addButtonSat")
  public void onAddButtonClicked(final String id, final ButtonClickedEvent event) {
    Sat sat = getSat();
    if (sat != null) {
      EntityContext.get().createEntity(sat, Sat.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "removeButtonSat")
  public void onRemoveButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    if (index >= 0) {
      EntityContext.get().deleteEntity(sats.get(index), Sat.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "updateButtonSat")
  public void onUpdateButtonClicked(final String id, final ButtonClickedEvent event) {
    Sat sat = getSat();
    if (sat != null) {
      EntityContext.get().updateEntity(sat, Sat.class);
    }
    update();
  }

  private void update() {
    sats = EntityContext.get().getAllEntities(Sat.class);

    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : sats) {
      dropDownItems.add(new JustAnExampleModelClass(((Sat) o).getSatId().toString()));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    if (sats.size() > 0) {
      dropDown.selectItemByIndex(0);
      fillTextField((Sat) sats.get(0));
    }
  }

  private void updateOrbit() {
    orbit = EntityContext.get().getAllEntities(Orbit.class);

    dropDownItemsOrbit = new ArrayList<JustAnExampleModelClass>();
    for (Object o : orbit) {
      dropDownItemsOrbit.add(new JustAnExampleModelClass(((Orbit) o).getOrbitId().toString()));
    }
    orbitDropDown.clear();
    orbitDropDown.addAllItems(dropDownItemsOrbit);
    if (orbit.size() > 0) {
      orbitDropDown.selectItemByIndex(0);
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

  private void fillTextField(Sat sat) {

  }

  private Sat getSat() {
    try {
      Sat sat = dropDown.getSelectedIndex() > -1 ? (Sat) sats.get(dropDown.getSelectedIndex()) : new Sat();
      return sat;
    } catch (Exception exc) {
      return null;
    }
  }
}
