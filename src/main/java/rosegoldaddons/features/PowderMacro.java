package rosegoldaddons.features;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.Main;
import rosegoldaddons.events.ReceivePacketEvent;
import rosegoldaddons.utils.RenderUtils;
import rosegoldaddons.utils.RotationUtils;

import java.awt.*;
import java.util.ArrayList;

public class PowderMacro {
    private static Vec3 closestChest = null;

    @SubscribeEvent
    public void receivePacket(ReceivePacketEvent event) {
        if (!Main.powderMacro) return;
        if (event.packet instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles) event.packet;
            if (packet.getParticleType().equals(EnumParticleTypes.CRIT)) {
                Vec3 particlePos = new Vec3(packet.getXCoordinate(), packet.getYCoordinate() - 0.7, packet.getZCoordinate());
                if (closestChest != null) {
                    double dist = closestChest.distanceTo(particlePos);
                    if (dist < 1) {
                        particlePos = particlePos.add(new Vec3(0, -1, 0));
                        int drill = findItemInHotbar("X655");
                        if(drill != -1) Main.mc.thePlayer.inventory.currentItem = drill;
                        RotationUtils.facePos(particlePos);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Main.powderMacro) return;
        closestChest = closestChest();
        if (closestChest != null) {
            RenderUtils.drawBlockBox(new BlockPos(closestChest.xCoord, closestChest.yCoord, closestChest.zCoord), new Color(255, 128, 0), true, event.partialTicks);
        }
    }

    private static Vec3 closestChest() {
        int r = 6;
        BlockPos playerPos = Main.mc.thePlayer.getPosition();
        playerPos.add(0, 1, 0);
        Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> chests = new ArrayList<Vec3>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Main.mc.theWorld.getBlockState(blockPos);
                //Main.mc.thePlayer.addChatMessage(new ChatComponentText(blockState.getBlock().toString()));
                if (blockState.getBlock() == Blocks.chest) {
                    chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
            }
        }
        double smallest = 9999;
        Vec3 closest = null;
        for (int i = 0; i <chests.size(); i++){
            double dist = chests.get(i).distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = chests.get(i);
            }
        }
        return closest;
    }

    public static int findItemInHotbar(String name) {
        InventoryPlayer inv = Main.mc.thePlayer.inventory;
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
