package satteliteExplorer.ui.controls.orbit;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import satteliteExplorer.ui.controls.common.CommonBuilders;
import satteliteExplorer.ui.controls.common.DialogPanelControlDefinition;

public class OrbitDialogControlDefinition {
  public static final String NAME = "orbitDialog";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new OrbitDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{

        // here is the drop down control
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Орбита:"));
          control(new DropDownBuilder("dropDownOrbit") {{

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
          control(new ButtonBuilder("addButtonOrbit", "Add"));
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
          control(new ButtonBuilder("removeButtonOrbit", "Remove"));
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
          control(new ButtonBuilder("updateButtonOrbit", "Update"));
        }});

        panel(builders.vspacer());
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Экцентриситет:"));
          control(new ControlBuilder("eccentricity", "textfield"));
        }});
        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Долгота восходящего узла:"));
          control(new ControlBuilder("longitudeOfAscendingNode", "textfield"));
        }});
        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Наклонение:"));
          control(new ControlBuilder("inclination", "textfield"));
        }});
        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Аргумент перигелия:"));
          control(new ControlBuilder("argumentOfPericenter", "textfield"));
        }});
        panel(builders.vspacer());

        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Большая полуось:"));
          control(new ControlBuilder("semiMajorAxis", "textfield"));
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
