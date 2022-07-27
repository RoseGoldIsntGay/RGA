package rosegoldaddons.mixins.blocksize;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockCrops.class)
public class MixinCrops extends BlockBush {

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        worldIn.getBlockState(pos).getBlock().setBlockBounds(0, 0, 0, 1, 1, 1);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        worldIn.getBlockState(pos).getBlock().setBlockBounds(0, 0, 0, 1, 1, 1);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }
}
