package rosegoldaddons.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WartSetup implements ICommand {
    public static int wartEnd = 99999;
    public static String cardinal = "west";

    @Override
    public String getCommandName() {
        return "wartsetup";
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
        if(args.length == 2) {
            if(args[0].equals("west")) {
                cardinal = "west";
            } else if(args[0].equals("north")) {
                cardinal = "north";
            } else if(args[0].equals("east")) {
                cardinal = "east";
            } else if(args[0].equals("south")) {
                cardinal = "south";
            }

            if(isNumeric(args[1])) {
                wartEnd = (int) Math.floor(Double.parseDouble(args[1]));
            }
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§cInvalid Arguments."));
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

    @Override
    public int compareTo(@NotNull ICommand o) {
        return 0;
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
