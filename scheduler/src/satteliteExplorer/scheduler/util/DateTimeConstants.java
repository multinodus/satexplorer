package satteliteExplorer.scheduler.util;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 16.12.12
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
public class DateTimeConstants {
  public static final long MSECS_IN_SECOND = 1000;
  public static final long SECS_IN_MINUTE = 60;
  public static final long MINS_IN_HOUR = 60;
  public static final long HOURS_IN_DAY = 24;
  public static final long MSECS_IN_MINUTE = MSECS_IN_SECOND * SECS_IN_MINUTE;
  public static final long MSECS_IN_HOUR = MSECS_IN_MINUTE * MINS_IN_HOUR;
  public static final long MSECS_IN_DAY = MSECS_IN_HOUR * HOURS_IN_DAY;
  public static final long MINS_IN_DAY = MINS_IN_HOUR * HOURS_IN_DAY;

  public static final int DAYS_IN_WEEK = 7;

  public static final double EPSILON = 0.000001;
}
