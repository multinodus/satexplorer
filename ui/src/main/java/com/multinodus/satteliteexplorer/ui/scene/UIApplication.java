/*
Copyright (c) 2012 Aaron Perkins

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.multinodus.satteliteexplorer.ui.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.multinodus.satteliteexplorer.db.entities.User;
import com.multinodus.satteliteexplorer.scheduler.optimizations.OptimizationServer;
import com.multinodus.satteliteexplorer.scheduler.optimizations.OptimizationServerImpl;
import com.multinodus.satteliteexplorer.ui.controls.chooseMethod.ChooseMethodDialogController;
import com.multinodus.satteliteexplorer.ui.controls.schedulingProcess.SchedulingProcessController;
import com.multinodus.satteliteexplorer.ui.scene.models.planet.Planet;
import com.multinodus.satteliteexplorer.ui.scene.models.planet.PlanetAppState;
import de.lessvoid.nifty.screen.Screen;

import java.awt.*;

/**
 * UIApplication
 */
public class UIApplication extends SimpleApplication {

  private static final String BINDING_NAME = "optimizeService";
  private PlanetAppState planetAppState;
  public Scene scene;
  public User user;
  public OptimizationServer optimizationServer;
  private boolean isOperator = false;
  private boolean playing = false;
  public Screen mainScreen;

  private int hoursHorizont;
  private int timeout;
  private String method;

  private Material easyMaterial;

  public static UIApplication app;

  public static void main(String[] args) {
    final LoginForm loginForm = new LoginForm();
//    loginForm.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
//    loginForm.setLocation(500, 300);
//    loginForm.setVisible(true);
//
//    while (loginForm.isVisible()) {
//    }

    User user = loginForm.getUser();

    if (user != null) {
      try {
        loginForm.dispose();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        AppSettings settings = new AppSettings(true);
//        settings.setFullscreen(true);
        settings.setFrequency(60);
        settings.setResolution(screenSize.width, screenSize.height - 65);
        settings.setTitle("Автоматизированная система планирования распределения задач ДЗЗ в группировке КА");
        UIApplication app = new UIApplication();
        UIApplication.app = app;

        app.user = user;

        app.setDisplayStatView(false);
        app.setShowSettings(false);
        app.setDisplayFps(false);
        app.setSettings(settings);

//        Registry registry = LocateRegistry.getRegistry("localhost", 2099);
//        app.optimizationServer = (OptimizationServer) registry.lookup(BINDING_NAME);
        app.optimizationServer = new OptimizationServerImpl();

        app.start();
      } catch (Exception exc) {
        System.out.println(exc.toString());
      }
    }
  }

  @Override
  public void simpleInitApp() {
    // Only show severe errors in log
    java.util.logging.Logger.getLogger("com.jme3").setLevel(java.util.logging.Level.SEVERE);

    isOperator = user.getRole().getName().equals("operator");
    // Toggle wireframe
    inputManager.addMapping("TOGGLE_WIREFRAME",
        new KeyTrigger(KeyInput.KEY_T));
    inputManager.addListener(actionListener, "TOGGLE_WIREFRAME");
    // Collision test
    inputManager.addMapping("COLLISION_TEST",
        new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
    inputManager.addListener(actionListener, "COLLISION_TEST");

    easyMaterial =  app.assetManager.loadMaterial("Materials/EasyMaterial.j3m");

    // Setup camera
    this.getCamera().setFrustumFar(10000000f);
    this.getCamera().setFrustumNear(1.0f);
    this.getFlyByCamera().setDragToRotate(true);

    // In orbit
    this.getCamera().setLocation(new Vector3f(0f, 0f, 180000f));

    // On surface
    //this.getCamera().setLocation(new Vector3f(-6657.5254f, 27401.822f, 57199.777f));
    //this.getCamera().lookAtDirection(new Vector3f(0.06276598f, 0.94458306f, -0.3222158f), Vector3f.UNIT_Y);

    // Add sun
    DirectionalLight sun = new DirectionalLight();
    sun.setColor(new ColorRGBA(0.45f, 0.5f, 0.6f, 1.0f));
    sun.setDirection(new Vector3f(-1f, -1f, 0f));
    rootNode.addLight(sun);

    AmbientLight ambientLight = new AmbientLight();
    ambientLight.setColor(new ColorRGBA(3.5f, 3.5f, 3.5f, 0.1f));
    rootNode.addLight(ambientLight);

    // Add sky
    Node sceneNode = new Node("Scene");
    sceneNode.attachChild(Utility.createSkyBox(this.getAssetManager(), "Textures/blue-glow-1024.dds"));
    rootNode.attachChild(sceneNode);

    // Add planet app state
    planetAppState = new PlanetAppState();
    stateManager.attach(planetAppState);

    scene = new Scene(assetManager, rootNode, planetAppState, this);

    MainScreen ui = new MainScreen();
    ui.simpleInitApp(this);

    scene.bindTimeLabel(app.mainScreen.findElementByName("timeLabel"));

//    NiftyImage newImage = ui.nifty.getRenderEngine().createImage("Textures/cross.jpg", false);
//    Element img = ui.screen.findElementByName("bgimg");
//    img.getRenderer(ImageRenderer.class).setImage(newImage);
  }

  @Override
  public void simpleUpdate(float tpf) {
    // slow camera down as we approach a planet
    Planet planet = planetAppState.getNearestPlanet();
    if (planet != null && planet.getPlanetToCamera() != null) {
      //System.out.println(planet.getName() + ": " + planet.getDistanceToCamera());
      this.getFlyByCamera().setMoveSpeed(
          FastMath.clamp(planet.getDistanceToCamera(), 5, 100000));
    }
    scene.updateView();
  }

  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean pressed, float tpf) {

      if (name.equals("TOGGLE_WIREFRAME") && !pressed) {
        for (Planet planet : planetAppState.getPlanets()) {
          planet.toogleWireframe();
        }
      }
    }
  };

  public boolean isPlaying() {
    return playing;
  }

  public void setPlaying(boolean playing) {
    this.playing = playing;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  public int getHoursHorizont() {
    return hoursHorizont;
  }

  public void setHoursHorizont(int hoursHorizont) {
    this.hoursHorizont = hoursHorizont;
  }

  public Material getEasyMaterial() {
    return easyMaterial;
  }
}