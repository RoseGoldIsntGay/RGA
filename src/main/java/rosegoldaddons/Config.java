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

    @Property(type = PropertyType.SLIDER, name = "Auto Arrow Align Delay", description = "How often to click an item frame (in miliseconds)",
            category = "Dungeons", subcategory = "General", max = 500)
    public int autoArrowAlignDelay = 10;

    @Property(type = PropertyType.SWITCH, name = "Party Untransfer", description = "When you really dont wanna be party leader.",
            category = "RoseGoldAddons", subcategory = "General")
    public boolean AutoUntransfer = true;

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
            category = "RoseGoldAddons", subcategory = "General", max = 100)
    public int macroRadius = 0;

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

    public Config() {
        super(new File("./config/rosegoldaddons/config.toml"), "RoseGold Addons", new JVMAnnotationPropertyCollector(), new ConfigSorting());
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
