package com.multinodus.satteliteexplorer.ui.controls.analysis;

import com.multinodus.satteliteexplorer.ui.controls.common.CommonBuilders;
import com.multinodus.satteliteexplorer.ui.controls.schedulingProcess.SchedulingProcessController;
import com.multinodus.satteliteexplorer.ui.scene.UIApplication;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.tabs.builder.TabBuilder;
import de.lessvoid.nifty.controls.tabs.builder.TabsBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 10.05.13
 * Time: 12:52
 * To change this template use File | Settings | File Templates.
 */
public class AnalysisDefinition {
  public static String NAME = "analysis";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty, final UIApplication app) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new AnalysisController());
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
          control(new TabsBuilder("tabs") {{
            control(new TabBuilder("tab_satExploration", "План съемки спутников") {{
              panel(new PanelBuilder() {{
                childLayoutHorizontal();
                image(new ImageBuilder("satExplorationImg") {{
                  name("satExplorationImg");
//                  filename("Textures/satExploration_chart.png");
                  width("100%");
                  height("100%");
                }});
              }});
            }});

            control(new TabBuilder("tab_satWorkload", "Загруженность спутников") {{
              panel(new PanelBuilder() {{
                childLayoutHorizontal();
                image(new ImageBuilder("satWorkloadImg") {{
                  name("satWorkloadImg");
//                  filename("Textures/satWorkload_chart.png");
                  width("100%");
                  height("100%");
                }});
              }});
            }});

            control(new TabBuilder("tab_satDistribution", "Распределение задач между КА") {{
              panel(new PanelBuilder() {{
                childLayoutHorizontal();
                image(new ImageBuilder("satDistributionImg") {{
                  name("satDistributionImg");
//                  filename("Textures/satDistribution_chart.png");
                  width("100%");
                  height("100%");
                }});
              }});
            }});

            control(new TabBuilder("tab_costPie", "Доля выполненных задач по стоимости") {{
              panel(new PanelBuilder() {{
                childLayoutHorizontal();
                image(new ImageBuilder("costPieImg") {{
                  name("costPieImg");
//                  filename("Textures/costPie_chart.png");
                  width("100%");
                  height("100%");
                }});
              }});
            }});

            control(new TabBuilder("tab_numberPie", "Доля выполненных задач по количеству") {{
              panel(new PanelBuilder() {{
                childLayoutHorizontal();
                image(new ImageBuilder("numberPieImg") {{
                  name("numberPieImg");
//                  filename("Textures/numberPie_chart.png");
                  width("100%");
                  height("100%");
                }});
              }});
            }});
          }});

          onShowEffect(builders.createFadeEffectIn());
          onHideEffect(builders.createFadeEffectOut());
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
