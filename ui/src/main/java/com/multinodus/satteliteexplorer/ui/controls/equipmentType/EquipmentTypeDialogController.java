package com.multinodus.satteliteexplorer.ui.controls.equipmentType;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import com.multinodus.satteliteexplorer.db.EntityContext;
import com.multinodus.satteliteexplorer.db.entities.EquipmentType;
import com.multinodus.satteliteexplorer.ui.controls.common.JustAnExampleModelClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The UserDialogController.
 *
 * @author void
 */
public class EquipmentTypeDialogController implements Controller {
  private Screen screen;
  private TextField equipmentTypeDescriptorTextField;

  private Button addButton;
  private Button removeButton;
  private Button updateButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private List<Object> equipmentTypes;
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
    dropDown = screen.findNiftyControl("dropDownEquipmentType", DropDown.class);
    equipmentTypeDescriptorTextField = screen.findNiftyControl("equipmentTypeDescription", TextField.class);

    addButton = screen.findNiftyControl("addButtonEquipmentType", Button.class);
    removeButton = screen.findNiftyControl("removeButtonEquipmentType", Button.class);
    updateButton = screen.findNiftyControl("updateButtonEquipmentType", Button.class);
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

  @NiftyEventSubscriber(id = "dropDownEquipmentType")
  public void onDropDownSelectionChanged(final String id, final DropDownSelectionChangedEvent<JustAnExampleModelClass> event) {
    if (event.getSelection() == null) {
      clearTextField();
    } else {
      fillTextField((EquipmentType) equipmentTypes.get(event.getSelectionItemIndex()));
    }
  }

  @NiftyEventSubscriber(id = "addButtonEquipmentType")
  public void onAddButtonClicked(final String id, final ButtonClickedEvent event) {
    EquipmentType equipmentType = getEquipmentType();
    if (equipmentType != null) {
      EntityContext.get().createEntity(equipmentType, EquipmentType.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "removeButtonEquipmentType")
  public void onRemoveButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    if (index >= 0) {
      EntityContext.get().deleteEntity(equipmentTypes.get(index), EquipmentType.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "updateButtonEquipmentType")
  public void onUpdateButtonClicked(final String id, final ButtonClickedEvent event) {
    EquipmentType equipmentType = getEquipmentType();
    if (equipmentType != null) {
      EntityContext.get().updateEntity(equipmentType, EquipmentType.class);
    }
    update();
  }

  private void update() {
    equipmentTypes = EntityContext.get().getAllEntities(EquipmentType.class);

    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : equipmentTypes) {
      dropDownItems.add(new JustAnExampleModelClass(((EquipmentType) o).getName()));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    if (equipmentTypes.size() > 0) {
      dropDown.selectItemByIndex(0);
      fillTextField((EquipmentType) equipmentTypes.get(0));
    }
  }

  private void clearTextField() {
    equipmentTypeDescriptorTextField.setText("");
  }

  private void fillTextField(EquipmentType equipmentType) {
    equipmentTypeDescriptorTextField.setText(equipmentType.getName());
  }

  private EquipmentType getEquipmentType() {
    try {
      EquipmentType equipmentType = dropDown.getSelectedIndex() > -1 ? (EquipmentType) equipmentTypes.get(dropDown.getSelectedIndex()) : new EquipmentType();
      equipmentType.setName(equipmentTypeDescriptorTextField.getText());
      return equipmentType;
    } catch (Exception exc) {
      return null;
    }
  }
}
