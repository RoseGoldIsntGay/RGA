package rosegoldaddons.features;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.commands.WartSetup;
import rosegoldaddons.utils.ChatUtils;
import rosegoldaddons.utils.RenderUtils;

import java.awt.*;
import java.util.ArrayList;

public class WartMacro {
    private boolean breaking = false;
    private boolean walking = false;
    private double savex = -999;
    private double savez = -999;
    private Thread thread;
    private Thread thread2;

    @SubscribeEvent
    public void move(TickEvent.ClientTickEvent event) {
        if (!Main.wartToggle || Minecraft.getMinecraft().currentScreen != null) {
            if (walking) {
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
                walking = false;
            }
            return;
        }
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(() -> {
                try {
                    //if (event.phase.toString().equals("START")) {
                        if (!walking) {
                            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
                            walking = true;
                        }
                        float prevYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
                        float nextYaw = -1;
                        if (WartSetup.cardinal.equals("east")) {
                            if (Minecraft.getMinecraft().thePlayer.getPosition().getZ() == WartSetup.wartEnd) {
                                nextYaw = 230;
                            } else if (Minecraft.getMinecraft().thePlayer.getPosition().getZ() == -WartSetup.wartEnd) {
                                nextYaw = 310;
                            }
                        } else if (WartSetup.cardinal.equals("north")) {
                            if (Minecraft.getMinecraft().thePlayer.getPosition().getX() == WartSetup.wartEnd) {
                                nextYaw = 140;
                            } else if (Minecraft.getMinecraft().thePlayer.getPosition().getX() == -WartSetup.wartEnd) {
                                nextYaw = 220;
                            }
                        } else if (WartSetup.cardinal.equals("west")) {
                            if (Minecraft.getMinecraft().thePlayer.getPosition().getZ() == WartSetup.wartEnd) {
                                nextYaw = 130;
                            } else if (Minecraft.getMinecraft().thePlayer.getPosition().getZ() == -WartSetup.wartEnd) {
                                nextYaw = 50;
                            }
                        } else if (WartSetup.cardinal.equals("south")) {
                            if (Minecraft.getMinecraft().thePlayer.getPosition().getX() == WartSetup.wartEnd) {
                                nextYaw = 40;
                            } else if (Minecraft.getMinecraft().thePlayer.getPosition().getX() == -WartSetup.wartEnd) {
                                nextYaw = 320;
                            }
                        }
                        if(nextYaw != -1) {
                            for (int i = 0; i < 50; i++) {
                                Minecraft.getMinecraft().thePlayer.rotationYaw += (nextYaw - prevYaw) / 50;
                                Thread.sleep(10);
                            }
                        }
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Wart Rotation");
            thread.start();
        }
    }


    @SubscribeEvent
    public void click(TickEvent.ClientTickEvent event) {
        if (!Main.wartToggle || Minecraft.getMinecraft().currentScreen != null) {
            if (breaking) {
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
                breaking = false;
            }
            return;
        }
        if (event.phase.toString().equals("START")) {
            MovingObjectPosition objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
            if (objectMouseOver != null && objectMouseOver.typeOfHit.toString() == "BLOCK") {
                BlockPos pos = objectMouseOver.getBlockPos();
                Block gem = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                if (gem == Blocks.nether_wart) {
                    if (!breaking) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), true);
                        breaking = true;
                    }
                } else {
                    if (breaking) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
                        breaking = false;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void antiStuck(TickEvent.ClientTickEvent event) {
        if (!Main.wartToggle || Minecraft.getMinecraft().currentScreen != null) return;
        if (event.phase.toString().equals("START")) {
            if (thread2 == null || !thread2.isAlive()) {
                thread2 = new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        double xdiff = Math.abs(savex - Minecraft.getMinecraft().thePlayer.posX);
                        double zdiff = Math.abs(savez - Minecraft.getMinecraft().thePlayer.posZ);
                        if(xdiff < 2 && zdiff < 2) {
                            ChatUtils.sendMessage("stuck detected.");
                            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), true);
                            Thread.sleep(500);
                            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), false);
                            Thread.sleep(1000);
                            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), true);
                            Thread.sleep(500);
                            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), false);
                        }
                        savex = Minecraft.getMinecraft().thePlayer.posX;
                        savez = Minecraft.getMinecraft().thePlayer.posZ;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, "Anti Stuck");
                thread2.start();
            }
        }
    }
}
