package rosegoldaddons.features;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.ChatUtils;
import rosegoldaddons.utils.RenderUtils;
import rosegoldaddons.utils.RotationUtils;
import scala.actors.threadpool.helpers.ThreadHelpers;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

public class ForagingIslandMacro {
    private Thread thread;
    private String[] cum = {"wtf??", "hello?", "hi?", "uhhhhhh", "what the", "??????"};

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (Main.forageOnIsland) {
            if (thread == null || !thread.isAlive()) {
                thread = new Thread(() -> {
                    try {
                        BlockPos furthestDirt = furthestEmptyDirt();
                        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("furthest dirt "+furthestDirt));
                        int sapling = findItemInHotbar("Jungle Sapling");
                        if(sapling == -1) {
                            sapling = findItemInHotbar("Oak Sapling");
                        }
                        int bonemeal = findItemInHotbar("Bone Meal");
                        int treecap = findItemInHotbar("Treecapitator");
                        int rod = findItemInHotbar("Rod");
                        if(sapling == -1) {
                            ChatUtils.sendMessage("§cNo jungle saplings in hotbar");
                        }
                        if(bonemeal == -1) {
                            ChatUtils.sendMessage("§cNo bonemeal in hotbar");
                        }
                        if(treecap == -1) {
                            ChatUtils.sendMessage("§cNo Treecapitator in hotbar");
                        }
                        if(rod == -1) {
                            ChatUtils.sendMessage("§cNo Fishing Rod in hotbar");
                        }
                        if(sapling == -1 || bonemeal == -1 || treecap == -1 || rod == -1) {
                            Main.forageOnIsland = false;
                            ChatUtils.sendMessage("§cForaging Macro Deactivated");
                            return;
                        }
                        if (furthestDirt != null) {
                            RotationUtils.facePos(new Vec3(furthestDirt.getX() + 0.5, furthestDirt.getY() - 0.5, furthestDirt.getZ() + 0.5));
                            Thread.sleep(Main.configFile.smoothLookVelocity * 2L);
                            if (sapling != -1) {
                                if (Minecraft.getMinecraft().objectMouseOver.typeOfHit.toString().equals("BLOCK")) {
                                    BlockPos pos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
                                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                                    if (block == Blocks.sapling) {
                                        click();
                                        Thread.sleep(20);
                                    }
                                }
                                Minecraft.getMinecraft().thePlayer.inventory.currentItem = sapling;
                                //Minecraft.getMinecraft().playerController.onPlayerRightClick(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(), furthestDirt, EnumFacing.NORTH, Minecraft.getMinecraft().objectMouseOver.hitVec);
                                rightClick();
                            }
                        } else {
                            BlockPos dirt = closestDirt();
                            if (dirt != null) {
                                RotationUtils.facePos(new Vec3(dirt.getX() + 0.5, dirt.getY(), dirt.getZ() + 0.5));
                                if (bonemeal != -1 && treecap != -1) {
                                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = bonemeal;
                                    Random rand = new Random();
                                    int toAdd = 0;
                                    if(Main.configFile.randomizeForaging) {
                                        toAdd = rand.nextInt(20);
                                    }
                                    ChatUtils.sendMessage("extra delay: "+toAdd+"%");
                                    Thread.sleep(Math.round(150*(1+(toAdd/100))));
                                    rightClick();
                                    rightClick();
                                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = treecap;
                                    Thread.sleep(Math.round(Main.configFile.treecapDelay*(1+(toAdd/100))));
                                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), true);
                                    Thread.sleep(Math.round(150*(1+(toAdd/100))));
                                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
                                    Thread.sleep(Math.round(25*(1+(toAdd/100))));
                                    Minecraft.getMinecraft().thePlayer.inventory.currentItem = rod;
                                    Thread.sleep(Math.round(Main.configFile.prerodDelay*(1+(toAdd/100))));
                                    rightClick();
                                    Thread.sleep(Math.round(Main.configFile.postrodDelay*(1+(toAdd/100))));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, "Island Foraging Macro");
                thread.start();
            }
        }
    }

    @SubscribeEvent
    public void renderWorld2(RenderWorldLastEvent event) {
        if (!Main.forageOnIsland) return;
        BlockPos e = closestDirt();
        if(e == null) {
            Main.forageOnIsland = false;
            ChatUtils.sendMessage("§cNo dirt in range of player");
            ChatUtils.sendMessage("§cForaging Macro Deactivated");
            if(Main.configFile.forageantisus) {
                int rand = new Random().nextInt(cum.length);
                int rand2 = new Random().nextInt(5000);
                new Thread(() -> {
                    try {
                        Thread.sleep(rand2);
                        Minecraft.getMinecraft().thePlayer.sendChatMessage(cum[rand]);
                        Thread.sleep(rand2*2);
                        Minecraft.getMinecraft().getNetHandler().getNetworkManager().closeChannel(new ChatComponentText("Antisus activated lets hope you didnt get banned"));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }).start();
            }
        }
    }

    private BlockPos furthestEmptyDirt() {
        int r = 5;
        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
        playerPos.add(0, 1, 0);
        Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> dirts = new ArrayList<Vec3>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                IBlockState blockState2 = Minecraft.getMinecraft().theWorld.getBlockState(blockPos.add(0, 1, 0));
                //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(blockState.getBlock().toString()));
                if (blockState.getBlock() == Blocks.dirt && blockState2.getBlock() == Blocks.air) {
                    dirts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
            }
        }
        double biggest = -1;
        Vec3 furthest = null;
        for (int i = 0; i < dirts.size(); i++) {
            double dist = dirts.get(i).distanceTo(playerVec);
            if (dist > biggest) {
                biggest = dist;
                furthest = dirts.get(i);
            }
        }
        if (furthest != null && biggest < 4) {
            return new BlockPos(furthest.xCoord, furthest.yCoord, furthest.zCoord);
        }
        return null;
    }

    private BlockPos closestDirt() {
        int r = 5;
        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
        playerPos.add(0, 1, 0);
        Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> dirts = new ArrayList<Vec3>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(blockState.getBlock().toString()));
                if (blockState.getBlock() == Blocks.dirt) {
                    dirts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
            }
        }
        double smallest = 9999;
        Vec3 closest = null;
        for (int i = 0; i < dirts.size(); i++) {
            double dist = dirts.get(i).distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = dirts.get(i);
            }
        }
        if (closest != null && smallest < 4) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }

    private static int findItemInHotbar(String name) {
        InventoryPlayer inv = Minecraft.getMinecraft().thePlayer.inventory;
        for (int i = 0; i < 9; i++) {
            ItemStack curStack = inv.getStackInSlot(i);
            if (curStack != null) {
                if (curStack.getDisplayName().contains(name)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void rightClick() {
        try {
            Method rightClickMouse;
            try {
                rightClickMouse = Minecraft.class.getDeclaredMethod("func_147121_ag");
            } catch (NoSuchMethodException e) {
                rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse");
            }
            rightClickMouse.setAccessible(true);
            rightClickMouse.invoke(Minecraft.getMinecraft());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
