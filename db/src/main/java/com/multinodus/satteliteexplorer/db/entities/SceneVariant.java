package com.multinodus.satteliteexplorer.db.entities;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 11.05.13
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "sceneVariant")
public class SceneVariant {
  private Integer sceneId;
  private String description;
  private Set<DataCenter> dataCenterSet = Sets.newHashSet();
  private Set<Sat> satSet = Sets.newHashSet();
  private Set<Task> taskSet = Sets.newHashSet();
  private long initialTime;

  public SceneVariant(){

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sceneId", unique = true, nullable = false)
  public Integer getSceneId() {
    return sceneId;
  }

  public void setSceneId(Integer sceneId) {
    this.sceneId = sceneId;
  }

  @Column(name = "description", nullable = false, length = 200)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "sceneVariant_dataCenter", joinColumns = {
      @JoinColumn(name = "sceneId", nullable = false, updatable = false) },
      inverseJoinColumns = { @JoinColumn(name = "dataCenterId",
          nullable = false, updatable = false) })
  public Set<DataCenter> getDataCenterSet() {
    return dataCenterSet;
  }

  public void setDataCenterSet(Set<DataCenter> dataCenterSet) {
    this.dataCenterSet = dataCenterSet;
  }

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "sceneVariant_sat", joinColumns = {
      @JoinColumn(name = "sceneId", nullable = false, updatable = false) },
      inverseJoinColumns = { @JoinColumn(name = "satId",
          nullable = false, updatable = false) })
  public Set<Sat> getSatSet() {
    return satSet;
  }

  public void setSatSet(Set<Sat> satSet) {
    this.satSet = satSet;
  }

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "sceneVariant_task", joinColumns = {
      @JoinColumn(name = "sceneId", nullable = false, updatable = false) },
      inverseJoinColumns = { @JoinColumn(name = "taskId",
          nullable = false, updatable = false) })
  public Set<Task> getTaskSet() {
    return taskSet;
  }

  public void setTaskSet(Set<Task> taskSet) {
    this.taskSet = taskSet;
  }

  @Column(name = "initialTime", nullable = false)
  public long getInitialTime() {
    return initialTime;
  }

  public void setInitialTime(long initialTime) {
    this.initialTime = initialTime;
  }
}
