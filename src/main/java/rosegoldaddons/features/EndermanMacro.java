package rosegoldaddons.features;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.RenderUtils;
import rosegoldaddons.utils.RotationUtils;

import java.awt.*;
import java.util.Random;

public class EndermanMacro {
    private static Entity enderman;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Main.configFile.EndermanESP && !Main.endermanMacro) return;
        enderman = getClosestEnderman();
        if(enderman != null && Main.endermanMacro) {
            RotationUtils.faceEntity(enderman);
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Main.configFile.EndermanESP) return;
        if (enderman == null) return;
        RenderUtils.drawEntityBox(enderman, Color.RED, true, event.partialTicks);
    }

    private static Entity getClosestEnderman() {
        Entity eman = null;
        double closest = 9999;
        for (Entity entity1 : (Minecraft.getMinecraft().theWorld.loadedEntityList)) {
            if (entity1 instanceof EntityEnderman && !(((EntityEnderman) entity1).getHealth() == 0)) {
                double dist = entity1.getDistanceSq(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ);
                if (dist < closest) {
                    if(Main.configFile.macroRadius != 0 && dist < Main.configFile.macroRadius) {
                        closest = dist;
                        eman = entity1;
                    } if(Main.configFile.macroRadius == 0) {
                        closest = dist;
                        eman = entity1;
                    }
                }
            }
        }
        return eman;
    }
}
