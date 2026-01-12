package me.daoge.itemnametag;

import org.allaymc.api.entity.interfaces.EntityItem;
import org.allaymc.api.eventbus.EventHandler;
import org.allaymc.api.eventbus.event.entity.EntitySpawnEvent;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.server.Server;
import org.allaymc.api.utils.TextFormat;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ItemNameTag extends Plugin {

    @Override
    public void onEnable() {
        Server.getInstance().getEventBus().registerListener(this);
        pluginLogger.info("ItemNameTag enabled!");
    }

    @EventHandler
    private void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof EntityItem entityItem) {
            ItemStack itemStack = entityItem.getItemStack();
            if (itemStack == null || itemStack.isEmptyOrAir()) {
                return;
            }

            String itemName = getItemDisplayName(itemStack);
            int count = itemStack.getCount();

            // Format: "ItemName §ax Count"
            String nameTag = itemName + TextFormat.GREEN + " x" + count;

            entityItem.setNameTag(nameTag);
            entityItem.setNameTagAlwaysShow(true);
        }
    }

    private String getItemDisplayName(ItemStack itemStack) {
        // Check if item has custom name first
        String customName = itemStack.getCustomName();
        if (customName != null && !customName.isEmpty()) {
            return customName;
        }

        // Convert identifier path to readable name
        // e.g., minecraft:grass_block -> Grass Block
        String path = itemStack.getItemType().getIdentifier().path();
        return formatIdentifierPath(path);
    }

    private String formatIdentifierPath(String path) {
        // Split by underscore, capitalize each word, join with space
        // e.g., grass_block -> Grass Block
        return Arrays.stream(path.split("_"))
                .map(word -> word.isEmpty() ? word :
                        Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
