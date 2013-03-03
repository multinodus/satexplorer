package satteliteExplorer.scheduler.transformations;

import com.jme3.math.Vector3f;

public class BasicVectors {
  //region ISK
  public Vector3f i_1;
  public Vector3f i_2;
  public Vector3f i_3;

  //region OSK
  public Vector3f i1_1;
  public Vector3f i1_2;
  public Vector3f i1_3;

  //region GSK
  public Vector3f i2_1;
  public Vector3f i2_2;
  public Vector3f i2_3;

  //region Ob
  public Vector3f iob_1;
  public Vector3f iob_2;
  public Vector3f iob_3;

  //region ESK
  public Vector3f ie_1;
  public Vector3f ie_2;
  public Vector3f ie_3;

  public BasicVectors() {

  }

  public void setISK(Vector3f i1, Vector3f i2, Vector3f i3) {
    i_1 = i1;
    i_2 = i2;
    i_3 = i3;
  }

  public void setISK(BasicVectors bv) {
    i_1 = bv.i_1;
    i_2 = bv.i_2;
    i_3 = bv.i_3;
  }

  public void setOSK(Vector3f i1, Vector3f i2, Vector3f i3) {
    i1_1 = i1;
    i1_2 = i2;
    i1_3 = i3;
  }

  public void setOSK(BasicVectors bv) {
    i1_1 = bv.i1_1;
    i1_2 = bv.i1_2;
    i1_3 = bv.i1_3;
  }

  public void setGSK(Vector3f i1, Vector3f i2, Vector3f i3) {
    i2_1 = i1;
    i2_2 = i2;
    i2_3 = i3;
  }

  public void setGSK(BasicVectors bv) {
    i2_1 = bv.i2_1;
    i2_2 = bv.i2_2;
    i2_3 = bv.i2_3;
  }

  public void setOb(Vector3f i1, Vector3f i2, Vector3f i3) {
    iob_1 = i1;
    iob_2 = i2;
    iob_3 = i3;
  }

  public void setOb(BasicVectors bv) {
    iob_1 = bv.iob_1;
    iob_2 = bv.iob_2;
    iob_3 = bv.iob_3;
  }

  public void setESK(Vector3f i1, Vector3f i2, Vector3f i3) {
    ie_1 = i1;
    ie_2 = i2;
    ie_3 = i3;
  }

  public void setESK(BasicVectors bv) {
    ie_1 = bv.ie_1;
    ie_2 = bv.ie_2;
    ie_3 = bv.ie_3;
  }
}