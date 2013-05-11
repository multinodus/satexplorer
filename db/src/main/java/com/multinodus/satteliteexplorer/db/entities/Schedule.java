package com.multinodus.satteliteexplorer.db.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 11.05.13
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "schedule")
public class Schedule {
  private Integer scheduleId;
  private Integer scheduleNumber;
  private Sat sat;
  private Float longitude;
  private Float latitude;
  private Long time;
  private Boolean input;

  public Schedule() {

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "scheduleId", unique = true, nullable = false)
  public Integer getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(Integer scheduleId) {
    this.scheduleId = scheduleId;
  }

  @Column(name = "scheduleNumber", nullable = false)
  public Integer getScheduleNumber() {
    return scheduleNumber;
  }

  public void setScheduleNumber(Integer scheduleNumber) {
    this.scheduleNumber = scheduleNumber;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "satId", nullable = false)
  public Sat getSat() {
    return sat;
  }

  public void setSat(Sat sat) {
    this.sat = sat;
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

  @Column(name = "time", nullable = false)
  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  @Column(name = "input", nullable = false)
  public Boolean getInput() {
    return input;
  }

  public void setInput(Boolean input) {
    this.input = input;
  }
}
