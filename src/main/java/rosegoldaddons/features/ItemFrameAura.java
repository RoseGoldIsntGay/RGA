package rosegoldaddons.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ItemFrameAura {
    private static ArrayList<Entity> itemFrames = new ArrayList<Entity>();
    private static boolean clicking = false;
    private static ArrayList<Entity> clicked = new ArrayList<Entity>();
    private Thread thread;

    public static void rightClick() {
        try {
            Method rightClickMouse = null;
            try {
                rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse");
            } catch (NoSuchMethodException e) {
                rightClickMouse = Minecraft.class.getDeclaredMethod("func_147121_ag");
            }
            rightClickMouse.setAccessible(true);
            rightClickMouse.invoke(Minecraft.getMinecraft());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (!Main.legitToggle) return;
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(() -> {
                try {
                    BlockPos topLeft = new BlockPos(196, 125, 278);
                    ArrayList<Entity> redWools = new ArrayList<Entity>();
                    ArrayList<Entity> greenWools = new ArrayList<Entity>();

                    itemFrames.forEach(itemFrame -> {
                        ItemStack itemStack = ((EntityItemFrame) itemFrame).getDisplayedItem();
                        if (itemStack != null) {
                            String itemString = itemStack.toString();
                            if (itemString.contains("cloth@14")) {
                                redWools.add(itemFrame);
                            } else if (itemString.contains("cloth@5")) {
                                greenWools.add(itemFrame);
                            }
                        }
                    });
                    String patternName = getPattern(redWools, greenWools, topLeft);
                    MovingObjectPosition objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
                    if (objectMouseOver != null && objectMouseOver.entityHit != null) {
                        Entity entity = objectMouseOver.entityHit;
                        if (entity instanceof EntityItemFrame) {
                            Entity itemFrame = entity;
                            ItemStack itemStack = ((EntityItemFrame) itemFrame).getDisplayedItem();
                            if (itemStack != null) {
                                String itemString = itemStack.toString();
                                if (itemString.contains("arrow@0")) {
                                    int endRotationAmount = howMuchToClick(patternName, itemFrame, topLeft, true);
                                    int currRotationAmount = ((EntityItemFrame) itemFrame).getRotation();
                                    int toClick = 0;
                                    if (currRotationAmount < endRotationAmount) {
                                        toClick = endRotationAmount - currRotationAmount;
                                    } else if (currRotationAmount > endRotationAmount) {
                                        currRotationAmount = currRotationAmount - 8;
                                        toClick = endRotationAmount - currRotationAmount;
                                    }
                                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("" + toClick));
                                    for (int i = 0; i < toClick; i++) {
                                        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("clicked"));
                                        rightClick();
                                        Thread.sleep(Main.configFile.auraDelay);
                                    }
                                    Thread.sleep(200);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Legit Mode");
            thread.start();
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        itemFrames.clear();
        for (Entity entity1 : (Minecraft.getMinecraft().theWorld.loadedEntityList)) {
            if (entity1 instanceof EntityItemFrame) {
                itemFrames.add(entity1);
            }
        }
    }

    private static void interactWithEntity(Entity entity) {
        PlayerControllerMP playerControllerMP = Minecraft.getMinecraft().playerController;
        playerControllerMP.interactWithEntitySendPacket(Minecraft.getMinecraft().thePlayer, entity);
    }

    public static void mainAura() {
        BlockPos topLeft = new BlockPos(196, 125, 278);
        ArrayList<Entity> redWools = new ArrayList<Entity>();
        ArrayList<Entity> greenWools = new ArrayList<Entity>();

        itemFrames.forEach(itemFrame -> {
            ItemStack itemStack = ((EntityItemFrame) itemFrame).getDisplayedItem();
            if (itemStack != null) {
                String itemString = itemStack.toString();
                if (itemString.contains("cloth@14")) {
                    redWools.add(itemFrame);
                } else if (itemString.contains("cloth@5")) {
                    greenWools.add(itemFrame);
                }
            }
        });

        String patternName = getPattern(redWools, greenWools, topLeft);
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(patternName));
        clickPatternAura(patternName, topLeft);
    }

    private static void clickPatternAura(String patternName, BlockPos topLeft) {
        new Thread(() -> {
            itemFrames.forEach(itemFrame -> {
                ItemStack itemStack = ((EntityItemFrame) itemFrame).getDisplayedItem();
                if (itemStack != null) {
                    String itemString = itemStack.toString();
                    if (itemString.contains("arrow@0")) {
                        int endRotationAmount = howMuchToClick(patternName, itemFrame, topLeft, false);
                        int currRotationAmount = ((EntityItemFrame) itemFrame).getRotation();
                        int toClick = 0;
                        if (currRotationAmount < endRotationAmount) {
                            toClick = endRotationAmount - currRotationAmount;
                        } else if (currRotationAmount > endRotationAmount) {
                            currRotationAmount = currRotationAmount - 8;
                            toClick = endRotationAmount - currRotationAmount;
                        }
                        for (int i = 0; i < toClick; i++) {
                            interactWithEntity(itemFrame);
                        }
                    }
                }
            });
        }).start();
    }

    private static int howMuchToClick(String patternName, Entity entity, BlockPos topLeft, boolean legitMode) {
        //prepare to see top tier code right here boys
        int y = entity.getPosition().getY();
        int z = entity.getPosition().getZ();
        int relativeY = topLeft.getY() - y;
        int relativeX = topLeft.getZ() - z; //intentional

        //shit tier code!! i didn't know how to use hashmaps when i made this :)
        if (patternName == "legs") {
            if (relativeY == 0) {
                if (relativeX == 3 || relativeX == 2) {
                    return 5;
                } else if (relativeX == 1) {
                    return 3;
                }
            }
            if (relativeY == 1 || relativeY == 2 || relativeY == 3) {
                if (relativeX == 1) {
                    return 3;
                } else if (relativeX == 3) {
                    return 7;
                }
            }
            if (relativeY == 4) {
                if (relativeX == 1) {
                    if (legitMode) return 5;
                    return 4;
                }
                if (relativeX == 3) {
                    return 7;
                }
            }
        }
        if (patternName == "lines") {
            if (relativeY == 4 && relativeX == 2) {
                if (legitMode) return 5;
                return 4;
            }
            return 5;
        }
        if (patternName == "S") {
            if (relativeY == 0) {
                if (relativeX == 4) {
                    return 3;
                }
                if (relativeX < 4) {
                    return 1;
                }
            }
            if (relativeY == 1) {
                if (relativeX == 2) {
                    return 7;
                }
                if (relativeX == 4) {
                    return 3;
                }
            }
            if (relativeY == 3) {
                if (relativeX == 0) {
                    return 7;
                }
                if (relativeX == 2) {
                    if (legitMode) return 3;
                    return 2;
                }
            }
            if (relativeY == 4) {
                if (relativeX == 0) {
                    return 7;
                }
                if (relativeX > 0) {
                    return 5;
                }
            }
        }
        if (patternName == "W") {
            if (relativeY == 1 || relativeY == 2) {
                return 3;
            }
            if (relativeY == 3) {
                if (relativeX == 0 || relativeX == 4) {
                    return 3;
                }
                if (relativeX == 2) {
                    if (legitMode) return 7;
                    return 6;
                }
            }
            if (relativeY == 4) {
                if (relativeX < 2) {
                    return 1;
                }
                if (relativeX == 2) {
                    return 7;
                }
                if (relativeX > 2) {
                    return 5;
                }
            }
        }
        if (patternName == "spiral") {
            if (relativeY == 0) {
                if (relativeX == 0) {
                    return 3;
                }
                return 5;
            }
            if (relativeY == 3 && relativeX == 2) {
                if (legitMode) return 7;
                return 6;
            }
            if (relativeY < 4) {
                if (relativeX == 0) {
                    return 3;
                }
                if (relativeX == 2 || relativeX == 4) {
                    return 7;
                }
            }
            if (relativeY == 4) {
                if (relativeX == 2) {
                    return 7;
                }
                return 1;
            }
        }
        if (patternName == "zigzag") {
            if (relativeY == 3) {
                if (relativeX == 2) {
                    if (legitMode) return 5;
                    return 4;
                }
            }
            if (relativeY == 1 || relativeY == 3 || relativeX == 3) {
                return 5;
            }
            if (relativeX == 2) {
                if (relativeY == 0) {
                    return 3;
                }
                if (relativeY == 2 || relativeY == 4) {
                    return 7;
                }
            }
        }
        if (patternName == "N") {
            if (relativeY == 3 && relativeX == 2) {
                if (legitMode) return 3;
                return 2;
            }
            if (relativeY == 0) {
                if (relativeX == 2) {
                    return 3;
                }
                return 5;
            }
            if (relativeY < 4) {
                if (relativeX == 0 || relativeX == 4) {
                    return 7;
                }
                if (relativeX == 2) {
                    return 3;
                }
            }
            if (relativeY == 4) {
                if (relativeX > 0) {
                    return 5;
                }
                if (relativeX == 0) {
                    return 7;
                }
            }
        }
        if (patternName == "bottleneck") {
            if (relativeY == 3 && relativeX == 1) {
                if (legitMode) return 7;
                return 6;
            }
            if (relativeY == 0) {
                if (relativeX < 2) {
                    return 1;
                }
                if (relativeX > 2) {
                    return 5;
                }
            }
            if (relativeY == 1 || relativeY == 3) {
                return 7;
            }
            if (relativeY == 2) {
                if (relativeX == 0 || relativeX == 4) {
                    return 7;
                }
                if (relativeX == 1) {
                    return 5;
                }
                if (relativeX == 3) {
                    return 1;
                }
            }
        }

        return 0;
    }

    private static String getPattern(ArrayList<Entity> redWools, ArrayList<Entity> greenWools, BlockPos topleft) {
        if (redWools.size() == 1) {
            if (greenWools.size() == 1) {
                Entity ry1 = (Entity) redWools.toArray()[0];
                Entity gy1 = (Entity) greenWools.toArray()[0];
                BlockPos redPos1 = ry1.getPosition();
                BlockPos greenPos1 = gy1.getPosition();
                //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("red 1: "+redPos1));
                int relativeR1 = topleft.getY() - redPos1.getY();
                int relativeG1 = topleft.getY() - greenPos1.getY();
                if (relativeG1 == 4 && relativeR1 == 4) {
                    return "legs";
                }
                if (relativeG1 == 4 && relativeR1 == 0) {
                    return "N";
                }
                if (relativeG1 == 4 && relativeR1 == 2) {
                    return "spiral";
                }
            } else if (greenWools.size() == 2) {
                Entity ry1 = (Entity) redWools.toArray()[0];
                BlockPos redPos1 = ry1.getPosition();
                int relativeR1 = topleft.getY() - redPos1.getY();
                if (relativeR1 == 2) {
                    return "W";
                }
                return "bottleneck";
            }

        } else if (redWools.size() == 2) {
            Entity ry1 = (Entity) redWools.toArray()[0];
            Entity ry2 = (Entity) redWools.toArray()[0];
            BlockPos redPos1 = ry1.getPosition();
            BlockPos redPos2 = ry2.getPosition();
            int relativeR1 = topleft.getY() - redPos1.getY();
            int relativeR2 = topleft.getY() - redPos2.getY();
            if (greenWools.size() > 1) return "zigzag";
            return "S";

        } else if (redWools.size() == 3) {
            return "lines";
        }

        return "Unrecognized";
    }
}
