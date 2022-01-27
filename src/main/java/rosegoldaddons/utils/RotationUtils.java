package rosegoldaddons.utils;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.Main;

public class RotationUtils {
    static boolean working = false;
    static boolean snek = false;
    public static Rotation startRot;
    public static Rotation neededChange;
    public static Rotation endRot;
    public static long startTime;
    public static long endTime;

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        update();
    }

    //bing chilling thx apfel
    public static void update() {
        if (System.currentTimeMillis() <= endTime) {
            if(startRot != null && endRot != null) {
                Main.mc.thePlayer.rotationYaw = interpolate(startRot.getYaw(), endRot.getYaw());
                Main.mc.thePlayer.rotationPitch = interpolate(startRot.getPitch(), endRot.getPitch());
            }
        } else {
            if(startRot != null && endRot != null) {
                Main.mc.thePlayer.rotationYaw = endRot.getYaw();
                Main.mc.thePlayer.rotationPitch = endRot.getPitch();
                reset();
            }
        }
    }

    private static void reset() {
        startRot = null;
        neededChange = null;
        endRot = null;
    }

    private static float interpolate(float f, float t) {
        float x = System.currentTimeMillis() - startTime;
        float u = (f - t) / 2.0f;
        return (float) (u * Math.cos((float) (x * Math.PI / (endTime - startTime))) - u + f);
    }

    public static void setup(Rotation rot, Long aimTime) {
        startRot = new Rotation(Main.mc.thePlayer.rotationYaw, Main.mc.thePlayer.rotationPitch);
        neededChange = getNeededChange(startRot, rot);
        endRot = new Rotation(startRot.getYaw() + neededChange.getYaw(), startRot.getPitch() + neededChange.getPitch());
        startTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis() + aimTime;
    }

    public static Rotation getNeededChange(Rotation startRot, Rotation endRot) {
        float yawChng = (float) (wrapAngleTo180(endRot.getYaw()) - wrapAngleTo180(startRot.getYaw()));
        if (yawChng <= -180.0F) {
            yawChng += 360.0F;
        } else if (yawChng > 180.0F) {
            yawChng += -360.0F;
        }

        return new Rotation(yawChng, endRot.getPitch() - startRot.getPitch());
    }


    public static void shootEman() {
        if (Main.mc.currentScreen != null) {
            if (Main.mc.currentScreen instanceof GuiIngameMenu || Main.mc.currentScreen instanceof GuiChat) {
            } else {
                return;
            }
        }
        if (snek) return;
        new Thread(() -> {
            try {
                snek = true;
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                Thread.sleep(50);
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
                Thread.sleep(50);
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                Thread.sleep(50);
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
                snek = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void facePos(Vec3 vector) {
        if (Main.mc.currentScreen != null) {
            if (Main.mc.currentScreen instanceof GuiIngameMenu || Main.mc.currentScreen instanceof GuiChat) {
            } else {
                return;
            }
        }
        if (working) return;
        new Thread(() -> {
            try {
                working = true;
                double diffX = vector.xCoord - Main.mc.thePlayer.posX;
                double diffY = vector.yCoord - (Main.mc.thePlayer.posY + Main.mc.thePlayer.getEyeHeight());
                double diffZ = vector.zCoord - Main.mc.thePlayer.posZ;
                double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

                float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
                float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI - 90.0D);
                pitch = MathHelper.wrapAngleTo180_float(pitch - Main.mc.thePlayer.rotationPitch);
                yaw = MathHelper.wrapAngleTo180_float(yaw - Main.mc.thePlayer.rotationYaw);

                for (int i = 0; i < Main.configFile.smoothLookVelocity; i++) {
                    Main.mc.thePlayer.rotationYaw += yaw / Main.configFile.smoothLookVelocity;
                    Main.mc.thePlayer.rotationPitch += pitch / Main.configFile.smoothLookVelocity;
                    Thread.sleep(1);
                }
                //setup(new Rotation(yaw, pitch), (long) Main.configFile.smoothLookVelocity);
                working = false;
                if (Main.endermanMacro) {
                    shootEman();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void faceAngles(double yaw, double pitch) {
        if (working) return;
        new Thread(() -> {
            try {
                working = true;
                for (int i = 0; i < Main.configFile.smoothLookVelocity; i++) {
                    Main.mc.thePlayer.rotationYaw += yaw / Main.configFile.smoothLookVelocity;
                    Main.mc.thePlayer.rotationPitch += pitch / Main.configFile.smoothLookVelocity;
                    Thread.sleep(1);
                }
                working = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void faceEntity(Entity en) {
        if (en instanceof EntityCreeper) {
            facePos(new Vec3(en.posX, en.posY, en.posZ));
        } else {
            facePos(new Vec3(en.posX, en.posY + Main.mc.thePlayer.getEyeHeight(), en.posZ));
        }
    }

    public static void packetFaceEntity(Entity en) {
        if (en == null) return;
        Vec3 vector = new Vec3(en.posX, en.posY - 1.5, en.posZ);
        if (Main.mc.currentScreen != null) {
            if (Main.mc.currentScreen instanceof GuiIngameMenu || Main.mc.currentScreen instanceof GuiChat) {
            } else {
                return;
            }
        }

        double diffX = vector.xCoord - (Main.mc).thePlayer.posX;
        double diffY = vector.yCoord - (Main.mc).thePlayer.posY;
        double diffZ = vector.zCoord - (Main.mc).thePlayer.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90) * -1 - Main.mc.thePlayer.rotationPitch);
        yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90 - Main.mc.thePlayer.rotationYaw);

        Main.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, Main.mc.thePlayer.onGround));

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
