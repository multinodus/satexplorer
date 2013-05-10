package satteliteExplorer.scheduler.models;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import satteliteExplorer.db.entities.Orbit;
import satteliteExplorer.db.entities.Sat;
import satteliteExplorer.scheduler.transformations.ISatMovement;
import satteliteExplorer.scheduler.transformations.SI_Transform;
import satteliteExplorer.scheduler.transformations.SatMovement;
import satteliteExplorer.scheduler.util.Pair;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 14.12.12
 * Time: 22:00
 * To change this template use File | Settings | File Templates.
 */
public class SatModel implements IUpdatable {
  private double trueAnomaly;
  private Vector3f position;
  private Vector3f realPosition;
  //-----------------------------------------------------------
  private Sat sat;
  private Orbit orbit;

  private double prevE;
  private double prevA;

  private Vector2f orbitPosition;
  private Vector2f orbitVelocity;
  private Vector3f orbitNormal;

  private ISatMovement satMovement;

  private double T = 0;
  private double dt = 0;

  private int counterOfReturn = 0;
  private int timeOfReturn = 5;

  private float explorationAngle;

  public SatModel(Sat sat) {
    this.sat = sat;
    this.orbit = sat.getOrbit();
    satMovement = SatMovement.getInstance();

    initialize();
  }

  private void initialize() {
    realPosition = new Vector3f();
    position = new Vector3f();
    prevA = orbit.getSemiMajorAxis();
    prevE = orbit.getEccentricity();

    orbitPosition = satMovement.getInitialPosition(orbit.getSemiMajorAxis(), orbit.getEccentricity(),
        SI_Transform.twoPiRangeConverter(trueAnomaly));
    orbitVelocity = satMovement.getInitialSpeed(orbitPosition, orbit.getSemiMajorAxis(), orbit.getEccentricity());

    T = 2 * Math.PI;
    double b1 = SI_Transform.timePeriodRotationInSI(orbit.getSemiMajorAxis());
    double b2 = SI_Transform.timeSIInModel(SI_Transform.EARTH_SPEED, b1);
    dt = b1 / b2;

    update();

    explorationAngle = (float) Math.atan((20 * sat.getEquipment().getSpan() / 2000) / (sat.getOrbit().getSemiMajorAxis() - SI_Transform.EARTH_RADIUS)) * FastMath.RAD_TO_DEG;
  }

  public void update() {
        /*if (orbit.getSemiMajorAxis() != prevA || orbit.getEccentricity() != prevE || counterOfReturn == timeOfReturn) {
            initialize();
            counterOfReturn = 0;
        } else {
            counterOfReturn++;
        }*/

    Pair<Vector2f, Vector2f> pair = satMovement.calcuteNextPosition((float) (trueAnomaly - Math.PI / 2), orbit.getEccentricity(), orbitPosition, orbitVelocity,
        (float) dt);
    orbitPosition = pair.f;
    orbitVelocity = pair.s;


    trueAnomaly = SI_Transform.twoPiRangeConverter(satMovement.calcuteTrueAnomaly(orbitPosition) + Math.PI / 2);

    Pair<Vector3f, Vector3f> pair3 = satMovement.transformEllipseToOrbit(new Vector2f(orbitPosition.x,
        orbitPosition.y), orbit.getInclination(), orbit.getLongitudeOfAscendingNode(), orbit.getArgumentOfPericenter());
    realPosition = pair3.f;
    orbitNormal = pair3.s;

    position.setX((float) SI_Transform.distanceSIInModel(realPosition.x));
    position.setY((float) SI_Transform.distanceSIInModel(realPosition.y));
    position.setZ((float) SI_Transform.distanceSIInModel(realPosition.z));
  }

  public Vector3f getRealPosition() {
    return realPosition;
  }

  public Vector3f getPosition() {
    return position;
  }

  public Orbit getOrbit() {
    return orbit;
  }

  public double getTrueAnomaly() {
    return trueAnomaly;
  }

  public Vector3f getOrbitNormal() {
    return orbitNormal;
  }

  public float getVisibleAngle() {
    return FastMath.DEG_TO_RAD * sat.getEquipment().getCriticalAngle();
  }

  public Sat getSat() {
    return sat;
  }

  public float getExplorationAngle() {
    return explorationAngle;
  }
}
