package com.multinodus.satteliteexplorer.ui.controls.task;

import com.multinodus.satteliteexplorer.ui.controls.common.CommonBuilders;
import com.multinodus.satteliteexplorer.ui.controls.common.DialogPanelControlDefinition;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;

/**
 * The ChatControlDialogRegister registers a new control (the whole ChatControlDialog) with
 * Nifty. We can later simply generate the whole dialog using a control with the given NAME.
 *
 * @author void
 */
public class TaskDialogDefinition {
  public static String NAME = "taskDialog";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new TasklDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        // here is the drop down control
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Задача:"));
          control(new DropDownBuilder("dropDownTask") {{

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
          control(new ButtonBuilder("addButtonTask", "Add"));
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
          control(new ButtonBuilder("removeButtonTask", "Remove"));
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
          control(new ButtonBuilder("updateButtonTask", "Update"));
        }});

        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Требуемый тип оборудования:"));
          control(new DropDownBuilder("dropDownEquipmentTypeForeign") {{

            width("*");
          }});
        }});

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Регион:"));
          control(new DropDownBuilder("dropDownRegionForeign") {{
            width("*");
          }});
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
