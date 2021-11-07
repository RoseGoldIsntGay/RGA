package rosegoldaddons.features;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.RenderUtils;
import rosegoldaddons.utils.RotationUtils;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class BlockBreakAura {
    BlockPos gem = null;
    private Thread thread;
    private boolean breaking = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(!Main.gemNukeToggle) {
            if(breaking) {
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
                breaking = false;
            }
            return;
        }
        if(event.phase.toString().equals("START") && gem != null) {
            RotationUtils.facePos(new Vec3(gem.getX()+0.5, gem.getY()-1, gem.getZ()+0.5));
            MovingObjectPosition objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
            if(objectMouseOver != null && objectMouseOver.typeOfHit.toString() == "BLOCK") {
                BlockPos pos = objectMouseOver.getBlockPos();
                Block gem = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                if(gem == Blocks.stained_glass || gem == Blocks.stained_glass_pane) {
                    if(!breaking) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), true);
                        breaking = true;
                    }
                } else {
                    if(breaking) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
                        breaking = false;
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if(!Main.gemNukeToggle) return;
        gem = closestGemstone();
        if (gem != null) {
            RenderUtils.drawBlockBox(gem, Color.RED, true, event.partialTicks);
        }
    }

    private BlockPos closestGemstone() {
        int r = 4;
        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
        playerPos.add(0, 1, 0);
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
        if (closest != null && smallest < 4) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }

}
