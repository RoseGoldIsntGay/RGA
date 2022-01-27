package rosegoldaddons.features;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.Main;
import rosegoldaddons.events.RenderLivingEntityEvent;

import java.util.Map;

public class SexAura {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void chat(ClientChatReceivedEvent event) {
        if(event.type == 0) {
            String message = event.message.getUnformattedText();
            String formatted = event.message.getFormattedText();
            if (message.startsWith("From") && message.contains("!SXAURA!")) {
                event.setCanceled(true);
                String sender = formatted.substring(formatted.indexOf("From")+5, formatted.indexOf(":"));
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(sender+"§d§l has sexed you!"));
                playAlert();
            }
            if(message.startsWith("§9Party") && message.contains("!SXAURA!")) {
                event.setCanceled(true);
                String sender = formatted.substring(formatted.indexOf("Party")+10, formatted.indexOf(":"));
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(sender+"§d§l has sexed you!"));
                playAlert();
            }
        }
    }

    @SubscribeEvent
    public void onEntityRender(RenderLivingEntityEvent event) {
        if (Main.init) {
            String text = event.entity.getDisplayName().getFormattedText();
            for (Map.Entry<String, String> entry : Main.resp.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (text.contains(key) && !text.contains(value)) {
                    event.entity.setCustomNameTag(text.replace(key, value));
                    break;
                }
            }
        }
    }

    private static void playAlert() {
        Main.mc.thePlayer.playSound("random.orb", 1, 0.5F);
    }
}
