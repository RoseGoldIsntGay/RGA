package rosegoldaddons.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import rosegoldaddons.Main;
import rosegoldaddons.events.MillisecondEvent;
import rosegoldaddons.utils.ChatUtils;

import java.lang.reflect.Method;

public class AutoClicker {
    private static final KeyBinding keyBinding = new KeyBinding("Auto Clicker", Keyboard.KEY_NONE, "RoseGoldAddons - Combat");
    private boolean toggled = false;
    private int count = 0;
    private long startedAt = 0;
    private long lastClickTime = 0;

    @SubscribeEvent
    public void onMillisecond(MillisecondEvent event) {
        if(!toggled) return;
        if(System.currentTimeMillis() - lastClickTime < (long) (1000 / Main.configFile.autoClickerCPS)) return;
        switch (Main.configFile.autoClickerMode) {
            case 1:
                MovingObjectPosition movingObjectPosition = Main.mc.objectMouseOver;
                if (movingObjectPosition != null && movingObjectPosition.entityHit != null) {
                    Main.mc.playerController.attackEntity(Main.mc.thePlayer, movingObjectPosition.entityHit);
                    Main.mc.thePlayer.swingItem();
                    count++;
                } else if (movingObjectPosition != null) {
                    Main.mc.thePlayer.swingItem();
                }
                break;
            case 0:
                rightClick();
                count++;
                break;
        }
        lastClickTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        int eventKey = Keyboard.getEventKey();
        if(eventKey != keyBinding.getKeyCode()) return;
        if(Keyboard.isKeyDown(eventKey)) {
            if(!toggled) {
                toggled = true;
                count = 0;
                startedAt = System.currentTimeMillis();
            }
        } else {
            toggled = false;
            ChatUtils.sendMessage(String.format("%s Clicks in %s milliseconds", count, System.currentTimeMillis() - startedAt));
        }
    }

    public static void init() {
        ClientRegistry.registerKeyBinding(keyBinding);
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
            rightClickMouse.invoke(Main.mc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
