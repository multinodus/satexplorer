package com.multinodus.satteliteexplorer.scheduler.transformations;

import com.jme3.math.Vector3f;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 14.12.12
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */
public class SI_Transform {
  public static Vector3f pointOfSpringEquinox;

  public static Vector3f sunPosition;

  public static Vector3f greenwich;

  public final static double SECOND_IN_DAY = 86400;

  public final static double EARTH_MASS = 5.9742e+24;

  public final static double GRAVITY_CONST = 6.67300e-11;

  public final static double EARTH_RADIUS = 6378.1e+3;

  public final static double SI_ON_MODEL_LENGTH_KOEF = 100;//EARTH_RADIUS / 63; //102763;

  public final static float ANGLE_EARTH = 0;//0.41f;

  public final static float EARTH_SPEED = 0.002f;

  public final static Date INITIAL_TIME = new Date(109, 11, 21, 15, 00, 00);

  public static double distanceModelInSI(double modelDistance) {
    return modelDistance * SI_ON_MODEL_LENGTH_KOEF;
  }

  public static double distanceSIInModel(double SIDistance) {
    return SIDistance / SI_ON_MODEL_LENGTH_KOEF;
  }

  public static double twoPiRangeConverter(double angle) {
    if (angle < 0) {
      angle = 2 * Math.PI + angle;
    }
    angle = angle % (2 * Math.PI);
    return angle;
  }

  public static double calcuteAngle(Vector3f v1, Vector3f v2) {
    return Math.acos(v1.dot(v2) / (v1.length() * v2.length()));
  }

  public static double timePeriodRotationInSI(double A) {
    return 2 * Math.PI * Math.sqrt(A * A * A) / Math.sqrt(GRAVITY_CONST * EARTH_MASS);
  }

  public static double bigOsInSI(double timePeriodRotation) {
    return Math.pow(timePeriodRotation * Math.sqrt(GRAVITY_CONST * EARTH_MASS) / (2 * Math.PI), 2.0 / 3.0);
  }

  public static double timeModelInSI(double EarthsAngularSpeed, double modelTime) {
    return SECOND_IN_DAY * (modelTime / ((2 * Math.PI) / EarthsAngularSpeed));
  }

  public static double timeSIInModel(double EarthsAngularSpeed, double SITime) {
    return (SITime / SECOND_IN_DAY) * ((2 * Math.PI) / EarthsAngularSpeed);
  }

  public static double angularSpeedModelInSI(double EarthsAngularSpeed, double modelAngularSpeed) {
    return modelAngularSpeed / timeModelInSI(EarthsAngularSpeed, 1);
  }

  public static double angularSpeedSIInModel(double EarthsAngularSpeed, double SIAngularSpeed) {
    return SIAngularSpeed / timeSIInModel(EarthsAngularSpeed, 1);
  }

  public static double meanAnomalyFrom(double eccentricAnomaly, double e) {
    return eccentricAnomaly - e * Math.sin(eccentricAnomaly);
  }

  public static double trueAnomalyFrom(double eccentricAnomaly, double e) {
    return 2 * Math.atan2(Math.sqrt(1 - e) * Math.cos(eccentricAnomaly / 2), Math.sqrt(1 + e) * Math.sin(eccentricAnomaly / 2));
  }

  public static String geographicCoordinates(double longitude, double latitude) {
    char longChar;
    char latiChar;
    if (longitude > 0) {
      longChar = 'E';
    } else {
      longChar = 'W';
    }
    if (latitude > 0) {
      latiChar = 'N';
    } else {
      latiChar = 'S';
    }

    longitude = Math.abs(longitude);
    latitude = Math.abs(latitude);

    double degreeLong = (longitude / Math.PI) * 180;
    double degreeLati = (latitude / Math.PI) * 180;

    String res = String.format("{0:F2} {1}; {2:F2} {3}", degreeLati, latiChar, degreeLong, longChar);
    return res;
  }
}
