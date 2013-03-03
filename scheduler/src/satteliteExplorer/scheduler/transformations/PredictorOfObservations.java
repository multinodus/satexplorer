package satteliteExplorer.scheduler.transformations;

import com.jme3.math.*;
import satteliteExplorer.db.entities.Orbit;
import satteliteExplorer.db.entities.Region;
import satteliteExplorer.scheduler.models.SatModel;
import satteliteExplorer.scheduler.models.SunModel;
import satteliteExplorer.scheduler.util.DateTimeConstants;
import satteliteExplorer.scheduler.util.Pair;
import satteliteExplorer.scheduler.util.VectorConstants;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 14.12.12
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class PredictorOfObservations {
  private volatile static satteliteExplorer.scheduler.transformations.PredictorOfObservations _instance;

  private static Object _sync = new Object();

  private PredictorOfObservations() {
  }

  public static satteliteExplorer.scheduler.transformations.PredictorOfObservations getInstance() {
    if (_instance == null) {
      synchronized (_sync) {
        _instance = new satteliteExplorer.scheduler.transformations.PredictorOfObservations();
      }
    }
    return _instance;
  }

  public Pair<SatModel, List<PredictorDataElement>> BestObservers(Date now, Date end, Region region, float detalization,
                                                                  List<SatModel> sats) {
    LinkedList<SatModel> res = new LinkedList<SatModel>();

    List<Pair<SatModel, Double>> timeOfObservation = new ArrayList<Pair<SatModel, Double>>();

    for (SatModel sat : sats) {
      List<PredictorDataElement> data = PredictAnglesToObject(now, end, sat, region, detalization);
      double time = data.size() > 1 ? AllSecondsOfObservation(data) : 0;
      timeOfObservation.add(new Pair<SatModel, Double>(sat, time));
    }

    Collections.sort(timeOfObservation, new Comparator<Pair<SatModel, Double>>() {
      @Override
      public int compare(Pair<SatModel, Double> o1, Pair<SatModel, Double> o2) {
        return (int) (o2.s - o1.s);
      }
    });

    for (Pair<SatModel, Double> p : timeOfObservation) {
      res.add(p.f);
    }

    return new Pair<SatModel, List<PredictorDataElement>>(res.get(0), PredictAnglesToObject(now, end, res.get(0), region, detalization));
  }

  public double AllSecondsOfObservation(List<PredictorDataElement> data) {
    int observationPeriodsCount = 0;
    double observationPeriodSeconds = (data.get(1).date.getTime() - data.get(0).date.getTime()) / DateTimeConstants.MSECS_IN_SECOND;

    for (PredictorDataElement element : data) {
      if (element.angle < element.visibleAngle) {
        observationPeriodsCount++;
      }
    }
    return (observationPeriodsCount * observationPeriodSeconds / 60.0);
  }

  public List<PredictorDataElement> PredictAnglesToObject(Date now, Date end, SatModel sat, Region region, float earthSpeed) {
    Date rrt = new Date(System.currentTimeMillis());

    Orbit orbit = sat.getOrbit();

    ISatMovement satsMovement = SatMovement.getInstance();

    List<PredictorDataElement> result = new ArrayList<PredictorDataElement>();

    Vector3f sunPosition = SunModel.Position;
    Vector3f satPosition = new Vector3f(sat.getRealPosition());
    Vector3f orbitNormal = sat.getOrbitNormal();
    Vector3f greenwich = SI_Transform.greenwich;

    double trueAnomaly = sat.getTrueAnomaly();

    Vector2f orbitPosition = satsMovement.GetInitialPosition(orbit.getSemiMajorAxis(), orbit.getEccentricity(),
        SI_Transform.twoPiRangeConverter(sat.getTrueAnomaly()));
    Vector2f orbitVelocity = satsMovement.GetInitialSpeed(orbitPosition, orbit.getSemiMajorAxis(), orbit.getEccentricity());

    synchronized (QuaternionTransformations.exSync) {
      QuaternionTransformations qt = QuaternionTransformations.I();
      BasicVectors basicVectors = new BasicVectors();
      BasicVectors bufBasicVectors = qt.basicVectors;
      qt.basicVectors = basicVectors;

      Vector3f i1_1 = SI_Transform.pointOfSpringEquinox.normalize();
      Vector3f i1_2 = VectorConstants.up.cross(SI_Transform.pointOfSpringEquinox).normalize();
      Vector3f i1_3 = VectorConstants.up;

      basicVectors.setISK(i1_1, i1_2, i1_3);

      Quaternion quaternionLongitude = qt.createQuaternion(basicVectors.i_3, -region.getLongitude());

      Quaternion quaternionLatitude = qt.createQuaternion(basicVectors.i_2, region.getLatitude());

      Quaternion quaternionIL = qt.createQuaternion(basicVectors.i_1, -orbit.getInclination()).mult(
          qt.createQuaternion(basicVectors.i_3, -orbit.getLongitudeOfAscendingNode()));

      float s = (float) SI_Transform.calcuteAngle(SI_Transform.pointOfSpringEquinox, greenwich);

      Vector3f cross = SI_Transform.pointOfSpringEquinox.cross(greenwich);
      if (cross.y > 0) {
        s = (float) (2 * Math.PI) - s;
      }

      double allSeconds = (end.getTime() - now.getTime()) / DateTimeConstants.MSECS_IN_SECOND;

      double seconds = 0;
      double b1 = SI_Transform.timePeriodRotationInSI(orbit.getSemiMajorAxis());
      double b2 = SI_Transform.timeSIInModel(earthSpeed, b1);
      double dt = FastMath.TWO_PI / b2;

      double dsecond = b1 / b2;
      double dsecond2 = SI_Transform.timeModelInSI(earthSpeed, dt);

      while (seconds < allSeconds) {
        Matrix3f matrix3f = new Matrix3f();
        matrix3f.fromAngleAxis(FastMath.HALF_PI, sunPosition.cross(VectorConstants.left));

        Vector3f ie_1 = sunPosition.normalize();
        Vector3f ie_2 = matrix3f.mult(sunPosition).normalize();
        Vector3f ie_3 = ie_1.cross(ie_2).normalize();

        basicVectors.setESK(ie_1, ie_2, ie_3);

        matrix3f = new Matrix3f();
        matrix3f.fromAngleAxis(FastMath.HALF_PI, orbitNormal.normalize());

        Vector3f i1 = satPosition.normalize();
        Vector3f i2 = matrix3f.mult(i1);
        Vector3f i3 = i1.cross(i2);

        basicVectors.setOSK(i1, i2, i3);

        basicVectors.setGSK(greenwich, VectorConstants.up.cross(greenwich), VectorConstants.up);

        double anglePOSEandSun = Math.acos(SI_Transform.pointOfSpringEquinox.dot(
            sunPosition) / (sunPosition.length() * SI_Transform.pointOfSpringEquinox.length()));

        if (SI_Transform.pointOfSpringEquinox.cross(sunPosition).z > 0) {
          anglePOSEandSun = FastMath.TWO_PI - anglePOSEandSun;
        }

        float H = satPosition.length() - (float) SI_Transform.EARTH_RADIUS;

        float visibleAngle = qt.calcute_r((float) sat.getVisibleAngle(), H,
            (float) SI_Transform.EARTH_RADIUS);

        boolean isDay = PredictNight(qt, region, 0.1, s, anglePOSEandSun);

        result.add(new PredictorDataElement(new Date(now.getTime() + (long) (seconds * DateTimeConstants.MSECS_IN_SECOND)), PredictAngleToObject(sat, s, trueAnomaly,
            quaternionLongitude, quaternionLatitude, quaternionIL, qt),
            visibleAngle, isDay));

        seconds += dsecond;
        s -= earthSpeed % FastMath.TWO_PI;

        Matrix3f rotationY = new Matrix3f();
        rotationY.fromAngleAxis(earthSpeed, VectorConstants.up);
        greenwich = rotationY.mult(greenwich);

        Pair<Vector2f, Vector2f> p = satsMovement.CalcuteNextPosition((float) trueAnomaly - FastMath.HALF_PI,
            orbit.getEccentricity(), orbitPosition, orbitVelocity, (float) dsecond);
        orbitPosition = p.f;
        orbitVelocity = p.s;

        Pair<Vector3f, Vector3f> p3 = satsMovement.TransformEllipseToOrbit(orbitPosition, (float) orbit.getInclination(),
            (float) orbit.getLongitudeOfAscendingNode(), (float) orbit.getArgumentOfPericenter());
        satPosition = p3.f;
        orbitNormal = p3.s;

        trueAnomaly = satsMovement.CalcuteTrueAnomaly(orbitPosition) + FastMath.HALF_PI;

        sunPosition = SunModel.calcutePosition(YearAngleFrom(new Date(now.getTime() + (long) (seconds * DateTimeConstants.MSECS_IN_SECOND))));
      }
      double span = System.currentTimeMillis() - rrt.getTime();

      qt.basicVectors = bufBasicVectors;
    }

    return result;
  }

  private float YearAngleFrom(Date date) {
    long ts = date.getTime() - SI_Transform.INITIAL_TIME.getTime();
    double minutes = (ts / DateTimeConstants.MSECS_IN_MINUTE) % (525960);
    float angle = (float) (2 * Math.PI * ((float) minutes / 525960.0f));
    return angle;
  }

  private float PredictAngleToObject(satteliteExplorer.scheduler.models.SatModel sat, float s, double trueAnomaly,
                                     Quaternion quaternionLongitude, Quaternion quaternionLatitude,
                                     Quaternion quaternionIL, satteliteExplorer.scheduler.transformations.QuaternionTransformations qt) {
    Quaternion quaternionWpV = qt.createQuaternion(qt.basicVectors.i_3, -sat.getOrbit().getArgumentOfPericenter() + FastMath.PI +
        trueAnomaly);
    Quaternion quaternionISK_GSK = qt.quanterionISK_GSK(s);

    float angle = (float) satteliteExplorer.scheduler.transformations.SI_Transform.calcuteAngle(
        qt.conjugate(quaternionWpV.mult(quaternionIL)).mult(qt.basicVectors.i_1),
        qt.conjugate(quaternionLatitude.mult(quaternionISK_GSK).mult(quaternionLongitude)).mult(qt.basicVectors.i_1));

    return angle;
  }

  public static boolean PredictNight(satteliteExplorer.scheduler.transformations.QuaternionTransformations qt,
                                     Region region, double Acr, float s, double anglePOSEandEarth) {
    Quaternion quaternionISK_GSK = qt.quanterionISK_GSK(s);

    Quaternion quaternionISK_Ob = qt.quaternionISK_Ob(quaternionISK_GSK, qt.createQuaternion(qt.basicVectors.i_3, -region.getLongitude()),
        qt.createQuaternion(qt.basicVectors.i_2, region.getLatitude()));

    Quaternion quaternionISK_ESK = qt.quaternionISK_ESK(anglePOSEandEarth);

    Quaternion quaternionDOb_ESK = qt.qauternionDOb_ESK(quaternionISK_Ob, quaternionISK_ESK);

    return qt.isLit(quaternionISK_ESK, quaternionISK_Ob, Acr);
  }

  public static List<SatModel> Predictor(Region region, Date start, Date finish, List<SatModel> group) {
    List<PredictorDataElement> data;
    List<List<Date>> VisibleList = new ArrayList<List<Date>>(group.size());
    for (int i = 0; i < group.size(); i++) {
      data = PredictorOfObservations.getInstance().PredictAnglesToObject(start, finish, group.get(i), region, 0.1f);
      List<Date> visTime = new ArrayList<Date>();
      for (PredictorDataElement element : data) {
        if ((element.angle < element.visibleAngle) && (element.isDay)) {
          visTime.add(element.date);
        }
      }
      VisibleList.add(visTime);
    }
    List<SatModel> bestSatModel = new ArrayList<SatModel>();
    for (int k = 0; k < 3; k++) {
      int countmax = 0;
      int num = 0;
      for (int i = 0; i < group.size(); i++) {
        if (VisibleList.get(i).size() > countmax) {
          countmax = VisibleList.get(i).size();
          num = i;
        }
      }
      bestSatModel.add(group.get(num));
      for (Date tm : VisibleList.get(num)) {
        for (int i = 0; i < group.size(); i++) {
          if ((i != num) && (VisibleList.get(i).contains(tm))) {
            VisibleList.get(i).remove(tm);
          }
        }
      }
      VisibleList.get(num).clear();
    }
    return bestSatModel;
  }

  public static List<Date> SmallPredictor(Region region, SatModel sat, Date start, Date finish) {
    List<PredictorDataElement> data = PredictorOfObservations.getInstance().PredictAnglesToObject(start, finish, sat, region, 0.1f);
    List<Date> visTime = new ArrayList<Date>();
    for (PredictorDataElement element : data) {
      if ((element.angle < element.visibleAngle) && (element.isDay)) {
        visTime.add(element.date);
      }
    }
    return visTime;
  }
}
