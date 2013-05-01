package satteliteExplorer.ui.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import satteliteExplorer.ui.controls.common.CommonBuilders;
import satteliteExplorer.ui.controls.common.DialogPanelControlDefinition;
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

  private static CommonBuilders builders = new CommonBuilders();

  public NiftyJmeDisplay simpleInitApp(SimpleApplication a) {
    /**
     * Nifty-JME integration
     */
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

    createDemoScreen(nifty);
    nifty.gotoScreen("demo");

    return niftyDisplay;
  }

  private static Screen createDemoScreen(final Nifty nifty) {
    final CommonBuilders common = new CommonBuilders();
    Screen screen = new ScreenBuilder("demo") {

      {
        if (PlanetSimpleTest.user.getRole().getName().equals("engeener")) {
          controller(new JmeControlsDemoScreenController(
              "menuButtonConstraint", "dialogConstraint",
              "menuButtonEffectiveness", "dialogEffectiveness"));
        }
        if (PlanetSimpleTest.user.getRole().getName().equals("operator")) {
          controller(new JmeControlsDemoScreenController(
              "menuButtonListBox", "dialogListBox",
              "menuButtonDataCenter", "dialogDataCenter",
              "menuButtonRegion", "dialogRegion",
              "menuButtonEquipment", "dialogEquipment",
              "menuButtonEquipmentType", "dialogEquipmentType",
              "menuButtonSat", "dialogSat",
              "menuButtonTask", "dialogTask",
              "menuButtonOrbit", "dialogOrbit"));
        }
        if (PlanetSimpleTest.user.getRole().getName().equals("admin")) {
          controller(new JmeControlsDemoScreenController(
              "menuButtonRole", "dialogRole",
              "menuButtonUser", "dialogUser"));
        }
        inputMapping("de.lessvoid.nifty.input.mapping.DefaultInputMapping"); // this will enable Keyboard events for the screen controller
        layer(new LayerBuilder("layer") {

          {
            childLayoutVertical();
            if (PlanetSimpleTest.user.getRole().getName().equals("operator")) {
              panel(new PanelBuilder("navigation") {

                {
                  width("100%");
                  height("35px");
                  backgroundColor("#5588");
                  childLayoutHorizontal();
                  padding("20px");
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonListBox", "", ""));
                  panel(builders.hspacer("10px"));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonOrbit", "Орбиты", ""));
                  panel(builders.hspacer("10px"));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonSat", "КА", ""));
                  panel(builders.hspacer("10px"));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonDataCenter", "ЦОД", ""));
                  panel(builders.hspacer("10px"));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonRegion", "Регион", ""));
                  panel(builders.hspacer("10px"));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonTask", "Задачи", ""));
                }
              });
            }
            panel(new PanelBuilder("navigation2") {
              {
                width("100%");
                height("35px");
                backgroundColor("#5588");
                childLayoutHorizontal();
                padding("20px");
                if (PlanetSimpleTest.user.getRole().getName().equals("admin")) {
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonRole", "Роли", ""));
                  panel(builders.hspacer("10px"));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonUser", "Пользователи", ""));
                  panel(builders.hspacer("10px"));
                }
                if (PlanetSimpleTest.user.getRole().getName().equals("operator")) {
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonEquipment", "Оборудование", ""));
                  panel(builders.hspacer("10px"));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonEquipmentType", "Типы оборудования", ""));
                  panel(builders.hspacer("10px"));
                  control(MenuButtonControlDefinition.getControlBuilder("menuButtonSchedule", "Запуск планирования", ""));
                }
              }
            });
            panel(new PanelBuilder("dialogParent") {

              {
                childLayoutOverlay();
                width("100%");
                alignCenter();
                valignCenter();
                if (PlanetSimpleTest.user.getRole().getName().equals("operator")) {
                  control(new ControlBuilder("dialogListBox", EmptyDialogControlDefinition.NAME));
                  control(new ControlBuilder("dialogOrbit", OrbitDialogControlDefinition.NAME));
                  control(new ControlBuilder("dialogEquipment", EquipmentDialogControlDefinition.NAME));
                  control(new ControlBuilder("dialogSat", SatControlDefinition.NAME));
                  control(new ControlBuilder("dialogTask", TaskDialogDefinition.NAME));
                  control(new ControlBuilder("dialogDataCenter", DataCenterDialogDefinition.NAME));
                  control(new ControlBuilder("dialogRegion", RegionDialogControlDefinition.NAME));
                  control(new ControlBuilder("dialogEquipmentType", EquipmentTypeDialogDefinition.NAME));
                }
                if (PlanetSimpleTest.user.getRole().getName().equals("admin")) {
                  control(new ControlBuilder("dialogRole", RoleDialogDefinition.NAME));
                  control(new ControlBuilder("dialogUser", UserDialogDefinition.NAME));
                }
              }
            });
          }
        });
      }
    }.build(nifty);
    return screen;
  }

  private static void registerMenuButtonHintStyle(final Nifty nifty) {
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

  private static void registerStyles(final Nifty nifty) {
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
