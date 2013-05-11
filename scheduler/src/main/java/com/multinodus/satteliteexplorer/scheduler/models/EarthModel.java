package com.multinodus.satteliteexplorer.scheduler.models;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.multinodus.satteliteexplorer.scheduler.transformations.QuaternionTransformations;
import com.multinodus.satteliteexplorer.scheduler.transformations.SI_Transform;
import com.multinodus.satteliteexplorer.scheduler.util.DateTimeConstants;
import com.multinodus.satteliteexplorer.scheduler.util.VectorConstants;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 14.12.12
 * Time: 22:04
 * To change this template use File | Settings | File Templates.
 */
public class EarthModel implements IUpdatable {
  private float rotateAngle;

  private float yearAngle = 0.0f;

  private final static float DELTA = -1.58f;

  public EarthModel() {
    Matrix3f matrix3f = new Matrix3f();
    matrix3f.fromAngleAxis(DELTA, new Vector3f(0, 1, 0));

    SunModel.initializeSun(new Vector3f(0, 0, 1), 0);
    SI_Transform.greenwich = matrix3f.mult(VectorConstants.right);
  }

  public Date dateTimeFrom(float yearAngle) {
    float days = (float) (365.25f * yearAngle / (2 * Math.PI));
    return new Date(SI_Transform.INITIAL_TIME.getTime() + (long) (days * DateTimeConstants.MSECS_IN_DAY));
  }

  public void yearAngleFrom(Date date) {
    long ts = date.getTime() - SI_Transform.INITIAL_TIME.getTime();
    double minutes = (ts / DateTimeConstants.MSECS_IN_MINUTE) % (525960);
    float angle = (float) (2 * Math.PI * ((float) minutes / 525960.0f));
    yearAngle = angle;
    float angle2 = ((float) minutes / 1440.0f);
    angle2 = angle2 - (float) Math.round(angle2);
    rotateAngle = (float) (2 * Math.PI * angle2);
  }

  public void update() {
    rotateAngle += SI_Transform.EARTH_SPEED;
    rotateAngle %= (float) (2 * Math.PI);

    yearAngle += SI_Transform.EARTH_SPEED / 365.25f;

    SunModel.update(yearAngle);

    Matrix3f rotationY = new Matrix3f();
    rotationY.fromAngleAxis(SI_Transform.EARTH_SPEED, new Vector3f(0, 1, 0));

    SI_Transform.greenwich = rotationY.mult(SI_Transform.greenwich);

    QuaternionTransformations.I().basicVectors.setGSK(SI_Transform.greenwich,
        VectorConstants.up.cross(SI_Transform.greenwich), VectorConstants.up);
  }

  public float getRotateAngle() {
    return rotateAngle;
  }

  public Date getCurrentTime() {
    return dateTimeFrom(yearAngle);
  }
}
