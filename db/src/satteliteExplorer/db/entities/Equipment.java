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
