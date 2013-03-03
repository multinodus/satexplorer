package satteliteExplorer.ui.controls.constraint;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import satteliteExplorer.db.EntityContext;
import satteliteExplorer.db.entities.Constraint;
import satteliteExplorer.ui.controls.common.JustAnExampleModelClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The UserDialogController.
 *
 * @author void
 */
public class ConstraintDialogController implements Controller {
  private Screen screen;
  private TextField constraintDescriptorTextField;

  private Button addButton;
  private Button removeButton;
  private Button updateButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private List<Object> constraints;
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
    dropDown = screen.findNiftyControl("dropDownConstraint", DropDown.class);
    constraintDescriptorTextField = screen.findNiftyControl("constraintDescription", TextField.class);

    addButton = screen.findNiftyControl("addButtonConstraint", Button.class);
    removeButton = screen.findNiftyControl("removeButtonConstraint", Button.class);
    updateButton = screen.findNiftyControl("updateButtonConstraint", Button.class);
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

  @NiftyEventSubscriber(id = "dropDownConstraint")
  public void onDropDownSelectionChanged(final String id, final DropDownSelectionChangedEvent<JustAnExampleModelClass> event) {
    if (event.getSelection() == null) {
      clearTextField();
    } else {
      fillTextField((Constraint) constraints.get(event.getSelectionItemIndex()));
    }
  }

  @NiftyEventSubscriber(id = "addButtonConstraint")
  public void onAddButtonClicked(final String id, final ButtonClickedEvent event) {
    Constraint constraint = getConstraint();
    if (constraint != null) {
      EntityContext.get().createEntity(constraint, Constraint.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "removeButtonConstraint")
  public void onRemoveButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    if (index >= 0) {
      EntityContext.get().deleteEntity(constraints.get(index), Constraint.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "updateButtonConstraint")
  public void onUpdateButtonClicked(final String id, final ButtonClickedEvent event) {
    Constraint constraint = getConstraint();
    if (constraint != null) {
      EntityContext.get().updateEntity(constraint, Constraint.class);
    }
    update();
  }

  private void update() {
    constraints = EntityContext.get().getAllEntities(Constraint.class);

    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : constraints) {
      dropDownItems.add(new JustAnExampleModelClass(((Constraint) o).getDescription()));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    if (constraints.size() > 0) {
      dropDown.selectItemByIndex(0);
      fillTextField((Constraint) constraints.get(0));
    }
  }

  private void clearTextField() {
    constraintDescriptorTextField.setText("");
  }

  private void fillTextField(Constraint constraint) {
    constraintDescriptorTextField.setText(constraint.getDescription());
  }

  private Constraint getConstraint() {
    try {
      Constraint constraint = dropDown.getSelectedIndex() > -1 ? (Constraint) constraints.get(dropDown.getSelectedIndex()) : new Constraint();
      constraint.setDescription(constraintDescriptorTextField.getText());
      return constraint;
    } catch (Exception exc) {
      return null;
    }
  }
}
