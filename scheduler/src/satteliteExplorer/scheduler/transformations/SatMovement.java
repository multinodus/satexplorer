package satteliteExplorer.scheduler.transformations;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import satteliteExplorer.scheduler.models.SunModel;
import satteliteExplorer.scheduler.util.Pair;
import satteliteExplorer.scheduler.util.VectorConstants;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 16.12.12
 * Time: 14:21
 * To change this template use File | Settings | File Templates.
 */
public class SatMovement implements ISatMovement {
  private volatile static SatMovement _instance;
  private static Object _sync = new Object();

  private SatMovement() {
  }

  public static SatMovement getInstance() {
    if (_instance == null) {
      synchronized (_sync) {
        _instance = new SatMovement();
      }
    }
    return _instance;
  }

  public Pair<Vector3f, Vector3f> TransformEllipseToOrbit(Vector2f pos,
                                                          float inclination, float longitudeOfAscendingNode, float argumentOfPerigee) {
    Vector3f position = new Vector3f(pos.x, 0, pos.y);

    Matrix3f LongitudeOfAscendingNodeMatrix3f = new Matrix3f();
    LongitudeOfAscendingNodeMatrix3f.fromAngleAxis(longitudeOfAscendingNode, VectorConstants.up);

    Vector3f axis = LongitudeOfAscendingNodeMatrix3f.mult(SunModel.PointOfSpringEquinox.normalize());

    Matrix3f InclinationMatrix3f = new Matrix3f();
    InclinationMatrix3f.fromAngleAxis(inclination, axis);

    Vector3f axis2 = InclinationMatrix3f.mult(SunModel.PointOfSpringEquinox.normalize());
    axis2.x += 0.00001f;
    Vector3f axis3 = axis.cross(axis2).normalize();
    Matrix3f ArgumentOfPerigee = new Matrix3f();
    ArgumentOfPerigee.fromAngleAxis((float) argumentOfPerigee, axis3);

    Matrix3f res = LongitudeOfAscendingNodeMatrix3f.mult(InclinationMatrix3f).mult(
        ArgumentOfPerigee);
    //Matrix3f res = InclinationMatrix3f;//ArgumentOfPerigee.mult(InclinationMatrix3f).mult(
    //        //LongitudeOfAscendingNodeMatrix3f);

    return new Pair<Vector3f, Vector3f>(res.mult(position), axis3);
  }

  public double CalcuteTrueAnomaly(Vector2f posXZ) {
    Vector3f position = new Vector3f(posXZ.x, 0, posXZ.y);
    float buffer = position.dot(new Vector3f(1, 0, 0)) / position.length();
    if (buffer > 1) buffer = 1;
    else if (buffer < -1) buffer = -1;

    double angle = Math.acos(buffer);

    if (position.x < 0) {
      angle = 2 * Math.PI - angle;
    }

    return angle;
  }

  public Vector2f GetInitialPosition(double a, double e, double trueAnomaly) {
    Vector2f res = new Vector2f((float) (a * (1 - e * e) / (1 + e * Math.cos(trueAnomaly))), 0);
    Matrix3f matrix3f = new Matrix3f();
    matrix3f.fromAngleAxis((float) SI_Transform.twoPiRangeConverter(trueAnomaly - Math.PI), new Vector3f(0, 0, 1));
    Vector3f v3 = matrix3f.mult(new Vector3f(res.x, res.y, 0));
    return new Vector2f(v3.x, v3.y);
  }

  public Vector2f GetInitialSpeed(Vector2f initialPosition, double a, double e) {
    Vector3f ellipseCenter = new Vector3f((float) (e * a), 0, 0);
    Vector3f velocityVector = new Vector3f(initialPosition.x, initialPosition.y, 0).subtract(ellipseCenter).cross(new Vector3f(0, 0, 1.0000004f));
    velocityVector = velocityVector.normalize();
    Vector3f res = velocityVector.mult((float) (Math.sqrt(SI_Transform.GRAVITY_CONST * SI_Transform.EARTH_MASS * (2.0f /
        initialPosition.length() - 1 / a))));
    return new Vector2f(res.x, res.y);
  }

  public Vector2f GetAcceleration(Vector2f position) {
    float lenght = position.length();
    return position.mult((float) (-SI_Transform.GRAVITY_CONST * SI_Transform.EARTH_MASS / (lenght * lenght * lenght)));
  }

  private Pair<Vector2f, Vector2f> EilerMove(Vector2f position, Vector2f velocity, float T, float dt) {
    Vector2f bufPosition = position;
    com.jme3.math.Vector2f newVelocity = velocity;

    float t = dt;
    do {
      newVelocity = EilerMove(newVelocity, GetAcceleration(bufPosition), dt);
      bufPosition = bufPosition.add(newVelocity.mult(dt));
      t += dt;
    }
    while (t < T);
    return new Pair<Vector2f, Vector2f>(bufPosition, newVelocity);
  }

  private Vector2f EilerMove(Vector2f position, Vector2f velocity, float dt) {
    return position.add(velocity.mult(dt));
  }

  public Pair<Vector2f, Vector2f> CalcuteNextPosition(float trueAnomaly, float e, Vector2f position, Vector2f velocity, float T) {
    if (trueAnomaly > Math.PI) {
      trueAnomaly = (float) (2 * Math.PI) - trueAnomaly;
    }

    float N = 5 * e / (float) Math.pow(trueAnomaly, 3);
    if (N > 10000) {
      N = 10000;
    }
    if (N < 100) {
      N = 100;
    }

    float dt = T / N;
    return EilerMove(position, velocity, T, dt);
  }
}
