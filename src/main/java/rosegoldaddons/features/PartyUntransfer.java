package rosegoldaddons.features;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.Main;

public class PartyUntransfer {
    @SubscribeEvent
    public void chat(ClientChatReceivedEvent event) {
        if (!Main.configFile.AutoUntransfer) return;
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText()).toLowerCase();
        if (message.contains("the party was transferred to")) {
            String playerName = Minecraft.getMinecraft().thePlayer.getName();
            String leader = stripRank(message.substring(message.indexOf("by")+3));
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/p transfer "+leader);
        }
        if(message.contains("has promoted")) {
            String playerName = Minecraft.getMinecraft().thePlayer.getName();
            String leader = stripRank(message.substring(0 ,message.indexOf("has promoted")-1));
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/p transfer "+leader);
        }
    }

    private String stripRank(String name) {
        if(!name.contains("]")) {
            System.out.println("non detected");
            return name;
        }
        return name.substring(name.indexOf("]")+2);
    }
}
