package satteliteExplorer.ui.scene.models;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 17.12.12
 * Time: 0:49
 * To change this template use File | Settings | File Templates.
 */
public class Shockwave implements IView {
  private static final int COUNT_FACTOR = 1;
  private static final float COUNT_FACTOR_F = 1f;
  private ParticleEmitter shockwave;

  public Shockwave(AssetManager assetManager, Node rootNode) {
    shockwave = new ParticleEmitter("Shockwave", ParticleMesh.Type.Triangle, 1 * COUNT_FACTOR);
    shockwave.setFaceNormal(Vector3f.UNIT_Y);
    shockwave.setStartColor(new ColorRGBA(.48f, 0.17f, 0.01f, (float) (.8f / COUNT_FACTOR_F)));
    shockwave.setEndColor(new ColorRGBA(.48f, 0.17f, 0.01f, 0f));

    shockwave.setStartSize(900f);
    shockwave.setEndSize(1000f);

    shockwave.setParticlesPerSec(0);
    shockwave.setGravity(0, 0, 0);
    shockwave.setLowLife(0.5f);
    shockwave.setHighLife(0.5f);
    shockwave.setInitialVelocity(new Vector3f(0, 0, 0));
    shockwave.setVelocityVariation(0f);
    shockwave.setImagesX(1);
    shockwave.setImagesY(1);
    Material mat = new Material(assetManager, "MatDefs/Planet/Particle.j3md");
    mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/shockwave.png"));
    shockwave.setMaterial(mat);

    rootNode.attachChild(shockwave);
  }

  public void update() {
    shockwave.emitAllParticles();
  }

  public ParticleEmitter getShockwave() {
    return shockwave;
  }
}
