package com.multinodus.satteliteexplorer.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 11.12.12
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "task")
public class Task implements Serializable {
  private Integer taskId;
  private Region region;
  private EquipmentType equipmentType;
  private Date start;
  private Date finish;
  private Float cost;

  public Task() {

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "taskId", unique = true, nullable = false)
  public Integer getTaskId() {
    return taskId;
  }

  public void setTaskId(Integer taskId) {
    this.taskId = taskId;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "regionId", nullable = false)
  public Region getRegion() {
    return region;
  }

  public void setRegion(Region region) {
    this.region = region;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "equipmentTypeId", nullable = false)
  public EquipmentType getEquipmentType() {
    return equipmentType;
  }

  public void setEquipmentType(EquipmentType equipmentType) {
    this.equipmentType = equipmentType;
  }

  @Column(name = "start", nullable = false)
  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  @Column(name = "finish", nullable = false)
  public Date getFinish() {
    return finish;
  }

  public void setFinish(Date finish) {
    this.finish = finish;
  }

  @Column(name = "cost", nullable = false)
  public Float getCost() {
    return cost;
  }

  public void setCost(Float cost) {
    this.cost = cost;
  }
}
