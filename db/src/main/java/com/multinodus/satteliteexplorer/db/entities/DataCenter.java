package com.multinodus.satteliteexplorer.db.entities;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 11.12.12
 * Time: 17:58
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "dataCenter")
public class DataCenter implements Serializable {
  private Integer dataCenterId;
  private Float longitude;
  private Float latitude;
  private Set<SceneVariant> sceneVariantSet = Sets.newHashSet();

  public DataCenter() {

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "dataCenterId", unique = true, nullable = false)
  public Integer getDataCenterId() {
    return dataCenterId;
  }

  public void setDataCenterId(Integer dataCenterId) {
    this.dataCenterId = dataCenterId;
  }

  @Column(name = "longitude", nullable = false)
  public Float getLongitude() {
    return longitude;
  }

  public void setLongitude(Float longitude) {
    this.longitude = longitude;
  }

  @Column(name = "latitude", nullable = false)
  public Float getLatitude() {
    return latitude;
  }

  public void setLatitude(Float latitude) {
    this.latitude = latitude;
  }

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "dataCenterSet")
  public Set<SceneVariant> getSceneVariantSet() {
    return sceneVariantSet;
  }

  public void setSceneVariantSet(Set<SceneVariant> sceneVariantSet) {
    this.sceneVariantSet = sceneVariantSet;
  }
}
