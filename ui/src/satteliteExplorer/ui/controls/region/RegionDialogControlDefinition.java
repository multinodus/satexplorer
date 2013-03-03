package satteliteExplorer.ui.controls.region;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import satteliteExplorer.ui.controls.common.CommonBuilders;
import satteliteExplorer.ui.controls.common.DialogPanelControlDefinition;

public class RegionDialogControlDefinition {
  public static final String NAME = "regionDialog";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new RegionDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{

        // here is the drop down control
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Регион:"));
          control(new DropDownBuilder("dropDownRegion") {{

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
          control(new ButtonBuilder("addButtonRegion", "Add"));
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
          control(new ButtonBuilder("removeButtonRegion", "Remove"));
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
          control(new ButtonBuilder("updateButtonRegion", "Update"));
        }});

        panel(builders.vspacer());
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Радиус:"));
          control(new ControlBuilder("radius", "textfield"));
        }});
        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Долгота:"));
          control(new ControlBuilder("longitude", "textfield"));
        }});
        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Широта:"));
          control(new ControlBuilder("latitude", "textfield"));
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
