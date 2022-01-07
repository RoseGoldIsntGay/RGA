package rosegoldaddons.features;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SexAura {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void chat(ClientChatReceivedEvent event) {
        if(event.type == 0) {
            String message = event.message.getUnformattedText();
            String formatted = event.message.getFormattedText();
            if (message.startsWith("From") && message.contains("!SXAURA!")) {
                event.setCanceled(true);
                String sender = formatted.substring(formatted.indexOf("From")+5, formatted.indexOf(":"));
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(sender+"§d§l has sexed you!"));
                playAlert();
            }
            if(message.startsWith("§9Party") && message.contains("!SXAURA!")) {
                event.setCanceled(true);
                String sender = formatted.substring(formatted.indexOf("Party")+10, formatted.indexOf(":"));
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(sender+"§d§l has sexed you!"));
                playAlert();
            }
        }
    }

    private static void playAlert() {
        Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1, 0.5F);
    }
}
