package pw.cinque.keystrokesmod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import pw.cinque.keystrokesmod.gui.GuiKeystrokes;

/**
 * This class draws the keystrokes on the screen when the {@link GuiKeystrokes} screen isn't open.
 */
@RequiredArgsConstructor
public class KeystrokesRenderer {

    private final KeystrokesMod keystrokesMod;

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus) {
            keystrokesMod.getKeyHolder().draw(0, 0);
        }
    }

}
