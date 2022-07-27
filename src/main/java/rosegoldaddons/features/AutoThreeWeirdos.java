package rosegoldaddons.features;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.ChatUtils;
import rosegoldaddons.utils.ScoreboardUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
 * Solver blatantly copied from DSM: https://github.com/bowser0000/SkyblockMod
 */

public class AutoThreeWeirdos {
    private static final ArrayList<String> solutions = new ArrayList<>(Arrays.asList(
            "The reward is not in my chest!",
            "At least one of them is lying, and the reward is not in",
            "My chest doesn't have the reward. We are all telling the truth",
            "My chest has the reward and I'm telling the truth",
            "The reward isn't in any of our chests",
            "Both of them are telling the truth."
            ));

    private static final ArrayList<String> weirdos = new ArrayList<>(Arrays.asList(
            "Baxter", "Benson", "Eveleth", "Hope", "Luverne", "Madelia", "Rose", "Victoria", "Morris", "Carver", "Ardis", "Lino",
            "Elmo", "Virginia", "Montgomery", "Winona", "Melrose", "Marshall", "Hugo", "Willmar", "Ramsey"
    ));

    private static BlockPos riddleChest = null;
    private static boolean talked = false;
    private static boolean opened = false;
    private static int debounce = 0;


    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if(!Main.configFile.autoThreeWeirdos || opened || !ScoreboardUtils.inDungeon) return;
        String message = removeFormatting(event.message.getUnformattedText());
        if (message.startsWith("[NPC]")) {
            for (String solution : solutions) {
                if (message.contains(solution)) {
                    String npcName = message.substring(message.indexOf("]") + 2, message.indexOf(":"));
                    if (riddleChest == null) {
                        List<Entity> entities = Main.mc.theWorld.getLoadedEntityList();
                        for (Entity entity : entities) {
                            if (entity == null || !entity.hasCustomName()) continue;
                            if (entity.getCustomNameTag().contains(npcName)) {
                                BlockPos npcLocation = new BlockPos(entity.posX, 69, entity.posZ);
                                if (Main.mc.theWorld.getBlockState(npcLocation.north()).getBlock() == Blocks.chest) {
                                    riddleChest = npcLocation.north();
                                } else if (Main.mc.theWorld.getBlockState(npcLocation.east()).getBlock() == Blocks.chest) {
                                    riddleChest = npcLocation.east();
                                } else if (Main.mc.theWorld.getBlockState(npcLocation.south()).getBlock() == Blocks.chest) {
                                    riddleChest = npcLocation.south();
                                } else if (Main.mc.theWorld.getBlockState(npcLocation.west()).getBlock() == Blocks.chest) {
                                    riddleChest = npcLocation.west();
                                } else {
                                    ChatUtils.sendMessage("Could not find correct riddle chest.");
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        else if(message.startsWith("PUZZLE") && message.contains("wasn't fooled by")) {
            opened = true;
            talked = true;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(!Main.configFile.autoThreeWeirdos || opened || !ScoreboardUtils.inDungeon) return;
        if(Main.mc.theWorld == null || Main.mc.thePlayer == null) return;
        if(event.phase == TickEvent.Phase.END) return;
        if(debounce > 0) debounce--;
        if(debounce != 0) return;
        if(!talked) {
            debounce = 5;
            if(!allWeirdosInRange()) return;
            interactWithWeirdos();
            talked = true;
        } else {
            if(riddleChest != null) {
                debounce = 10;
                interactWithChest();
            }
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        riddleChest = null;
        opened = false;
        talked = false;
    }

    private static boolean allWeirdosInRange() {
        int count = 0;
        List<Entity> entities = Main.mc.theWorld.getLoadedEntityList();
        for (Entity entity : entities) {
            if (entity == null || !entity.hasCustomName()) continue;
            String name = removeFormatting(entity.getCustomNameTag());
            if(weirdos.contains(name)) {
                float range = entity.getDistanceToEntity(Main.mc.thePlayer);
                if(range < 4) {
                    count++;
                }
            }
        }
        if(count == 1 || count == 2) {
            ChatUtils.sendMessage("Detected an incorrect amount of weirdos in range, try moving closer or DMing RoseGold#5441 the names of the weirdos");
        }
        return count == 3 || count == 2;
    }

    private static void interactWithWeirdos() {
        List<Entity> entities = Main.mc.theWorld.getLoadedEntityList();
        for (Entity entity : entities) {
            if (entity == null || !entity.hasCustomName()) continue;
            String name = removeFormatting(entity.getCustomNameTag());
            if(weirdos.contains(name)) {
                interactWithEntity(entity);
            }
        }
    }

    private static void interactWithChest() {
        Vec3 playerPos = Main.mc.thePlayer.getPositionEyes(1f);
        if(playerPos.distanceTo(new Vec3(riddleChest.getX() + 0.5, riddleChest.getY() + 0.5, riddleChest.getZ() + 0.5)) > 5) {
            ChatUtils.sendMessage("§cWalk closer to chest!");
        }
        if(Main.mc.playerController.onPlayerRightClick(
                Main.mc.thePlayer,
                Main.mc.theWorld,
                Main.mc.thePlayer.inventory.getCurrentItem(),
                riddleChest,
                EnumFacing.fromAngle(Main.mc.thePlayer.rotationYaw),
                new Vec3(Math.random(), Math.random(), Math.random())
        )) {
            Main.mc.thePlayer.swingItem();
            opened = true;
        }
    }

    private static void interactWithEntity(Entity entity) {
        PlayerControllerMP playerControllerMP = Main.mc.playerController;
        playerControllerMP.interactWithEntitySendPacket(Main.mc.thePlayer, entity);
    }

    private static String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }

}
