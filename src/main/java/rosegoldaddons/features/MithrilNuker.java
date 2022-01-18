package rosegoldaddons.features;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.ChatUtils;
import rosegoldaddons.utils.PlayerUtils;
import rosegoldaddons.utils.RenderUtils;
import rosegoldaddons.utils.RotationUtils;

import java.awt.*;
import java.util.ArrayList;

public class MithrilNuker {
    private static int currentDamage;
    private static byte blockHitDelay = 0;
    private static BlockPos blockPos;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Main.mithrilNuker) {
            currentDamage = 0;
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            if(PlayerUtils.pickaxeAbilityReady) {
                Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(Minecraft.getMinecraft().thePlayer.inventory.currentItem));
            }
            if(currentDamage > 100) {
                currentDamage = 0;
            }
            if(blockPos != null && Minecraft.getMinecraft().theWorld != null) {
                IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                if (blockState.getBlock() == Blocks.bedrock || blockState.getBlock() == Blocks.air) {
                    currentDamage = 0;
                }
            }
            if(currentDamage == 0) {
                blockPos = closestMithril();
            }
            if (blockPos != null) {
                if (blockHitDelay > 0) {
                    blockHitDelay--;
                    return;
                }
                if (currentDamage == 0) {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                    if(Main.configFile.mithrilLook) {
                        RotationUtils.facePos(new Vec3(blockPos.getX() + 0.5, blockPos.getY() - 1, blockPos.getZ() + 0.5));
                    }
                }
                PlayerUtils.swingItem();

                currentDamage += 1; //finally used after all
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Main.mithrilNuker) return;
        if (blockPos != null) {
            IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
            if(blockState.getBlock() == Blocks.stone) {
                RenderUtils.drawBlockBox(blockPos, Color.WHITE, true, event.partialTicks);
            } else {
                RenderUtils.drawBlockBox(blockPos, Color.BLUE, true, event.partialTicks);
            }
        }
    }

    private BlockPos closestMithril() {
        int r = 6;
        if (Minecraft.getMinecraft().thePlayer == null) return null;
        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> chests = new ArrayList<Vec3>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                if (isMithril(blockState)) {
                    chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
                if(Main.configFile.includeOres) {
                    if (blockState.getBlock() == Blocks.coal_ore || blockState.getBlock() == Blocks.diamond_ore || blockState.getBlock() == Blocks.gold_ore || blockState.getBlock() == Blocks.redstone_ore || blockState.getBlock() == Blocks.iron_ore || blockState.getBlock() == Blocks.lapis_ore || blockState.getBlock() == Blocks.emerald_ore || blockState.getBlock() == Blocks.netherrack ) {
                        chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                }
            }
        }
        double smallest = 9999;
        Vec3 closest = null;
        for (int i = 0; i < chests.size(); i++) {
            double dist = chests.get(i).distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = chests.get(i);
            }
        }
        if (closest != null && smallest < 5) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }

    private boolean isMithril(IBlockState blockState) {
        if(blockState.getBlock() == Blocks.prismarine) {
            return true;
        } else if(blockState.getBlock() == Blocks.wool) {
            return true;
        } else if(blockState.getBlock() == Blocks.stained_hardened_clay) {
            return true;
        } else if(!Main.configFile.ignoreTitanium && blockState.getBlock() == Blocks.stone && blockState.getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE_SMOOTH) {
            return true;
        } else if(blockState.getBlock() == Blocks.gold_block) {
            return true;
        }
        return false;
    }
}
