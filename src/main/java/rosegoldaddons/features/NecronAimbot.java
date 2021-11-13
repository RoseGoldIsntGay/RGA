package rosegoldaddons.features;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.RenderUtils;
import rosegoldaddons.utils.RotationUtils;

import java.awt.*;
import java.util.Random;

public class NecronAimbot {
    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Main.necronAimbot) return;
        Entity necron = getClosestWither();
        if (necron == null) return;
        RenderUtils.drawEntityBox(necron, Color.RED, true, event.partialTicks);
        RotationUtils.faceEntity(necron);
    }

    private static Entity getClosestWither() {
        Entity necron = null;
        Double closest = Double.valueOf(9999);
        for (Entity entity1 : (Minecraft.getMinecraft().theWorld.loadedEntityList)) {
            if (entity1 instanceof EntityWither && !(((EntityWither) entity1).getHealth() == 0)) {
                double dist = entity1.getDistanceSq(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ);
                if (dist < closest) {
                    closest = dist;
                    necron = entity1;
                }
            }
        }
        return necron;
    }
}
