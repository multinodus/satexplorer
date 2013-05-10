package satteliteExplorer.ui.scene;

import com.jme3.asset.AssetManager;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import satteliteExplorer.scheduler.World;
import satteliteExplorer.scheduler.models.EarthModel;
import satteliteExplorer.scheduler.models.IUpdatable;
import satteliteExplorer.scheduler.models.RegionModel;
import satteliteExplorer.scheduler.models.SatModel;
import satteliteExplorer.ui.scene.models.EarthView;
import satteliteExplorer.ui.scene.models.IView;
import satteliteExplorer.ui.scene.models.RegionView;
import satteliteExplorer.ui.scene.models.SatView;
import satteliteExplorer.ui.scene.models.planet.Planet;
import satteliteExplorer.ui.scene.models.planet.PlanetAppState;

import java.util.ArrayList;
import java.util.Collection;

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

  public Scene(AssetManager assetManager, Node rootNode, PlanetAppState planetAppState) {
    world = new World();

    Node satNode = new Node("SatNode");
    rootNode.attachChild(satNode);

    Spatial satPrototype = assetManager.loadModel("Scenes/antenna.j3o");

    for (IUpdatable entity : world.getEntities()) {
      if (entity instanceof RegionModel) {
        RegionModel regionModel = (RegionModel) entity;

        //Cylinder sphere = new Cylinder(32, 32, 1000, 10000);
//        Torus sphere = new Torus(32, 32, 100, regionModel.getRadius());
//        Geometry regionSpatial = new Geometry("", sphere);
//        regionSpatial.setMaterial(assetManager.loadMaterial("Materials/EasyMaterial.j3m"));
//        rootNode.attachChild(regionSpatial);

        RegionView regionView = new RegionView(regionModel, null);
        views.add(regionView);
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
      if (entity instanceof EarthModel) {
        Planet planet = Utility.createEarthLikePlanet(assetManager, 63781f, 800f, 4);
        planetAppState.addPlanet(planet);
        rootNode.attachChild(planet);

        EarthView earthView = new EarthView((EarthModel) entity, planet);
        views.add(earthView);
      }
    }
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
      world.update();
      Thread.sleep(50);
    } catch (InterruptedException exc) {
      isStopped = true;
    }
  }

  public void updateView() {
    for (IView view : views) {
      view.update();
    }
  }

  public World getWorld() {
    return world;
  }
}
