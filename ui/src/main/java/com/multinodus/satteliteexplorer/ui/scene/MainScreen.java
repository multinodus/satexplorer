package com.multinodus.satteliteexplorer.ui.scene;

import com.jme3.niftygui.NiftyJmeDisplay;
import com.multinodus.satteliteexplorer.ui.controls.chooseMethod.ChooseMethodDialogControlDefinition;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import com.multinodus.satteliteexplorer.ui.controls.common.CommonBuilders;
import com.multinodus.satteliteexplorer.ui.controls.common.DialogPanelControlDefinition;
import com.multinodus.satteliteexplorer.ui.controls.common.ImagePanelDefinition;
import com.multinodus.satteliteexplorer.ui.controls.common.MenuButtonControlDefinition;
import com.multinodus.satteliteexplorer.ui.controls.dataCenter.DataCenterDialogDefinition;
import com.multinodus.satteliteexplorer.ui.controls.equipment.EquipmentDialogControlDefinition;
import com.multinodus.satteliteexplorer.ui.controls.equipmentType.EquipmentTypeDialogDefinition;
import com.multinodus.satteliteexplorer.ui.controls.listbox.EmptyDialogControlDefinition;
import com.multinodus.satteliteexplorer.ui.controls.orbit.OrbitDialogControlDefinition;
import com.multinodus.satteliteexplorer.ui.controls.region.RegionDialogControlDefinition;
import com.multinodus.satteliteexplorer.ui.controls.role.RoleDialogDefinition;
import com.multinodus.satteliteexplorer.ui.controls.sat.SatControlDefinition;
import com.multinodus.satteliteexplorer.ui.controls.task.TaskDialogDefinition;
import com.multinodus.satteliteexplorer.ui.controls.user.UserDialogDefinition;

/**
 * Nifty GUI 1.3 demo using Java.
 * Based on /nifty-default-controls-examples/trunk/src/main/java/de/lessvoid/nifty/examples/.
 *
 * @author zathras
 */
public class MainScreen {

  private CommonBuilders builders = new CommonBuilders();
  private UIApplication app;

  public NiftyJmeDisplay simpleInitApp(UIApplication a) {
    /**
     * Nifty-JME integration
     */
    app = a;

    NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
        a.getAssetManager(), a.getInputManager(), a.getAudioRenderer(), a.getGuiViewPort());
    Nifty nifty = niftyDisplay.getNifty();
    a.getGuiViewPort().addProcessor(niftyDisplay);
    //flyCam.setDragToRotate(true);

    /**
     * nifty demo code
     */
    nifty.loadStyleFile("nifty-default-styles.xml");
    nifty.loadControlFile("nifty-default-controls.xml");
    nifty.registerMouseCursor("hand", "Interface/mouse-cursor-hand.png", 5, 4);
    //nifty.setDebugOptionPanelColors(true);
    registerMenuButtonHintStyle(nifty);
    registerStyles(nifty);

    // register some helper controls
    MenuButtonControlDefinition.register(nifty);
    DialogPanelControlDefinition.register(nifty);

    // register the dialog controls
    EmptyDialogControlDefinition.register(nifty);
    OrbitDialogControlDefinition.register(nifty);
    TaskDialogDefinition.register(nifty);
    EquipmentDialogControlDefinition.register(nifty);
    SatControlDefinition.register(nifty);
    DataCenterDialogDefinition.register(nifty);
    RegionDialogControlDefinition.register(nifty);
    EquipmentTypeDialogDefinition.register(nifty);
    RoleDialogDefinition.register(nifty);
    UserDialogDefinition.register(nifty);
    ImagePanelDefinition.register(nifty);
    ChooseMethodDialogControlDefinition.register(nifty);

    createDemoScreen(nifty);
    nifty.gotoScreen("demo");

    return niftyDisplay;
  }

  private Screen createDemoScreen(final Nifty nifty) {
    final CommonBuilders common = new CommonBuilders();
    Screen screen = new ScreenBuilder("demo") {

      {
        if (app.user.getRole().getName().equals("operator")) {
          controller(new MainScreenController(app,
              "menuButtonListBox", "dialogListBox",
              "menuButtonDataCenter", "dialogDataCenter",
              "menuButtonRegion", "dialogRegion",
              "menuButtonEquipment", "dialogEquipment",
              "menuButtonEquipmentType", "dialogEquipmentType",
              "menuButtonSat", "dialogSat",
              "menuButtonTask", "dialogTask",
              "menuButtonOrbit", "dialogOrbit",
              "menuButtonRole", "dialogRole",
              "menuButtonUser", "dialogUser",
              "menuButtonSchedule", ChooseMethodDialogControlDefinition.NAME));
        }
//        if (UIApplication.user.getRole().getName().equals("admin")) {
//          controller(new MainScreenController(
//              );
//        }
        inputMapping("de.lessvoid.nifty.input.mapping.DefaultInputMapping"); // this will enable Keyboard events for the screen controller
        layer(new LayerBuilder("layer") {
          {
            childLayoutVertical();

            panel(new PanelBuilder("mainPanel") {
              {
                alignCenter();
                valignTop();
                childLayoutHorizontal();

                panel(new PanelBuilder("dialogParent") {
                  {
                    childLayoutOverlay();
    //                width("85%");
                    alignLeft();
                    valignCenter();
                    if (app.user.getRole().getName().equals("operator")) {
                      control(new ControlBuilder("dialogListBox", EmptyDialogControlDefinition.NAME));
                      control(new ControlBuilder("dialogOrbit", OrbitDialogControlDefinition.NAME));
                      control(new ControlBuilder("dialogEquipment", EquipmentDialogControlDefinition.NAME));
                      control(new ControlBuilder("dialogSat", SatControlDefinition.NAME));
                      control(new ControlBuilder("dialogTask", TaskDialogDefinition.NAME));
                      control(new ControlBuilder("dialogDataCenter", DataCenterDialogDefinition.NAME));
                      control(new ControlBuilder("dialogRegion", RegionDialogControlDefinition.NAME));
                      control(new ControlBuilder("dialogEquipmentType", EquipmentTypeDialogDefinition.NAME));
                      control(new ControlBuilder("dialogRole", RoleDialogDefinition.NAME));
                      control(new ControlBuilder("dialogUser", UserDialogDefinition.NAME));
                      control(new ControlBuilder(ChooseMethodDialogControlDefinition.NAME, ChooseMethodDialogControlDefinition.NAME));
                    }
                    if (app.user.getRole().getName().equals("admin")) {

                    }
                  }
                });

                if (app.user.getRole().getName().equals("operator")) {
                  panel(new PanelBuilder("navigation") {
                    {
                      valignTop();
                      alignRight();
                      width("130px");
                      height("35px");
    //                  backgroundColor("#5588");
                      childLayoutVertical();
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonListBox", "", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonOrbit", "Орбиты", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonSat", "КА", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonDataCenter", "ППИ", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonRegion", "Регионы", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonTask", "Задачи", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonEquipment", "Оборудование", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonEquipmentType", "Типы оборудования", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonRole", "Роли", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonUser", "Пользователи", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonSceneVariant", "Сцены", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonLoadScene", "Загрузить сцену", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonSaveScene", "Сохранить сцену", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonSchedule", "Запуск планирования", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonAnalyzeSchedule", "Анализировать план", ""));
                      control(MenuButtonControlDefinition.getControlBuilder("menuButtonSaveSchedule", "Сохранить план", ""));
                    }
                  });
                }
    //            panel(new PanelBuilder("navigation2") {
    //              {
    //                width("100%");
    //                height("35px");
    //                backgroundColor("#5588");
    //                childLayoutHorizontal();
    //                padding("20px");
    //                if (UIApplication.user.getRole().getName().equals("admin")) {
    //
    //                }
    //                if (UIApplication.user.getRole().getName().equals("operator")) {
    //
    //                }
    //              }
    //            });
              }
            });

            panel(new PanelBuilder("playPanel") {
              {

                childLayoutHorizontal();
                backgroundColor("#5588");
                height("50px");
                valignBottom();
                panel(builders.hspacer("45%"));
                panel(new PanelBuilder() {{
                  childLayoutCenter();
                  alignCenter();
                  valignCenter();
                control(new ButtonBuilder("playButton", ">") {{
                  width("40px");
                  height("40px");
                }
                });
                }});
                panel(new PanelBuilder() {{
                  childLayoutCenter();
                  alignCenter();
                  valignCenter();
                  control(new ButtonBuilder("pauseButton", "||") {{
                    width("40px");
                    height("40px");
                  }
                  });
                }});

                panel(builders.hspacer("35%"));

                panel(new PanelBuilder() {{
                  childLayoutCenter();
                  alignRight();
                  valignCenter();
                  control(builders.createLabel("timeLabel", app.scene.getWorld().getCurrentTime().toString(), "100%"));
                }});
              }
            });
          }
        });
      }
    }.build(nifty);
    return screen;
  }

  private void registerMenuButtonHintStyle(final Nifty nifty) {
    new StyleBuilder() {

      {
        id("special-hint");
        base("nifty-panel-bright");
        childLayoutCenter();
        onShowEffect(new EffectBuilder("fade") {

          {
            length(150);
            effectParameter("start", "#0");
            effectParameter("end", "#d");
            inherit();
            neverStopRendering(true);
          }
        });
        onShowEffect(new EffectBuilder("move") {

          {
            length(150);
            inherit();
            neverStopRendering(true);
            effectParameter("mode", "fromOffset");
            effectParameter("offsetY", "-15");
          }
        });
        onCustomEffect(new EffectBuilder("fade") {

          {
            length(150);
            effectParameter("start", "#d");
            effectParameter("end", "#0");
            inherit();
            neverStopRendering(true);
          }
        });
        onCustomEffect(new EffectBuilder("move") {

          {
            length(150);
            inherit();
            neverStopRendering(true);
            effectParameter("mode", "toOffset");
            effectParameter("offsetY", "-15");
          }
        });
      }
    }.build(nifty);

    new StyleBuilder() {

      {
        id("special-hint#hint-text");
        base("base-font");
        alignLeft();
        valignCenter();
        textHAlignLeft();
        color(new Color("#000f"));
      }
    }.build(nifty);
  }

  private void registerStyles(final Nifty nifty) {
    new StyleBuilder() {

      {
        id("base-font-link");
        base("base-font");
        color("#8fff");
        interactOnRelease("$action");
        onHoverEffect(new HoverEffectBuilder("changeMouseCursor") {

          {
            effectParameter("id", "hand");
          }
        });
      }
    }.build(nifty);

    new StyleBuilder() {

      {
        id("creditsImage");
        alignCenter();
      }
    }.build(nifty);

    new StyleBuilder() {

      {
        id("creditsCaption");
        font("Interface/verdana-48-regular.fnt");
        width("100%");
        textHAlignCenter();
      }
    }.build(nifty);

    new StyleBuilder() {

      {
        id("creditsCenter");
        base("base-font");
        width("100%");
        textHAlignCenter();
      }
    }.build(nifty);
  }
}
