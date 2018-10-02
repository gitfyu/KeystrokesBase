package pw.cinque.keystrokesmod;

import lombok.RequiredArgsConstructor;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

@RequiredArgsConstructor
class KeystrokesCommand extends CommandBase {

    private final KeystrokesMod keystrokesMod;

    @Override
    public String getCommandName() {
        return "keystrokesmod";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
       keystrokesMod.openGui();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
