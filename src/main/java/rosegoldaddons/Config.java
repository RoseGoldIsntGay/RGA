package rosegoldaddons;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Comparator;


public class Config extends Vigilant {
    public static Config INSTANCE = new Config();

    @Property(type = PropertyType.SWITCH, name = "Auto Start Dungeon + Ready", description = "Automatically starts the dungeon and gets ready.",
            category = "Dungeons", subcategory = "General")
    public boolean AutoReady = true;

    public boolean autoArrowAlign = true;

    @Property(type = PropertyType.SWITCH, name = "Party Untransfer", description = "When you really dont wanna be party leader.",
            category = "RoseGoldAddons", subcategory = "General")
    public boolean AutoUntransfer = false;

    @Property(type = PropertyType.SWITCH, name = "Enderman ESP", description = "**NOT** Needed for enderman macro.",
            category = "RoseGoldAddons", subcategory = "General")
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

    @Property(type = PropertyType.SLIDER, name = "Smooth Look Velocity", description = "How fast should head rotation changes be (in miliseconds)",
            category = "RoseGoldAddons", subcategory = "General", min = 1, max = 200)
    public int smoothLookVelocity = 50;

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

    @Property(type = PropertyType.SWITCH, name = "Radomize Delay", description = "Add slight randomization to delay",
            category = "Foraging", subcategory = "General")
    public boolean randomizeForaging = true;

    @Property(type = PropertyType.SWITCH, name = "Admin Antisus", description = "Act as if you're there when you get AFK checked (not recommended to leave on)",
            category = "Foraging", subcategory = "General")
    public boolean forageantisus = false;

    @Property(type = PropertyType.SWITCH, name = "Prioritize Gemstone Blocks", description = "Will first search for full blocks, then panes",
            category = "Mining", subcategory = "General")
    public boolean prioblocks = false;

    @Property(type = PropertyType.SLIDER, name = "Hardstone Nuker Height", description = "Range to break above the player",
            category = "Mining", subcategory = "General", max = 5)
    public int hardrange = 0;

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

    @Property(type = PropertyType.SWITCH, name = "Look at nuked block", description = "Looks at currently nuked block to look less sus",
            category = "Mining", subcategory = "General")
    public boolean mithrilLook = false;

    @Property(type = PropertyType.SWITCH, name = "Skip Titanium", description = "Mithril nuker will ignore titanium",
            category = "Mining", subcategory = "General")
    public boolean ignoreTitanium = false;

    @Property(type = PropertyType.SWITCH, name = "Include Ores", description = "Hardstone Nuker will also nuke ores",
            category = "Mining", subcategory = "General")
    public boolean includeOres = false;

    @Property(type = PropertyType.SWITCH, name = "Auto Slayer", description = "Automatically use batphone",
            category = "RoseGoldAddons", subcategory = "General")
    public boolean autoSlayer = false;

    @Property(type = PropertyType.SWITCH, name = "Click Maddox", description = "Automatically click maddox on boss kill",
            category = "RoseGoldAddons", subcategory = "General")
    public boolean clickMaddox = false;

    @Property(type = PropertyType.SELECTOR, name = "Slayer Type", description = "Type of slayer to auto start",
            category = "RoseGoldAddons", subcategory = "General", options = {"None", "Zombie 3", "Zombie 4", "Zombie 5", "Spider 3", "Spider 4", "Sven 3", "Sven 4", "Enderman 2", "Enderman 3", "Enderman 4"})
    public int slayerTypeIndex = 0;

    @Property(type = PropertyType.SWITCH, name = "Potato Mode", description = "This brings back memories...",
            category = "RoseGoldAddons", subcategory = "General")
    public boolean guilag = false;

    @Property(type = PropertyType.SWITCH, name = "Hilarity", description = "Those pesky admins!",
            category = "RoseGoldAddons", subcategory = "General")
    public boolean funnyStuff = true;

    @Property(type = PropertyType.SWITCH, name = "Nucleus ESP", description = "ESP for rare items dropped from nucleus",
            category = "ESP", subcategory = "General")
    public boolean nucleusESP = false;

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
