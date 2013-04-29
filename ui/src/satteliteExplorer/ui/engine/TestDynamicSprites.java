package satteliteExplorer.ui.engine;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import satteliteExplorer.ui.engine.sprites.Sprite;
import satteliteExplorer.ui.engine.sprites.SpriteImage;
import satteliteExplorer.ui.engine.sprites.SpriteManager;
import satteliteExplorer.ui.engine.sprites.SpriteMesh;
import satteliteExplorer.ui.engine.util.FileUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//all tests enabled:
//45-55 fps: KEEP_BUFFER, 30000 sprites. Memory: 200m at start then decreasing to 100m.
//50 fps: ALLOCATE_NEW_BUFFER, 30000 sprites. Memory: 200m then increasing to 1giga.
public class TestDynamicSprites extends SimpleApplication {
  private SpriteManager spriteManager;
  private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
  private SpriteImage[] npcList;

  //performance
  private int MAX_SPRITES = 1;
  private SpriteMesh.Strategy strategy = SpriteMesh.Strategy.ALLOCATE_NEW_BUFFER;
  private int MAX_TEXTURE_WIDTH = 1024;
  private int MAX_TEXTURE_HEIGHT = 1024;
  //test
  private int SPRITE_TO_USE = 0;
  private float MIN_POS = 0;
  private float MAX_POS = 30;

  public static void main(String[] args) {
    Logger.getLogger("").setLevel(Level.SEVERE);
    TestDynamicSprites app = new TestDynamicSprites();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    com.jme3.scene.Node node = new Node("billboard");
    BillboardControl billboardNode = new BillboardControl();
    node.addControl(billboardNode);
    rootNode.attachChild(node);

    spriteManager = new SpriteManager(MAX_TEXTURE_WIDTH, MAX_TEXTURE_HEIGHT, strategy, node, assetManager);
    getStateManager().attach(spriteManager);

    getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    getFlyByCamera().setMoveSpeed(50);
    getCamera().setLocation(new Vector3f(-15, 0, 55));
    getCamera().lookAtDirection(new Vector3f(12, 7.5f, -15), Vector3f.UNIT_Y);

    File npcLocation = new File(FileUtilities.ASSET_DIRECTORY + "Textures/");
    String[] fileList = npcLocation.list(FileUtilities.SUPPORTED_IMAGES);
    npcList = new SpriteImage[fileList.length];
    for (int i = 0; i < fileList.length; i++) {
      npcList[i] = spriteManager.createSpriteImage("Textures/" + fileList[i], false);
    }

    for (int i = 0; i < MAX_SPRITES; i++) {
      Sprite sprite = new Sprite(npcList[SPRITE_TO_USE]);
      sprite.getPosition().x = MIN_POS + (float) (Math.random() * MAX_POS);
      sprite.getPosition().y = MIN_POS + (float) (Math.random() * MAX_POS);
      sprite.getPosition().z = 3 + MIN_POS + (float) (Math.random() * MAX_POS);
      sprites.add(sprite);
    }
  }

  @Override
  public void simpleUpdate(float tpf) {
    super.simpleUpdate(tpf);
  }
}