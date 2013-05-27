package com.multinodus.satteliteexplorer.ui.scene.models;

import com.jme3.math.Matrix3f;
import com.jme3.scene.Spatial;
import com.multinodus.satteliteexplorer.scheduler.models.DataCenterModel;
import com.multinodus.satteliteexplorer.scheduler.models.RegionModel;
import com.multinodus.satteliteexplorer.scheduler.util.VectorConstants;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 17.12.12
 * Time: 22:59
 * To change this template use File | Settings | File Templates.
 */
public class DataCenterView implements IView {
  DataCenterModel dataCenterModel;
  private Spatial spatial;

  public DataCenterView(DataCenterModel dataCenterModel, Spatial spatial) {
    this.dataCenterModel = dataCenterModel;
    this.spatial = spatial;
    update();
  }

  public void update() {
    spatial.setLocalTranslation(dataCenterModel.getPosition());
    Matrix3f matrix3f = new Matrix3f();
    matrix3f.fromStartEndVectors(VectorConstants.right, dataCenterModel.getPosition().normalize());
    spatial.setLocalRotation(matrix3f);
  }
}
