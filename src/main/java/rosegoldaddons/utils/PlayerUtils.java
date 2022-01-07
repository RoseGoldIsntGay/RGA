package rosegoldaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.Main;

public class PlayerUtils {
    public static boolean pickaxeAbilityReady = false;

    public static void swingItem() {
        MovingObjectPosition movingObjectPosition = Minecraft.getMinecraft().objectMouseOver;
        if (movingObjectPosition != null && movingObjectPosition.entityHit == null) {
            Minecraft.getMinecraft().thePlayer.swingItem();
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        pickaxeAbilityReady = false;
    }

    @SubscribeEvent
    public void chat(ClientChatReceivedEvent event) {
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (message.contains(":")) return;
        if(message.contains("You used your")) {
            pickaxeAbilityReady = false;
        } else if(message.contains("is now available!")) {
            pickaxeAbilityReady = true;
        }
    }
}
