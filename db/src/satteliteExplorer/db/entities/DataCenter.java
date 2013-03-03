package satteliteExplorer.db.entities;

import javax.persistence.*;
import java.io.Serializable;

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
  private Equipment equipment;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "equipmentId", nullable = false)
  public Equipment getEquipment() {
    return equipment;
  }

  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }
}
