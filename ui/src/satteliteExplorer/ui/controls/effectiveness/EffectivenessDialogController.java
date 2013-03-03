package satteliteExplorer.ui.controls.effectiveness;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import satteliteExplorer.db.EntityContext;
import satteliteExplorer.db.entities.Effectiveness;
import satteliteExplorer.ui.controls.common.JustAnExampleModelClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The UserDialogController.
 *
 * @author void
 */
public class EffectivenessDialogController implements Controller {
  private Screen screen;
  private TextField effectivenessDescriptorTextField;

  private Button addButton;
  private Button removeButton;
  private Button updateButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private List<Object> effectivenesss;
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
    dropDown = screen.findNiftyControl("dropDownEffectiveness", DropDown.class);
    effectivenessDescriptorTextField = screen.findNiftyControl("effectivenessDescription", TextField.class);

    addButton = screen.findNiftyControl("addButtonEffectiveness", Button.class);
    removeButton = screen.findNiftyControl("removeButtonEffectiveness", Button.class);
    updateButton = screen.findNiftyControl("updateButtonEffectiveness", Button.class);
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

  @NiftyEventSubscriber(id = "dropDownEffectiveness")
  public void onDropDownSelectionChanged(final String id, final DropDownSelectionChangedEvent<JustAnExampleModelClass> event) {
    if (event.getSelection() == null) {
      clearTextField();
    } else {
      fillTextField((Effectiveness) effectivenesss.get(event.getSelectionItemIndex()));
    }
  }

  @NiftyEventSubscriber(id = "addButtonEffectiveness")
  public void onAddButtonClicked(final String id, final ButtonClickedEvent event) {
    Effectiveness effectiveness = getEffectiveness();
    if (effectiveness != null) {
      EntityContext.get().createEntity(effectiveness, Effectiveness.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "removeButtonEffectiveness")
  public void onRemoveButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    if (index >= 0) {
      EntityContext.get().deleteEntity(effectivenesss.get(index), Effectiveness.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "updateButtonEffectiveness")
  public void onUpdateButtonClicked(final String id, final ButtonClickedEvent event) {
    Effectiveness effectiveness = getEffectiveness();
    if (effectiveness != null) {
      EntityContext.get().updateEntity(effectiveness, Effectiveness.class);
    }
    update();
  }

  private void update() {
    effectivenesss = EntityContext.get().getAllEntities(Effectiveness.class);

    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : effectivenesss) {
      dropDownItems.add(new JustAnExampleModelClass(((Effectiveness) o).getDescription()));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    if (effectivenesss.size() > 0) {
      dropDown.selectItemByIndex(0);
      fillTextField((Effectiveness) effectivenesss.get(0));
    }
  }

  private void clearTextField() {
    effectivenessDescriptorTextField.setText("");
  }

  private void fillTextField(Effectiveness effectiveness) {
    effectivenessDescriptorTextField.setText(effectiveness.getDescription());
  }

  private Effectiveness getEffectiveness() {
    try {
      Effectiveness effectiveness = dropDown.getSelectedIndex() > -1 ? (Effectiveness) effectivenesss.get(dropDown.getSelectedIndex()) : new Effectiveness();
      effectiveness.setDescription(effectivenessDescriptorTextField.getText());
      return effectiveness;
    } catch (Exception exc) {
      return null;
    }
  }
}
