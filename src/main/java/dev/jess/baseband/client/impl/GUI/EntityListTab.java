package dev.jess.baseband.client.impl.GUI;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Utils.RenderingMethods;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EntityListTab extends PinableFrame {

	public ArrayList<Entity> list = new ArrayList<>();

	public EntityListTab() {
		super("Entity List", new String[]{}, 150);
	}

	public void renderFrameText() {

		if (this.open) {
			List<Entity> entityList = Wrapper.mc.world.loadedEntityList;
			if (Wrapper.mc.player != null && entityList.size() > 1) {

				Map<String, Integer> entityMap = new HashMap<>();
				List<Entity> copy = new ArrayList<>(entityList);
				for (Entity e : copy) {
					if (e instanceof EntityPlayer) continue;

					String name = e.getName();

					int add = 1;

					if (e instanceof EntityItem) {
						name = ((EntityItem) e).getItem().getItem().getItemStackDisplayName(((EntityItem) e).getItem());
						add = ((EntityItem) e).getItem().getCount();
					}

					int count = entityMap.containsKey(name) ? entityMap.get(name) : 0;
					entityMap.put(name, count + add);
				}

				//entityMap = sortByValue(entityMap);

				ArrayList<String> kek = new ArrayList<>();
				for (Map.Entry<String, Integer> entry : entityMap.entrySet()) {
					kek.add(entry.getKey() + " (" + entry.getValue() + ")");
				}

				if (kek.isEmpty()) {
					this.setWidth(defaultWidth);
				} else {
					this.setWidth(Wrapper.fr.getStringWidth(BaseBand.getLongestStringArray(kek)));
				}

				RenderingMethods.drawBorderedRectReliantGui(this.x, this.y + this.barHeight + 1, this.x + this.getWidth(), this.y + this.barHeight + 3 + entityMap.size() * 10, 1, new Color(25, 25, 25, 128).getRGB(), Color.gray.getRGB());

				int yCount = this.y + this.barHeight + 3;
				for (Map.Entry<String, Integer> entry : entityMap.entrySet()) {
					Wrapper.fr.drawString(entry.getKey() + " (" + entry.getValue() + ")", this.x + 2, yCount, - 1);
					yCount += 10;
				}


			}
		}

	}


}

