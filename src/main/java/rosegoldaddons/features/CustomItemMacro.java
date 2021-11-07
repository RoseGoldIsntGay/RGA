package rosegoldaddons.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.commands.UseCooldown;
import rosegoldaddons.commands.WartSetup;

import java.lang.reflect.Method;

public class CustomItemMacro {
    private Thread thread;
    private int milis = 0;

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!Main.autoUseItems) return;
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(() -> {
                try {
                    milis++;
                    int prevItem = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
                    for (String i : UseCooldown.items.keySet()) {
                        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(Math.floor(UseCooldown.items.get(i)/1000) + " " + milis));
                        if (milis % Math.floor(UseCooldown.items.get(i)/100) == 0) {
                            int slot = findItemInHotbar(i);
                            if (slot != -1) {
                                Minecraft.getMinecraft().thePlayer.inventory.currentItem = slot;
                                rightClick();
                                Thread.sleep(1);
                            }
                        }
                    }
                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = prevItem;
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Custom Item Use");
            thread.start();
        }
    }

    public static void rightClick() {
        try {
            Method rightClickMouse = null;
            try {
                rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse");
            } catch (NoSuchMethodException e) {
                rightClickMouse = Minecraft.class.getDeclaredMethod("func_147121_ag");
            }
            rightClickMouse.setAccessible(true);
            rightClickMouse.invoke(Minecraft.getMinecraft());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int findItemInHotbar(String name) {
        InventoryPlayer inv = Minecraft.getMinecraft().thePlayer.inventory;
        for (int i = 0; i < 9; i++) {
            ItemStack curStack = inv.getStackInSlot(i);
            if (curStack != null) {
                if (curStack.getDisplayName().contains(name)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
