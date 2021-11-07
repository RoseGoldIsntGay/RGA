package rosegoldaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.commands.Backpack;
import rosegoldaddons.commands.LobbySwap;

import java.util.List;

public class OpenSkyblockGui {
    int windowId;
    boolean openingWardrobe = false;
    boolean lobbySwapping = false;
    boolean openingBP = false;

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!rosegoldaddons.commands.Rosedrobe.openWardrobe || openingWardrobe) return;
        new Thread(() -> {
            try {
                openingWardrobe = true;
                if (event.gui instanceof GuiChest) {
                    Container container = ((GuiChest) event.gui).inventorySlots;
                    if (container instanceof ContainerChest) {
                        String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                        List<Slot> invSlots = container.inventorySlots;
                        if (chestName.contains("Pets")) {
                            int i;
                            for (i = 0; i < invSlots.size(); i++) {
                                if (!invSlots.get(i).getHasStack()) continue;
                                String slotName = StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName());
                                //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(slotName));
                                if (slotName.equals("Go Back")) {
                                    clickSlot(invSlots.get(i));
                                }
                            }
                        } else if (chestName.contains("SkyBlock")) {
                            int i;
                            for (i = 0; i < invSlots.size(); i++) {
                                if (!invSlots.get(i).getHasStack()) continue;
                                if (StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName()).equals("Wardrobe")) {
                                    clickSlot(invSlots.get(i));
                                    if (rosegoldaddons.commands.Rosedrobe.slot == 0)
                                        rosegoldaddons.commands.Rosedrobe.openWardrobe = false;
                                }
                            }
                        } else if (chestName.contains("Wardrobe")) {
                            if (rosegoldaddons.commands.Rosedrobe.slot != 0) {
                                int i;
                                for (i = 0; i < invSlots.size(); i++) {
                                    if (!invSlots.get(i).getHasStack()) continue;
                                    String slotName = "Slot " + rosegoldaddons.commands.Rosedrobe.slot + ":";
                                    if (StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName()).contains(slotName)) {
                                        clickSlot(invSlots.get(i));
                                        Minecraft.getMinecraft().thePlayer.closeScreen();
                                        rosegoldaddons.commands.Rosedrobe.openWardrobe = false;
                                    }
                                }
                            }
                        }
                    }
                }
                openingWardrobe = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SubscribeEvent
    public void guiDraw2(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!LobbySwap.swapLobby || lobbySwapping) return;
        new Thread(() -> {
            try {
                lobbySwapping = true;
                if (event.gui instanceof GuiChest) {
                    Container container = ((GuiChest) event.gui).inventorySlots;
                    if (container instanceof ContainerChest) {
                        String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                        List<Slot> invSlots = container.inventorySlots;
                        if (chestName.contains("SkyBlock")) {
                            int i;
                            for (i = 0; i < invSlots.size(); i++) {
                                if (!invSlots.get(i).getHasStack()) continue;
                                if (StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName()).equals("Enter the Crystal Hollows")) {
                                    clickSlot(invSlots.get(i));
                                }
                            }
                        } else if (chestName.contains("Enter the")) {
                            int i;
                            for (i = 0; i < invSlots.size(); i++) {
                                if (!invSlots.get(i).getHasStack()) continue;
                                if (StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName()).equals("Confirm")) {
                                    clickSlot(invSlots.get(i));
                                    LobbySwap.swapLobby = false;
                                }
                            }
                            Thread.sleep(2000);
                            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(Minecraft.getMinecraft().theWorld.getWorldTime() + " ticks"));
                        }
                    }
                }
                lobbySwapping = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SubscribeEvent
    public void guiDraw3(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Backpack.openBP || openingBP) return;
        new Thread(() -> {
            try {
                openingBP = true;
                if (event.gui instanceof GuiChest) {
                    Container container = ((GuiChest) event.gui).inventorySlots;
                    if (container instanceof ContainerChest) {
                        String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                        List<Slot> invSlots = container.inventorySlots;
                        if (chestName.contains("Storage")) {
                            if (Backpack.bpSlot != 0) {
                                int i;
                                for (i = 0; i < invSlots.size(); i++) {
                                    if (!invSlots.get(i).getHasStack()) continue;
                                    String slotName = "Slot " + Backpack.bpSlot;
                                    if (StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName()).contains(slotName)) {
                                        clickSlot(invSlots.get(i));
                                        Backpack.openBP = false;
                                    }
                                }
                            }
                        }
                    }
                }
                openingBP = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void clickSlot(Slot slot) {
        windowId = Minecraft.getMinecraft().thePlayer.openContainer.windowId;
        Minecraft.getMinecraft().playerController.windowClick(windowId, slot.slotNumber, 0, 0, Minecraft.getMinecraft().thePlayer);
    }
}
