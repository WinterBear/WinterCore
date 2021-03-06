package io.github.winterbear.wintercore.wonderhaul.tags;


import io.github.winterbear.wintercore.utils.SoundUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

/**
 * Created by WinterBear on 05/06/2019.
 */
public class RepairTag implements Tag{


    @Override
    public boolean apply(ItemStack item, TagApplication application) {
        Player player = application.getPlayer();
        if(item.getItemMeta() instanceof Damageable){
            Damageable meta = ((Damageable) item.getItemMeta());
            if(meta.hasDamage()){
                item.setDurability((short) 0);
                player.updateInventory();
                SoundUtils.playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
                sendMessage(player, "Item was repaired successfully!");
                return true;
            } else {
                sendMessage(player, "Item was already at max durability.");
                return false;
            }
        }
        sendMessage(player, "Item does not have durability.");
        return false;
    }

    @Override
    public String getDisplayName() {
        return "Repair Tag";
    }

    @Override
    public String getInstructions() {
        return "Right click a damaged piece of equipment to repair it!";
    }

    @Override
    public String getDescription() {
        return "A scrap of cloth with an ancient script woven into the fabric. Holding it brings a feeling of restoration and renewal.";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.of("#ff0059");
    }
}
