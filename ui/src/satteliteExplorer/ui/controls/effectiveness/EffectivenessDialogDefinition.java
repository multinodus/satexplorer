package satteliteExplorer.ui.controls.effectiveness;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import satteliteExplorer.ui.controls.common.CommonBuilders;
import satteliteExplorer.ui.controls.common.DialogPanelControlDefinition;

/**
 * The DragAndDropDialogRegister registers a new control (the whole DragAndDropDialog) with
 * Nifty. We can later simply generate the whole dialog using a control with the given NAME.
 *
 * @author void
 */
public class EffectivenessDialogDefinition {
  public static String NAME = "effectivenessDialog";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new EffectivenessDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Целевая функция:"));
          control(new DropDownBuilder("dropDownEffectiveness") {{

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
          control(new ButtonBuilder("addButtonEffectiveness", "Add"));
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
          control(new ButtonBuilder("removeButtonEffectiveness", "Remove"));
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
          control(new ButtonBuilder("updateButtonEffectiveness", "Update"));
        }});

        panel(builders.vspacer());
        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Целевая функция:"));
          control(new ControlBuilder("effectivenessDescription", "textfield"));
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
