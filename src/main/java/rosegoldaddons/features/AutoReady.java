package rosegoldaddons.features;

import com.mojang.realmsclient.gui.ChatFormatting;

import java.awt.Color;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.Config;
import rosegoldaddons.Main;
import rosegoldaddons.events.RenderLivingEntityEvent;
import rosegoldaddons.utils.OutlineUtils;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import scala.collection.parallel.ParIterableLike;

public class AutoReady {
    boolean startDung = false;
    boolean checkingEntities = false;
    String currentMenu = "";
    static int windowId;

    private double getDist2Entities(Entity entity1, Entity entity2) {
        return Math.sqrt(Math.pow((entity1.getPosition().getX() - entity2.getPosition().getX()), 2) + Math.pow((entity1.getPosition().getY() - entity2.getPosition().getY()), 2) + Math.pow((entity1.getPosition().getZ() - entity2.getPosition().getZ()), 2));
    }

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
