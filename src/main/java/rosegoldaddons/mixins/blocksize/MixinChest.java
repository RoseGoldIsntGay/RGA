package rosegoldaddons.mixins.blocksize;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rosegoldaddons.Main;

@Mixin(BlockChest.class)
public class MixinChest extends Block {

    public MixinChest(Material materialIn) {
        super(materialIn);
    }

    @Inject(method = "setBlockBoundsBasedOnState", at = @At("HEAD"), cancellable = true)
    private void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos, CallbackInfo ci) {
        if(Main.configFile.dungeonBlocksBig) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.875F, 1.0F);
            ci.cancel();
        }
    }
}
