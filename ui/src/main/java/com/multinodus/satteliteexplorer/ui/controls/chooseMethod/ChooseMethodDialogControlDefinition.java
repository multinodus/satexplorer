package com.multinodus.satteliteexplorer.ui.controls.chooseMethod;

import com.multinodus.satteliteexplorer.ui.controls.common.CommonBuilders;
import com.multinodus.satteliteexplorer.ui.controls.common.DialogPanelControlDefinition;
import com.multinodus.satteliteexplorer.ui.scene.UIApplication;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;

public class ChooseMethodDialogControlDefinition {
  public static final String NAME = "chooseMethodDialog";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty, final UIApplication app) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new ChooseMethodDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{

        // here is the drop down control
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Алгоритм:"));
          control(new DropDownBuilder("dropDownChooseMethod") {{
            width("*");
          }});
        }});

        panel(builders.vspacer());
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Тайм-аут (с):"));
          control(new ControlBuilder("timeout", "textfield"));
        }});
        panel(builders.vspacer());

        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Горизонт планирования (часов):"));
          control(new ControlBuilder("horizont", "textfield"));
        }});
        panel(builders.vspacer());

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
          control(new ButtonBuilder("okButtonChooseMethod", "OK"));
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
