package rosegoldaddons.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;

import java.lang.reflect.Method;
import java.util.List;

public class StrandedVillagerMacro {
    public static boolean drop = false;
    public static boolean dropped = false;
    public static boolean trade = false;
    public static boolean traded = false;
    public static boolean emptySack = false;
    public static boolean emptied = false;
    private static boolean closed = false;

    private static int currentCost = 0;
    private static int deb = 0;
    private static int deb2 = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Main.strandedVillagers) return;
        if (event.phase == TickEvent.Phase.END) return;
        if (deb != 0) return;
        deb = Main.configFile.strandedCropDebounc;
        if (!emptied) {
            if (Main.mc.currentScreen == null) {
                rightClick();
                emptySack = true;
            }
        } else {
            if (!dropped) {
                if (!closed) {
                    Main.mc.thePlayer.closeScreen();
                    closed = true;
                } else {
                    Main.mc.thePlayer.dropOneItem(true);
                    closed = false;
                    dropped = true;
                }
            } else {
                if (!traded) {
                    if (Main.mc.currentScreen == null) {
                        rightClick();
                        trade = true;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void debounce(TickEvent.ClientTickEvent event) {
        if (!Main.strandedVillagers) return;
        if (event.phase == TickEvent.Phase.END) return;
        if (deb > 0) deb--;
        if (deb2 > 0) deb2--;
    }

    @SubscribeEvent
    public void guiDraw2(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Main.strandedVillagers) return;
        if (deb2 != 0) return;
        if (event.gui instanceof GuiChest) {
            Container container = ((GuiChest) event.gui).inventorySlots;
            if (container instanceof ContainerChest) {
                String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (emptySack) {
                    List<Slot> chestInventory = ((GuiChest) Main.mc.currentScreen).inventorySlots.inventorySlots;
                    if (chestName.equals("Agronomy Sack")) {
                        for (Slot slot : chestInventory) {
                            if (!slot.getHasStack()) continue;
                            switch (Main.configFile.strandedType) {
                                case 0:
                                    if (slot.getStack().getDisplayName().contains("Cocoa Beans")) {
                                        clickSlot(slot.slotNumber, 0, 0);
                                        emptySack = false;
                                        emptied = true;
                                    }
                                    break;
                                case 1:
                                    if (slot.getStack().getDisplayName().contains("Potato")) {
                                        clickSlot(slot.slotNumber, 0, 0);
                                        emptySack = false;
                                        emptied = true;
                                    }
                                    break;
                                case 2:
                                    if (slot.getStack().getDisplayName().contains("Sugar Cane")) {
                                        clickSlot(slot.slotNumber, 0, 0);
                                        emptySack = false;
                                        emptied = true;
                                    }
                                    break;
                            }
                        }
                    } else if(chestName.equals("Combat Sack")) {
                        for (Slot slot : chestInventory) {
                            if (!slot.getHasStack()) continue;
                            switch (Main.configFile.strandedType) {
                                case 3:
                                    if (slot.getStack().getDisplayName().contains("Ender Pearl")) {
                                        clickSlot(slot.slotNumber, 0, 0);
                                        emptySack = false;
                                        emptied = true;
                                    }
                                    break;
                            }
                        }
                    } else {
                        if(Main.configFile.strandedType == 3) {
                            for (Slot slot : chestInventory) {
                                if (!slot.getHasStack()) continue;
                                if (slot.getStack().getDisplayName().contains("Combat Sack")) {
                                    clickSlot(slot.slotNumber, 1, 0);
                                }
                            }
                        } else {
                            for (Slot slot : chestInventory) {
                                if (!slot.getHasStack()) continue;
                                if (slot.getStack().getDisplayName().contains("Agronomy Sack")) {
                                    clickSlot(slot.slotNumber, 1, 0);
                                }
                            }
                        }
                    }
                } else if (trade) {
                    List<Slot> chestInventory = ((GuiChest) Main.mc.currentScreen).inventorySlots.inventorySlots;
                    if (chestName.equals("Shop Trading Options")) {
                        switch (Main.configFile.strandedType) {
                            case 0:
                                int cropAmount = getAmountOfCropType("Cocoa Beans");
                                for (Slot slot : chestInventory) {
                                    if (!slot.getHasStack()) continue;
                                    if (slot.slotNumber > 36) continue;
                                    if (slot.getStack().getDisplayName().contains("Emerald")) {
                                        if (cropAmount >= currentCost * 10) {
                                            clickSlot(slot.slotNumber+2, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else if (cropAmount >= currentCost * 5) {
                                            clickSlot(slot.slotNumber+1, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else if (cropAmount >= currentCost) {
                                            clickSlot(slot.slotNumber, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else {
                                            reset();
                                        }
                                    }
                                }
                                break;
                            case 1:
                                cropAmount = getAmountOfCropType("Potato");
                                for (Slot slot : chestInventory) {
                                    if (!slot.getHasStack()) continue;
                                    if (slot.slotNumber > 36) continue;
                                    if (slot.getStack().getDisplayName().contains("Emerald")) {
                                        if (cropAmount >= currentCost * 10) {
                                            clickSlot(slot.slotNumber+2, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else if (cropAmount >= currentCost * 5) {
                                            clickSlot(slot.slotNumber+1, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else if (cropAmount >= currentCost) {
                                            clickSlot(slot.slotNumber, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else {
                                            reset();
                                        }
                                    }
                                }
                                break;
                            case 2:
                                cropAmount = getAmountOfCropType("Sugar Cane");
                                for (Slot slot : chestInventory) {
                                    if (!slot.getHasStack()) continue;
                                    if (slot.slotNumber > 36) continue;
                                    if (slot.getStack().getDisplayName().contains("Emerald")) {
                                        if (cropAmount >= currentCost * 10) {
                                            clickSlot(slot.slotNumber+2, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else if (cropAmount >= currentCost * 5) {
                                            clickSlot(slot.slotNumber+1, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else if (cropAmount >= currentCost) {
                                            clickSlot(slot.slotNumber, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else {
                                            reset();
                                        }
                                    }
                                }
                                break;
                            case 3:
                                cropAmount = getAmountOfCropType("Ender Pearl");
                                for (Slot slot : chestInventory) {
                                    if (!slot.getHasStack()) continue;
                                    if (slot.slotNumber > 36) continue;
                                    if (slot.getStack().getDisplayName().contains("Emerald")) {
                                        if (cropAmount >= currentCost * 10) {
                                            clickSlot(slot.slotNumber+2, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else if (cropAmount >= currentCost * 5) {
                                            clickSlot(slot.slotNumber+1, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else if (cropAmount >= currentCost) {
                                            clickSlot(slot.slotNumber, 0, 0);
                                            deb2 = Main.configFile.strandedGUIDebounc;
                                            break;
                                        } else {
                                            reset();
                                        }
                                    }
                                }
                                break;
                        }

                    } else if (chestName.contains("Villager")) {
                        switch (Main.configFile.strandedType) {
                            case 0:
                                for (Slot slot : chestInventory) {
                                    if (!slot.getHasStack()) continue;
                                    if (slot.slotNumber > 36) continue;
                                    String nbt = slot.getStack().serializeNBT().toString();
                                    if (nbt.contains("Cocoa Beans") && slot.getStack().getDisplayName().contains("Emerald")) {
                                        currentCost = Integer.parseInt(nbt.substring(nbt.indexOf("x") + 1, nbt.indexOf("4:\"\"") - 2));
                                        clickSlot(slot.slotNumber, 1, 0);
                                    }
                                }
                                break;
                            case 1:
                                for (Slot slot : chestInventory) {
                                    if (!slot.getHasStack()) continue;
                                    if (slot.slotNumber > 36) continue;
                                    String nbt = slot.getStack().serializeNBT().toString();
                                    if (nbt.contains("Potato") && slot.getStack().getDisplayName().contains("Emerald")) {
                                        currentCost = Integer.parseInt(nbt.substring(nbt.indexOf("x") + 1, nbt.indexOf("4:\"\"") - 2));
                                        clickSlot(slot.slotNumber, 1, 0);
                                    }
                                }
                                break;
                            case 2:
                                for (Slot slot : chestInventory) {
                                    if (!slot.getHasStack()) continue;
                                    if (slot.slotNumber > 36) continue;
                                    String nbt = slot.getStack().serializeNBT().toString();
                                    if (nbt.contains("Sugar Cane") && slot.getStack().getDisplayName().contains("Emerald")) {
                                        currentCost = Integer.parseInt(nbt.substring(nbt.indexOf("x") + 1, nbt.indexOf("4:\"\"") - 2));
                                        clickSlot(slot.slotNumber, 1, 0);
                                    }
                                }
                                break;
                            case 3:
                                for (Slot slot : chestInventory) {
                                    if (!slot.getHasStack()) continue;
                                    if (slot.slotNumber > 36) continue;
                                    String nbt = slot.getStack().serializeNBT().toString();
                                    if (nbt.contains("Ender Pearl") && slot.getStack().getDisplayName().contains("Emerald")) {
                                        currentCost = Integer.parseInt(nbt.substring(nbt.indexOf("x") + 1, nbt.indexOf("4:\"\"") - 2));
                                        clickSlot(slot.slotNumber, 1, 0);
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    private static void reset() {
        Main.mc.thePlayer.closeScreen();
        drop = false;
        dropped = false;
        trade = false;
        traded = false;
        emptySack = false;
        emptied = false;
        currentCost = 0;
        deb = 0;
    }

    private int getAmountOfCropType(String type) {
        int total = 0;
        List<Slot> inventory = Main.mc.thePlayer.inventoryContainer.inventorySlots;
        for (Slot slot : inventory) {
            if (!slot.getHasStack()) continue;
            if (slot.getStack().getDisplayName().contains(type)) {
                total = total + slot.getStack().stackSize;
            }
        }
        return total;
    }

    private void clickSlot(int slot, int type, int windowAdd) {
        Main.mc.playerController.windowClick(Main.mc.thePlayer.openContainer.windowId + windowAdd, slot, type, 0, Main.mc.thePlayer);
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
