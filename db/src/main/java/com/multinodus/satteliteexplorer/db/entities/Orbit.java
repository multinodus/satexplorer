package com.multinodus.satteliteexplorer.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 11.12.12
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "orbit")
public class Orbit implements Serializable {
  private Integer orbitId;
  private Float semiMajorAxis;
  private Float eccentricity;
  private Float inclination;
  private Float longitudeOfAscendingNode;
  private Float argumentOfPericenter;
  private Set<Sat> sats = new HashSet<Sat>();

  public Orbit() {

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orbitId", unique = true, nullable = false)
  public Integer getOrbitId() {
    return orbitId;
  }

  public void setOrbitId(Integer orbitId) {
    this.orbitId = orbitId;
  }

  @Column(name = "semiMajorAxis", nullable = false)
  public Float getSemiMajorAxis() {
    return semiMajorAxis;
  }

  public void setSemiMajorAxis(Float semiMajorAxis) {
    this.semiMajorAxis = semiMajorAxis;
  }

  @Column(name = "eccentricity", nullable = false)
  public Float getEccentricity() {
    return eccentricity;
  }

  public void setEccentricity(Float eccentricity) {
    this.eccentricity = eccentricity;
  }

  @Column(name = "inclination", nullable = false)
  public Float getInclination() {
    return inclination;
  }

  public void setInclination(Float inclination) {
    this.inclination = inclination;
  }

  @Column(name = "longitudeOfAscendingNode", nullable = false)
  public Float getLongitudeOfAscendingNode() {
    return longitudeOfAscendingNode;
  }

  public void setLongitudeOfAscendingNode(Float longitudeOfAscendingNode) {
    this.longitudeOfAscendingNode = longitudeOfAscendingNode;
  }

  @Column(name = "argumentOfPericenter", nullable = false)
  public Float getArgumentOfPericenter() {
    return argumentOfPericenter;
  }

  public void setArgumentOfPericenter(Float argumentOfPericenter) {
    this.argumentOfPericenter = argumentOfPericenter;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "orbit")
  public Set<Sat> getSats() {
    return sats;
  }

  public void setSats(Set<Sat> sats) {
    this.sats = sats;
  }
}
