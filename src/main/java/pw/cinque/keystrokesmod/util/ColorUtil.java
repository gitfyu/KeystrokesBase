package pw.cinque.keystrokesmod.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.lwjgl.opengl.GL11;

/**
 * This class contains color-related utility methods for the renderer.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ColorUtil {

    /**
     * Sets the current OpenGL color.
     *
     * @param color The ARGB color
     */
    public static void setGlColor(int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0f;
        float red = (color >> 16 & 0xFF) / 255.0f;
        float green = (color >> 8 & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;

        GL11.glColor4f(red, green, blue, alpha);
    }

}
