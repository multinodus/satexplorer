package satteliteExplorer.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 11.12.12
 * Time: 18:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "region")
public class Region implements Serializable {
  private Integer regionId;
  private Float radius;
  private Float longitude;
  private Float latitude;
  private Set<Task> tasks = new HashSet<Task>();

  public Region() {

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "regionId", unique = true, nullable = false)
  public Integer getRegionId() {
    return regionId;
  }

  public void setRegionId(Integer regionId) {
    this.regionId = regionId;
  }

  @Column(name = "radius", nullable = false)
  public Float getRadius() {
    return radius;
  }

  public void setRadius(Float radius) {
    this.radius = radius;
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

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "region")
  public Set<Task> getTasks() {
    return tasks;
  }

  public void setTasks(Set<Task> tasks) {
    this.tasks = tasks;
  }
}

