package rosegoldaddons.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import rosegoldaddons.Main;
import rosegoldaddons.events.ClickEvent;
import rosegoldaddons.utils.ChatUtils;
import rosegoldaddons.utils.RenderUtils;

import java.awt.*;
import java.util.ArrayList;

public class EntityReach {
    private Thread thread;
    private static Entity toInteract;

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (!Main.configFile.entityReach) return;
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            if (toInteract != null) {
                if (toInteract instanceof EntityArmorStand) {
                    interactWithEntity2(toInteract);
                }
                interactWithEntity(toInteract);
                toInteract = null;
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Main.configFile.entityReach) return;
        ArrayList<Entity> entities = getAllEntitiesInRange();
        for (Entity entity : entities) {
            if (isLookingAtAABB(entity.getEntityBoundingBox(), event)) {
                toInteract = entity;
                return;
            }
        }
        if (!Main.configFile.sticky) {
            toInteract = null;
        }
    }

    @SubscribeEvent
    public void renderWorld2(RenderWorldLastEvent event) {
        if (!Main.configFile.entityReach) return;
        if (toInteract != null) {
            RenderUtils.drawEntityBox(toInteract, Color.RED, true, event.partialTicks);
        }
    }

    /*@SubscribeEvent
    public void onInteract(ClickEvent.Right event) {
        MovingObjectPosition objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (objectMouseOver != null) {
            ChatUtils.sendMessage(""+objectMouseOver.entityHit);
        }
    }*/

    private static boolean isLookingAtAABB(AxisAlignedBB aabb, RenderWorldLastEvent event) {
        Vec3 position = new Vec3(Minecraft.getMinecraft().thePlayer.posX, (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight()), Minecraft.getMinecraft().thePlayer.posZ);
        Vec3 look = Minecraft.getMinecraft().thePlayer.getLook(event.partialTicks);
        look = scaleVec(look, 0.2F);
        for (int i = 0; i < 200; i++) {
            if (aabb.minX <= position.xCoord && aabb.maxX >= position.xCoord && aabb.minY <= position.yCoord && aabb.maxY >= position.yCoord && aabb.minZ <= position.zCoord && aabb.maxZ >= position.zCoord) {
                return true;
            }
            position = position.add(look);
        }

        return false;
    }

    private static ArrayList<Entity> getAllEntitiesInRange() {
        ArrayList<Entity> entities = new ArrayList<>();
        Vec3 playerPos = Minecraft.getMinecraft().thePlayer.getPositionVector();
        playerPos.add(new Vec3(0, 1, 0));
        for (Entity entity1 : (Minecraft.getMinecraft().theWorld.loadedEntityList)) {
            if (playerPos.distanceTo(entity1.getPositionVector()) < 30 && !(entity1 instanceof EntityPlayerSP)) {
                entities.add(entity1);
            }
        }
        return entities;
    }

    private static void interactWithEntity(Entity entity) {
        PlayerControllerMP playerControllerMP = Minecraft.getMinecraft().playerController;
        playerControllerMP.interactWithEntitySendPacket(Minecraft.getMinecraft().thePlayer, entity);
    }

    private static void interactWithEntity2(Entity entity) {
        PlayerControllerMP playerControllerMP = Minecraft.getMinecraft().playerController;
        MovingObjectPosition mop = new MovingObjectPosition(entity);
        playerControllerMP.isPlayerRightClickingOnEntity(Minecraft.getMinecraft().thePlayer, entity, mop);
    }


    private static Vec3 scaleVec(Vec3 vec, float f) {
        return new Vec3(vec.xCoord * (double) f, vec.yCoord * (double) f, vec.zCoord * (double) f);
    }

}


