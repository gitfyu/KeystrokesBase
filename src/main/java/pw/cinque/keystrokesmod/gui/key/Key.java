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
import pw.cinque.keystrokesmod.util.ClickCounter;
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
    @Getter
    private double height = 24.0;
    private Type type = Type.NORMAL;
    private ClickCounter clickCounter;
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
        clickCounter = new ClickCounter();
        return this;
    }

    /**
     * Marks this key as the right mouse button.
     */
    public Key setRightMouse() {
        height = 20.0;
        type = Type.RIGHT_MOUSE;
        clickCounter = new ClickCounter();
        return this;
    }

    private boolean isPressed() {
        int keyCode = keyBinding.getKeyCode();
        boolean pressed = keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) :
                Keyboard.isKeyDown(keyCode);

        if (clickCounter != null && !wasPressed && pressed) {
            clickCounter.onClick();
        }

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

        GL11.glColor4f(brightness, brightness, brightness, 0.6f);
        drawRect(0.0, 0.0, width, height);

        int color = parent.getColor().getRGB();

        if (parent.isOutlineEnabled()) {
            ColorUtil.setGlColor(pressed ? ColorUtil.invert(color) : color);
            drawRect(0.0, 0.0, width, 1.0);
            drawRect(width - 1.0, 0.0, width, height);
            drawRect(width, height, 0.0, height - 1.0);
            drawRect(1.0, height, 0.0, 0.0);
        }

        switch (type) {
            case NORMAL:
                drawKeyText(Keyboard.getKeyName(keyBinding.getKeyCode()), height, width, pressed,
                        color);
                return;
            case SPACE_BAR:
                drawSpaceBar(height, width, pressed, color);
                return;
            case LEFT_MOUSE:
                drawKeyText(getMouseText(true), height, width, pressed, color);
                return;
            case RIGHT_MOUSE:
                drawKeyText(getMouseText(false), height, width, pressed, color);
        }
    }

    private void drawKeyText(String text, double keyHeight, double keyWidth, boolean pressed,
                             int color) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int textWidth = fontRenderer.getStringWidth(text);
        int x = ((int) keyWidth - textWidth) / 2;
        int y = ((int) keyHeight - fontRenderer.FONT_HEIGHT) / 2 + 1;

        fontRenderer.drawString(text, x, y, pressed ? ColorUtil.invert(color) : color);
    }

    private void drawSpaceBar(double keyHeight, double keyWidth, boolean pressed, int color) {
        ColorUtil.setGlColor(pressed ? ColorUtil.invert(color) : color);
        drawRect(keyWidth * 0.25, keyHeight / 2.0 - 1.0, keyWidth * 0.75, keyHeight / 2.0 + 1.0);
    }

    private String getMouseText(boolean left) {
        int cps = clickCounter.getCps();

        if (cps == 0) {
            return left ? "LMB" : "RMB";
        } else {
            return cps + " CPS";
        }
    }

    private void drawRect(double x1, double y1, double x2, double y2) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(x1, y2, 0.0);
        GL11.glVertex3d(x2, y2, 0.0);
        GL11.glVertex3d(x2, y1, 0.0);
        GL11.glVertex3d(x1, y1, 0.0);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private enum Type {

        NORMAL, SPACE_BAR, LEFT_MOUSE, RIGHT_MOUSE

    }

}
