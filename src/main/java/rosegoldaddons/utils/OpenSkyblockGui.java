package rosegoldaddons.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.commands.Rosedrobe;
import rosegoldaddons.commands.Rosepet;
import rosegoldaddons.features.SexAura;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OpenSkyblockGui {
    private static boolean openTrades = false;
    public static boolean doTrades = false;
    private static int deb = 0;
    public static int die = -1;
    public static boolean sellAll = false;
    public static boolean selling = false;
    public static int enableChat = -1;
    public static boolean doVisit = false;
    private static boolean didVisit = false;
    private static final KeyBinding[] binds = Main.mc.gameSettings.keyBindings;
    private static int sellAttempts = 0;
    public static boolean storeInChest = false;
    public static boolean storing = false;
    public static boolean openedChest = false;

    public static String[] endermanDrops = {
            "summoning eye", "ender pearl", "bone", "obsidian", "crystal fragment", "eye of ender", "end stone", "arrow", "combat exp, rune"
    };

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Rosedrobe.openWardrobe && !openTrades && !Rosepet.openPetS && !sellAll && !storeInChest) return;
        if (event.gui instanceof GuiChest) {
            Container container = ((GuiChest) event.gui).inventorySlots;
            if (container instanceof ContainerChest) {
                String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (Rosedrobe.openWardrobe) {
                    if (chestName.contains("Pets")) {
                        clickSlot(48, 0, 0);
                        clickSlot(32, 0, 1);
                        if (Rosedrobe.slot > 0) {
                            clickSlot(Rosedrobe.slot + 35, 0, 2);
                            Main.mc.thePlayer.closeScreen();
                        }
                        Rosedrobe.openWardrobe = false;
                    }
                } else if (openTrades) {
                    if (chestName.contains("Pets")) {
                        clickSlot(48, 0, 0);
                        clickSlot(22, 0, 1);
                        openTrades = false;
                    }
                } else if (Rosepet.openPetS) {
                    if (chestName.contains("Pets")) {
                        int currPage = 1;
                        int lastPage = 1;
                        if (chestName.startsWith("(")) {
                            currPage = Integer.parseInt(chestName.substring(chestName.indexOf("(") + 1, chestName.indexOf("/")));
                            lastPage = Integer.parseInt(chestName.substring(chestName.indexOf("/") + 1, chestName.indexOf(")")));
                        }
                        String petName = Rosepet.name;
                        int petSlot = Rosepet.petSlot;
                        if (petSlot != 0) {
                            clickSlot(petSlot - 1, 0, 0);
                            Rosepet.petSlot = 0;
                            Rosepet.openPetS = false;
                            return;
                        }
                        if (!petName.equals("")) {
                            List<Slot> chestInventory = ((GuiChest) Main.mc.currentScreen).inventorySlots.inventorySlots;
                            for (Slot slot : chestInventory) {
                                if (!slot.getHasStack()) continue;
                                if (slot.getStack().getDisplayName().contains(petName)) {
                                    clickSlot(slot.slotNumber, 0, 0);
                                    Main.mc.thePlayer.closeScreen();
                                    Rosepet.openPetS = false;
                                    return;
                                }
                            }
                            if (currPage < lastPage) {
                                clickSlot(53, 0, 0);
                            } else {
                                Rosepet.openPetS = false;
                                Main.mc.thePlayer.closeScreen();
                                ChatUtils.sendMessage("No pet named " + petName + " found.");
                            }
                        } else {
                            ChatUtils.sendMessage("Invalid Pet Name");
                            Rosepet.openPetS = false;
                        }

                    }
                } else if (sellAll && deb == 0) {
                    selling = true;
                    if (chestName.contains("Bazaar")) {
                        List<Slot> chestInventory = ((GuiChest) Main.mc.currentScreen).inventorySlots.inventorySlots;
                        for (Slot slot : chestInventory) {
                            if (!slot.getHasStack()) continue;
                            if (slot.getStack().getDisplayName().contains("Inventory Now")) {
                                if (slot.getStack().serializeNBT().toString().contains("don't have items")) {
                                    ChatUtils.sendMessage("Couldn't find any items to sell to bazaar");
                                    sellAttempts = 0;
                                    sellAll = false;
                                    selling = false;
                                    Main.mc.thePlayer.closeScreen();
                                } else {
                                    clickSlot(slot.slotNumber, 0, 0);
                                }
                            }
                        }
                    } else if (chestName.contains("you sure?")) {
                        List<Slot> chestInventory = ((GuiChest) Main.mc.currentScreen).inventorySlots.inventorySlots;
                        for (Slot slot : chestInventory) {
                            if (!slot.getHasStack()) continue;
                            if (slot.getStack().getDisplayName().contains("whole inventory")) {
                                clickSlot(slot.slotNumber, 0, 0);
                                sellAttempts++;
                                deb = 5;
                                break;
                            } else if (slot.getStack().getDisplayName().contains("sold!")) {
                                ChatUtils.sendMessage("Sold!");
                                sellAttempts = 0;
                                sellAll = false;
                                selling = false;
                                Main.mc.thePlayer.closeScreen();
                                return;
                            }
                        }
                    }
                } else if(storeInChest && deb == 0) {
                    storing = true;
                    if (chestName.contains("Chest")) {
                        List<Slot> chestInventory = ((GuiChest) Main.mc.currentScreen).inventorySlots.inventorySlots;
                        for (Slot slot : chestInventory) {
                            if (!slot.getHasStack()) continue;
                            if(slot.slotNumber < 53) continue;
                            String display = slot.getStack().getDisplayName().toLowerCase();
                            for(String drop : endermanDrops) {
                                if (display.contains(drop)) {
                                    clickSlot(slot.slotNumber, 0, 1, 0);
                                    deb = 5;
                                    return;
                                }
                            }
                        }
                        ChatUtils.sendMessage("done storing");
                        storeInChest = false;
                        storing = false;
                        openedChest = false;
                        Main.mc.thePlayer.closeScreen();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void debounce(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (deb > 0) deb--;
        if(storeInChest && !openedChest && !ShadyRotation.running) {
            storing = true;
            Vec3 chest = closestChest();
            if(chest == null) {
                ChatUtils.sendMessage("No chest found in island, going back to macroing");
                storeInChest = false;
                storing = false;
            } else {
                ShadyRotation.smoothLook(ShadyRotation.vec3ToRotation(chest), Main.configFile.smoothLookVelocity, () -> {
                    rightClick();
                    openedChest = true;
                });
            }
        }
        if (sellAttempts > 9) {
            Main.mc.thePlayer.closeScreen();
            Main.mc.thePlayer.sendChatMessage("/bz");
            sellAttempts = 0;
        }
        if (deb == 0 && didVisit && doVisit) {
            doVisit = false;
            didVisit = false;
            Main.mc.thePlayer.sendChatMessage("/trade " + removeFormatting(SexAura.sender.split(" ")[1]));
            doTrades = true;
            die = 200;
        }
        if (doTrades) {
            if (die > 0) {
                die--;
            }
        }
        if (die == 0) {
            doTrades = false;
            enableChat = 40;
            die = -1;
        }
        if (enableChat > 0) enableChat--;
        if (enableChat == 0) {
            SexAura.blocked = false;
            enableChat = -1;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (Main.mc.thePlayer == null || Main.mc.theWorld == null) return;
        if (Main.configFile.stopKeyboard || SexAura.blocked) {
            for (KeyBinding keyBinding : binds) {
                KeyBinding.setKeyBindState(keyBinding.getKeyCode(), false);
            }
        }
    }

    @SubscribeEvent
    public void guiDraw2(GuiScreenEvent.BackgroundDrawnEvent event) {
        if(!doVisit) return;
        if (event.gui instanceof GuiChest) {
            Container container = ((GuiChest) event.gui).inventorySlots;
            if (container instanceof ContainerChest) {
                if (doVisit && deb == 0) {
                    List<Slot> chestInventory = ((GuiChest) Main.mc.currentScreen).inventorySlots.inventorySlots;
                    for (Slot slot : chestInventory) {
                        if (!slot.getHasStack()) continue;
                        if (slot.getStack().getDisplayName().contains("Visit player")) {
                            if (!didVisit) {
                                clickSlot(slot.slotNumber, 0, 0);
                                didVisit = true;
                                deb = 100;
                            }
                        }
                    }
                }
                if (doTrades && deb == 0) {
                    List<Slot> chestInventory = ((GuiChest) Main.mc.currentScreen).inventorySlots.inventorySlots;
                    if (!chestInventory.get(30).getHasStack()) {
                        for (int i = chestInventory.size() - 1; i > 0; i--) {
                            Slot slot = chestInventory.get(i);
                            if (!slot.getHasStack()) continue;
                            if (slot.getStack().getDisplayName().contains("SkyBlock")) continue;
                            if (slot.slotNumber < 44) continue;
                            clickSlot(slot.slotNumber, 1, 0);
                            deb = 5;
                        }
                    }
                    if (chestInventory.get(39).getHasStack() && (chestInventory.get(39).getStack().getDisplayName().contains("Warning!") || chestInventory.get(39).getStack().getDisplayName().contains("Deal!"))) {
                        clickSlot(39, 0, 0);
                        doTrades = false;
                        enableChat = 40;
                    }
                }
            }
        }
    }

    public static void openTradesMenu() {
        openTrades = true;
        Main.mc.thePlayer.sendChatMessage("/pets");
    }

    private void clickSlot(int slot, int type, int windowAdd) {
        Main.mc.playerController.windowClick(Main.mc.thePlayer.openContainer.windowId + windowAdd, slot, type, 0, Main.mc.thePlayer);
    }

    private void clickSlot(int slot, int type, int mode, int windowAdd) {
        Main.mc.playerController.windowClick(Main.mc.thePlayer.openContainer.windowId + windowAdd, slot, type, mode, Main.mc.thePlayer);
    }

    private String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }

    private Vec3 closestChest() {
        if (Main.mc.theWorld == null) return null;
        if (Main.mc.thePlayer == null) return null;
        int r = 6;
        BlockPos playerPos = Main.mc.thePlayer.getPosition();
        playerPos.add(0, 1, 0);
        Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> chests = new ArrayList<>();
        for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
            IBlockState blockState = Main.mc.theWorld.getBlockState(blockPos);
            if (blockState.getBlock() == Blocks.chest) {
                chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
            }
        }

        double smallest = 9999;
        Vec3 closest = null;
        for (Vec3 chest : chests) {
            double dist = chest.distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = chest;
            }
        }
        return closest;
    }

    public static void rightClick() {
        try {
            Method rightClickMouse;
            try {
                rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse");
            } catch (NoSuchMethodException e) {
                rightClickMouse = Minecraft.class.getDeclaredMethod("func_147121_ag");
            }
            rightClickMouse.setAccessible(true);
            rightClickMouse.invoke(Main.mc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
