package com.multinodus.satteliteexplorer.ui.controls.user;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import com.multinodus.satteliteexplorer.ui.controls.common.CommonBuilders;
import com.multinodus.satteliteexplorer.ui.controls.common.DialogPanelControlDefinition;

/**
 * The DragAndDropDialogRegister registers a new control (the whole DragAndDropDialog) with
 * Nifty. We can later simply generate the whole dialog using a control with the given NAME.
 *
 * @author void
 */
public class UserDialogDefinition {
  public static String NAME = "userDialog";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new UserDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Пользователь:"));
          control(new DropDownBuilder("dropDownUser") {{

            width("*");
          }});
        }});

        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(new LabelBuilder() {{
            width("*");
            alignLeft();
            textVAlignCenter();
            textHAlignLeft();
          }});
          panel(builders.hspacer("9px"));
          control(new ButtonBuilder("addButtonUser", "Add"));
        }});

        // the changed event and the remove item button
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(new LabelBuilder() {{
            width("*");
            alignLeft();
            textVAlignCenter();
            textHAlignLeft();
          }});
          panel(builders.hspacer("9px"));
          control(new ButtonBuilder("removeButtonUser", "Remove"));
        }});

        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(new LabelBuilder() {{
            width("*");
            alignLeft();
            textVAlignCenter();
            textHAlignLeft();
          }});
          panel(builders.hspacer("9px"));
          control(new ButtonBuilder("updateButtonUser", "Update"));
        }});

        panel(builders.vspacer());
        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Логин:"));
          control(new ControlBuilder("login", "textfield"));
        }});

        panel(builders.vspacer());
        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Пароль:"));
          control(new ControlBuilder("password", "textfield"));
        }});

        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Роль:"));
          control(new DropDownBuilder("dropDownRoleForeign") {{
            width("*");
          }});
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
