package rosegoldaddons.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.ChatUtils;
import rosegoldaddons.utils.RenderUtils;
import rosegoldaddons.utils.RotationUtils;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class BloodTriggerBot {
    private static String[] names = {"Bonzo", "Revoker", "Psycho", "Reaper", "Cannibal", "Mute", "Ooze", "Putrid", "Freak", "Leech", "Tear", "Parasite", "Flamer", "Skull", "Mr. Dead", "Vader", "Frost", "Walker", "WanderingSoul", "Shadow Assassin", "Lost Adventurer", "Livid", "Professor"};
    private static ArrayList<Entity> bloodMobs = null;
    private Thread thread;

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Main.bloodTriggerBot) return;
        bloodMobs = getAllBloodMobs();
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(() -> {
                try {
                    for (Entity entity : bloodMobs) {
                        if (isLookingAtAABB(entity.getEntityBoundingBox(), event)) {
                            click();
                            Thread.sleep(100);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Blood room thingy");
            thread.start();
        }
    }

    private static boolean isLookingAtAABB(AxisAlignedBB aabb, RenderWorldLastEvent event) {
        Vec3 position = new Vec3(Minecraft.getMinecraft().thePlayer.posX, (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight()), Minecraft.getMinecraft().thePlayer.posZ);
        Vec3 look = Minecraft.getMinecraft().thePlayer.getLook(event.partialTicks);
        look = scaleVec(look, 0.5F);
        for (int i = 0; i < 64; i++) {
            if (aabb.minX <= position.xCoord && aabb.maxX >= position.xCoord && aabb.minY <= position.yCoord && aabb.maxY >= position.yCoord && aabb.minZ <= position.zCoord && aabb.maxZ >= position.zCoord) {
                return true;
            }
            position = position.add(look);
        }

        return false;
    }

    private static ArrayList<Entity> getAllBloodMobs() {
        ArrayList<Entity> bloodMobs = new ArrayList<>();
        for (Entity entity1 : (Minecraft.getMinecraft().theWorld.loadedEntityList)) {
            if (entity1 instanceof EntityOtherPlayerMP && !entity1.isDead) {
                for (String name : names) {
                    if (entity1.getName().contains(name)) {
                        bloodMobs.add(entity1);
                    }
                }
            }
            if (entity1 instanceof EntityWither) {
                bloodMobs.add(entity1);
            }
        }
        return bloodMobs;
    }

    private static Vec3 scaleVec(Vec3 vec, float f) {
        return new Vec3(vec.xCoord * (double)f, vec.yCoord * (double)f, vec.zCoord * (double)f);
    }

    public static void click() {
        try {
            Method clickMouse;
            try {
                clickMouse = Minecraft.class.getDeclaredMethod("func_147116_af");
            } catch (NoSuchMethodException e) {
                clickMouse = Minecraft.class.getDeclaredMethod("clickMouse");
            }
            clickMouse.setAccessible(true);
            clickMouse.invoke(Minecraft.getMinecraft());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
