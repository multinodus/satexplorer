package com.multinodus.satteliteexplorer.ui.controls.schedulingProcess;

import com.multinodus.satteliteexplorer.ui.controls.common.CommonBuilders;
import com.multinodus.satteliteexplorer.ui.scene.UIApplication;
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
public class SchedulingProcessDefinition {
  public static String NAME = "schedulingProcess";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty, final UIApplication app) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new SchedulingProcessController());
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
          height("70%");
          padding("14px,20px,26px,19px");
          image(new ImageBuilder("schedulingProcessImg") {{
            name("schedulingProcessImg");
            filename("Textures/chart.png");
            width("100%");
            height("100%");
          }});
          onShowEffect(builders.createFadeEffectIn());
          onHideEffect(builders.createFadeEffectOut());
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
