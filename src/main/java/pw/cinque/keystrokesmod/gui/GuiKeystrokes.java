package pw.cinque.keystrokesmod.gui;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.GuiScreen;
import pw.cinque.keystrokesmod.KeystrokesMod;
import pw.cinque.keystrokesmod.gui.key.KeyHolder;

/**
 * This <code>GuiScreen</code> allows the user to change the position of the {@link KeyHolder}.
 */
@RequiredArgsConstructor
public class GuiKeystrokes extends GuiScreen {

    private final KeystrokesMod keystrokesMod;
    private int lastMouseX;
    private int lastMouseY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        keystrokesMod.getKeyHolder().draw(mouseX - lastMouseX, mouseY - lastMouseY);
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0) {
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            keystrokesMod.getKeyHolder().onMousePress(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int which) {
        if (which != -1) {
            keystrokesMod.getKeyHolder().onMouseRelease();
        }
    }

    @Override
    public void onGuiClosed() {
        keystrokesMod.getKeyHolder().onMouseRelease();
    }

}
