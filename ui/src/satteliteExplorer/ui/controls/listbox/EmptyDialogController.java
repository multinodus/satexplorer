package satteliteExplorer.ui.controls.listbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

import java.util.Properties;

/**
 * The ListBoxDialog to show off the new ListBox and a couple of more new Nifty 1.3 things.
 *
 * @author void
 */
public class EmptyDialogController implements Controller {
  private Screen screen;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.screen = screen;
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {

  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }
}
