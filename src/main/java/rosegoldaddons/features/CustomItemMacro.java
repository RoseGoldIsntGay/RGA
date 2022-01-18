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
                    int prevItem = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
                    for (String i : UseCooldown.RCitems.keySet()) {
                        if (milis % Math.floor(UseCooldown.RCitems.get(i)/100) == 0) {
                            int slot = findItemInHotbar(i);
                            if (slot != -1) {
                                Minecraft.getMinecraft().thePlayer.inventory.currentItem = slot;
                                Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(slot));
                            }
                        }
                    }
                    for (String i : UseCooldown.LCitems.keySet()) {
                        if (milis % Math.floor(UseCooldown.LCitems.get(i)/100) == 0) {
                            int slot = findItemInHotbar(i);
                            if (slot != -1) {
                                Minecraft.getMinecraft().thePlayer.inventory.currentItem = slot;
                                Thread.sleep(100);
                                click();
                            }
                        }
                    }
                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = prevItem;
                    milis++;
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Custom Item Use");
            thread.start();
        }
    }

    public static void click() {
        /*try {
            Method clickMouse;
            try {
                clickMouse = Minecraft.class.getDeclaredMethod("func_147116_af");
            } catch (NoSuchMethodException e) {
                clickMouse = Minecraft.class.getDeclaredMethod("clickMouse");
            }
            clickMouse.setAccessible(true);
            clickMouse.invoke(Minecraft.getMinecraft());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        Minecraft.getMinecraft().thePlayer.swingItem();
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
