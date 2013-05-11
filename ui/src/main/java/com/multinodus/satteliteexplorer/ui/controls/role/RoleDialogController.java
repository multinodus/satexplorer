package com.multinodus.satteliteexplorer.ui.controls.role;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import com.multinodus.satteliteexplorer.db.EntityContext;
import com.multinodus.satteliteexplorer.db.entities.Role;
import com.multinodus.satteliteexplorer.ui.controls.common.JustAnExampleModelClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The RoleDialogController.
 *
 * @author void
 */
public class RoleDialogController implements Controller {
  private Screen screen;
  private TextField roleDescriptorTextField;

  private Button addButton;
  private Button removeButton;
  private Button updateButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private List<Object> roles;
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
    dropDown = screen.findNiftyControl("dropDownRole", DropDown.class);
    roleDescriptorTextField = screen.findNiftyControl("roleDescription", TextField.class);

    addButton = screen.findNiftyControl("addButtonRole", Button.class);
    removeButton = screen.findNiftyControl("removeButtonRole", Button.class);
    updateButton = screen.findNiftyControl("updateButtonRole", Button.class);
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

  @NiftyEventSubscriber(id = "dropDownRole")
  public void onDropDownSelectionChanged(final String id, final DropDownSelectionChangedEvent<JustAnExampleModelClass> event) {
    if (event.getSelection() == null) {
      clearTextField();
    } else {
      fillTextField((Role) roles.get(event.getSelectionItemIndex()));
    }
  }

  @NiftyEventSubscriber(id = "addButtonRole")
  public void onAddButtonClicked(final String id, final ButtonClickedEvent event) {
    Role role = getRole();
    if (role != null) {
      EntityContext.get().createEntity(role, Role.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "removeButtonRole")
  public void onRemoveButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    if (index >= 0) {
      EntityContext.get().deleteEntity(roles.get(index), Role.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "updateButtonRole")
  public void onUpdateButtonClicked(final String id, final ButtonClickedEvent event) {
    Role role = getRole();
    if (role != null) {
      EntityContext.get().updateEntity(role, Role.class);
    }
    update();
  }

  private void update() {
    roles = EntityContext.get().getAllEntities(Role.class);

    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : roles) {
      dropDownItems.add(new JustAnExampleModelClass(((Role) o).getName().toString()));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    if (roles.size() > 0) {
      dropDown.selectItemByIndex(0);
      fillTextField((Role) roles.get(0));
    }
  }

  private void clearTextField() {
    roleDescriptorTextField.setText("");
  }

  private void fillTextField(Role role) {
    roleDescriptorTextField.setText(role.getName());
  }

  private Role getRole() {
    try {
      Role role = dropDown.getSelectedIndex() > -1 ? (Role) roles.get(dropDown.getSelectedIndex()) : new Role();
      role.setName(roleDescriptorTextField.getText());
      return role;
    } catch (Exception exc) {
      return null;
    }
  }
}
