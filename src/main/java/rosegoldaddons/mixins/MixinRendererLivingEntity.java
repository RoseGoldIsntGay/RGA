package rosegoldaddons.mixins;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.codec.digest.DigestUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rosegoldaddons.Main;
import rosegoldaddons.events.RenderLivingEntityEvent;

@Mixin(value = RendererLivingEntity.class, priority = 1001)
@SideOnly(Side.CLIENT)
public abstract class MixinRendererLivingEntity {
    @Shadow
    protected ModelBase mainModel;

    @Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
    private <T extends EntityLivingBase> void renderModel(T entity, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new RenderLivingEntityEvent(entity, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor, mainModel)))
            ci.cancel();
    }

    /*
    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/IChatComponent;getFormattedText()Ljava/lang/String;"))
    public String redirectGetFormattedText(IChatComponent instance) {
        if(!Main.configFile.customNames) return instance.getFormattedText();
        if (Main.init && Main.configFile.alchsleep == 63 && Main.configFile.skiblock == 263) return instance.getFormattedText();
        String unformatted = stripString(instance.getUnformattedText()).replace(":"," ").replace("'"," ");
        String[] words = unformatted.split(" ");
        String[] formatteds = instance.getUnformattedText().replace(":"," ").replace("'"," ").split(" ");
        for(String word : words) {
            if (Main.hashedCache.contains(word)) continue;
            if(word.equals("")) continue;
            if (Main.nameCache.containsKey(word)) {
                String[] replaces = Main.nameCache.get(word).split(" ");
                for(String replace : replaces) {
                    for(String formatted : formatteds) {
                        if(replace.equals(formatted)) return instance.getFormattedText();
                    }
                }
                String color = getColorBeforeIndex(instance.getUnformattedText(), instance.getUnformattedText().indexOf(word));
                return instance.getUnformattedText().replace(word, Main.nameCache.get(word) + color);
            } else {
                String hashed = DigestUtils.sha256Hex(word + word);
                if (Main.names.containsKey(hashed)) {
                    Main.nameCache.put(word, Main.names.get(hashed));
                } else {
                    Main.hashedCache.add(word);
                }
            }
        }
        return instance.getFormattedText();
    }
    */

    private String getColorBeforeIndex(String str, int index) {
        String lastColor = "";
        for (int i = 0; i < str.length(); i++) {
            if (i == index) break;
            if (str.charAt(i) == '§' && i + 1 < str.length() && str.charAt(i + 1) != 'r' && str.charAt(i + 1) != 'l' && str.charAt(i + 1) != 'k'
                    && str.charAt(i + 1) != 'm' && str.charAt(i + 1) != 'n' && str.charAt(i + 1) != 'o') {
                lastColor = str.charAt(i) + "" + str.charAt(i + 1);
            }
        }
        return lastColor;
    }

    private String stripString(String s) {
        char[] nonValidatedString = StringUtils.stripControlCodes(s).toCharArray();
        StringBuilder validated = new StringBuilder();

        for (char a : nonValidatedString) {
            if ((int) a < 127 && (int) a > 20) {
                validated.append(a);
            }
        }

        return validated.toString();
    }
}
