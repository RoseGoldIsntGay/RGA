package rosegoldaddons.features;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.RenderUtils;
import rosegoldaddons.utils.RotationUtils;

import java.awt.*;
import java.util.Random;

public class GhostMacro {
    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (Main.GhostMacro) {
            Entity entity1 = getClosestCreeper();
            if (entity1 == null) return;
            RenderUtils.drawEntityBox(entity1, Color.RED, true, event.partialTicks);
            RotationUtils.faceEntity(entity1);
            Random r = new Random();
            if(r.nextInt(800) == 1) {
                RotationUtils.antiAfk();
            }
        }
    }

    private static Entity getClosestCreeper() {
        Entity eman = null;
        Double closest = Double.valueOf(9999);
        for (Entity entity1 : (Minecraft.getMinecraft().theWorld.loadedEntityList)) {
            if (entity1 instanceof EntityCreeper && !(((EntityCreeper) entity1).getHealth() == 0)) {
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
