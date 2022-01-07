package rosegoldaddons.features;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;

import java.util.ArrayList;

public class CanePlanter {
    //when you're down bad
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Main.placeCane || event.phase == TickEvent.Phase.END) {
            return;
        }
        int cane = findItemInHotbar("Cane");
        BlockPos dirt = furthestEmptyDirt();
        if (cane != -1 && dirt != null) {
            ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(cane);
            Minecraft.getMinecraft().thePlayer.inventory.currentItem = cane;
            Minecraft.getMinecraft().playerController.onPlayerRightClick(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, item, dirt, EnumFacing.UP, Minecraft.getMinecraft().thePlayer.getLookVec());
        }
    }

    private BlockPos furthestEmptyDirt() {
        int r = 5;
        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
        playerPos.add(0, 1, 0);
        Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> dirts = new ArrayList<Vec3>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                IBlockState blockState2 = Minecraft.getMinecraft().theWorld.getBlockState(blockPos.add(0, 1, 0));
                //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(blockState.getBlock().toString()));
                if (blockState.getBlock() == Blocks.dirt && blockState2.getBlock() == Blocks.air) {
                    dirts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
            }
        }
        double biggest = -1;
        Vec3 furthest = null;
        for (int i = 0; i < dirts.size(); i++) {
            double dist = dirts.get(i).distanceTo(playerVec);
            if (dist > biggest) {
                biggest = dist;
                furthest = dirts.get(i);
            }
        }
        if (furthest != null && biggest < 4) {
            return new BlockPos(furthest.xCoord, furthest.yCoord, furthest.zCoord);
        }
        return null;
    }

    private static int findItemInHotbar(String name) {
        InventoryPlayer inv = Minecraft.getMinecraft().thePlayer.inventory;
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
