package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Utils.SecondCounter;

import java.awt.*;


public class AntiTrip extends Module {
    public AntiTrip() {
        super("AntiTrip", "moneymod+2", Category.MOVEMENT, new Color(232,199,155,255).getRGB());
    }

    SecondCounter fall = new SecondCounter();

    public void renderWorld(){
        this.setMcHudMeta(""+fall.getCount());
    }


    public void tick(){
        if (mc.player == null || mc.world == null)
            return;
        if (mc.player.onGround) {
            return;
        }
        if (!mc.player.onGround && mc.player.fallDistance >= 4.0F) {
            mc.player.motionY = 0.05D;
            fall.increment();
        }
    }
}
