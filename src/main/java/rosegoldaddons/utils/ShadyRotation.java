package rosegoldaddons.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.events.PlayerMoveEvent;

public class ShadyRotation {
    private static float pitchDifference;
    public static float yawDifference;
    private static int ticks = -1;
    private static int tickCounter = 0;
    private static Runnable callback = null;
    private static boolean async = false;

    private static float serverPitch;
    private static float serverYaw;

    public static boolean running = false;
    public static boolean runningAsync = false;

    public static class Rotation {
        public float pitch;
        public float yaw;
        public boolean async;

        public Rotation(float pitch, float yaw) {
            this.pitch = pitch;
            this.yaw = yaw;
            this.async = false;
        }

        public Rotation(float pitch, float yaw, boolean async) {
            this.pitch = pitch;
            this.yaw = yaw;
            this.async = async;
        }

        public void setAsync(boolean async) {
            this.async = async;
        }
    }

    private static double wrapAngleTo180(double angle) {
        return angle - Math.floor(angle / 360 + 0.5) * 360;
    }

    private static float wrapAngleTo180(float angle) {
        return (float) (angle - Math.floor(angle / 360 + 0.5) * 360);
    }

    public static Rotation getRotationToBlock(BlockPos block) {
        double diffX = block.getX() - Main.mc.thePlayer.posX + 0.5;
        double diffY = block.getY() - Main.mc.thePlayer.posY + 0.5 - Main.mc.thePlayer.getEyeHeight();
        double diffZ = block.getZ() - Main.mc.thePlayer.posZ + 0.5;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90)*-1);
        yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

        return new Rotation(pitch, yaw);
    }

    public static Rotation getRotationToEntity(Entity entity) {
        double diffX = entity.posX - Main.mc.thePlayer.posX;
        double diffY = entity.posY + entity.getEyeHeight() - Main.mc.thePlayer.posY - Main.mc.thePlayer.getEyeHeight();
        double diffZ = entity.posZ - Main.mc.thePlayer.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90)*-1);
        yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

        return new Rotation(pitch, yaw);
    }

    public static Rotation vec3ToRotation(Vec3 vec) {
        double diffX = vec.xCoord - Main.mc.thePlayer.posX;
        double diffY = vec.yCoord - Main.mc.thePlayer.posY - Main.mc.thePlayer.getEyeHeight();
        double diffZ = vec.zCoord - Main.mc.thePlayer.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90)*-1);
        yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

        return new Rotation(pitch, yaw);
    }

    public static void smoothLook(Rotation rotation, int ticks, Runnable callback, boolean async) {
        if(ticks == 0) {
            look(rotation);
            callback.run();
            return;
        }

        ShadyRotation.callback = callback;

        ShadyRotation.async = rotation.async;
        pitchDifference = wrapAngleTo180(rotation.pitch - Main.mc.thePlayer.rotationPitch);
        yawDifference = wrapAngleTo180(rotation.yaw - Main.mc.thePlayer.rotationYaw);

        ShadyRotation.ticks = ticks * 20;
        ShadyRotation.tickCounter = 0;
    }

    public static void smoothLook(Rotation rotation, int ticks, Runnable callback) {
        smoothLook(rotation, ticks, callback, false);
    }

    public static void smartLook(Rotation rotation, int ticksPer180, Runnable callback) {
        float rotationDifference = Math.max(
                Math.abs(rotation.pitch - Main.mc.thePlayer.rotationPitch),
                Math.abs(rotation.yaw - Main.mc.thePlayer.rotationYaw)
        );
        smoothLook(rotation, (int) (rotationDifference / 180 * ticksPer180), callback);
    }

    public static void look(Rotation rotation) {
        Main.mc.thePlayer.rotationPitch = rotation.pitch;
        Main.mc.thePlayer.rotationYaw = rotation.yaw;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onUpdatePre(PlayerMoveEvent.Pre pre) {
        //if(Main.oringo) return;
        serverPitch = Main.mc.thePlayer.rotationPitch;
        serverYaw = Main.mc.thePlayer.rotationYaw;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdatePost(PlayerMoveEvent.Post post) {
        //if(Main.oringo) return;
        Main.mc.thePlayer.rotationPitch = serverPitch;
        Main.mc.thePlayer.rotationYaw = serverYaw;
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(Main.mc.thePlayer == null) return;
        if(tickCounter < ticks) {
            if(!async) {
                running = true;
                runningAsync = false;
            } else {
                runningAsync = true;
                running = false;
            }
            Main.mc.thePlayer.rotationPitch += pitchDifference / ticks;
            Main.mc.thePlayer.rotationYaw += yawDifference / ticks;
            tickCounter++;
        } else if(callback != null) {
            if(!async) {
                running = false;
            } else {
                runningAsync = false;
            }
            callback.run();
            callback = null;
        }
    }
}
