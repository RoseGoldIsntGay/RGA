package rosegoldaddons.mixins.blocksize;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rosegoldaddons.Main;

@Mixin(BlockButton.class)
public abstract class MixinButton extends Block {

    public MixinButton(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    @Inject(method = "updateBlockBounds", at = @At("HEAD"), cancellable = true)
    private void changeBlockBounds(IBlockState state, CallbackInfo ci) {
        if(Main.configFile.dungeonBlocksBig) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            ci.cancel();
        }
    }
}
