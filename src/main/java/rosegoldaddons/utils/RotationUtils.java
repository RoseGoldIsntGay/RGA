package rosegoldaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import rosegoldaddons.Main;

import java.security.Key;

public class RotationUtils {
    static boolean working = false;
    static boolean snek = false;
    static boolean antiafking = false;

    public static void antiAfk() {
        if(Minecraft.getMinecraft().currentScreen != null) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu || Minecraft.getMinecraft().currentScreen instanceof GuiChat) {}
            else {
                return;
            }
        }
        if(snek) return;
        KeyBinding right = Minecraft.getMinecraft().gameSettings.keyBindRight;
        KeyBinding left = Minecraft.getMinecraft().gameSettings.keyBindLeft;
        new Thread(() -> {
            try {
                KeyBinding.setKeyBindState(right.getKeyCode(), true);
                Thread.sleep(50);
                KeyBinding.setKeyBindState(right.getKeyCode(), false);
                Thread.sleep(200);
                KeyBinding.setKeyBindState(left.getKeyCode(), true);
                Thread.sleep(50);
                KeyBinding.setKeyBindState(left.getKeyCode(), false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void shootEman() {
        if(Minecraft.getMinecraft().currentScreen != null) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu || Minecraft.getMinecraft().currentScreen instanceof GuiChat) {}
            else {
                return;
            }
        }
        if(snek) return;
        new Thread(() -> {
            try {
                snek = true;
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
                Thread.sleep(50);
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
                Thread.sleep(50);
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
                Thread.sleep(50);
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
                snek = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void facePos(Vec3 vector) {
        if(Minecraft.getMinecraft().currentScreen != null) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu || Minecraft.getMinecraft().currentScreen instanceof GuiChat) {}
            else {
                return;
            }
        }
        if(working) return;
        new Thread(() -> {
            try {
                working = true;
                double diffX = vector.xCoord - (Minecraft.getMinecraft()).thePlayer.posX;
                double diffY = vector.yCoord - (Minecraft.getMinecraft()).thePlayer.posY;
                double diffZ = vector.zCoord - (Minecraft.getMinecraft()).thePlayer.posZ;
                double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

                float pitch = (float) -Math.atan2(dist, diffY);
                float yaw = (float) Math.atan2(diffZ, diffX);
                pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90)*-1 - Minecraft.getMinecraft().thePlayer.rotationPitch);
                yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90 - Minecraft.getMinecraft().thePlayer.rotationYaw);

                for(int i = 0; i < Main.configFile.smoothLookVelocity; i++) {
                    Minecraft.getMinecraft().thePlayer.rotationYaw += yaw/Main.configFile.smoothLookVelocity;
                    Minecraft.getMinecraft().thePlayer.rotationPitch += pitch/Main.configFile.smoothLookVelocity;
                    Thread.sleep(1);
                }
                working = false;
                if(Main.endermanMacro) {
                    shootEman();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void faceEntity(Entity en) {
        if(en instanceof EntityCreeper) {
            facePos(new Vec3(en.posX, en.posY-1.5, en.posZ));
        } else {
            facePos(new Vec3(en.posX, en.posY, en.posZ));
        }
    }

    public static void faceEntity2(Entity en) {
        facePos(new Vec3(en.posX, en.posY + en.getEyeHeight() - en.height / 1.5D, en.posZ));
    }

    private static double wrapAngleTo180(double angle) {
        angle %= 360;
        while (angle >= 180) {
            angle -= 360;
        }
        while (angle < -180) {
            angle += 360;
        }
        return angle;
    }

}
