package satteliteExplorer.ui.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import satteliteExplorer.ui.controls.common.CommonBuilders;
import satteliteExplorer.ui.controls.common.DialogPanelControlDefinition;
import satteliteExplorer.ui.controls.common.ImagePanelDefinition;
import satteliteExplorer.ui.controls.common.MenuButtonControlDefinition;
import satteliteExplorer.ui.controls.dataCenter.DataCenterDialogDefinition;
import satteliteExplorer.ui.controls.equipment.EquipmentDialogControlDefinition;
import satteliteExplorer.ui.controls.equipmentType.EquipmentTypeDialogDefinition;
import satteliteExplorer.ui.controls.listbox.EmptyDialogControlDefinition;
import satteliteExplorer.ui.controls.orbit.OrbitDialogControlDefinition;
import satteliteExplorer.ui.controls.region.RegionDialogControlDefinition;
import satteliteExplorer.ui.controls.role.RoleDialogDefinition;
import satteliteExplorer.ui.controls.sat.SatControlDefinition;
import satteliteExplorer.ui.controls.task.TaskDialogDefinition;
import satteliteExplorer.ui.controls.user.UserDialogDefinition;

/**
 * Nifty GUI 1.3 demo using Java.
 * Based on /nifty-default-controls-examples/trunk/src/main/java/de/lessvoid/nifty/examples/.
 *
 * @author zathras
 */
public class JmeControlsDemo {

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
    nifty.registerSound("intro", "Interface/sound/19546__tobi123__Gong_mf2.wav");
    nifty.registerMusic("credits", "Interface/sound/Loveshadow_-_Almost_Given_Up.ogg");
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

    createDemoScreen(nifty);
    nifty.gotoScreen("demo");

    return niftyDisplay;
  }

  private Screen createDemoScreen(final Nifty nifty) {
    final CommonBuilders common = new CommonBuilders();
    Screen screen = new ScreenBuilder("demo") {

      {
        if (app.user.getRole().getName().equals("operator")) {
          controller(new JmeControlsDemoScreenController( app,
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
              "menuButtonSchedule", "chartImg"));
        }
//        if (UIApplication.user.getRole().getName().equals("admin")) {
//          controller(new JmeControlsDemoScreenController(
//              );
//        }
        inputMapping("de.lessvoid.nifty.input.mapping.DefaultInputMapping"); // this will enable Keyboard events for the screen controller
        layer(new LayerBuilder("layer") {

          {
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
                  control(new ControlBuilder("chartImg", ImagePanelDefinition.NAME));
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
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonRegion", "Регион", ""));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonTask", "Задачи", ""));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonEquipment", "Оборудование", ""));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonEquipmentType", "Типы оборудования", ""));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonSchedule", "Запуск планирования", ""));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonRole", "Роли", ""));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonUser", "Пользователи", ""));
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
