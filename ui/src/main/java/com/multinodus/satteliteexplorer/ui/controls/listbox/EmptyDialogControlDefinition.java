package com.multinodus.satteliteexplorer.ui.controls.listbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import com.multinodus.satteliteexplorer.ui.controls.common.CommonBuilders;
import com.multinodus.satteliteexplorer.ui.controls.common.DialogPanelControlDefinition;

/**
 * The ListBoxDialogControlDefinition registers a new control with Nifty
 * that represents the whole Dialog. This gives us later an appropriate
 * ControlBuilder to actual construct the Dialog (as a control).
 *
 * @author void
 */
public class EmptyDialogControlDefinition {
  public static final String NAME = "listBoxDialogControl";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new EmptyDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        width("1024px");
        height("1024px");
        // the actual list box panel at the top
                /*panel(new PanelBuilder() {
                {
                    alignCenter();
                    valignCenter();
                    childLayoutOverlay();
                    //width("1024px");
                    //height("1024px");
                    image(new ImageBuilder() {
                      {
                        filename("Interface/Images/earth.jpg");
                      }
                    });
                    }
                });*/
      }});
    }}.registerControlDefintion(nifty);
  }
}
