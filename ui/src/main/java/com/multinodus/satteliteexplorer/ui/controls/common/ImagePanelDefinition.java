package com.multinodus.satteliteexplorer.ui.controls.common;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 10.05.13
 * Time: 12:52
 * To change this template use File | Settings | File Templates.
 */
public class ImagePanelDefinition {
  public static String NAME = "imagePanel";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new ImagePanelController());
      set("childRootId", "#effectPanel");
      panel(new PanelBuilder() {{
        visible(false);
        childLayoutCenter();
        panel(new PanelBuilder("#effectPanel") {{
//          style("nifty-panel");
          backgroundColor("#5588");
          childLayoutCenter();
          alignCenter();
          valignCenter();
          width("90%");
          height("90%");
          padding("14px,20px,26px,19px");
          image(new ImageBuilder() {{
            name("chartImg");
            filename("Textures/chart.png");
          }});
          onShowEffect(builders.createFadeEffectIn());
          onHideEffect(builders.createFadeEffectOut());
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
