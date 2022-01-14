package rosegoldaddons.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.ChatUtils;
import rosegoldaddons.utils.PlayerUtils;
import rosegoldaddons.utils.RenderUtils;

import java.awt.*;
import java.util.ArrayList;

public class GemstoneAura {
    private static int currentDamage;
    private static byte blockHitDelay = 0;
    private static BlockPos blockPos;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Main.gemNukeToggle) {
            currentDamage = 0;
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            if(PlayerUtils.pickaxeAbilityReady && Minecraft.getMinecraft().thePlayer != null) {
                Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(Minecraft.getMinecraft().thePlayer.inventory.currentItem));
            }
            if(currentDamage > 100) {
                currentDamage = 0;
            }
            if(blockPos != null) {
                IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                if (blockState.getBlock() != Blocks.stained_glass && blockState.getBlock() != Blocks.stained_glass_pane) {
                    currentDamage = 0;
                }
            }
            if(currentDamage == 0) {
                blockPos = closestGemstone();
            }
            if (blockPos != null) {
                if (blockHitDelay > 0) {
                    blockHitDelay--;
                    return;
                }
                MovingObjectPosition fake = Minecraft.getMinecraft().objectMouseOver;
                fake.hitVec = new Vec3(blockPos);
                EnumFacing enumFacing = fake.sideHit;
                if (currentDamage == 0 && enumFacing != null) {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, enumFacing));
                }

                PlayerUtils.swingItem();

                currentDamage += 1;
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Main.gemNukeToggle) return;
        if (blockPos != null) {
            IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
            EnumDyeColor dyeColor = null;
            Color color = Color.BLACK;
            if (blockState.getBlock() == Blocks.stained_glass) {
                dyeColor = blockState.getValue(BlockStainedGlass.COLOR);
            }
            if (blockState.getBlock() == Blocks.stained_glass_pane) {
                dyeColor = blockState.getValue(BlockStainedGlassPane.COLOR);
            }
            if (dyeColor == EnumDyeColor.RED) {
                color = new Color(188, 3, 29);
            } else if (dyeColor == EnumDyeColor.PURPLE) {
                color = new Color(137, 0, 201);
            } else if (dyeColor == EnumDyeColor.LIME) {
                color = new Color(157, 249, 32);
            } else if (dyeColor == EnumDyeColor.LIGHT_BLUE) {
                color = new Color(60, 121, 224);
            } else if (dyeColor == EnumDyeColor.ORANGE) {
                color = new Color(237, 139, 35);
            } else if (dyeColor == EnumDyeColor.YELLOW) {
                color = new Color(249, 215, 36);
            } else if (dyeColor == EnumDyeColor.MAGENTA) {
                color = new Color(214, 15, 150);
            }
            RenderUtils.drawBlockBox(blockPos, color, true, event.partialTicks);
        }
    }

    private BlockPos closestGemstone() {
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
                //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(blockState.getBlock().toString()));
                if (blockState.getBlock() == Blocks.stained_glass) {
                    chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
                if(!Main.configFile.prioblocks) {
                    if (blockState.getBlock() == Blocks.stained_glass_pane) {
                        chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                }
            }
            if(Main.configFile.prioblocks) {
                for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                    IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                    if(!Main.configFile.prioblocks) {
                        if (blockState.getBlock() == Blocks.stained_glass_pane) {
                            chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
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
}
