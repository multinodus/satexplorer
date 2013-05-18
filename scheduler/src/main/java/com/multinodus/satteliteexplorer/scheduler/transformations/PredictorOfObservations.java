package com.multinodus.satteliteexplorer.scheduler.transformations;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.jme3.math.*;
import com.multinodus.satteliteexplorer.db.EntityContext;
import com.multinodus.satteliteexplorer.db.entities.*;
import com.multinodus.satteliteexplorer.scheduler.World;
import com.multinodus.satteliteexplorer.scheduler.models.SatModel;
import com.multinodus.satteliteexplorer.scheduler.models.SunModel;
import com.multinodus.satteliteexplorer.scheduler.optimizations.IKnapsackData;
import com.multinodus.satteliteexplorer.scheduler.util.DateTimeConstants;
import com.multinodus.satteliteexplorer.scheduler.util.KnapsackData;
import com.multinodus.satteliteexplorer.scheduler.util.Pair;
import com.multinodus.satteliteexplorer.scheduler.util.VectorConstants;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 14.12.12
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class PredictorOfObservations {
  private class RegionQuaternions {
    Quaternion quaternionLongitude;
    Quaternion quaternionLatitude;
    Quaternion quaternionIL;

    public RegionQuaternions(Quaternion quaternionLongitude, Quaternion quaternionLatitude, Quaternion quaternionIL) {
      this.quaternionIL = quaternionIL;
      this.quaternionLatitude = quaternionLatitude;
      this.quaternionLongitude = quaternionLongitude;
    }
  }

  private volatile static PredictorOfObservations _instance;

  private static Object _sync = new Object();

  private PredictorOfObservations() {
  }

  public static PredictorOfObservations getInstance() {
    if (_instance == null) {
      synchronized (_sync) {
        _instance = new com.multinodus.satteliteexplorer.scheduler.transformations.PredictorOfObservations();
      }
    }
    return _instance;
  }

  public void observe(Date now, Date end, Collection<Task> tasks, Collection<SatModel> sats, Collection<DataCenter> dataCenters, float detalization,
                      Map<SatModel, Multimap<Task, PredictedDataElement>> taskObservations,
                      Map<SatModel, List<PredictedDataElement>> dataCenterObservations) {
    for (SatModel sat : sats) {
      predictObservations(now, end, sat, tasks, dataCenters, detalization, taskObservations, dataCenterObservations);
    }
  }

  public IKnapsackData getKnapsackData(World world, int hourHorizont){
    Map<SatModel, Multimap<Task, PredictedDataElement>> taskObservations = Maps.newHashMap();
    Map<SatModel, List<PredictedDataElement>> dataCenterObservations = Maps.newHashMap();
    observe(SI_Transform.INITIAL_TIME, new Date(SI_Transform.INITIAL_TIME.getTime() + hourHorizont * DateTimeConstants.MSECS_IN_HOUR),
        world.getTasks(), world.getSatModels(), world.getDataCenters(), 0.05f, taskObservations, dataCenterObservations);
    IKnapsackData knapsackData = calculateKnapsackData(findEpisodes(taskObservations, dataCenterObservations));
    return knapsackData;
  }

  private IKnapsackData calculateKnapsackData(List<Pair<SatModel, List<Pair<Task, PredictedDataElement>>>> episodes) {
    List<Object> satList = EntityContext.get().getAllEntities(Sat.class);
    List<Object> taskList = EntityContext.get().getAllEntities(Task.class);

    int n = taskList.size();
    float profit[][] = new float[n][];
    float weight[][] = new float[n][];

    int m = episodes.size() + 1;

    for (int i = 0; i < n; i++) {
      profit[i] = new float[m];
      weight[i] = new float[m];
    }

    float capacity[] = new float[m];

    Map<Object, Integer> taskIndexes = Maps.newHashMap();

    int taskIndex = 0;
    for (Object task : taskList) {
      taskIndexes.put(task, taskIndex);
      taskIndex++;
    }

    for (int j = 0; j < episodes.size(); j++) {
      Pair<SatModel, List<Pair<Task, PredictedDataElement>>> episode = episodes.get(j);
      for (Pair<Task, PredictedDataElement> p : episode.s) {
        Date explorationDate = p.s.date;
        Task task = p.f;
        int i = taskIndexes.get(p.f);
        float cost;
        if (explorationDate.after(task.getStart()) && explorationDate.before(task.getFinish())) {
          cost = task.getCost();
        } else {
          cost = task.getCost() / 4;
        }
        profit[i][j] = cost;
        weight[i][j] = episode.f.getSat().getEquipment().getSnapshotVolume();
      }
    }

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < episodes.size(); j++) {
        if (weight[i][j] < 0.001) {
          weight[i][j] = Float.MAX_VALUE;
        }
      }
    }

    for (int j = 0; j < episodes.size(); j++) {
      capacity[j] = episodes.get(j).f.getSat().getEquipment().getStorageCapacity();
    }

    capacity[episodes.size()] = Float.MAX_VALUE;

    return new KnapsackData(n, m, profit, weight, capacity);
  }

  private List<Pair<SatModel, List<Pair<Task, PredictedDataElement>>>> findEpisodes(Map<SatModel, Multimap<Task, PredictedDataElement>> taskObservations,
                                                                                   Map<SatModel, List<PredictedDataElement>> dataCenterObservations) {
    List<Pair<SatModel, List<Pair<Task, PredictedDataElement>>>> episodes = Lists.newArrayList();
    for (SatModel sat : taskObservations.keySet()) {
      List<Pair<Task, PredictedDataElement>> allDataElements = Lists.newArrayList();
      Multimap<Task, PredictedDataElement> multimap = taskObservations.get(sat);
      for (Task task : multimap.keys()) {
        Collection<PredictedDataElement> observations = multimap.get(task);
        for (PredictedDataElement dataElement : observations) {
          allDataElements.add(new Pair<Task, PredictedDataElement>(task, dataElement));
        }
      }
      Collections.sort(allDataElements, new Comparator<Pair<Task, PredictedDataElement>>() {
        @Override
        public int compare(Pair<Task, PredictedDataElement> o1, Pair<Task, PredictedDataElement> o2) {
          return o1.s.date.compareTo(o2.s.date);
        }
      });

      List<PredictedDataElement> dataCenterExplorations = dataCenterObservations.get(sat);
      if (!dataCenterExplorations.isEmpty()) {
        int dataCenterIndex = 0;
        PredictedDataElement previousDataElement = null;
        PredictedDataElement dataCenterExploration = dataCenterExplorations.get(0);
        List<Pair<Task, PredictedDataElement>> episode = Lists.newArrayList();
        for (Pair<Task, PredictedDataElement> p : allDataElements) {
          if ((previousDataElement == null || previousDataElement.date.before(dataCenterExploration.date)) &&
              (p.s.date.after(dataCenterExploration.date))) {
            episodes.add(new Pair<SatModel, List<Pair<Task, PredictedDataElement>>>(sat, episode));
            episode = Lists.newArrayList();
            dataCenterIndex++;
            dataCenterExploration = dataCenterExplorations.get(dataCenterIndex);
          }
          episode.add(p);
          previousDataElement = p.s;
        }
        episodes.add(new Pair<SatModel, List<Pair<Task, PredictedDataElement>>>(sat, episode));
      } else {
        episodes.add(new Pair<SatModel, List<Pair<Task, PredictedDataElement>>>(sat, allDataElements));
      }
    }
    return episodes;
  }

  private void predictObservations(Date now, Date end, SatModel sat, Collection<Task> tasks, Collection<DataCenter> dataCenters, float earthSpeed,
                                  Map<SatModel, Multimap<Task, PredictedDataElement>> taskObservations,
                                  Map<SatModel, List<PredictedDataElement>> dataCenterObservations) {
    Date rrt = new Date(System.currentTimeMillis());

    Orbit orbit = sat.getOrbit();

    ISatMovement satsMovement = SatMovement.getInstance();

    Multimap<Task, PredictedDataElement> taskExplorations = ArrayListMultimap.create();
    List<PredictedDataElement> dataCenterExplorations = Lists.newArrayList();

    Vector3f sunPosition = SunModel.Position;
    Vector3f satPosition = new Vector3f(sat.getRealPosition());
    Vector3f orbitNormal = sat.getOrbitNormal();
    Vector3f greenwich = SI_Transform.greenwich;

    double trueAnomaly = sat.getTrueAnomaly();

    Vector2f orbitPosition = satsMovement.getInitialPosition(orbit.getSemiMajorAxis(), orbit.getEccentricity(),
        SI_Transform.twoPiRangeConverter(sat.getTrueAnomaly()));
    Vector2f orbitVelocity = satsMovement.getInitialSpeed(orbitPosition, orbit.getSemiMajorAxis(), orbit.getEccentricity());

    synchronized (QuaternionTransformations.exSync) {
      QuaternionTransformations qt = QuaternionTransformations.I();
      BasicVectors basicVectors = new BasicVectors();
      BasicVectors bufBasicVectors = qt.basicVectors;
      qt.basicVectors = basicVectors;

      Vector3f i1_1 = SI_Transform.pointOfSpringEquinox.normalize();
      Vector3f i1_2 = VectorConstants.up.cross(SI_Transform.pointOfSpringEquinox).normalize();
      Vector3f i1_3 = VectorConstants.up;

      basicVectors.setISK(i1_1, i1_2, i1_3);

      Map<Region, RegionQuaternions> quaternionMap = Maps.newHashMap();
      for (Task task : tasks) {
        Quaternion quaternionLongitude = qt.createQuaternion(basicVectors.i_3, -task.getRegion().getLongitude());

        Quaternion quaternionLatitude = qt.createQuaternion(basicVectors.i_2, task.getRegion().getLatitude());

        Quaternion quaternionIL = qt.createQuaternion(basicVectors.i_1, -orbit.getInclination()).mult(
            qt.createQuaternion(basicVectors.i_3, -orbit.getLongitudeOfAscendingNode()));
        quaternionMap.put(task.getRegion(), new RegionQuaternions(quaternionLongitude, quaternionLongitude, quaternionIL));
      }

      Map<DataCenter, RegionQuaternions> dataCenterRegionQuaternionsMap = Maps.newHashMap();
      for (DataCenter dataCenter : dataCenters) {
        Quaternion quaternionLongitude = qt.createQuaternion(basicVectors.i_3, -dataCenter.getLongitude());

        Quaternion quaternionLatitude = qt.createQuaternion(basicVectors.i_2, dataCenter.getLatitude());

        Quaternion quaternionIL = qt.createQuaternion(basicVectors.i_1, -orbit.getInclination()).mult(
            qt.createQuaternion(basicVectors.i_3, -orbit.getLongitudeOfAscendingNode()));
        dataCenterRegionQuaternionsMap.put(dataCenter, new RegionQuaternions(quaternionLongitude, quaternionLongitude, quaternionIL));
      }

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

        float visibleAngle = sat.getExplorationAngle();//qt.calcute_r((float) sat.getVisibleAngle(), H, (float) SI_Transform.EARTH_RADIUS);

        for (Task task : tasks) {
          boolean isDay = predictNight(qt, task.getRegion(), 0.1, s, anglePOSEandSun);
          RegionQuaternions q = quaternionMap.get(task.getRegion());
          float angle = predictAngleToObject(sat, s, trueAnomaly, q.quaternionLongitude, q.quaternionLatitude, q.quaternionIL, qt);
          if (angle < visibleAngle) {
            taskExplorations.put(task, new PredictedDataElement(new Date(now.getTime() + (long) (seconds * DateTimeConstants.MSECS_IN_SECOND)), angle,
                visibleAngle, isDay));
          }
        }

        for (DataCenter dataCenter : dataCenters) {
          RegionQuaternions q = dataCenterRegionQuaternionsMap.get(dataCenter);
          float angle = predictAngleToObject(sat, s, trueAnomaly, q.quaternionLongitude, q.quaternionLatitude, q.quaternionIL, qt);
          // TODO set radio vision angle
          if (angle < 0.3) {
            dataCenterExplorations.add(new PredictedDataElement(new Date(now.getTime() + (long) (seconds * DateTimeConstants.MSECS_IN_SECOND)), angle,
                visibleAngle, true));
          }
        }

        seconds += dsecond;
        s -= earthSpeed % FastMath.TWO_PI;

        Matrix3f rotationY = new Matrix3f();
        rotationY.fromAngleAxis(earthSpeed, VectorConstants.up);
        greenwich = rotationY.mult(greenwich);

        Pair<Vector2f, Vector2f> p = satsMovement.calcuteNextPosition((float) trueAnomaly - FastMath.HALF_PI,
            orbit.getEccentricity(), orbitPosition, orbitVelocity, (float) dsecond);
        orbitPosition = p.f;
        orbitVelocity = p.s;

        Pair<Vector3f, Vector3f> p3 = satsMovement.transformEllipseToOrbit(orbitPosition, (float) orbit.getInclination(),
            (float) orbit.getLongitudeOfAscendingNode(), (float) orbit.getArgumentOfPericenter());
        satPosition = p3.f;
        orbitNormal = p3.s;

        trueAnomaly = satsMovement.calcuteTrueAnomaly(orbitPosition) + FastMath.HALF_PI;

        sunPosition = SunModel.calcutePosition(yearAngleFrom(new Date(now.getTime() + (long) (seconds * DateTimeConstants.MSECS_IN_SECOND))));
      }
      double span = System.currentTimeMillis() - rrt.getTime();

      qt.basicVectors = bufBasicVectors;
    }

    taskObservations.put(sat, taskExplorations);
    dataCenterObservations.put(sat, dataCenterExplorations);
  }

  private float yearAngleFrom(Date date) {
    long ts = date.getTime() - SI_Transform.INITIAL_TIME.getTime();
    double minutes = (ts / DateTimeConstants.MSECS_IN_MINUTE) % (525960);
    float angle = (float) (2 * Math.PI * ((float) minutes / 525960.0f));
    return angle;
  }

  private float predictAngleToObject(com.multinodus.satteliteexplorer.scheduler.models.SatModel sat, float s, double trueAnomaly,
                                     Quaternion quaternionLongitude, Quaternion quaternionLatitude,
                                     Quaternion quaternionIL, com.multinodus.satteliteexplorer.scheduler.transformations.QuaternionTransformations qt) {
    Quaternion quaternionWpV = qt.createQuaternion(qt.basicVectors.i_3, -sat.getOrbit().getArgumentOfPericenter() + FastMath.PI +
        trueAnomaly);
    Quaternion quaternionISK_GSK = qt.quanterionISK_GSK(s);

    float angle = (float) com.multinodus.satteliteexplorer.scheduler.transformations.SI_Transform.calcuteAngle(
        qt.conjugate(quaternionWpV.mult(quaternionIL)).mult(qt.basicVectors.i_1),
        qt.conjugate(quaternionLatitude.mult(quaternionISK_GSK).mult(quaternionLongitude)).mult(qt.basicVectors.i_1));

    return angle;
  }

  public static boolean predictNight(com.multinodus.satteliteexplorer.scheduler.transformations.QuaternionTransformations qt,
                                     Region region, double Acr, float s, double anglePOSEandEarth) {
    Quaternion quaternionISK_GSK = qt.quanterionISK_GSK(s);

    Quaternion quaternionISK_Ob = qt.quaternionISK_Ob(quaternionISK_GSK, qt.createQuaternion(qt.basicVectors.i_3, -region.getLongitude()),
        qt.createQuaternion(qt.basicVectors.i_2, region.getLatitude()));

    Quaternion quaternionISK_ESK = qt.quaternionISK_ESK(anglePOSEandEarth);

//    Quaternion quaternionDOb_ESK = qt.qauternionDOb_ESK(quaternionISK_Ob, quaternionISK_ESK);

    return qt.isLit(quaternionISK_ESK, quaternionISK_Ob, Acr);
  }
}
