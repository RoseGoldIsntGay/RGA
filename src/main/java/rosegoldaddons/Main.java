package rosegoldaddons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;
import rosegoldaddons.commands.*;
import rosegoldaddons.features.BlockBreakAura;
import rosegoldaddons.features.*;
import rosegoldaddons.utils.OpenSkyblockGui;

import java.io.*;

@Mod(modid = "timechanger", name = "RoseGold Addons", version = "2.1")
public class Main {
    public static GuiScreen display = null;
    public static Config configFile = Config.INSTANCE;
    public static KeyBinding[] keyBinds = new KeyBinding[11];
    public static int tickCount = 0;
    public static boolean endermanMacro = false;
    public static boolean powderMacro = false;
    public static String u = "";
    public static boolean AOTSMacro = false;
    public static boolean SoulWhipMacro = false;
    public static boolean verified = false;
    public static boolean GhostMacro = false;
    public static boolean legitToggle = false;
    public static boolean gemNukeToggle = false;
    public static boolean wartToggle = false;
    public static boolean autoUseItems = false;
    public static boolean autoHardStone = false;

    @Mod.EventHandler
    public void onFMLInitialization(FMLPreInitializationEvent event) {
        File directory = new File(event.getModConfigurationDirectory(), "rosegoldaddons");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new AutoReady());
        MinecraftForge.EVENT_BUS.register(new OpenSkyblockGui());
        MinecraftForge.EVENT_BUS.register(new EndermanMacro());
        MinecraftForge.EVENT_BUS.register(new ItemFrameAura());
        MinecraftForge.EVENT_BUS.register(new PowderMacro());
        MinecraftForge.EVENT_BUS.register(new SwordSwapping());
        MinecraftForge.EVENT_BUS.register(new PartyUntransfer());
        MinecraftForge.EVENT_BUS.register(new GhostMacro());
        MinecraftForge.EVENT_BUS.register(new BlockBreakAura());
        MinecraftForge.EVENT_BUS.register(new WartMacro());
        MinecraftForge.EVENT_BUS.register(new CustomItemMacro());
        MinecraftForge.EVENT_BUS.register(new HardstoneMacro());

        configFile.initialize();
        ClientCommandHandler.instance.registerCommand(new OpenSettings());
        ClientCommandHandler.instance.registerCommand(new Rosedrobe());
        ClientCommandHandler.instance.registerCommand(new LobbySwap());
        ClientCommandHandler.instance.registerCommand(new Backpack());
        ClientCommandHandler.instance.registerCommand(new WartSetup());
        ClientCommandHandler.instance.registerCommand(new UseCooldown());

        keyBinds[0] = new KeyBinding("Custom Item Macro Toggle", Keyboard.KEY_NONE, "RoseGold Addons");
        keyBinds[1] = new KeyBinding("Toggle Enderman Macro", Keyboard.KEY_NONE, "RoseGold Addons");
        keyBinds[2] = new KeyBinding("Item Frame Aura", Keyboard.KEY_NONE, "RoseGold Addons");
        keyBinds[3] = new KeyBinding("Powder Macro Toggle", Keyboard.KEY_NONE, "RoseGold Addons");
        keyBinds[4] = new KeyBinding("AOTS SS Toggle", Keyboard.KEY_NONE, "RoseGold Addons");
        keyBinds[5] = new KeyBinding("Soul Whip SS Toggle", Keyboard.KEY_NONE, "RoseGold Addons");
        keyBinds[6] = new KeyBinding("Ghost Macro Toggle", Keyboard.KEY_NONE, "RoseGold Addons");
        keyBinds[7] = new KeyBinding("Item Frame Legit Mode Toggle", Keyboard.KEY_NONE, "RoseGold Addons");
        keyBinds[8] = new KeyBinding("Gemstone Smart Nuke", Keyboard.KEY_NONE, "RoseGold Addons");
        keyBinds[9] = new KeyBinding("Wart Macro", Keyboard.KEY_NONE, "RoseGold Addons");
        keyBinds[10] = new KeyBinding("Hardstone Macro", Keyboard.KEY_NONE, "RoseGold Addons");

        for (KeyBinding keyBind : keyBinds) {
            ClientRegistry.registerKeyBinding(keyBind);
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (display != null) {
            try {
                Minecraft.getMinecraft().displayGuiScreen(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
            display = null;
        }
    }

    @SubscribeEvent
    public void connect(FMLNetworkEvent.ClientConnectedToServerEvent event) {

    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent event) {
        if (keyBinds[0].isPressed()) {
            autoUseItems = !autoUseItems;
            String str = autoUseItems ? "§aCustom Item Macro Activated" : "§cCustom Item Macro Deactivated";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(str));
        } else if (keyBinds[1].isPressed()) {
            endermanMacro = !endermanMacro;
            String str = endermanMacro ? "§aZealot Macro Activated" : "§cZealot Macro Deactivated";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(str));
        } else if (keyBinds[2].isPressed()) {
            ItemFrameAura.mainAura();
        } else if (keyBinds[3].isPressed()) {
            powderMacro = !powderMacro;
            String str = powderMacro ? "§aPowder Macro Activated" : "§cPowder Macro Deactivated";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(str));
        } else if (keyBinds[4].isPressed()) {
            AOTSMacro = !AOTSMacro;
            String str = AOTSMacro ? "§aAOTS Macro Activated" : "§cAOTS Macro Deactivated";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(str));
        } else if (keyBinds[5].isPressed()) {
            SoulWhipMacro = !SoulWhipMacro;
            String str = SoulWhipMacro ? "§aSoul Whip Macro Activated" : "§cSoul Whip Macro Deactivated";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(str));
        } else if(keyBinds[6].isPressed()) {
            GhostMacro = !GhostMacro;
            String str = GhostMacro ? "§aGhost Macro Activated" : "§cGhost Macro Deactivated";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(str));
        } else if(keyBinds[7].isPressed()) {
            legitToggle = !legitToggle;
            String str = legitToggle ? "§aLegit Mode Activated" : "§cLegit Mode Deactivated";
            SwordSwapping.tickCount = 0;
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(str));
        } else if(keyBinds[8].isPressed()) {
            gemNukeToggle = !gemNukeToggle;
            String str = gemNukeToggle ? "§aGemstone Nuke Activated" : "§cGemstone Nuke Deactivated";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(str));
        } else if(keyBinds[9].isPressed()) {
            wartToggle = !wartToggle;
            String str = wartToggle ? "§aWart Macro Activated" : "§cWart Macro Deactivated";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(str));
        } else if(keyBinds[10].isPressed()) {
            autoHardStone = !autoHardStone;
            String str = autoHardStone ? "§aHardstone Macro Activated" : "§cHardstone Macro Deactivated";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(str));
        }
    }

}
