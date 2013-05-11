package com.multinodus.satteliteexplorer.scheduler.transformations;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.multinodus.satteliteexplorer.scheduler.util.Pair;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 16.12.12
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public interface ISatMovement {
  Pair<Vector3f, Vector3f> transformEllipseToOrbit(Vector2f position,
                                                   float inclination, float longitudeOfAscendingNode, float argumentOfPerigee);

  double calcuteTrueAnomaly(Vector2f position);

  Vector2f getInitialPosition(double a, double e, double trueAnomaly);

  Vector2f getInitialSpeed(Vector2f initialPosition, double a, double e);

  Vector2f getAcceleration(Vector2f position);

  Pair<Vector2f, Vector2f> calcuteNextPosition(float trueAnomaly, float e, Vector2f position, Vector2f velocity, float T);
}
