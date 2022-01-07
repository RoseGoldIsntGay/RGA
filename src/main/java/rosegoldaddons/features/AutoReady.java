package rosegoldaddons.features;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.Main;
import net.minecraft.client.multiplayer.PlayerControllerMP;

public class AutoReady {
    boolean startDung = false;
    boolean checkingEntities = false;
    static int windowId;

    private static void interactWithEntity(Entity entity) {
        PlayerControllerMP playerControllerMP = Minecraft.getMinecraft().playerController;
        playerControllerMP.interactWithEntitySendPacket(Minecraft.getMinecraft().thePlayer, entity);
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Main.configFile.AutoReady || !startDung || checkingEntities) return;
        new Thread(() -> {
            try {
                checkingEntities = true;
                Thread.sleep(500);
                Entity mort = null;
                for (Entity entity1 : (Minecraft.getMinecraft().theWorld.loadedEntityList)) {
                    if (entity1.getName().contains("Mort")) {
                        mort = entity1;
                        interactWithEntity(mort);
                        startDung = false;
                        break;
                    }
                }
                checkingEntities = false;
            } catch (Exception e) {}
        }).start();
    }

    @SubscribeEvent
    public void chat(ClientChatReceivedEvent event) {
        if (!Main.configFile.AutoReady) return;
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText()).toLowerCase();
        if (message.contains("active potion effects have been paused")) {
            startDung = true;
        }
    }

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Main.configFile.AutoReady) return;
        if (event.gui instanceof GuiChest) {
            Container container = ((GuiChest) event.gui).inventorySlots;
            if (container instanceof ContainerChest) {
                String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                List<Slot> invSlots = container.inventorySlots;
                //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(chestName));
                if (chestName.contains("Start Dungeon?")) {
                    int i;
                    for(i = 0; i < invSlots.size(); i++) {
                        if(!invSlots.get(i).getHasStack()) continue;
                        String slotName = StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName());
                        if(slotName.equals("Start Dungeon?")) {
                            clickSlot(invSlots.get(i));
                        }
                    }
                } else if (chestName.contains("Catacombs -")) {
                    int i;
                    for(i = 0; i < invSlots.size(); i++) {
                        if(!invSlots.get(i).getHasStack()) continue;
                        String slotName = StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName());
                        if(slotName.contains(Minecraft.getMinecraft().thePlayer.getName())) {
                            i += 9;
                            if(invSlots.get(i).getHasStack()) {
                                if (StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName()).equals("Not Ready")) {
                                    clickSlot(invSlots.get(i));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void clickSlot(Slot slot) {
        windowId = Minecraft.getMinecraft().thePlayer.openContainer.windowId;
        Minecraft.getMinecraft().playerController.windowClick(windowId, slot.slotNumber, 1, 0, Minecraft.getMinecraft().thePlayer);
    }
}
