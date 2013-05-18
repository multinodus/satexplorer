package com.multinodus.satteliteexplorer.ui.controls.chooseMethod;

import com.multinodus.satteliteexplorer.ui.controls.common.ImagePanelDefinition;
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

public class ChooseMethodDialogController implements Controller {
  private Screen screen;
  private TextField timeoutTextField;

  private Button okButton;

  private DropDown<JustAnExampleModelClass> dropDown;

  private static String[] methods = {"Genetic algorithm", "Branch&Bound", "Adopt"};
  private static String[] nativeMethods = {"genetic", "ilp", "adopt"};
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
    dropDown = screen.findNiftyControl("dropDownChooseMethod", DropDown.class);
    timeoutTextField = screen.findNiftyControl("timeout", TextField.class);

    okButton = screen.findNiftyControl("okButtonOrbit", Button.class);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    dropDownItems = new ArrayList<JustAnExampleModelClass>();
    for (String method : methods) {
      dropDownItems.add(new JustAnExampleModelClass(method));
    }
    dropDown.clear();
    dropDown.addAllItems(dropDownItems);
    dropDown.selectItemByIndex(0);

    timeoutTextField.setText("60");
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

  @NiftyEventSubscriber(id = "okButtonOrbit")
  public void onOkButtonClicked(final String id, final ButtonClickedEvent event) {
    int index = dropDown.getSelectedIndex();
    String method = nativeMethods[index];
    this.screen.findElementByName(ChooseMethodDialogControlDefinition.NAME).hide();
    this.screen.findElementByName(ImagePanelDefinition.NAME).show();
  }
}
