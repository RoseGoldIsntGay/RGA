package rosegoldaddons.features;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.*;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.events.ReceivePacketEvent;
import rosegoldaddons.utils.RenderUtils;
import rosegoldaddons.utils.RotationUtils;

import java.awt.*;
import java.util.ArrayList;

public class HardstoneMacro {
    private ArrayList<Vec3> solved = new ArrayList<>();
    private BlockPos closestStone = null;
    private Vec3 closestChest = null;
    private Thread thread;
    private boolean breaking = false;
    private boolean stopHardstone = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Main.autoHardStone) {
            if (breaking) {
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
                breaking = false;
            }
            return;
        }
        if (event.phase.toString().equals("START") && closestStone != null && !stopHardstone) {
            RotationUtils.facePos(new Vec3(closestStone.getX() + 0.5, closestStone.getY() - 1, closestStone.getZ() + 0.5));
            //Minecraft.getMinecraft().playerController.onPlayerDamageBlock(closestStone, EnumFacing.fromAngle(Minecraft.getMinecraft().thePlayer.rotationYaw));
            MovingObjectPosition objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
            if (objectMouseOver != null && objectMouseOver.typeOfHit.toString() == "BLOCK") {
                BlockPos pos = objectMouseOver.getBlockPos();
                Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                if (block == Blocks.stone || block == Blocks.coal_ore || block == Blocks.diamond_ore || block == Blocks.emerald_ore
                        || block == Blocks.gold_ore || block == Blocks.iron_ore || block == Blocks.lapis_ore || block == Blocks.redstone_ore) {
                    int pickaxe = PowderMacro.findItemInHotbar("Jungle");
                    if (pickaxe != -1) Minecraft.getMinecraft().thePlayer.inventory.currentItem = pickaxe;
                    if (!breaking) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), true);
                        breaking = true;
                    }
                } else {
                    if (breaking) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
                        breaking = false;
                    }
                }

            }
        }
        if (stopHardstone) {
            if (breaking) {
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
                breaking = false;
            }
        }
    }

    @SubscribeEvent
    public void receivePacket(ReceivePacketEvent event) {
        if (!Main.autoHardStone) return;
        if (event.packet instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles) event.packet;
            if (packet.getParticleType().equals(EnumParticleTypes.CRIT)) {
                Vec3 particlePos = new Vec3(packet.getXCoordinate(), packet.getYCoordinate() - 0.7, packet.getZCoordinate());
                if (closestChest != null) {
                    stopHardstone = true;
                    double dist = closestChest.distanceTo(particlePos);
                    if (dist < 1) {
                        particlePos = particlePos.add(new Vec3(0, -1, 0));
                        int drill = PowderMacro.findItemInHotbar("X655");
                        if (drill != -1) Minecraft.getMinecraft().thePlayer.inventory.currentItem = drill;
                        RotationUtils.facePos(particlePos);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        new Thread(() -> {
            try {
                if (event.gui instanceof GuiChest) {
                    Container container = ((GuiChest) event.gui).inventorySlots;
                    if (container instanceof ContainerChest) {
                        String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                        if (chestName.contains("Treasure")) {
                            breaking = false;
                            solved.add(closestChest);
                            stopHardstone = false;
                            Thread.sleep(20);
                            Minecraft.getMinecraft().thePlayer.closeScreen();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Main.autoHardStone) return;
        closestStone = closestStone();
        closestChest = closestChest();
        if (closestStone != null) {
            RenderUtils.drawBlockBox(closestStone, new Color(128, 128, 128), true, event.partialTicks);
        }
        if (closestChest != null) {
            RenderUtils.drawBlockBox(new BlockPos(closestChest.xCoord, closestChest.yCoord, closestChest.zCoord), new Color(255, 128, 0), true, event.partialTicks);
        } else {
            stopHardstone = false;
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        solved.clear();
    }

    private BlockPos closestStone() {
        int r = 6;
        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
        playerPos.add(0, 1, 0);
        Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, 1, r);
        Vec3i vec3i2 = new Vec3i(r, 0, r);
        ArrayList<Vec3> stones = new ArrayList<Vec3>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i2))) {
                IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(blockState.getBlock().toString()));
                if (blockState.getBlock() == Blocks.stone) {
                    stones.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
            }
        }
        double smallest = 9999;
        Vec3 closest = null;
        for (Vec3 stone : stones) {
            double dist = stone.distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = stone;
            }
        }
        if (closest != null && smallest < 5) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }

    private Vec3 closestChest() {
        int r = 6;
        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
        playerPos.add(0, 1, 0);
        Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> chests = new ArrayList<>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(blockState.getBlock().toString()));
                if (blockState.getBlock() == Blocks.chest) {
                    chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
            }
        }
        double smallest = 9999;
        Vec3 closest = null;
        for (Vec3 chest : chests) {
            if(!solved.contains(chest)) {
                double dist = chest.distanceTo(playerVec);
                if (dist < smallest) {
                    smallest = dist;
                    closest = chest;
                }
            }
        }
        return closest;
    }
}
