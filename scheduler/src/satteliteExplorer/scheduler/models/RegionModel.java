package satteliteExplorer.scheduler.models;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import satteliteExplorer.db.entities.Region;
import satteliteExplorer.scheduler.transformations.QuaternionTransformations;
import satteliteExplorer.scheduler.transformations.SI_Transform;
import satteliteExplorer.scheduler.util.VectorConstants;


/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 14.12.12
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
public class RegionModel implements IUpdatable {
  Region region;
  Vector3f position;
  Vector3f realPosition;
  boolean isNight;

  public RegionModel(Region region) {
    this.region = region;

    position = calcPosition(region.getLongitude(), region.getLatitude(),
        (float) SI_Transform.distanceSIInModel(SI_Transform.EARTH_RADIUS));

    realPosition = new Vector3f();
    realPosition.setX((float) SI_Transform.distanceModelInSI(position.x));
    realPosition.setY((float) SI_Transform.distanceModelInSI(position.y));
    realPosition.setZ((float) SI_Transform.distanceModelInSI(position.z));
  }

  private Vector3f calcPosition(double longitude, double latitude, double radius) {
    Vector3f res = new Vector3f((float) radius, 0, 0);

    double angle = Math.acos(VectorConstants.right.dot(SI_Transform.greenwich) / SI_Transform.greenwich.length());
    if (SI_Transform.greenwich.cross(VectorConstants.right).y > 0) {
      angle *= -1;
    }

    double rotateAngle = longitude + angle;
    if (rotateAngle > Math.PI) {
      rotateAngle = -2 * Math.PI + rotateAngle;
    }

    Matrix3f rotationZ = new Matrix3f();
    rotationZ.fromAngleAxis((float) latitude, new Vector3f(0, 0, 1));

    Matrix3f rotationY = new Matrix3f();
    rotationY.fromAngleAxis((float) rotateAngle, new Vector3f(0, 1, 0));

    res = rotationY.mult(rotationZ).mult(res);
    return res;
  }

  private double calcLongitude(Vector3f position) {
    Vector3f projPosition = new Vector3f(position.x, 0, position.z);
    double angle = Math.acos(SI_Transform.greenwich.dot(projPosition) /
        (SI_Transform.greenwich.length() * projPosition.length()));
    if (SI_Transform.greenwich.cross(projPosition).y < 0) {
      angle *= -1;
    }

    if (angle > Math.PI) {
      angle = -2 * Math.PI + angle;
    }

    return angle;
  }

  private double calcLatitude(Vector3f position) {
    double angle = Math.acos(Math.sqrt(position.x * position.x + position.z * position.z) / position.length());
    if (position.y < 0) {
      angle *= -1;
    }
    return angle;
  }

  public void update() {
    position = calcPosition(region.getLongitude(), region.getLatitude(),
        (float) SI_Transform.distanceSIInModel(SI_Transform.EARTH_RADIUS));

    realPosition.setX((float) SI_Transform.distanceModelInSI(position.x));
    realPosition.setY((float) SI_Transform.distanceModelInSI(position.y));
    realPosition.setZ((float) SI_Transform.distanceModelInSI(position.z));

    float s = (float) SI_Transform.calcuteAngle(SI_Transform.pointOfSpringEquinox, SI_Transform.greenwich);
    Vector3f cross = SI_Transform.pointOfSpringEquinox.cross(SI_Transform.greenwich);
    if (cross.y > 0) {
      s = (float) (2 * Math.PI) - s;
    }

    float anglePOSandSun = (float) SI_Transform.calcuteAngle(SI_Transform.pointOfSpringEquinox, SI_Transform.sunPosition);

    if (SI_Transform.pointOfSpringEquinox.cross(SI_Transform.sunPosition).z > 0) {
      anglePOSandSun = (float) (2 * Math.PI - anglePOSandSun);
    }

    isNight = !QuaternionTransformations.I().isLit(region, 0.314, s, anglePOSandSun);
  }

  public Vector3f getRealPosition() {
    return realPosition;
  }

  public Vector3f getPosition() {
    return position;
  }

  public float getRadius() {
    return region.getRadius();
  }
}
