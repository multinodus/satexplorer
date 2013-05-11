package com.multinodus.satteliteexplorer.db.entities;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 11.12.12
 * Time: 17:38
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "sat")
public class Sat implements Serializable {
  private Integer satId;
  private Orbit orbit;
  private Equipment equipment;
  private Float trueAnomaly;
  private Set<SceneVariant> sceneVariantSet = Sets.newHashSet();
  private Set<Schedule> schedules = Sets.newHashSet();

  public Sat() {

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "satId", unique = true, nullable = false)
  public Integer getSatId() {
    return satId;
  }

  public void setSatId(Integer satId) {
    this.satId = satId;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "orbitId", nullable = false)
  public Orbit getOrbit() {
    return orbit;
  }

  public void setOrbit(Orbit orbit) {
    this.orbit = orbit;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "equipmentId", nullable = false)
  public Equipment getEquipment() {
    return equipment;
  }

  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }

  @Column(name = "trueAnomaly", nullable = false)
  public Float getTrueAnomaly() {
    return trueAnomaly;
  }

  public void setTrueAnomaly(Float trueAnomaly) {
    this.trueAnomaly = trueAnomaly;
  }

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "satSet")
  public Set<SceneVariant> getSceneVariantSet() {
    return sceneVariantSet;
  }

  public void setSceneVariantSet(Set<SceneVariant> sceneVariantSet) {
    this.sceneVariantSet = sceneVariantSet;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "sat")
  public Set<Schedule> getSchedules() {
    return schedules;
  }

  public void setSchedules(Set<Schedule> schedules) {
    this.schedules = schedules;
  }
}

