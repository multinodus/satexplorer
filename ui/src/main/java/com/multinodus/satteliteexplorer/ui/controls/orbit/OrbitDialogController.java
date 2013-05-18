package com.multinodus.satteliteexplorer.ui.controls.orbit;

import com.multinodus.satteliteexplorer.db.EntityContext;
import com.multinodus.satteliteexplorer.db.entities.Orbit;
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

public class OrbitDialogController implements Controller {
  private Screen screen;
  private TextField semiMajorAxisTextField;
  private TextField eccentricityTextField;
  private TextField inclinationTextField;
  private TextField longitudeOfAscendingNodeTextField;
  private TextField argumentOfPericenterTextField;

  private Button addButton;
  private Button removeButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private List<Object> orbits;
  private List<JustAnExampleModelClass> dropDownItems;


  @SuppressWarnings("unchecked")
  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.screen = screen;
    dropDown = screen.findNiftyControl("dropDownOrbit", DropDown.class);
    semiMajorAxisTextField = screen.findNiftyControl("semiMajorAxis", TextField.class);
    eccentricityTextField = screen.findNiftyControl("eccentricity", TextField.class);
    inclinationTextField = screen.findNiftyControl("inclination", TextField.class);
    longitudeOfAscendingNodeTextField = screen.findNiftyControl("longitudeOfAscendingNode", TextField.class);
    argumentOfPericenterTextField = screen.findNiftyControl("argumentOfPericenter", TextField.class);

    addButton = screen.findNiftyControl("addButtonOrbit", Button.class);
    removeButton = screen.findNiftyControl("removeButtonOrbit", Button.class);
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

  @NiftyEventSubscriber(id = "dropDownOrbit")
  public void onDropDownSelectionChanged(final String id, final DropDownSelectionChangedEvent<JustAnExampleModelClass> event) {
    if (event.getSelection() == null) {
      clearTextField();
    } else {
      fillTextField((Orbit) orbits.get(event.getSelectionItemIndex()));
    }
  }

  @NiftyEventSubscriber(id = "addButtonOrbit")
  public void onAddButtonClicked(final String id, final ButtonClickedEvent event) {
    Orbit orbit = getOrbit();
    if (orbit != null) {
      EntityContext.get().createEntity(orbit, Orbit.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "removeButtonOrbit")
  public void onRemoveButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    if (index >= 0) {
      EntityContext.get().deleteEntity(orbits.get(index), Orbit.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "updateButtonOrbit")
  public void onUpdateButtonClicked(final String id, final ButtonClickedEvent event) {
    Orbit orbit = getOrbit();
    if (orbit != null) {
      EntityContext.get().updateEntity(orbit, Orbit.class);
    }
    update();
  }

  private void update() {
    orbits = EntityContext.get().getAllEntities(Orbit.class);

    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : orbits) {
      dropDownItems.add(new JustAnExampleModelClass(((Orbit) o).getOrbitId().toString()));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    if (orbits.size() > 0) {
      dropDown.selectItemByIndex(0);
      fillTextField((Orbit) orbits.get(0));
    }
  }

  private void clearTextField() {
    semiMajorAxisTextField.setText("");
    eccentricityTextField.setText("");
    inclinationTextField.setText("");
    longitudeOfAscendingNodeTextField.setText("");
    argumentOfPericenterTextField.setText("");
  }

  private void fillTextField(Orbit orbit) {
    semiMajorAxisTextField.setText(orbit.getSemiMajorAxis().toString());
    eccentricityTextField.setText(orbit.getEccentricity().toString());
    inclinationTextField.setText(orbit.getInclination().toString());
    longitudeOfAscendingNodeTextField.setText(orbit.getLongitudeOfAscendingNode().toString());
    argumentOfPericenterTextField.setText(orbit.getLongitudeOfAscendingNode().toString());
  }

  private Orbit getOrbit() {
    try {
      Orbit orbit = dropDown.getSelectedIndex() > -1 ? (Orbit) orbits.get(dropDown.getSelectedIndex()) : new Orbit();
      orbit.setSemiMajorAxis(Float.parseFloat(semiMajorAxisTextField.getText()));
      orbit.setEccentricity(Float.parseFloat(eccentricityTextField.getText()));
      orbit.setInclination(Float.parseFloat(inclinationTextField.getText()));
      orbit.setLongitudeOfAscendingNode(Float.parseFloat(longitudeOfAscendingNodeTextField.getText()));
      orbit.setArgumentOfPericenter(Float.parseFloat(argumentOfPericenterTextField.getText()));
      return orbit;
    } catch (Exception exc) {
      return null;
    }
  }
}
