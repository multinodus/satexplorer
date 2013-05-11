package com.multinodus.satteliteexplorer.ui.scene.models;

import com.jme3.math.Matrix3f;
import com.jme3.scene.Spatial;
import com.multinodus.satteliteexplorer.scheduler.models.RegionModel;
import com.multinodus.satteliteexplorer.scheduler.util.VectorConstants;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 17.12.12
 * Time: 22:59
 * To change this template use File | Settings | File Templates.
 */
public class RegionView implements IView {
  RegionModel regionModel;
  private Spatial spatial;

  public RegionView(RegionModel regionModel, Spatial spatial) {
    this.regionModel = regionModel;
    this.spatial = spatial;
    update();
  }

  public void update() {
    if (spatial != null) {
      spatial.setLocalTranslation(regionModel.getPosition());
      Matrix3f matrix3f = new Matrix3f();
      matrix3f.fromStartEndVectors(VectorConstants.right, regionModel.getPosition().normalize());
      spatial.setLocalRotation(matrix3f);
    }
  }
}
