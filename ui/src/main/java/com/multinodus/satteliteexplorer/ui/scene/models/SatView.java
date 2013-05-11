package com.multinodus.satteliteexplorer.ui.scene.models;

import com.jme3.light.SpotLight;
import com.jme3.scene.Spatial;
import com.multinodus.satteliteexplorer.scheduler.models.SatModel;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 16.12.12
 * Time: 18:27
 * To change this template use File | Settings | File Templates.
 */
public class SatView implements IView {
  SatModel satModel;
  Spatial spatial;
  SpotLight spotLight;
  //Shockwave shockwave;

  public SatView(SatModel satModel, Spatial spatial, SpotLight spotLight, Shockwave shockwave) {
    this.satModel = satModel;
    this.spatial = spatial;
    this.spotLight = spotLight;
    //this.shockwave = shockwave;
    update();
  }

  public void update() {
    spatial.setLocalTranslation(satModel.getPosition());
    spotLight.setPosition(satModel.getPosition());
    spotLight.setDirection(satModel.getPosition());

    //shockwave.getShockwave().setLocalTranslation(satModel.getRealPosition());
  }
}
