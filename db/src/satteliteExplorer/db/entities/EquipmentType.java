package satteliteExplorer.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 11.12.12
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "equipmentType")
public class EquipmentType implements Serializable {
  private Integer equipmentTypeId;
  private String name;
  private Set<Equipment> equipments = new HashSet<Equipment>();
  private Set<Task> tasks = new HashSet<Task>();

  public EquipmentType() {

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "equipmentTypeId", unique = true, nullable = false)
  public Integer getEquipmentTypeId() {
    return equipmentTypeId;
  }

  public void setEquipmentTypeId(Integer equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  @Column(name = "name", nullable = false, length = 40)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipmentType")
  public Set<Equipment> getEquipments() {
    return equipments;
  }

  public void setEquipments(Set<Equipment> equipments) {
    this.equipments = equipments;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipmentType")
  public Set<Task> getTasks() {
    return tasks;
  }

  public void setTasks(Set<Task> tasks) {
    this.tasks = tasks;
  }
}
