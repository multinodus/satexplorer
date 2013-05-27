package com.multinodus.satteliteexplorer.ui.scene.models;

import com.jme3.math.Matrix3f;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Curve;
import com.multinodus.satteliteexplorer.scheduler.models.RegionModel;
import com.multinodus.satteliteexplorer.scheduler.util.VectorConstants;
import com.multinodus.satteliteexplorer.ui.scene.UIApplication;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 17.12.12
 * Time: 22:59
 * To change this template use File | Settings | File Templates.
 */
public class RegionView implements IView {
  RegionModel regionModel;
  private Spatial spatial;
  private Spatial connection;
  private Node rootNode;

  public RegionView(RegionModel regionModel, Spatial spatial, Node rootNode) {
    this.regionModel = regionModel;
    this.spatial = spatial;
    this.rootNode = rootNode;
    update();
  }

  public void update() {
    if (regionModel.getSatModel() != null){
      if (connection != null){
        rootNode.detachChild(connection);
      }
      Spline spline =new Spline();
      spline.addControlPoint(regionModel.getPosition());
      spline.addControlPoint(regionModel.getSatModel().getPosition());
      spline.setCycle(true);
      spline.setCurveTension(0.83f);
      Curve curve=new Curve(spline,10); //10 is the number of sub-segments between control points
      curve.setLineWidth(1000);
      connection = new Geometry("", curve);
      connection.setMaterial(UIApplication.app.getEasyMaterial());
      rootNode.attachChild(connection);
      connection.setLocalTranslation(Vector3f.ZERO);
    }

    spatial.setLocalTranslation(regionModel.getPosition());
    Matrix3f matrix3f = new Matrix3f();
    matrix3f.fromStartEndVectors(VectorConstants.right, regionModel.getPosition().normalize());
    spatial.setLocalRotation(matrix3f);
  }
}
