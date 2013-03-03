package satteliteExplorer.ui.controls.equipment;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import satteliteExplorer.db.EntityContext;
import satteliteExplorer.db.entities.Equipment;
import satteliteExplorer.db.entities.EquipmentType;
import satteliteExplorer.ui.controls.common.JustAnExampleModelClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The ListBoxDialog to show off the new ListBox and a couple of more new Nifty 1.3 things.
 *
 * @author void
 */
public class EquipmentDialogController implements Controller {
  private Screen screen;
  private TextField speedTextField;
  private TextField delayTextField;
  private DropDown<JustAnExampleModelClass> equipmentTypeDropDown;

  private Button addButton;
  private Button removeButton;
  private Button updateButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private List<Object> equipments;
  private List<JustAnExampleModelClass> dropDownItems;

  private List<Object> equipmentType;
  private List<JustAnExampleModelClass> dropDownItemsEquipmentType;


  @SuppressWarnings("unchecked")
  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.screen = screen;
    dropDown = screen.findNiftyControl("dropDownEquipment", DropDown.class);
    speedTextField = screen.findNiftyControl("speed", TextField.class);
    delayTextField = screen.findNiftyControl("delay", TextField.class);
    equipmentTypeDropDown = screen.findNiftyControl("dropDownEquipmentTypeEquipmentForeign", DropDown.class);

    addButton = screen.findNiftyControl("addButtonEquipment", Button.class);
    removeButton = screen.findNiftyControl("removeButtonEquipment", Button.class);
    updateButton = screen.findNiftyControl("updateButtonEquipment", Button.class);
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

  @NiftyEventSubscriber(id = "dropDownEquipment")
  public void onDropDownSelectionChanged(final String id, final DropDownSelectionChangedEvent<JustAnExampleModelClass> event) {
    if (event.getSelection() == null) {
      clearTextField();
    } else {
      fillTextField((Equipment) equipments.get(event.getSelectionItemIndex()));
    }
  }

  @NiftyEventSubscriber(id = "addButtonEquipment")
  public void onAddButtonClicked(final String id, final ButtonClickedEvent event) {
    Equipment equipment = getEquipment();
    if (equipment != null) {
      EntityContext.get().createEntity(equipment, Equipment.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "removeButtonEquipment")
  public void onRemoveButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    if (index >= 0) {
      EntityContext.get().deleteEntity(equipments.get(index), Equipment.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "updateButtonEquipment")
  public void onUpdateButtonClicked(final String id, final ButtonClickedEvent event) {
    Equipment equipment = getEquipment();
    if (equipment != null) {
      EntityContext.get().updateEntity(equipment, Equipment.class);
    }
    update();
  }

  private void update() {
    equipments = EntityContext.get().getAllEntities(Equipment.class);

    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : equipments) {
      dropDownItems.add(new JustAnExampleModelClass(((Equipment) o).getEquipmentId().toString()));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    if (equipments.size() > 0) {
      dropDown.selectItemByIndex(0);
      fillTextField((Equipment) equipments.get(0));
    }
  }

  private void updateEquipmentType() {
    equipmentType = EntityContext.get().getAllEntities(EquipmentType.class);

    dropDownItemsEquipmentType = new ArrayList<JustAnExampleModelClass>();
    for (Object o : equipmentType) {
      dropDownItemsEquipmentType.add(new JustAnExampleModelClass(((EquipmentType) o).getName()));
    }
    equipmentTypeDropDown.clear();
    equipmentTypeDropDown.addAllItems(dropDownItemsEquipmentType);
    if (equipmentType.size() > 0) {
      equipmentTypeDropDown.selectItemByIndex(0);
    }
  }

  private void clearTextField() {
    speedTextField.setText("");
    delayTextField.setText("");
  }

  private void fillTextField(Equipment equipment) {
    speedTextField.setText(equipment.getSpeed().toString());
    delayTextField.setText(equipment.getDelay().toString());
  }

  private Equipment getEquipment() {
    try {
      Equipment equipment = dropDown.getSelectedIndex() > -1 ? (Equipment) equipments.get(dropDown.getSelectedIndex()) : new Equipment();
      equipment.setSpeed(Float.parseFloat(speedTextField.getText()));
      equipment.setDelay(Long.parseLong(delayTextField.getText()));
      return equipment;
    } catch (Exception exc) {
      return null;
    }
  }
}
