package com.multinodus.satteliteexplorer.scheduler;

import com.jme3.math.FastMath;
import com.multinodus.satteliteexplorer.db.EntityContext;
import com.multinodus.satteliteexplorer.db.entities.*;
import com.multinodus.satteliteexplorer.scheduler.models.*;
import com.multinodus.satteliteexplorer.scheduler.transformations.SI_Transform;
import com.multinodus.satteliteexplorer.scheduler.util.DateTimeConstants;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 16.12.12
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class World {
  private Collection<IUpdatable> entities = new ArrayList<IUpdatable>();
  private List<SatModel> satModels;
  private List<RegionModel> regionModels;
  private List<DataCenterModel> dataCenterModels;
  private List<Task> tasks;
  private List<DataCenter> dataCenters;
  private Date currentTime;
  private EarthModel earthModel;

  private Random rnd = new Random();

  public World() {
//    generate();
    load();
  }

  public void add(IUpdatable entity) {
    entities.add(entity);
  }

  public void addAll(Collection<IUpdatable> entities) {
    this.entities.addAll(entities);
  }

  public void update() {
    for (IUpdatable entity : entities) {
      entity.update();
    }
    currentTime = earthModel.getCurrentTime();
  }

  private void generate() {
    int orbitCount = 6;
    int satCount = 1;
    int regionCount = 500;
    int equipmentTypeCount = 1;
    int equipmentCount = 1;
    int dataCentersCount = 4;

    Collection<Object> entities = new ArrayList<Object>();

    Role role = new Role();
    role.setName("operator");
    User user = new User();
    user.setRole(role);
    user.setLogin("operator");
    user.setPassword("operator");
    entities.add(role);
    entities.add(user);

    List<EquipmentType> equipmentTypes = new ArrayList<EquipmentType>();
    for (int i = 0; i < equipmentTypeCount; i++) {
      EquipmentType equipmentType = new EquipmentType();
      equipmentType.setName("Тип№" + i);
      equipmentTypes.add(equipmentType);
    }
    entities.addAll(equipmentTypes);

    List<Equipment> equipments = new ArrayList<Equipment>();
    for (EquipmentType type : equipmentTypes) {
      for (int i = 0; i < equipmentCount; i++) {
        Equipment equipment = new Equipment();
        equipment.setDelay((long) (rnd.nextFloat() * DateTimeConstants.MSECS_IN_MINUTE));
        equipment.setCriticalAngle(30.0f);
        equipment.setDeltaAngle(2.0f);
        equipment.setSnapshotTime(7 * DateTimeConstants.MSECS_IN_SECOND);
        equipment.setSnapshotVolume(10.0f);
        equipment.setResolution(36.0f);
        equipment.setSpan(45000.0f);
        equipment.setStorageCapacity(100.0f);
        equipment.setSpeed(1.6f);
        equipment.setEquipmentType(type);
        equipments.add(equipment);
      }
    }
    entities.addAll(equipments);

    Collection<Orbit> orbits = new ArrayList<Orbit>();
    for (int i = 0; i < orbitCount; i++) {
      Orbit orbit = new Orbit();
      orbit.setSemiMajorAxis((float) SI_Transform.EARTH_RADIUS + 650000 + rnd.nextInt(350000));
      orbit.setArgumentOfPericenter(rnd.nextFloat() * FastMath.TWO_PI);
      orbit.setEccentricity(rnd.nextFloat() * 0.1f);
      orbit.setInclination(FastMath.HALF_PI/*rnd.nextFloat()* FastMath.TWO_PI*/);
      orbit.setLongitudeOfAscendingNode(rnd.nextFloat() * FastMath.TWO_PI);
      orbits.add(orbit);
    }
    entities.addAll(orbits);

    ArrayList<Sat> sats = new ArrayList<Sat>();
    for (Orbit orbit : orbits) {
      for (int i = 0; i < satCount; i++) {
        Sat sat = new Sat();
        sat.setOrbit(orbit);
        sat.setEquipment(equipments.get((int) Math.round(rnd.nextFloat() * (equipmentCount - 1))));
        sat.setTrueAnomaly(0.0f);
        sats.add(sat);
      }
    }
    entities.addAll(sats);

    ArrayList<Region> regions = new ArrayList<Region>();
    for (int i = 0; i < regionCount; i++) {
      Region region = new Region();
      region.setLatitude(-FastMath.PI / 2 + rnd.nextFloat() * FastMath.PI);
      region.setLongitude(-FastMath.PI + rnd.nextFloat() * FastMath.TWO_PI);
      region.setRadius(1000f);
      regions.add(region);
    }
    entities.addAll(regions);

    ArrayList<DataCenter> dataCenters = new ArrayList<DataCenter>();
    for (int i = 0; i < dataCentersCount; i++) {
      DataCenter dataCenter = new DataCenter();
      dataCenter.setLatitude(-FastMath.PI / 2 + rnd.nextFloat() * FastMath.PI);
      dataCenter.setLongitude(-FastMath.PI + rnd.nextFloat() * FastMath.TWO_PI);
      dataCenters.add(dataCenter);
    }
    entities.addAll(dataCenters);

    ArrayList<Task> tasks = new ArrayList<Task>();
    for (int i = 0; i < regionCount; i++) {
      Task task = new Task();
      task.setEquipmentType(equipmentTypes.get((int) Math.round(rnd.nextFloat() * (equipmentTypeCount - 1))));
      task.setRegion(regions.get(i/*(int) Math.round(rnd.nextFloat() * (regionCount - 1))*/));
      task.setCost(rnd.nextFloat());
      task.setStart(new Date(SI_Transform.INITIAL_TIME.getTime() + (long) (rnd.nextFloat() * DateTimeConstants.DAYS_IN_WEEK * DateTimeConstants.MSECS_IN_DAY)));
      task.setFinish(new Date(task.getStart().getTime() + (long) (rnd.nextFloat() * DateTimeConstants.MSECS_IN_DAY)));
      tasks.add(task);
    }
    entities.addAll(tasks);

    EntityContext.get().createEntities(entities);
  }

  private void load() {
    earthModel = new EarthModel();
    entities.add(earthModel);

    satModels = new ArrayList<SatModel>();
    for (Object sat : EntityContext.get().getAllEntities(Sat.class)) {
      Sat s = (Sat) sat;
      SatModel satModel = new SatModel(s);
      satModels.add(satModel);
      entities.add(satModel);
    }

    regionModels = new ArrayList<RegionModel>();
    tasks = new ArrayList<Task>();
    for (Object task : EntityContext.get().getAllEntities(Task.class)) {
      tasks.add((Task) task);

      Region r = ((Task) task).getRegion();
      RegionModel regionModel = new RegionModel(r, (Task)task);
      regionModels.add(regionModel);
      entities.add(regionModel);
    }

    dataCenterModels = new ArrayList<DataCenterModel>();
    dataCenters = new ArrayList<DataCenter>();
    for (Object dataCenter : EntityContext.get().getAllEntities(DataCenter.class)) {
      DataCenter d = (DataCenter) dataCenter;
      dataCenters.add(d);
      DataCenterModel dataCenterModel = new DataCenterModel(d);
      dataCenterModels.add(dataCenterModel);
      entities.add(dataCenterModel);
    }

    currentTime = earthModel.getCurrentTime();
  }

  public Collection<IUpdatable> getEntities() {
    return entities;
  }

  public List<SatModel> getSatModels() {
    return satModels;
  }

  public List<RegionModel> getRegionModels() {
    return regionModels;
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public List<DataCenter> getDataCenters() {
    return dataCenters;
  }

  public Date getCurrentTime() {
    return currentTime;
  }

  public void setCurrentTime(Date currentTime) {
    this.currentTime = currentTime;
  }
}
