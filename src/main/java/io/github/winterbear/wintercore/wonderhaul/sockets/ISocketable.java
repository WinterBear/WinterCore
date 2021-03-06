package io.github.winterbear.wintercore.wonderhaul.sockets;

import io.github.winterbear.wintercore.utils.TexturedHead;
import io.github.winterbear.wintercore.wonderhaul.sockets.application.SocketApplication;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Created by WinterBear on 30/08/2020.
 */
public interface ISocketable {

    TexturedHead getTexture();

    String getItemName();

    String getAbilityName();

    ChatColor getColor();

    TriggerType getTriggerType();

    SocketType getSocketType();

    Collection<Material> getApplicableItems();

    String getApplicationInstructions();

    boolean apply(ItemStack item, SocketApplication socketApplication);

    ItemStack getItem();

    Sound getSound();

    String getDescription();

    Ability getAbility();


}
