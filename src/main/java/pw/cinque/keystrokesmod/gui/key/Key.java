package pw.cinque.keystrokesmod.gui.key;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pw.cinque.keystrokesmod.KeystrokesMod;
import pw.cinque.keystrokesmod.util.ColorUtil;

/**
 * This class represents a keyboard key (or mouse button).
 *
 * @see KeyHolder
 */
@RequiredArgsConstructor
public class Key {

    private final KeyBinding keyBinding;
    @Setter(AccessLevel.PACKAGE)
    private KeyHolder parent;
    @Setter(AccessLevel.PACKAGE)
    private KeystrokesMod keystrokesMod;
    @Getter
    private double height = 24.0;
    private Type type = Type.NORMAL;
    private boolean wasPressed;
    private long pressTime;

    /**
     * Marks this key as the spacebar key.
     */
    public Key setSpaceBar() {
        height = 14.0;
        type = Type.SPACE_BAR;
        return this;
    }

    /**
     * Marks this key as the left mouse button.
     */
    public Key setLeftMouse() {
        height = 20.0;
        type = Type.LEFT_MOUSE;
        return this;
    }

    /**
     * Marks this key as the right mouse button.
     */
    public Key setRightMouse() {
        height = 20.0;
        type = Type.RIGHT_MOUSE;
        return this;
    }

    private boolean isPressed() {
        int keyCode = keyBinding.getKeyCode();
        boolean pressed = keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) :
                Keyboard.isKeyDown(keyCode);

        if (wasPressed != pressed) {
            pressTime = System.currentTimeMillis();
        }

        wasPressed = pressed;
        return pressed;
    }

    /**
     * Draws the key to the screen.
     *
     * @param width  The key width
     * @param height The key height
     */
    public void draw(double width, double height) {
        boolean pressed = isPressed();
        float pressModifier = Math.min(1.0f, (System.currentTimeMillis() - pressTime) / 100.0f);
        float brightness = (pressed ? pressModifier : (1.0f - pressModifier)) * 0.8f;

        // draw key background
        GL11.glColor4f(brightness, brightness, brightness, 0.6f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(0.0, height, 0.0);
        GL11.glVertex3d(width, height, 0.0);
        GL11.glVertex3d(width, 0.0, 0.0);
        GL11.glVertex3d(0.0, 0.0, 0.0);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (parent.isOutlineEnabled()) {
            drawColoredRect(0.0, 0.0, width, 1.0, pressed);
            drawColoredRect(width - 1.0, 0.0, width, height, pressed);
            drawColoredRect(width, height, 0.0, height - 1.0, pressed);
            drawColoredRect(1.0, height, 0.0, 0.0, pressed);
        }

        switch (type) {
            case NORMAL:
                drawKeyText(Keyboard.getKeyName(keyBinding.getKeyCode()), height, width, pressed);
                return;
            case SPACE_BAR:
                drawSpaceBar(height, width, pressed);
                return;
            case LEFT_MOUSE:
                drawKeyText(getMouseText(true), height, width, pressed);
                return;
            case RIGHT_MOUSE:
                drawKeyText(getMouseText(false), height, width, pressed);
        }
    }

    private void drawKeyText(String text, double keyHeight, double keyWidth, boolean pressed) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int textWidth = fontRenderer.getStringWidth(text);
        int x = ((int) keyWidth - textWidth) / 2;
        int y = ((int) keyHeight - fontRenderer.FONT_HEIGHT) / 2 + 1;

        fontRenderer.drawString(text, x, y, parent.getColor(x, pressed));
    }

    private void drawSpaceBar(double keyHeight, double keyWidth, boolean pressed) {
        drawColoredRect(keyWidth * 0.25, keyHeight / 2.0 - 1.0, keyWidth * 0.75,
                keyHeight / 2.0 + 1.0, pressed);
    }

    private String getMouseText(boolean left) {
        int cps = (left ? keystrokesMod.getLeftClickCounter() :
                keystrokesMod.getRightClickCounter()).getCps();

        if (cps == 0) {
            return left ? "LMB" : "RMB";
        } else {
            return cps + " CPS";
        }
    }

    private void drawColoredRect(double x1, double y1, double x2, double y2, boolean invertColor) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);
        ColorUtil.setGlColor(parent.getColor(x1, invertColor));
        GL11.glVertex3d(x1, y2, 0.0);
        ColorUtil.setGlColor(parent.getColor(x2, invertColor));
        GL11.glVertex3d(x2, y2, 0.0);
        GL11.glVertex3d(x2, y1, 0.0);
        ColorUtil.setGlColor(parent.getColor(x1, invertColor));
        GL11.glVertex3d(x1, y1, 0.0);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private enum Type {

        NORMAL, SPACE_BAR, LEFT_MOUSE, RIGHT_MOUSE

    }

}
