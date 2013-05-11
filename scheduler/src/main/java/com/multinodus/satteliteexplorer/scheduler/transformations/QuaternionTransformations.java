package com.multinodus.satteliteexplorer.scheduler.transformations;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.multinodus.satteliteexplorer.db.entities.Orbit;
import com.multinodus.satteliteexplorer.db.entities.Region;
import com.multinodus.satteliteexplorer.scheduler.models.SatModel;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 14.12.12
 * Time: 21:03
 * To change this template use File | Settings | File Templates.
 */
public class QuaternionTransformations {
  public BasicVectors basicVectors;

  public static Object exSync = new Object();

  private volatile static QuaternionTransformations _instance;
  private static Object _sync = new Object();

  private QuaternionTransformations() {
    basicVectors = new BasicVectors();
  }

  public static QuaternionTransformations I() {
    if (_instance == null) {
      synchronized (exSync) {
        synchronized (_sync) {
          _instance = new QuaternionTransformations();
        }
      }
    }
    return _instance;
  }

  public Quaternion createQuaternion(Vector3f vectorPart, double scalarPart) {
    Quaternion quat = new Quaternion();
    quat = quat.fromAngleAxis((float) scalarPart, vectorPart);
    return quat;
  }

  public Quaternion conjugate(Quaternion quaternion) {
    return new Quaternion(-quaternion.getX(), -quaternion.getY(), -quaternion.getZ(), quaternion.getW());
  }

  public float calcuteLatitudeArgument(float argumentOfPerigee, float trueAnomaly) {
    return argumentOfPerigee + trueAnomaly;
  }

  public Quaternion quaternionISK_KA(Quaternion longitudeOfAscendingNode, Quaternion inclination, Quaternion latitudeArgument) {
    return quaternionISK_OSK(longitudeOfAscendingNode, inclination, latitudeArgument);
  }

  public Quaternion quaternionISK_OSK(Quaternion longitudeOfAscendingNode, Quaternion inclination, Quaternion latitudeArgument) {
    return latitudeArgument.mult(inclination).mult(longitudeOfAscendingNode);
  }

  public Quaternion quanterionISK_GSK(float s) {
    return createQuaternion(basicVectors.i_3, s);
  }

  public Quaternion quaternionGSK_Ob(Quaternion longitude, Quaternion latitude) {
    return longitude.mult(latitude);
  }

  public Quaternion quaternionISK_Ob(Quaternion quaternionISK_GSK, Quaternion longitude, Quaternion latitude) {
    return latitude.mult(quaternionISK_GSK).mult(longitude);
  }

  public Quaternion quaternionKA_Ob(Quaternion quaternionISK_KA, Quaternion quaternionISK_Ob) {
    return quaternionISK_Ob.mult(conjugate(quaternionISK_KA));
  }

  public Quaternion quaternionISK_Ob(Quaternion quaternionISK_KA, Quaternion quaternionKA_Ob) {
    return quaternionISK_KA.mult(quaternionKA_Ob);
  }

  public float calcute_r(float visiblesAngle, float H, float EarthsRadius) {
    return (float) Math.asin(((EarthsRadius + H) / EarthsRadius) * Math.sin(visiblesAngle)) - visiblesAngle;
  }

  public boolean inFieldOfView(Quaternion quaternionKA_Ob, float visiblesAngle, float H,
                               float EarthsRadius) {
    return (2 * Math.acos(quaternionKA_Ob.getW()) < calcute_r(visiblesAngle, H, EarthsRadius));
  }

  public float calcute_BetaMax(float visiblesAngle, float H, float EarthsRadius) {
    return (float) Math.acos(((EarthsRadius * Math.cos(visiblesAngle)) / (EarthsRadius + H))) - visiblesAngle;
  }

  public boolean inFieldOfView(Quaternion quaternionISK_Ob, Quaternion quaternionISK_KA,
                               float visiblesAngle, float H, float EarthsRadius) {
    double angle = SI_Transform.calcuteAngle(conjugate(quaternionISK_Ob).mult(basicVectors.i_1),
        conjugate(quaternionISK_KA).mult(basicVectors.i_1));
    return angle < calcute_r(visiblesAngle, H, EarthsRadius);
  }

  public boolean inFieldOfRadioView(Quaternion quaternionISK_Ob, Quaternion quaternionISK_KA,
                                    float visiblesAngle, float H, float EarthsRadius) {
    double angle = SI_Transform.calcuteAngle(conjugate(quaternionISK_Ob).mult(basicVectors.i_1),
        conjugate(quaternionISK_KA).mult(basicVectors.i_1));

    return angle < calcute_BetaMax(visiblesAngle, H, EarthsRadius);
  }

  // Ac - угол между направлением на т.в.р и линией Земля-Солнце
  public Quaternion quaternionISK_ESK(double Ac) {
    double Bc = 2 * Math.PI * (23 + 27 / 60) / 360;

    Quaternion quat1 = new Quaternion().fromAngleAxis((float) -Bc, basicVectors.i_1);
    Quaternion quat2 = new Quaternion().fromAngleAxis((float) Ac, basicVectors.i_3);

    return quat2.mult(quat1);
  }

  public Quaternion qauternionDOb_ESK(Quaternion quaternionISK_Ob, Quaternion quaternionISK_ESK) {
    return quaternionISK_ESK.mult(conjugate(quaternionISK_Ob));
  }

  public boolean isLit(Quaternion quaternionISK_ESK, Quaternion quaternionISK_Ob, double Acr) {
    double angle = SI_Transform.calcuteAngle(conjugate(quaternionISK_ESK).mult(basicVectors.i_1),
        conjugate(quaternionISK_Ob).mult(basicVectors.i_1));

    return (angle < Math.PI / 2 - Acr);
  }

  public boolean isLit(Quaternion quaternionDOb_ESK, double Acr) {
    double res = Math.acos(quaternionDOb_ESK.getW());
    return (2 * Math.acos(quaternionDOb_ESK.getW()) < Math.PI / 2 - Acr);
  }

  public boolean inFieldOfView(SatModel sat, Region region, float visiblesAngle, float s) {
    Orbit orbit = sat.getOrbit();
    Quaternion quaternionISK_GSK = quanterionISK_GSK(s);

    Quaternion quaternionISK_KA = quaternionISK_KA(createQuaternion(basicVectors.i_3, -orbit.getLongitudeOfAscendingNode()),
        createQuaternion(basicVectors.i_1, -orbit.getInclination()), createQuaternion(basicVectors.i_3,
        -orbit.getArgumentOfPericenter() + Math.PI + sat.getTrueAnomaly()));

    Quaternion quaternionISK_Ob = quaternionISK_Ob(quaternionISK_GSK, createQuaternion(basicVectors.i_3,
        -region.getLongitude()),
        createQuaternion(basicVectors.i_2, region.getLatitude()));

    Quaternion quaternionKA_Ob = quaternionKA_Ob(quaternionISK_KA, quaternionISK_Ob);

    return inFieldOfView(quaternionISK_Ob, quaternionISK_KA, visiblesAngle, (float) sat.getRealPosition().length() -
        (float) SI_Transform.EARTH_RADIUS, (float) SI_Transform.EARTH_RADIUS);
  }

  public boolean isLit(Region region, double Acr, float s, double anglePOSEandSun) {
    Quaternion quaternionISK_GSK = quanterionISK_GSK(s);

    Quaternion quaternionISK_Ob = quaternionISK_Ob(quaternionISK_GSK, createQuaternion(basicVectors.i_3, -region.getLongitude()),
        createQuaternion(basicVectors.i_2, region.getLatitude()));

    Quaternion quaternionISK_ESK = quaternionISK_ESK(anglePOSEandSun);

    Quaternion quaternionDOb_ESK = qauternionDOb_ESK(quaternionISK_Ob, quaternionISK_ESK);

    return isLit(quaternionISK_ESK, quaternionISK_Ob, Acr);
  }

  public boolean isVisible(SatModel sat, Region region) {
    Matrix3f matrix3f = new Matrix3f();
    matrix3f.fromAngleAxis((float) Math.PI / 2, sat.getOrbitNormal().normalize());

    Vector3f i1 = sat.getRealPosition().normalize();
    Vector3f i2 = matrix3f.mult(i1);
    Vector3f i3 = i1.cross(i2);

    basicVectors.setOSK(i1, i2, i3);

    float s = (float) SI_Transform.calcuteAngle(SI_Transform.pointOfSpringEquinox, SI_Transform.greenwich);
    Vector3f cross = SI_Transform.pointOfSpringEquinox.cross(SI_Transform.greenwich);
    if (cross.y > 0) {
      s = (float) (2 * Math.PI) - s;
    }

    float anglePOSandSun = (float) SI_Transform.calcuteAngle(SI_Transform.pointOfSpringEquinox, SI_Transform.sunPosition);

    return inFieldOfView(sat, region, sat.getVisibleAngle(), s);
  }
}
