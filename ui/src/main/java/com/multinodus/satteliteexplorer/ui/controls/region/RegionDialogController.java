package com.multinodus.satteliteexplorer.ui.controls.region;

import com.multinodus.satteliteexplorer.db.EntityContext;
import com.multinodus.satteliteexplorer.db.entities.Region;
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

public class RegionDialogController implements Controller {
  private Screen screen;
  private TextField radiusTextField;
  private TextField longitudeTextField;
  private TextField latitudeTextField;

  private Button addButton;
  private Button removeButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private List<Object> regions;
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
    dropDown = screen.findNiftyControl("dropDownRegion", DropDown.class);
    radiusTextField = screen.findNiftyControl("radius", TextField.class);
    longitudeTextField = screen.findNiftyControl("longitude", TextField.class);
    latitudeTextField = screen.findNiftyControl("latitude", TextField.class);

    addButton = screen.findNiftyControl("addButtonRegion", Button.class);
    removeButton = screen.findNiftyControl("removeButtonRegion", Button.class);
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

  @NiftyEventSubscriber(id = "dropDownRegion")
  public void onDropDownSelectionChanged(final String id, final DropDownSelectionChangedEvent<JustAnExampleModelClass> event) {
    if (event.getSelection() == null) {
      clearTextField();
    } else {
      fillTextField((Region) regions.get(event.getSelectionItemIndex()));
    }
  }

  @NiftyEventSubscriber(id = "addButtonRegion")
  public void onAddButtonClicked(final String id, final ButtonClickedEvent event) {
    Region region = getRegion();
    if (region != null) {
      EntityContext.get().createEntity(region, Region.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "removeButtonRegion")
  public void onRemoveButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    if (index >= 0) {
      EntityContext.get().deleteEntity(regions.get(index), Region.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "updateButtonRegion")
  public void onUpdateButtonClicked(final String id, final ButtonClickedEvent event) {
    Region region = getRegion();
    if (region != null) {
      EntityContext.get().updateEntity(region, Region.class);
    }
    update();
  }

  private void update() {
    regions = EntityContext.get().getAllEntities(Region.class);

    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : regions) {
      dropDownItems.add(new JustAnExampleModelClass(((Region) o).getRegionId().toString()));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    if (regions.size() > 0) {
      dropDown.selectItemByIndex(0);
      fillTextField((Region) regions.get(0));
    }
  }

  private void clearTextField() {
    radiusTextField.setText("");
    longitudeTextField.setText("");
    latitudeTextField.setText("");
  }

  private void fillTextField(Region region) {
    radiusTextField.setText(region.getRadius().toString());
    longitudeTextField.setText(region.getLongitude().toString());
    latitudeTextField.setText(region.getLatitude().toString());
  }

  private Region getRegion() {
    try {
      Region region = dropDown.getSelectedIndex() > -1 ? (Region) regions.get(dropDown.getSelectedIndex()) : new Region();
      region.setRadius(Float.parseFloat(radiusTextField.getText()));
      region.setLongitude(Float.parseFloat(longitudeTextField.getText()));
      region.setLatitude(Float.parseFloat(latitudeTextField.getText()));
      return region;
    } catch (Exception exc) {
      return null;
    }
  }
}
