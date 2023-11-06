package client.gui.inventory;

import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;

public interface InvSlotControl extends NiftyControl {

	void activate();

	String getText();

	void setText(final String text);

	int getTextWidth();

	int getTextHeight();

	RenderFont getFont();

	void setFont(final RenderFont fontParam);

	VerticalAlign getTextVAlign();

	void setTextVAlign(final VerticalAlign newTextVAlign);

	HorizontalAlign getTextHAlign();

	void setTextHAlign(final HorizontalAlign newTextHAlign);

	Color getTextColor();

	void setTextColor(final Color newColor);

	void setIcon(final String iconPath);

}
