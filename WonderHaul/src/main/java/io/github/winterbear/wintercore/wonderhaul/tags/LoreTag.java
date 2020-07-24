package io.github.winterbear.wintercore.wonderhaul.tags;

import io.github.winterbear.wintercore.ConfigLoader;
import io.github.winterbear.wintercore.utils.LoreUtils;
import io.github.winterbear.wintercore.utils.RandomUtils;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LoreTag implements Tag{



    @Override
    public boolean apply(ItemStack item, TagApplication application) {
        List<String> tagLore = application.getTagItem().getItemMeta().getLore();
        List<String> lore = tagLore.subList(1, tagLore.size());
        LoreUtils.addLore(item, lore, application.getPlayer());
        sendMessage(application.getPlayer(), "Lore was added successfully!");
        return true;

    }

    private String pickLore(ItemStack item){
        List<String> lore = ConfigLoader.getConfig("lore").getStringList("lore.generic");
        return RandomUtils.getRandomElementOf(lore);
    }

    @Override
    public String getDisplayName() {
        return "&eLore Tag";
    }

    @Override
    public String getInstructions() {
        return "&7Right click with a piece of equipment to add lore to it!";
    }

    @Override
    public ItemStack modify(ItemStack item){
        LoreUtils.addMultiLineLore(item, "&7" + pickLore(item));
        return item;
    }
}