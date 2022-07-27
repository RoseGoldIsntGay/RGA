package rosegoldaddons.mixins;

import net.minecraft.client.gui.FontRenderer;
import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.util.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import rosegoldaddons.Main;
import rosegoldaddons.utils.ChatUtils;

import java.util.Arrays;
import java.util.Locale;

@Mixin(value = FontRenderer.class)
public abstract class MixinRenderString {
    private static final char[] normal = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
    private static final char[] custom = "ｑｗｅｒｔｙｕｉｏｐａｓｄｆｇｈｊｋｌｚｘｃｖｂｎｍｑｗｅｒｔｙｕｉｏｐａｓｄｆｇｈｊｋｌｚｘｃｖｂｎｍ".toCharArray();

    @ModifyVariable(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At(value = "FIELD"))
    private String replaceName(String text) {
        if (Main.mc.theWorld == null || Main.mc.thePlayer == null) return text;
        if(Main.configFile.antiRacism == 0 || Main.configFile.antiRacism == 1) text = removeRacism(text);
        if (Main.configFile.wydsi && text.contains("727")) {
            text = text.replace("727", "726");
        }
        if (Main.pauseCustom) return text;
        if (Main.init && Main.configFile.alchsleep != 88 || Main.configFile.skiblock != 263) {
            String[] words = removeFormatting(text).replace(":", " ").replace("'", " ").split(" ");
            String[] formatteds = text.replace(":", " ").replace("'", " ").split(" ");
            for (String word : words) {
                if (word.equals("") || Main.hashedCache.contains(word)) continue;
                if (Main.rankCache.containsKey(word)) {
                    String rank = getRank(text, word);
                    if (rank != null) {
                        text = text.replace(rank, Main.rankCache.get(word));
                    }
                } else {
                    String hashed = DigestUtils.sha256Hex(word + word);
                    if (Main.ranks.containsKey(hashed) && !Main.rankCache.containsKey(word)) {
                        String rank = getRank(text, word);
                        if (rank != null) {
                            Main.rankCache.put(word, Main.ranks.get(hashed));
                        }
                    }
                    if (!Main.names.containsKey(hashed) && !Main.ranks.containsKey(hashed) && !Main.hashedCache.contains(word)) {
                        Main.hashedCache.add(word);
                    }
                }
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
                    if (Main.names.containsKey(hashed) && !Main.nameCache.containsKey(word)) {
                        Main.nameCache.put(word, Main.names.get(hashed));
                    }
                    if (!Main.names.containsKey(hashed) && !Main.ranks.containsKey(hashed) && !Main.hashedCache.contains(word)) {
                        Main.hashedCache.add(word);
                    }
                }
            }
        }
        return text;
    }

    private String removeRacism(String str) {
        str = str.replace("ˌ","");
        for(int i = 0; i < custom.length; i++) {
            if(str.contains(String.valueOf(custom[i]))) {
                str = str.replace(custom[i], normal[i]);
            }
        }
        for (String word : Main.blacklist) {
            if (str.toLowerCase().contains(word.toLowerCase())) {
                char[] censored = new char[word.length()];
                Arrays.fill(censored, '*');
                return str.replaceAll("(?i)"+word, new String(censored));
            }
        }
        return str;
    }

    private String getRank(String str, String sub) {
        if (!str.contains("[") || !str.contains("]")) return null;
        if (sub.contains("[") || sub.contains("]")) return null;
        if (str.indexOf(sub) - sub.length() >= str.indexOf("]")) return null;
        if (str.indexOf(sub) < str.indexOf("[")) return null;

        return str.substring(str.indexOf("["), str.indexOf("]") + 1);
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

    private String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }
}
