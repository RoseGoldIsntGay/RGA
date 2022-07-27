package rosegoldaddons.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.*;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class EndermanMacro {
    private final KeyBinding sneak = Minecraft.getMinecraft().gameSettings.keyBindSneak;
    private final KeyBinding walkForward = Minecraft.getMinecraft().gameSettings.keyBindForward;
    private final KeyBinding jump = Minecraft.getMinecraft().gameSettings.keyBindJump;
    private final KeyBinding sprint = Minecraft.getMinecraft().gameSettings.keyBindSprint;
    public static long ms = -1;
    private static Entity enderman;
    public static int ticks = 0;
    public static int pausedTicks = 0;
    private boolean sneaking = false;
    private boolean moving = false;
    private boolean pauseMacro = false;
    private boolean sold = false;
    private boolean stored = false;
    private boolean sentl = false;
    private int totalShifts = 0;
    private int mainDeb = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (pauseMacro) return;
        ticks++;
        if (!Main.configFile.EndermanESP && !Main.endermanMacro) return;
        if (ticks % Math.pow(2, Main.configFile.endermanSpeed) != 0) return;
        if (Main.mc.currentScreen != null && !(Main.mc.currentScreen instanceof GuiIngameMenu) && !(Main.mc.currentScreen instanceof GuiChat)) return;
        if(mainDeb != 0) return;
        sold = false;
        stored = false;
        enderman = getClosestEnderman();
        if (Main.endermanMacro && !ShadyRotation.running) {
            if (enderman != null) {
                ShadyRotation.smoothLook(
                        ShadyRotation.getRotationToEntity(enderman),
                        Main.configFile.smoothLookVelocity,
                        () -> {
                            if (!sneaking && totalShifts % 2 == 0) {
                                if(Main.mc.thePlayer.inventory.getStackInSlot(Main.mc.thePlayer.inventory.currentItem) != null) {
                                    if(Main.configFile.endermanRC || (Main.configFile.zealotRC && enderman instanceof EntityEnderman && ((EntityEnderman) enderman).getHeldBlockState().getBlock() == Blocks.end_portal_frame))
                                    Main.mc.playerController.sendUseItem(Main.mc.thePlayer, Main.mc.theWorld, Main.mc.thePlayer.inventory.getStackInSlot(Main.mc.thePlayer.inventory.currentItem));
                                }
                                if(!Main.configFile.endermanRC) KeyBinding.setKeyBindState(sneak.getKeyCode(), true);
                                KeyBinding.setKeyBindState(jump.getKeyCode(), true);
                                totalShifts++;
                                sneaking = true;
                            }
                        });
            } else {
                KeyBinding.setKeyBindState(jump.getKeyCode(), true);
                if(Main.configFile.zealotOnly) {
                    if(ScoreboardUtils.inLimbo) {
                        mainDeb = 20;
                        Main.mc.thePlayer.sendChatMessage("/l");
                        return;
                    }
                    if(!ScoreboardUtils.inSkyblock) {
                        mainDeb = 20;
                        Main.mc.thePlayer.sendChatMessage("/play sb");
                        return;
                    }
                    if (!ScoreboardUtils.inDragonNest) {
                        mainDeb = 20;
                        Main.mc.thePlayer.sendChatMessage("/warp drag");
                        return;
                    }
                }
                if(Main.configFile.endermanRandom && !ShadyRotation.runningAsync) {
                    ShadyRotation.smoothLook(
                            new ShadyRotation.Rotation(new Random().nextInt(30), Main.mc.thePlayer.rotationYaw + new Random().nextInt(80)-40),
                            10,
                            () -> {},
                            true
                    );
                }
            }
        }
    }

    @SubscribeEvent
    public void movementControl(TickEvent.ClientTickEvent event) {
        if (!Main.configFile.endermanMove) return;
        if (event.phase == TickEvent.Phase.END) return;
        if (Main.mc.currentScreen != null && !(Main.mc.currentScreen instanceof GuiIngameMenu) && !(Main.mc.currentScreen instanceof GuiChat)) return;
        if (Main.endermanMacro) {
            if (pausedTicks % 40 != 0) return;
            KeyBinding.setKeyBindState(walkForward.getKeyCode(), true);
            KeyBinding.setKeyBindState(sprint.getKeyCode(), true);
            moving = true;
            if (Main.configFile.endermanTimer != 0 && ticks > Main.configFile.endermanTimer * 1200) {
                pauseMacro = true;
                moving = false;
                KeyBinding.setKeyBindState(walkForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(jump.getKeyCode(), false);
                KeyBinding.setKeyBindState(sprint.getKeyCode(), false);
                KeyBinding.setKeyBindState(sneak.getKeyCode(), false);
                if (OpenSkyblockGui.selling || OpenSkyblockGui.storing) return;
                if(!Main.configFile.endermanIronman) {
                    if (!sold) {
                        OpenSkyblockGui.sellAll = true;
                        OpenSkyblockGui.selling = true;
                        Main.mc.thePlayer.sendChatMessage("/bz");
                        sold = true;
                        return;
                    }
                }
                if (Main.configFile.endermanLobby) {
                    if (ScoreboardUtils.inDragonNest) {
                        if(!sentl) {
                            Main.mc.thePlayer.sendChatMessage("/l");
                            sentl = true;
                            return;
                        } else {
                            ChatUtils.sendMessage("Successfully rejoined a new lobby, macro uptime: "+millisToHours(ms));
                            pauseMacro = false;
                            ticks = 0;
                            sentl = false;
                        }
                    } else {
                        if(ScoreboardUtils.inSkyblock && !ScoreboardUtils.inPrivateIsland) {
                            Main.mc.thePlayer.sendChatMessage("/is");
                        }
                        if(ScoreboardUtils.inPrivateIsland) {
                            if(Main.configFile.endermanIronman && !stored) {
                                OpenSkyblockGui.storeInChest = true;
                                stored = true;
                                return;
                            }
                            Main.mc.thePlayer.sendChatMessage("/warp drag");
                        }
                    }
                    if (!ScoreboardUtils.inSkyblock) {
                        Main.mc.thePlayer.sendChatMessage("/play sb");
                    }
                } else {
                    if (ScoreboardUtils.inDragonNest) {
                        Main.mc.thePlayer.sendChatMessage("/is");
                    }
                    if (ScoreboardUtils.inPrivateIsland) {
                        if(Main.configFile.endermanIronman && !stored) {
                            OpenSkyblockGui.storeInChest = true;
                            stored = true;
                            return;
                        }
                        Main.mc.thePlayer.sendChatMessage("/warp drag");
                        pauseMacro = false;
                        ticks = 0;
                    }
                }
            }
        } else {
            if (!moving) return;
            KeyBinding.setKeyBindState(walkForward.getKeyCode(), false);
            KeyBinding.setKeyBindState(jump.getKeyCode(), false);
            KeyBinding.setKeyBindState(sprint.getKeyCode(), false);
            moving = false;
        }
    }

    @SubscribeEvent
    public void debounce(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (pauseMacro) pausedTicks++;
        if(mainDeb > 0) mainDeb--;
    }

    @SubscribeEvent
    public void stuckControl(TickEvent.ClientTickEvent event) {
        if (!Main.endermanMacro || !Main.configFile.endermanMove) return;
        if(event.phase == TickEvent.Phase.END) return;
        if (Main.mc.objectMouseOver != null && Main.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos lookingAt = Main.mc.objectMouseOver.getBlockPos();
            BlockPos playerPos = Main.mc.thePlayer.getPosition();
            if(playerPos.distanceSq(lookingAt) > Main.configFile.endermanStuckDist) return;
            switch (Main.mc.thePlayer.getHorizontalFacing()) {
                case NORTH:
                    ShadyRotation.smoothLook(new ShadyRotation.Rotation(Main.mc.thePlayer.rotationPitch, 270), Main.configFile.smoothLookVelocity, () -> {});
                    break;
                case EAST:
                    ShadyRotation.smoothLook(new ShadyRotation.Rotation(Main.mc.thePlayer.rotationPitch, 0), Main.configFile.smoothLookVelocity, () -> {});
                    break;
                case SOUTH:
                    ShadyRotation.smoothLook(new ShadyRotation.Rotation(Main.mc.thePlayer.rotationPitch, 90), Main.configFile.smoothLookVelocity, () -> {});
                    break;
                case WEST:
                    ShadyRotation.smoothLook(new ShadyRotation.Rotation(Main.mc.thePlayer.rotationPitch, 180), Main.configFile.smoothLookVelocity, () -> {});
                    break;
            }
        }
    }

    @SubscribeEvent
    public void sneakControl(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (pauseMacro) return;
        if (Main.mc.currentScreen != null && !(Main.mc.currentScreen instanceof GuiIngameMenu) && !(Main.mc.currentScreen instanceof GuiChat)) return;
        if (ticks % Math.pow(2, Main.configFile.endermanSpeed) != 0) return;
        if(totalShifts % 2 == 1) {
            if(!Main.configFile.endermanRC) KeyBinding.setKeyBindState(sneak.getKeyCode(), true);
            totalShifts++;
            sneaking = true;
            return;
        }
        if (sneaking) {
            if(!Main.configFile.endermanRC) KeyBinding.setKeyBindState(sneak.getKeyCode(), false);
            sneaking = false;
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Main.configFile.EndermanESP) return;
        if (enderman != null) {
            RenderUtils.drawEntityBox(enderman, Color.RED, Main.configFile.lineWidth, event.partialTicks);
        }
    }

    private static String millisToHours(long millis) {
        long curr = System.currentTimeMillis();
        int seconds = (int) ((curr - millis)/1000);
        return String.format("%02d:%02d:%02d", seconds/3600, (seconds%3600)/60, seconds%60);
    }

    private static Entity getClosestEnderman() {
        Entity eman = null;
        double closest = 9999;
        if (Main.mc.theWorld == null) return null;
        for (Entity entity1 : (Main.mc.theWorld.loadedEntityList)) {
            if (entity1 instanceof EntityEnderman && !(((EntityEnderman) entity1).getHealth() == 0)) {
                if(((EntityEnderman) entity1).getHeldBlockState().getBlock() == Blocks.end_portal_frame) {
                    double dist = entity1.getDistance(Main.mc.thePlayer.posX, Main.mc.thePlayer.posY, Main.mc.thePlayer.posZ);
                    if(dist < 40) return entity1;
                }
                if(!Main.mc.thePlayer.canEntityBeSeen(entity1)) continue;
                if(Main.configFile.zealotOnly) {
                    Entity armorStand = null;
                    List<Entity> possibleEntities = entity1.getEntityWorld().getEntitiesInAABBexcluding(entity1, entity1.getEntityBoundingBox().expand(0, 4, 0), entity -> !(entity instanceof EntityEnderman));
                    for (Entity en : possibleEntities) {
                        if (en instanceof EntityArmorStand) {
                            armorStand = en;
                            break;
                        }
                    }
                    if (armorStand == null) return null;
                    if (!armorStand.getCustomNameTag().contains("Zealot")) continue;
                }
                double dist = entity1.getDistance(Main.mc.thePlayer.posX, Main.mc.thePlayer.posY, Main.mc.thePlayer.posZ);
                if (dist < closest) {
                    if (Main.configFile.macroRadius != 0 && dist < Main.configFile.macroRadius) {
                        closest = dist;
                        eman = entity1;
                    }
                    if (Main.configFile.macroRadius == 0) {
                        closest = dist;
                        eman = entity1;
                    }
                }
            }
        }
        return eman;
    }
}
