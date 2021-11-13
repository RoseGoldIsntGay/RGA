package rosegoldaddons.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import org.jetbrains.annotations.NotNull;
import rosegoldaddons.utils.ChatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UseCooldown implements ICommand {
    public static HashMap<String, Integer> items = new HashMap<String, Integer>();

    @Override
    public String getCommandName() {
        return "usecooldown";
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
        if (args.length == 1 && isNumeric(args[0])) {
            InventoryPlayer inv = Minecraft.getMinecraft().thePlayer.inventory;

            ItemStack curStack = inv.getStackInSlot(Minecraft.getMinecraft().thePlayer.inventory.currentItem);
            if (curStack != null) {
                int cd = Integer.parseInt(args[0]);
                if (cd == 0) {
                    items.remove(curStack.getDisplayName());
                    ChatUtils.sendMessage("§aSuccessfully Removed " + curStack.getDisplayName() + "§a.");
                    return;
                }
                if (cd < 1) {
                    ChatUtils.sendMessage("§cInvalid Miliseconds, Minimum delay 1 Milisecond.");
                    return;
                }
                ChatUtils.sendMessage("§aSuccessfully Added " + curStack.getDisplayName() + "§a with delay of " + cd + " ms.");
                items.put(curStack.getDisplayName(), cd);
            } else {
                ChatUtils.sendMessage("§cError getting current held item.");
            }
        } else {
            ChatUtils.sendMessage("§cInvalid Arguments.");
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
