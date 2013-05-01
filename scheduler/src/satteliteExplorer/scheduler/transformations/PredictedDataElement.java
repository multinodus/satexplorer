package satteliteExplorer.scheduler.transformations;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 18.12.12
 * Time: 20:47
 * To change this template use File | Settings | File Templates.
 */
public class PredictedDataElement {
  public Date date;
  public float angle;
  public float visibleAngle;
  public boolean isDay;

  public PredictedDataElement(Date date, float angle, float visibleAngle, boolean isDay) {
    this.date = date;
    this.angle = angle;
    this.visibleAngle = visibleAngle;
    this.isDay = isDay;
  }
}
