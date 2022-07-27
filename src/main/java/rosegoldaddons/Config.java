package rosegoldaddons;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import org.jetbrains.annotations.NotNull;
import rosegoldaddons.utils.ChatUtils;
import rosegoldaddons.utils.OpenSkyblockGui;

import java.io.File;
import java.util.Comparator;


public class Config extends Vigilant {
    public static Config INSTANCE = new Config();

    @Property(type = PropertyType.SWITCH, name = "Auto Start Dungeon + Ready", description = "Automatically starts the dungeon and gets ready.",
            category = "Dungeons", subcategory = "General")
    public boolean AutoReady = false;

    @Property(type = PropertyType.SLIDER, name = "Highlight Line Width", description = "Determine the line thickness of all ESPs, 0 to disable ESPs",
            category = "ESP", subcategory = "General", max = 10)
    public int lineWidth = 3;

    @Property(type = PropertyType.SWITCH, name = "Dungeon ESPs", description = "Edited from ShadyAddons' ESP",
            category = "ESP", subcategory = "General")
    public boolean dungeonESP = false;

    @Property(type = PropertyType.SELECTOR, name = "Auto Ghost Block", description = "Choose which mode auto ghost block will follow",
            category = "Dungeons", subcategory = "General", options = {"While Held", "On Press"})
    public int ghostIndex = 0;

    @Property(type = PropertyType.SWITCH, name = "Auto Ghost Block Stairs", description = "Automatically ghost block stairs you are standing on when you sneak",
            category = "Dungeons", subcategory = "General")
    public boolean AutoGB = false;

    @Property(type = PropertyType.SWITCH, name = "Add Flipped Stairs", description = "Ghost Block upside-down stair when jumped into",
            category = "Dungeons", subcategory = "General")
    public boolean AutoGBTopStair = false;

    @Property(type = PropertyType.SWITCH, name = "Add Misc. Blocks", description = "Adds other phase-able blocks to Auto Ghost Block",
            category = "Dungeons", subcategory = "General")
    public boolean AutoGBMisc = false;

    public boolean autoArrowAlign = true;

    @Property(type = PropertyType.SWITCH, name = "Party Untransfer", description = "When you really dont wanna be party leader.",
            category = "RoseGoldAddons", subcategory = "General")
    public boolean AutoUntransfer = false;

    @Property(type = PropertyType.SLIDER, name = "Smooth Look Velocity", description = "How fast should head rotation changes be (in ticks)",
            category = "RoseGoldAddons", subcategory = "General", min = 1, max = 40)
    public int smoothLookVelocity = 5;

    @Property(type = PropertyType.SWITCH, name = "Enderman ESP", description = "**NOT** Needed for enderman macro.",
            category = "ESP", subcategory = "General")
    public boolean EndermanESP = false;

    @Property(type = PropertyType.SWITCH, name = "Use Utility Items when Swapping", description = "Automatically use Tuba / Orb / Wand when AOTS or Whip swap are enabled",
            category = "RoseGoldAddons", subcategory = "General")
    public boolean UseUtility = false;

    @Property(type = PropertyType.SWITCH, name = "Entity Reach", description = "Interact with entities from far away",
            category = "RoseGoldAddons", subcategory = "General")
    public boolean entityReach = false;

    @Property(type = PropertyType.SWITCH, name = "Entity Reach Sticky Mode", description = "Remember last entity looked at",
            category = "RoseGoldAddons", subcategory = "General")
    public boolean sticky = false;

    @Property(type = PropertyType.SLIDER, name = "Sword Swap Delay", description = "How often to swap swords (in miliseconds)",
            category = "RoseGoldAddons", subcategory = "General", max = 2000)
    public int swapFrequency = 500;

    @Property(type = PropertyType.SLIDER, name = "Macro Range", description = "Look for entities only in radius of the player, 0 = unlimited",
            category = "RoseGoldAddons", subcategory = "General", max = 300)
    public int macroRadius = 0;

    @Property(type = PropertyType.SWITCH, name = "Ping on world change", description = "Send a message to a Discord Webhook to ping on world change",
            category = "Discord", subcategory = "General")
    public boolean pingworldchange = false;

    @Property(type = PropertyType.TEXT, name = "Discord ID", description = "Discord ID to ping",
            category = "Discord", subcategory = "General")
    public String discordid = "";

    @Property(type = PropertyType.TEXT, name = "Webhook URL", description = "Webhook URL to use when pinging",
            category = "Discord", subcategory = "General")
    public String hookurl = "";

    @Property(type = PropertyType.SLIDER, name = "Delay Before Breaking Tree", description = "Miliseconds to wait before breaking tree",
             category = "Foraging", subcategory = "General", max = 2000)
    public int treecapDelay = 1000;

    @Property(type = PropertyType.SLIDER, name = "Delay Before Using Rod", description = "Miliseconds to wait before using rod",
            category = "Foraging", subcategory = "General", max = 500)
    public int prerodDelay = 150;

    @Property(type = PropertyType.SLIDER, name = "Delay After Using Rod", description = "Miliseconds to wait after using rod (before starting over)",
            category = "Foraging", subcategory = "General", max = 500)
    public int postrodDelay = 150;

    @Property(type = PropertyType.SWITCH, name = "Randomize Delay", description = "Add slight randomization to delay",
            category = "Foraging", subcategory = "General")
    public boolean randomizeForaging = false;

    @Property(type = PropertyType.SWITCH, name = "Admin Antisus", description = "Act as if you're there when you get AFK checked (not recommended to leave on)",
            category = "Foraging", subcategory = "General")
    public boolean forageantisus = false;

    @Property(type = PropertyType.SWITCH, name = "Prioritize Gemstone Blocks", description = "Will first search for full blocks, then panes",
            category = "Mining", subcategory = "General")
    public boolean prioblocks = false;

    @Property(type = PropertyType.SLIDER, name = "Hardstone Nuker Height", description = "Range to break above the player",
            category = "Mining", subcategory = "General",max = 5)
    public int hardrange = 0;

    @Property(type = PropertyType.SLIDER, name = "Hardstone Nuker Depth", description = "Range to break below the player",
            category = "Mining", subcategory = "General",max = 3)
    public int hardrangeDown = 0;

    @Property(type = PropertyType.SELECTOR, name = "Hardstone Nuker Shape", description = "Choose which pattern hardstone nuker will follow",
            category = "Mining", subcategory = "General", options = {"Closest Block", "Facing Axis"})
    public int hardIndex = 0;

    @Property(type = PropertyType.SWITCH, name = "Server Side Rotations", description = "Hardstone nuker's chest solver will use server side rotations for opening treasure chests",
            category = "Mining", subcategory = "General")
    public boolean serverSideChest = false;

    @Property(type = PropertyType.SELECTOR, name = "Actions", description = "Type of action to perform when opening a brewing stand",
            category = "Alchemy", subcategory = "General", options = {"Collect + Sell", "Insert Water Bottles", "Insert Nether Wart", "Insert Cane / Eye", "Insert Glowstone"})
    public int alchindex = 0;

    @Property(type = PropertyType.SWITCH, name = "Auto Open Brewing Stand", description = "Only for Collecting + Selling, use with an auto brewer",
            category = "Alchemy", subcategory = "General")
    public boolean openstand = false;

    @Property(type = PropertyType.SWITCH, name = "Close GUI When Done", description = "Automatically close the GUI when done",
            category = "Alchemy", subcategory = "General")
    public boolean alchclose = true;

    @Property(type = PropertyType.SLIDER, name = "Action Delay", description = "Millisecond delay between actions",
            category = "Alchemy", subcategory = "General", max = 1000)
    public int alchsleep = 300;

    @Property(type = PropertyType.SELECTOR, name = "Nuker Crop Type", description = "Select the type of crop you want to nuke",
            category = "Farming", subcategory = "General", options = {"Any Crop Except Cane or Cactus", "Cane or Cactus", "Nether Wart", "Wheat", "Carrot", "Potato", "Pumpkin", "Melon", "Mushroom", "Cocoa"})
    public int farmNukeIndex = 0;

    @Property(type = PropertyType.SELECTOR, name = "Crop Nuker Shape", description = "Choose which pattern crop nuker will follow",
            category = "Farming", subcategory = "General", options = {"Closest Block", "Facing Axis"})
    public int farmShapeIndex = 0;

    @Property(type = PropertyType.SELECTOR, name = "Crop Nuker Speed", description = "Choose how many blocks per second Crop Nuker will break",
            category = "Farming", subcategory = "General", options = {"40 BPS", "80 BPS"})
    public int farmSpeedIndex = 0;

    @Property(type = PropertyType.SWITCH, name = "Look at nuked block", description = "Looks at currently nuked block to look less sus",
            category = "Mining", subcategory = "General")
    public boolean mithrilLook = false;

    @Property(type = PropertyType.SWITCH, name = "Skip Titanium", description = "Mithril nuker will ignore titanium",
            category = "Mining", subcategory = "General")
    public boolean ignoreTitanium = false;

    @Property(type = PropertyType.SWITCH, name = "Include Ores", description = "Hardstone Nuker, Mithril Nuker and Mithril Macro will also target ores",
            category = "Mining", subcategory = "General")
    public boolean includeOres = false;

    @Property(type = PropertyType.SWITCH, name = "Include Sand & Gravel", description = "Hardstone Nuker will also target sand and gravel",
            category = "Mining", subcategory = "General")
    public boolean includeExcavatable = false;

    @Property(type = PropertyType.SWITCH, name = "Only Ores", description = "Mithril Macro will only target ores",
            category = "Mining", subcategory = "General")
    public boolean onlyOres = false;

    @Property(type = PropertyType.SELECTOR, name = "Mithril Macro Priority", description = "Determine the order the macro will breaks blocks in",
            category = "Mining", subcategory = "General", options = {"Highest value to lowest", "Lowest value to highest","Any"})
    public int mithrilMacroPrio = 0;

    @Property(type = PropertyType.SWITCH, name = "Pingless Mining", description = "Mines the next block before the previous block breaks, instabreak only",
            category = "Mining", subcategory = "General")
    public boolean pinglessMining = false;

    @Property(type = PropertyType.SELECTOR, name = "Pingless Mining Speed", description = "Determine how long to wait before mining the next block",
            category = "Mining", subcategory = "General", options = {"20 BPS (Legit)", "40 BPS", "80 BPS"})
    public int pinglessSpeed = 0;

    @Property(type = PropertyType.SWITCH, name = "Auto Slayer", description = "Automatically use batphone",
            category = "Slayer", subcategory = "General")
    public boolean autoSlayer = false;

    @Property(type = PropertyType.SWITCH, name = "Click Maddox", description = "Automatically click maddox on boss kill",
            category = "Slayer", subcategory = "General")
    public boolean clickMaddox = false;

    @Property(type = PropertyType.SELECTOR, name = "Slayer Type", description = "Type of slayer to auto start",
            category = "Slayer", subcategory = "General", options = {"None", "Zombie 3", "Zombie 4", "Zombie 5", "Spider 3", "Spider 4", "Sven 3", "Sven 4", "Enderman 2", "Enderman 3", "Enderman 4"})
    public int slayerTypeIndex = 0;

    @Property(type = PropertyType.SWITCH, name = "Force Slayer", description = "Starts a slayer quest if you dont have one, restarts if it is not done after 5 minutes",
            category = "Slayer", subcategory = "General")
    public boolean forceSlayer = false;

    @Property(type = PropertyType.SELECTOR, name = "Anti Racism", description = "Prevent racist messages from showing up in chat",
            category = "RoseGoldAddons", subcategory = "General", options = {"Censor", "Block", "None"})
    public int antiRacism = 0;

    @Property(type = PropertyType.SWITCH, name = "Pause All Macros", description = "Pause all macros on world change",
            category = "Macros", subcategory = "General")
    public boolean pauseAllWorldChange = false;

    @Property(type = PropertyType.SWITCH, name = "Auto Leave Limbo", description = "Automatically leaves limbo",
            category = "Macros", subcategory = "General")
    public boolean autoLeaveLimbo = false;

    @Property(type = PropertyType.SWITCH, name = "Join SkyBlock", description = "Automatically joins skyblock after leaving limbo",
            category = "Macros", subcategory = "General")
    public boolean autoLimboSB = true;

    @Property(type = PropertyType.SWITCH, name = "Enderman Move Macro", description = "Add primitive movement to enderman macro",
            category = "Macros", subcategory = "Enderman")
    public boolean endermanMove = false;

    @Property(type = PropertyType.SWITCH, name = "Enderman Lobby", description = "Go to lobby instead of island when switching lobbies",
            category = "Macros", subcategory = "Enderman")
    public boolean endermanLobby = false;

    @Property(type = PropertyType.SWITCH, name = "Randomization", description = "Random head movements when not shooting endermen",
            category = "Macros", subcategory = "Enderman")
    public boolean endermanRandom = false;

    @Property(type = PropertyType.SWITCH, name = "Right Click", description = "Right click instead of shifting",
            category = "Macros", subcategory = "Enderman")
    public boolean endermanRC = false;

    @Property(type = PropertyType.SWITCH, name = "Special Zealot Right Click", description = "Right click on special zealots (spirit sceptre recommended)",
            category = "Macros", subcategory = "Enderman")
    public boolean zealotRC = false;

    @Property(type = PropertyType.SWITCH, name = "Zealot Only", description = "Only target zealots",
            category = "Macros", subcategory = "Enderman")
    public boolean zealotOnly = false;

    @Property(type = PropertyType.SLIDER, name = "Lobby Swap Timer", description = "How many minutes to wait until finding a new lobby",
            category = "Macros", subcategory = "Enderman", max = 30)
    public int endermanTimer = 10;

    @Property(type = PropertyType.SWITCH, name = "Ironman Mode", description = "Lobby swap without using the bazaar",
            category = "Macros", subcategory = "Enderman")
    public boolean endermanIronman = false;

    @Property(type = PropertyType.SELECTOR, name = "Shifting / Right Click Speed", description = "Determine the speed of the enderman macro",
            category = "Macros", subcategory = "Enderman", options = {"x2", "x1", "x0.5", "x0.25"})
    public int endermanSpeed = 1;

    @Property(type = PropertyType.SLIDER, name = "Stuck Distance", description = "Distance to block to turn away (square)",
            category = "Macros", subcategory = "Enderman", max = 10)
    public int endermanStuckDist = 3;

    @Property(type = PropertyType.SELECTOR, name = "Type", description = "Choose a type",
            category = "Macros", subcategory = "Stranded Villagers", options = {"Cocoa", "Potato", "Sugar Cane", "Ender Pearls"})
    public int strandedType = 0;

    @Property(type = PropertyType.SLIDER, name = "Interaction Delay", description = "How many ticks to wait next interaction",
            category = "Macros", subcategory = "Stranded Villagers", max = 20)
    public int strandedCropDebounc = 10;

    @Property(type = PropertyType.SLIDER, name = "GUI Interaction Delay", description = "How many ticks to wait next GUI interaction",
            category = "Macros", subcategory = "Stranded Villagers", max = 10)
    public int strandedGUIDebounc = 5;

    @Property(type = PropertyType.SELECTOR, name = "Custom Name Type", description = "ignore if you dont know what these mean",
            category = "RoseGoldAddons", subcategory = "General", options = {"MixinEntityPlayer", "MixinEntityPlayer + MixinEntity"})
    public int nameRenderType = 0;

    @Property(type = PropertyType.SWITCH, name = "Potato Mode", description = "This brings back memories...",
            category = "Memes", subcategory = "General")
    public boolean guilag = false;

    @Property(type = PropertyType.SWITCH, name = "Hilarity", description = "Pls no wipe mr admin :(",
            category = "Memes", subcategory = "General")
    public boolean funnyStuff = true;

    @Property(type = PropertyType.SLIDER, name = "Intensity", description = "I love hilarity so much I want to see it more please!",
            category = "Memes", subcategory = "General", min = 1, max = 3000)
    public int skiblock = 3000;

    @Property(type = PropertyType.SWITCH, name = "i dont wanna see it", description = "please i dont wanna see it",
            category = "Memes", subcategory = "General")
    public boolean wydsi = true;

    @Property(type = PropertyType.SWITCH, name = "Nucleus ESP", description = "ESP for rare items dropped from nucleus",
            category = "ESP", subcategory = "General")
    public boolean nucleusESP = false;

    @Property(type = PropertyType.SWITCH, name = "Big Blocks", description = "Change levers, buttons, chests and Redstone Key skulls to 1x1x1",
            category = "Dungeons", subcategory = "General")
    public boolean dungeonBlocksBig = false;

    @Property(type = PropertyType.SWITCH, name = "Auto 3 Weirdos", description = "beta",
            category = "Dungeons", subcategory = "General")
    public boolean autoThreeWeirdos = false;

    @Property(type = PropertyType.SWITCH, name = "Force Dungeon", description = "",
            category = "testing", subcategory = "General")
    public boolean forceDungeon = false;

    @Property(type = PropertyType.BUTTON, name = "Sell All", description = "",
            category = "testing", subcategory = "General")
    public void sellAllTest() {
        ChatUtils.sendMessage("Selling All!");
        Main.mc.thePlayer.sendChatMessage("/bz");
        OpenSkyblockGui.sellAll = true;
    }

    @Property(type = PropertyType.SWITCH, name = "KeyBoard Event Cancel", description = "",
            category = "testing", subcategory = "General")
    public boolean stopKeyboard = false;

    @Property(type = PropertyType.SELECTOR, name = "Auto Click Mode", description = "",
            category = "Macros", subcategory = "General", options = {"Right Click", "Left Click"})
    public int autoClickerMode = 0;

    @Property(type = PropertyType.SLIDER, name = "Auto Click CPS", description = "",
            category = "Macros", subcategory = "General", min = 1, max = 1000)
    public int autoClickerCPS = 10;

    public Config() {
        super(new File("./config/rosegoldaddons/config.toml"), "RoseGoldAddons", new JVMAnnotationPropertyCollector(), new ConfigSorting());
        initialize();
    }

    public static class ConfigSorting extends SortingBehavior {
        @NotNull
        @Override
        public Comparator<Category> getCategoryComparator() {
            return (o1, o2) -> {
                if(o1.getName().equals("RoseGoldAddons")) {
                    return -1;
                } else if(o2.getName().equals("RoseGoldAddons")) {
                    return 1;
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        }
    }
}
