package com.multinodus.satteliteexplorer.ui.scene;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MainScreenController implements ScreenController, KeyInputHandler {
  private static final Logger logger = Logger.getLogger(MainScreenController.class.getName());

  private UIApplication app;
  private Nifty nifty;
  private Screen screen;

  // This simply maps the IDs of the MenuButton elements to the corresponding Dialog elements we'd
  // like to show with the given MenuButton. This map will make our life a little bit easier when
  // switching between the menus.
  private Map<String, String> buttonToDialogMap = new Hashtable<String, String>();

  // We keep all the button IDs in this list so that we can later decide if an index is before or
  // after the current button.
  private List<String> buttonIdList = new ArrayList<String>();

  // This keeps the current menu button
  private String currentMenuButtonId;

  public MainScreenController(UIApplication app, final String... mapping) {
    this.app = app;
    if (mapping == null || mapping.length == 0 || mapping.length % 2 != 0) {
      logger.warning("expecting pairs of values that map menuButton IDs to dialog IDs");
    } else {
      for (int i = 0; i < mapping.length / 2; i++) {
        String menuButtonId = mapping[i * 2 + 0];
        String dialogId = mapping[i * 2 + 1];
        buttonToDialogMap.put(menuButtonId, dialogId);
        buttonIdList.add(menuButtonId);
        if (i == 0) {
          currentMenuButtonId = menuButtonId;
        }
      }
    }
  }

  @Override
  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }

  @Override
  public void onStartScreen() {
        /*
        screen.findElementByName(buttonToDialogMap.get(currentMenuButtonId)).show();
        screen.findElementByName(currentMenuButtonId).startEffect(EffectEventId.onCustom, null, "selected");
        */
  }

  @Override
  public void onEndScreen() {
  }

  @NiftyEventSubscriber(id = "playButton")
  public void onPlayButtonClicked(final String id, final ButtonClickedEvent event) {
    app.setPlaying(true);
  }

  @NiftyEventSubscriber(id = "pauseButton")
  public void onPauseButtonClicked(final String id, final ButtonClickedEvent event) {
    app.setPlaying(false);
  }

  @Override
  public boolean keyEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @NiftyEventSubscriber(pattern = "menuButton.*")
  public void onMenuButtonListBoxClick(final String id, final NiftyMousePrimaryClickedEvent clickedEvent) {
    changeDialogTo(id);
  }

  private void changeDialogTo(final String id) {
    if (!id.equals(currentMenuButtonId)) {
      int currentIndex = buttonIdList.indexOf(currentMenuButtonId);
      int nextIndex = buttonIdList.indexOf(id);


      Element nextElement = screen.findElementByName(buttonToDialogMap.get(id));
      if (id.indexOf("ListBox") == -1) {
        nextElement.show();
      }

      Element currentElement = screen.findElementByName(buttonToDialogMap.get(currentMenuButtonId));
      currentElement.hide();

      screen.findElementByName(currentMenuButtonId).stopEffect(EffectEventId.onCustom);
      screen.findElementByName(id).startEffect(EffectEventId.onCustom, null, "selected");
      currentMenuButtonId = id;
    }
  }
}
