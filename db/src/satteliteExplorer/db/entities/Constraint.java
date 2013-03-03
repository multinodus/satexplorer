package satteliteExplorer.db.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 11.12.12
 * Time: 18:57
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "constraint")
public class Constraint implements Serializable {
  private Integer constraintId;
  private String description;

  public Constraint() {

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "constraintId", unique = true, nullable = false)
  public Integer getConstraintId() {
    return constraintId;
  }

  public void setConstraintId(Integer constraintId) {
    this.constraintId = constraintId;
  }

  @Column(name = "descriptionId", unique = true, nullable = false)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
