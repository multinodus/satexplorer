package satteliteExplorer.scheduler.models;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import satteliteExplorer.scheduler.transformations.QuaternionTransformations;
import satteliteExplorer.scheduler.transformations.SI_Transform;
import satteliteExplorer.scheduler.util.VectorConstants;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 14.12.12
 * Time: 22:02
 * To change this template use File | Settings | File Templates.
 */
public class SunModel {
  public static Vector3f Position;
  public static Vector3f PointOfSpringEquinox;


  public static Vector3f RotToEcl(Vector2f vectorXZ) {
    Vector3f vector = new Vector3f(vectorXZ.x, 0, vectorXZ.y);
    return createRotationX().mult(vector);
  }

  private static Matrix3f createRotationX() {
    Matrix3f matrix3f = new Matrix3f();
    matrix3f.fromAngleAxis(SI_Transform.ANGLE_EARTH, new Vector3f(1, 0, 0));
    return matrix3f;
  }

  public static Vector3f calcutePosition(float angle) {
    Vector3f axis = createRotationX().mult(new Vector3f(0, 0, 1));
    Vector3f axis2 = axis.cross(VectorConstants.right);

    Matrix3f matrix3f = new Matrix3f();
    matrix3f.fromAngleAxis((float) (angle % (2 * Math.PI)), axis2);
    return matrix3f.mult(axis);
  }

  public static void update(float angle) {
    Vector3f axis = createRotationX().mult(new Vector3f(0, 0, 1));
    Vector3f axis2 = axis.cross(VectorConstants.right);

    Matrix3f m = new Matrix3f();
    m.fromAngleAxis((float) (angle % (2 * Math.PI)), axis2);
    Position = m.mult(axis);

    SI_Transform.sunPosition = Position;

    Matrix3f mp = new Matrix3f();
    mp.fromAngleAxis((float) Math.PI / 2, axis2);
    PointOfSpringEquinox = mp.mult(axis);

    SI_Transform.pointOfSpringEquinox = PointOfSpringEquinox;

    Vector3f i1 = PointOfSpringEquinox.normalize();
    Vector3f i2 = VectorConstants.up.cross(PointOfSpringEquinox).normalize();
    Vector3f i3 = VectorConstants.up;

    QuaternionTransformations.I().basicVectors.setISK(i1, i2, i3);

    Matrix3f matrix3f = new Matrix3f();
    matrix3f.fromAngleAxis((float) Math.PI / 2, Position.cross(VectorConstants.left));

    Vector3f ie_1 = Position.normalize();
    Vector3f ie_2 = matrix3f.mult(Position).normalize();
    Vector3f ie_3 = ie_1.cross(ie_2).normalize();
    QuaternionTransformations.I().basicVectors.setESK(ie_1, ie_2, ie_3);
  }

  public static void initializeSun(Vector3f sunPosition, float phase) {
    Vector3f axis = sunPosition.cross(VectorConstants.down);
    Matrix3f matrix3f = new Matrix3f();
    matrix3f.fromAngleAxis(SI_Transform.ANGLE_EARTH, axis);
    Position = matrix3f.mult(sunPosition);

    SI_Transform.sunPosition = Position;

    Matrix3f mp = new Matrix3f();
    mp.fromAngleAxis((float) Math.PI / 2, Position.cross(VectorConstants.right));

    PointOfSpringEquinox = mp.mult(Position);

    SI_Transform.pointOfSpringEquinox = PointOfSpringEquinox;

    Vector3f i1 = PointOfSpringEquinox.normalize();
    Vector3f i2 = VectorConstants.up.cross(PointOfSpringEquinox).normalize();
    Vector3f i3 = VectorConstants.up;


    QuaternionTransformations.I().basicVectors.setISK(i1, i2, i3);

    Matrix3f m = new Matrix3f();
    m.fromAngleAxis((float) Math.PI / 2, Position.cross(VectorConstants.left));

    Vector3f ie_1 = Position.normalize();
    Vector3f ie_2 = m.mult(Position).normalize();
    Vector3f ie_3 = ie_1.cross(ie_2).normalize();
    QuaternionTransformations.I().basicVectors.setESK(ie_1, ie_2, ie_3);
  }
}
