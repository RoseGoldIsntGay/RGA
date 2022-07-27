package rosegoldaddons.mixins.blocksize;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rosegoldaddons.Main;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Shadow
    protected double minX;

    @Shadow
    protected double minY;

    @Shadow
    protected double minZ;

    @Shadow
    protected double maxX;

    @Shadow
    protected double maxY;

    @Shadow
    protected double maxZ;

    @Shadow public abstract boolean isFullBlock();

    @Inject(method = "setBlockBounds", at = @At("HEAD"), cancellable = true)
    private void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, CallbackInfo ci) {
        if(Minecraft.getMinecraft().theWorld != null) {
            /*if (Main.configFile.allBlocksBig && isFullBlock()) {
                this.minX = 0.0D;
                this.minY = 0.0D;
                this.minZ = 0.0D;
                this.maxX = 1.0D;
                this.maxY = 1.0D;
                this.maxZ = 1.0D;
                ci.cancel();
            }*/
        }
    }
}
