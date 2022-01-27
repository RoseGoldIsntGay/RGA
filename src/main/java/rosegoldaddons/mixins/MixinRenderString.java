package rosegoldaddons.mixins;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import rosegoldaddons.Main;

import java.util.Map;

@Mixin(FontRenderer.class)
public abstract class MixinRenderString {

    @ModifyVariable(method = "renderString", at = @At(value = "FIELD"))
    private String replaceName(String text) {
        if(Main.configFile.wydsi && text.contains("727")) {
            text = text.replace("727", "726");
        }
        if (Main.init) {
            for (Map.Entry<String, String> entry : Main.resp.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (text.contains(key) && !text.contains(value)) {
                    text = text.replace(key, value)+"§r";
                    break;
                }
            }
        }
        return text;
    }
}
