package rosegoldaddons.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rosegoldaddons.Main;
import rosegoldaddons.utils.ChatUtils;

import java.util.Map;

@Mixin(value = FontRenderer.class, priority = 999)
public abstract class MixinRenderString {
    @Shadow
    protected abstract void renderStringAtPos(String text, boolean shadow);

    @Inject(method = "renderStringAtPos", at = @At("HEAD"), cancellable = true)
    private void renderString(String text, boolean shadow, CallbackInfo ci) {
        if (Main.init) {
            for (Map.Entry<String, String> entry : Main.resp.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (text.contains(key)) {
                    ci.cancel();
                    renderStringAtPos(text.replaceAll(key, value).replace("&", "§"), shadow);
                }
            }
        }
    }
}
