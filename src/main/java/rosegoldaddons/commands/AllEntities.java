package rosegoldaddons.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;
import rosegoldaddons.utils.ChatUtils;

import java.util.ArrayList;
import java.util.List;

public class AllEntities implements ICommand {
    @Override
    public String getCommandName() {
        return "allentities";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        for (Entity entity1 : (Minecraft.getMinecraft().theWorld.loadedEntityList)) {
            ChatUtils.sendMessage(""+entity1);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public int compareTo(@NotNull ICommand o) {
        return 0;
    }
}
