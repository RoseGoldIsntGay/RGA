package rosegoldaddons.mixins;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rosegoldaddons.events.PlayerMoveEvent;
import rosegoldaddons.events.ScreenClosedEvent;
import rosegoldaddons.features.SexAura;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {
    @Inject(method = "closeScreen", at = @At("HEAD"), cancellable = true)
    public void closeScreen(CallbackInfo ci) {
        if(SexAura.blocked) ci.cancel();
        MinecraftForge.EVENT_BUS.post(new ScreenClosedEvent());
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void onUpdateWalking(CallbackInfo cir) {
        if (MinecraftForge.EVENT_BUS.post(new PlayerMoveEvent.Pre())) cir.cancel();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
    public void onWalking(CallbackInfo cir) {
        if (MinecraftForge.EVENT_BUS.post(new PlayerMoveEvent.Post())) cir.cancel();
    }
}
