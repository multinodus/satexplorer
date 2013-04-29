package satteliteExplorer.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 11.12.12
 * Time: 17:48
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "equipment")
public class Equipment implements Serializable {
  private Integer equipmentId;
  private Long delay;
  private Float speed;
  private Set<Sat> sats = new HashSet<Sat>();
  private Set<DataCenter> dataCenters = new HashSet<DataCenter>();
  private EquipmentType equipmentType;
  private Float deltaAngle;
  private Float criticalAngle;
  private Float span;
  private Float resolution;
  private Float storageCapacity;
  private Long workTime;
  private Long frameTime;

  public Equipment() {

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "equipmentId", unique = true, nullable = false)
  public Integer getEquipmentId() {
    return equipmentId;
  }

  public void setEquipmentId(Integer equipmentId) {
    this.equipmentId = equipmentId;
  }

  @Column(name = "delay", nullable = false)
  public Long getDelay() {
    return delay;
  }

  public void setDelay(Long delay) {
    this.delay = delay;
  }

  @Column(name = "speed", nullable = false)
  public Float getSpeed() {
    return speed;
  }

  public void setSpeed(Float speed) {
    this.speed = speed;
  }

  @Column(name = "deltaAngle", nullable = false)
  public Float getDeltaAngle() {
    return deltaAngle;
  }

  public void setDeltaAngle(Float deltaAngle) {
    this.deltaAngle = deltaAngle;
  }

  @Column(name = "criticalAngle", nullable = false)
  public Float getCriticalAngle() {
    return criticalAngle;
  }

  public void setCriticalAngle(Float criticalAngle) {
    this.criticalAngle = criticalAngle;
  }

  @Column(name = "span", nullable = false)
  public Float getSpan() {
    return span;
  }

  public void setSpan(Float span) {
    this.span = span;
  }

  @Column(name = "resolution", nullable = false)
  public Float getResolution() {
    return resolution;
  }

  public void setResolution(Float resolution) {
    this.resolution = resolution;
  }

  @Column(name = "storageCapacity", nullable = false)
  public Float getStorageCapacity() {
    return storageCapacity;
  }

  public void setStorageCapacity(Float storageCapacity) {
    this.storageCapacity = storageCapacity;
  }

  @Column(name = "workTime", nullable = false)
  public Long getWorkTime() {
    return workTime;
  }

  public void setWorkTime(Long workTime) {
    this.workTime = workTime;
  }

  @Column(name = "frameTime", nullable = false)
  public Long getFrameTime() {
    return frameTime;
  }

  public void setFrameTime(Long frameTime) {
    this.frameTime = frameTime;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipment")
  public Set<Sat> getSats() {
    return sats;
  }

  public void setSats(Set<Sat> sats) {
    this.sats = sats;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipment")
  public Set<DataCenter> getDataCenters() {
    return dataCenters;
  }

  public void setDataCenters(Set<DataCenter> dataCenters) {
    this.dataCenters = dataCenters;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "equipmentTypeId", nullable = false)
  public EquipmentType getEquipmentType() {
    return equipmentType;
  }

  public void setEquipmentType(EquipmentType equipmentType) {
    this.equipmentType = equipmentType;
  }
}
