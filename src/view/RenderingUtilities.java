package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class RenderingUtilities {

	/**
	 * Draw a centered text to the screen.
	 * @param g Does the actual drawing of primitives.
	 * @param font The font used to draw the text.
	 * @param x Horizontal position of the center of the text (0 = left).
	 * @param y Vertical position of the center of the text (0 = top).
	 * @param text Text to draw.
	 */
	public static void drawCenteredText(final Graphics g, final Font font,
		final int x, final int y, final String text) {

		final FontRenderContext context = new FontRenderContext(null, true, true);
		final Rectangle2D rect = font.getStringBounds(text, context);
		final int posX = x - (int)(rect.getWidth() / 2), posY = y + (int)(rect.getHeight());

		g.drawString(text, posX, posY);
	}

}
