package com.multinodus.satteliteexplorer.ui.controls.equipmentType;

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
public class EquipmentTypeDialogDefinition {
  public static String NAME = "equipmentTypeDialog";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new EquipmentTypeDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Тип оборудования:"));
          control(new DropDownBuilder("dropDownEquipmentType") {{

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
          control(new ButtonBuilder("addButtonEquipmentType", "Add"));
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
          control(new ButtonBuilder("removeButtonEquipmentType", "Remove"));
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
          control(new ButtonBuilder("updateButtonEquipmentType", "Update"));
        }});

        panel(builders.vspacer());
        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Название:"));
          control(new ControlBuilder("equipmentTypeDescription", "textfield"));
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
