package satteliteExplorer.scheduler;

import com.jme3.math.FastMath;
import satteliteExplorer.db.EntityContext;
import satteliteExplorer.db.entities.*;
import satteliteExplorer.scheduler.models.EarthModel;
import satteliteExplorer.scheduler.models.IUpdatable;
import satteliteExplorer.scheduler.models.RegionModel;
import satteliteExplorer.scheduler.models.SatModel;
import satteliteExplorer.scheduler.util.DateTimeConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

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
  private List<Task> tasks;

  private Random rnd = new Random();

  public World() {
    //generate();
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
  }

  private void generate() {
    int orbitCount = 6;
    int satCount = 1;
    int regionCount = 5;
    int equipmentTypeCount = 3;
    int equipmentCount = 5;
    int taskCount = 10;

    Collection<Object> entities = new ArrayList<Object>();

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
        equipment.setSpeed(10000f);
        equipment.setEquipmentType(type);
        equipments.add(equipment);
      }
    }
    entities.addAll(equipments);

    Collection<Orbit> orbits = new ArrayList<Orbit>();
    for (int i = 0; i < orbitCount; i++) {
      Orbit orbit = new Orbit();
      orbit.setSemiMajorAxis(9000000f + rnd.nextInt(900000));
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
        sats.add(sat);
      }
    }
    entities.addAll(sats);

    ArrayList<Region> regions = new ArrayList<Region>();
    for (int i = 0; i < regionCount; i++) {
      Region region = new Region();
      region.setLatitude(-FastMath.PI + rnd.nextFloat() * FastMath.TWO_PI);
      region.setLongitude(-FastMath.PI + rnd.nextFloat() * FastMath.TWO_PI);
      region.setRadius(15000f);
      regions.add(region);
    }
    entities.addAll(regions);

    ArrayList<Task> tasks = new ArrayList<Task>();
    for (int i = 0; i < taskCount; i++) {
      Task task = new Task();
      task.setEquipmentType(equipmentTypes.get((int) Math.round(rnd.nextFloat() * (equipmentTypeCount - 1))));
      task.setRegion(regions.get((int) Math.round(rnd.nextFloat() * (regionCount - 1))));
      tasks.add(task);
    }
    entities.addAll(tasks);

    EntityContext.get().createEntities(entities);
  }

  private void load() {
    EarthModel earthModel = new EarthModel();
    entities.add(earthModel);

    satModels = new ArrayList<SatModel>();
    for (Object sat : EntityContext.get().getAllEntities(Sat.class)) {
      Sat s = (Sat) sat;
      SatModel satModel = new SatModel(s);
      satModels.add(satModel);
      entities.add(satModel);
    }

    regionModels = new ArrayList<RegionModel>();
    for (Object region : EntityContext.get().getAllEntities(Region.class)) {
      Region r = (Region) region;
      RegionModel regionModel = new RegionModel(r);
      regionModels.add(regionModel);
      entities.add(regionModel);
    }

    tasks = new ArrayList<Task>();
    for (Object task : EntityContext.get().getAllEntities(Task.class)) {
      tasks.add((Task) task);
    }
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
}
