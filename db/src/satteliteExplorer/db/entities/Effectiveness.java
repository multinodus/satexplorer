package satteliteExplorer.db.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 11.12.12
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "effectiveness")
public class Effectiveness implements Serializable {
  private Integer effectivenessId;
  private String description;

  public Effectiveness() {

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "effectivenessId", unique = true, nullable = false)
  public Integer getEffectivenessId() {
    return effectivenessId;
  }

  public void setEffectivenessId(Integer effectivenessId) {
    this.effectivenessId = effectivenessId;
  }

  @Column(name = "descriptionId", unique = true, nullable = false)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
