package rosegoldaddons.features;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.ScoreboardUtils;

public class AutoLeaveLimbo {
    private int deb = 0;
    private boolean playSb = false;
    private int prev = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(!Main.configFile.autoLeaveLimbo || Main.endermanMacro) return;
        if(event.phase == TickEvent.Phase.END) return;
        if(deb > 0) deb--;
        if(deb != 0) return;
        deb = 20;
        if(ScoreboardUtils.inLimbo) {
            if(prev < 10) {
                prev++;
            } else {
                Main.mc.thePlayer.sendChatMessage("/l");
                prev = 0;
                playSb = true;
                return;
            }
        } else {
            prev = 0;
        }
        if(!ScoreboardUtils.inSkyblock && playSb && Main.configFile.autoLimboSB) {
            Main.mc.thePlayer.sendChatMessage("/play sb");
            playSb = false;
        }
    }
}
