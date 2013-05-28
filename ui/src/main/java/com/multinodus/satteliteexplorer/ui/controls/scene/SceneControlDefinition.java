package com.multinodus.satteliteexplorer.ui.controls.scene;

import com.multinodus.satteliteexplorer.ui.controls.chooseMethod.ChooseMethodDialogController;
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
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;

public class SceneControlDefinition {
  public static final String NAME = "scene";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty, final UIApplication app) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new SceneController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        // here is the drop down control
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          panel(new PanelBuilder() {{
            childLayoutVertical();
            control(builders.createLabel("КА:"));
            control(new ListBoxBuilder("satListBox") {{
              width("*");
              height("100%");
              displayItems(5);
            }});
            height("40%");
          }});

          panel(builders.hspacer("5"));

          panel(new PanelBuilder() {{
            childLayoutVertical();
            control(builders.createLabel("Выбранные КА:"));
            control(new ListBoxBuilder("selectedSatListBox") {{
              width("*");
              height("100%");
              displayItems(5);
            }});
            height("40%");
          }});
        }});

        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          panel(new PanelBuilder() {{
            childLayoutVertical();
            control(builders.createLabel("ППИ:"));
            control(new ListBoxBuilder("dataCenterListBox") {{
              width("*");
              displayItems(5);
            }});
            height("40%");
          }});

          panel(builders.hspacer("5"));

          panel(new PanelBuilder() {{
            childLayoutVertical();
            control(builders.createLabel("Выбранные ППИ:"));
            control(new ListBoxBuilder("selectedDataCenterListBox") {{
              width("*");
              displayItems(5);
            }});
            height("40%");
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
          control(new ButtonBuilder("okButtonScene", "OK"));
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
