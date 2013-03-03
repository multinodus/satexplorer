package satteliteExplorer.db.entities;

import javax.persistence.*;
import java.io.Serializable;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "equipmentId", nullable = false)
  public Equipment getEquipment() {
    return equipment;
  }

  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }
}

