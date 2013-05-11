package com.multinodus.satteliteexplorer.ui.scene.models;

import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.multinodus.satteliteexplorer.scheduler.models.EarthModel;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 16.12.12
 * Time: 18:27
 * To change this template use File | Settings | File Templates.
 */
public class EarthView implements IView {
  EarthModel earthModel;
  Spatial spatial;
  private float previousAngle;

  public EarthView(EarthModel earthModel, Spatial spatial) {
    this.earthModel = earthModel;
    this.spatial = spatial;
    update();
  }

  public void update() {
    float earthAngle = earthModel.getRotateAngle() > 0 ? earthModel.getRotateAngle() : earthModel.getRotateAngle() + FastMath.TWO_PI;

    float angle = Math.abs(earthAngle - previousAngle);

    if (angle > FastMath.HALF_PI) {
      angle = FastMath.TWO_PI - previousAngle + earthAngle;
    }

    spatial.rotate(0, angle, 0);

    previousAngle = earthAngle;
  }
}
