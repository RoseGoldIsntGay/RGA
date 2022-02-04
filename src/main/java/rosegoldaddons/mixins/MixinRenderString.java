package rosegoldaddons.mixins;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import rosegoldaddons.Main;

@Mixin(FontRenderer.class)
public abstract class MixinRenderString {

    @ModifyVariable(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At(value = "FIELD"))
    private String replaceName(String text) {
        if (Main.mc.theWorld == null || Main.mc.thePlayer == null) return text;
        if (Main.configFile.wydsi && text.contains("727")) {
            text = text.replace("727", "726");
        }
        if (Main.init && Main.configFile.alchsleep != 88 || Main.configFile.skiblock != 263) {
            String[] words = stripString(text).replace(":"," ").replace("'"," ").split(" ");
            String[] formatteds = text.replace(":"," ").replace("'"," ").split(" ");
            for (String word : words) {
                if (Main.hashedCache.contains(word)) continue;
                if(Main.rankCache.containsKey(word)) {
                    String rank = getRank(text, word);
                    if(rank != null) {
                        text = text.replace(rank, Main.rankCache.get(word));
                    }
                } else {
                    String hashed = DigestUtils.sha256Hex(word + word);
                    if (Main.ranks.containsKey(hashed)) {
                        String rank = getRank(text, word);
                        if(rank != null) {
                            Main.rankCache.put(word, Main.ranks.get(hashed));
                            System.out.println(word+":"+Main.ranks.get(hashed));
                        }
                    }
                }
            }
            for (String word : words) {
                if (Main.hashedCache.contains(word)) continue;
                if (Main.nameCache.containsKey(word)) {
                    String[] replaces = Main.nameCache.get(word).split(" ");
                    for (String replace : replaces) {
                        for (String formatted : formatteds) {
                            if (replace.equals(formatted)) return text;
                        }
                    }
                    String color = getColorBeforeIndex(text, text.indexOf(word));
                    text = text.replace(word, Main.nameCache.get(word) + color);
                } else {
                    String hashed = DigestUtils.sha256Hex(word + word);
                    if (Main.names.containsKey(hashed)) {
                        Main.nameCache.put(word, Main.names.get(hashed));
                    } else {
                        Main.hashedCache.add(word);
                    }
                }
            }
        }
        return text;
    }

    private String getRank(String str, String sub) {
        if(!str.contains("[") || !str.contains("]")) return null;
        if(sub.contains("[") || sub.contains("]")) return null;
        if(str.indexOf(sub)-sub.length() >= str.indexOf("]")) return null;
        if(str.indexOf(sub) < str.indexOf("[")) return null;

        return str.substring(str.indexOf("["), str.indexOf("]")+1);
    }

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
