package satteliteExplorer.ui.controls.user;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import satteliteExplorer.db.EntityContext;
import satteliteExplorer.db.entities.Role;
import satteliteExplorer.db.entities.User;
import satteliteExplorer.ui.controls.common.JustAnExampleModelClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The UserDialogController.
 *
 * @author void
 */
public class UserDialogController implements Controller {
  private Screen screen;
  private TextField loginTextField;
  private TextField passwordTextField;
  private DropDown<JustAnExampleModelClass> roleDropDown;

  private Button addButton;
  private Button removeButton;
  private Button updateButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private List<Object> users;
  private List<JustAnExampleModelClass> dropDownItems;

  private List<Object> roles;
  private List<JustAnExampleModelClass> dropDownItemsRoles;


  @SuppressWarnings("unchecked")
  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.screen = screen;
    dropDown = screen.findNiftyControl("dropDownUser", DropDown.class);
    loginTextField = screen.findNiftyControl("login", TextField.class);
    passwordTextField = screen.findNiftyControl("password", TextField.class);
    roleDropDown = screen.findNiftyControl("dropDownRoleForeign", DropDown.class);

    addButton = screen.findNiftyControl("addButtonUser", Button.class);
    removeButton = screen.findNiftyControl("removeButtonUser", Button.class);
    updateButton = screen.findNiftyControl("updateButtonUser", Button.class);
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

  @NiftyEventSubscriber(id = "dropDownUser")
  public void onDropDownSelectionChanged(final String id, final DropDownSelectionChangedEvent<JustAnExampleModelClass> event) {
    if (event.getSelection() == null) {
      clearTextField();
    } else {
      fillTextField((User) users.get(event.getSelectionItemIndex()));
      updateRole((User) users.get(event.getSelectionItemIndex()));
    }
  }

  @NiftyEventSubscriber(id = "addButtonUser")
  public void onAddButtonClicked(final String id, final ButtonClickedEvent event) {
    User user = getUser();
    if (user != null) {
      EntityContext.get().createEntity(user, User.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "removeButtonUser")
  public void onRemoveButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    if (index >= 0) {
      EntityContext.get().deleteEntity(users.get(index), User.class);
    }
    update();
  }

  @NiftyEventSubscriber(id = "updateButtonUser")
  public void onUpdateButtonClicked(final String id, final ButtonClickedEvent event) {
    User user = getUser();
    if (user != null) {
      EntityContext.get().updateEntity(user, User.class);
    }
    update();
  }

  private void update() {
    users = EntityContext.get().getAllEntities(User.class);

    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (Object o : users) {
      dropDownItems.add(new JustAnExampleModelClass(((User) o).getLogin().toString()));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    if (users.size() > 0) {
      dropDown.selectItemByIndex(0);
      fillTextField((User) users.get(0));
      updateRole((User) users.get(0));
    } else {
      updateRole(null);
    }
  }

  private void updateRole(User user) {
    roles = EntityContext.get().getAllEntities(Role.class);

    dropDownItemsRoles = new ArrayList<JustAnExampleModelClass>();
    int index = 0;
    int i = 0;
    for (Object o : roles) {
      dropDownItemsRoles.add(new JustAnExampleModelClass(((Role) o).getName()));
      if (user != null && ((Role) o).getRoleId() == user.getRole().getRoleId()) {
        index = i;
      }
      i++;
    }
    roleDropDown.clear();
    roleDropDown.addAllItems(dropDownItemsRoles);
    if (roles.size() > 0) {
      roleDropDown.selectItemByIndex(index);
    }
  }

  private void clearTextField() {
    loginTextField.setText("");
    passwordTextField.setText("");
  }

  private void fillTextField(User user) {
    loginTextField.setText(user.getLogin());
    passwordTextField.setText(user.getPassword());
  }

  private User getUser() {
    try {
      User user = dropDown.getSelectedIndex() > -1 ? (User) users.get(dropDown.getSelectedIndex()) : new User();
      user.setLogin(loginTextField.getText());
      user.setPassword(passwordTextField.getText());
      user.setRole((Role) roles.get(roleDropDown.getSelectedIndex()));
      return user;
    } catch (Exception exc) {
      return null;
    }
  }
}
