package satteliteExplorer.ui.controls.common;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 10.05.13
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class ImagePanelController implements Controller {
  private Element element;
  private FocusHandler focusHandler;
  private Nifty nifty;
  private Screen screen;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.element = element;
    this.focusHandler = screen.getFocusHandler();
    this.nifty = nifty;
    this.screen = screen;
  }


  public void changeImage() {
    NiftyImage newImage = nifty.getRenderEngine().createImage("Textures/chart.png", false);

    Element element = screen.findElementByName("chartImg");

    element.getRenderer(ImageRenderer.class).setImage(newImage);
  }

  @Override
  public void onStartScreen() {
//    changeImage();
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  @Override
  public void onFocus(final boolean getFocus) {

  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }
}
