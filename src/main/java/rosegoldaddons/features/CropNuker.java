package rosegoldaddons.features;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.PlayerUtils;

import java.util.ArrayList;

public class CropNuker {
    private static BlockPos crop;
    private static ArrayList<BlockPos> broken = new ArrayList<>();
    private static int ticks = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) return;
        if (!Main.nukeCrops || Minecraft.getMinecraft().thePlayer == null) {
            broken.clear();
            return;
        }
        crop = closestCrop();
        if (crop != null) {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, crop, EnumFacing.DOWN));
            PlayerUtils.swingItem();
            broken.add(crop);
        }

    }

    private BlockPos closestCrop() {
        int r = 6;
        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        Vec3i vec3iCane = new Vec3i(r, 0, r);
        ArrayList<Vec3> warts = new ArrayList<>();
        if (playerPos != null) {
            switch (Main.configFile.farmNukeIndex) {
                case 0:
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() == Blocks.nether_wart || blockState.getBlock() == Blocks.potatoes || blockState.getBlock() == Blocks.wheat || blockState.getBlock() == Blocks.carrots || blockState.getBlock() == Blocks.pumpkin || blockState.getBlock() == Blocks.melon_block || blockState.getBlock() == Blocks.brown_mushroom || blockState.getBlock() == Blocks.red_mushroom || blockState.getBlock() == Blocks.cocoa) {
                            if (!broken.contains(blockPos)) {
                                warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                            }
                        }
                    }
                    break;
                case 1:
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3iCane), playerPos.subtract(vec3iCane))) {
                        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() == Blocks.reeds || blockState.getBlock() == Blocks.cactus) {
                            if (!broken.contains(blockPos)) {
                                warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                            }
                        }
                    }
                    break;
                case 2:
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() == Blocks.nether_wart) {
                            if (!broken.contains(blockPos)) {
                                warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                            }
                        }
                    }
                    break;
                case 3:
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() == Blocks.wheat) {
                            if (!broken.contains(blockPos)) {
                                warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                            }
                        }
                    }
                    break;
                case 4:
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() == Blocks.carrots) {
                            if (!broken.contains(blockPos)) {
                                warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                            }
                        }
                    }
                    break;
                case 5:
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() == Blocks.potatoes) {
                            if (!broken.contains(blockPos)) {
                                warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                            }
                        }
                    }
                    break;
                case 6:
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() == Blocks.pumpkin) {
                            if (!broken.contains(blockPos)) {
                                warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                            }
                        }
                    }
                    break;
                case 7:
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() == Blocks.melon_block) {
                            if (!broken.contains(blockPos)) {
                                warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                            }
                        }
                    }
                    break;
                case 8:
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() == Blocks.cocoa) {
                            if (!broken.contains(blockPos)) {
                                warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                            }
                        }
                    }
                    break;
            }
        }
        double smallest = 9999;
        Vec3 closest = null;
        for (Vec3 wart : warts) {
            double dist = wart.distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = wart;
            }
        }
        if (closest != null && smallest < 5) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }
}
