package com.multinodus.satteliteexplorer.ui.scene;

import com.jme3.asset.AssetManager;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Torus;
import com.multinodus.satteliteexplorer.db.entities.DataCenter;
import com.multinodus.satteliteexplorer.db.entities.Sat;
import com.multinodus.satteliteexplorer.scheduler.World;
import com.multinodus.satteliteexplorer.scheduler.models.*;
import com.multinodus.satteliteexplorer.ui.scene.models.*;
import com.multinodus.satteliteexplorer.ui.scene.models.planet.Planet;
import com.multinodus.satteliteexplorer.ui.scene.models.planet.PlanetAppState;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 16.12.12
 * Time: 18:39
 * To change this template use File | Settings | File Templates.
 */
public class Scene {
  private World world;
  private Collection<IView> views = new ArrayList<IView>();
  private boolean isStopped;
  private UIApplication app;
  private Element timeLabel;
  private Node rootNode;
  private PlanetAppState planetAppState;
  private AssetManager assetManager;

  public Scene(AssetManager assetManager, Node rootNode, PlanetAppState planetAppState, UIApplication app) {
    this.app = app;
    this.assetManager = assetManager;
    this.rootNode = rootNode;
    this.planetAppState = planetAppState;

    world = new World();

    Planet planet = Utility.createEarthLikePlanet(assetManager, 63781f, 800f, 4);
    planetAppState.addPlanet(planet);
    rootNode.attachChild(planet);

    EarthView earthView = new EarthView(world.getEarthModel(), planet);
    views.add(earthView);
  }

  public void createWorld(List<Sat> selectedSats, List<DataCenter> selectedDataCenters){
    world.load(selectedSats, selectedDataCenters);

    Node satNode = new Node("SatNode");
    rootNode.attachChild(satNode);

    Spatial satPrototype = assetManager.loadModel("Scenes/antenna.j3o");

    for (IUpdatable entity : world.getEntities()) {
      if (entity instanceof RegionModel) {
        RegionModel regionModel = (RegionModel) entity;

        //Cylinder sphere = new Cylinder(32, 32, 1000, 10000);
        Torus sphere = new Torus(4, 4, 50, 500);
        Geometry regionSpatial = new Geometry("", sphere);
        regionSpatial.setMaterial(assetManager.loadMaterial("Materials/EasyMaterial.j3m"));
        rootNode.attachChild(regionSpatial);

        RegionView regionView = new RegionView(regionModel, regionSpatial, rootNode);
        views.add(regionView);
      }
      if (entity instanceof DataCenterModel) {
        DataCenterModel dataCenter = (DataCenterModel) entity;

        Torus sphere = new Torus(32, 32, 50, 5000);
        Geometry regionSpatial = new Geometry("", sphere);
        regionSpatial.setMaterial(assetManager.loadMaterial("Materials/EasyMaterial.j3m"));
        rootNode.attachChild(regionSpatial);

        DataCenterView dataCenterView = new DataCenterView(dataCenter, regionSpatial);
        views.add(dataCenterView);
      }
      if (entity instanceof SatModel) {
        SatModel satModel = (SatModel) entity;
        Spatial sat = satPrototype.clone();

        sat.scale(1000f, 1000f, 1000f);
        satNode.attachChild(sat);

        SpotLight spotLight = new SpotLight();
        spotLight.setColor(new ColorRGBA(9f, 0.f, 0f, 1.0f));
        spotLight.setSpotInnerAngle(satModel.getExplorationAngle());
        spotLight.setSpotOuterAngle(satModel.getExplorationAngle());
        spotLight.setSpotRange(30800);
        rootNode.addLight(spotLight);

        //Shockwave shockwave = new Shockwave(assetManager, rootNode);

        SatView satView = new SatView(satModel, sat, spotLight, null);
        views.add(satView);
      }
    }

    startWorldUpdate();
  }

  public void startWorldUpdate() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        while (!isStopped) {
          update();
        }
      }
    }).start();
  }

  private void update() {
    try {
      if (app.isPlaying()) {
        world.update();
      }
      Thread.sleep(50);
    } catch (InterruptedException exc) {
      isStopped = true;
    }
  }

  public void updateView() {
    for (IView view : views) {
      view.update();
    }
    timeLabel.getNiftyControl(Label.class).setText(world.getCurrentTime().toString());
  }

  public World getWorld() {
    return world;
  }

  public void bindTimeLabel(Element timeLabel){
    this.timeLabel = timeLabel;
  }
}
