package io.github.winterbear.wintercore.commands;

import io.github.winterbear.WinterCoreUtils.ChatUtils;
import io.github.winterbear.WinterCoreUtils.CommandRegistry;
import io.github.winterbear.WinterCoreUtils.CommandSenderUtils;
import io.github.winterbear.WinterCoreUtils.CommandWrapper;
import io.github.winterbear.wintercore.Annotations.Command;
import io.github.winterbear.wintercore.utils.LoreUtils;
import io.github.winterbear.wintercore.utils.MicroblockUtils;
import io.github.winterbear.wintercore.wonderhaul.equipment.generators.Generators;
import io.github.winterbear.wintercore.wonderhaul.equipment.gobblers.GobblerGenerator;
import io.github.winterbear.wintercore.wonderhaul.packs.PackGenerator;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * Created by WinterBear on 20/05/2019.
 */
public class DevTools {

    @Command(permission = "dev.sethealth")
    public static CommandWrapper setHealth(){
        return CommandRegistry.createCommand("sethealth", (sender, command, alias, args) -> {
            int health = Integer.parseInt(args[0]);
            if(args.length > 1){
                Player player = sender.getServer().getPlayer(args[1]);
                if(player != null){
                    player.setHealth(health);
                    ChatUtils.send(sender, "&9Notice&8: &7Set " + args[1] + "'s health to " + health);
                } else {
                    ChatUtils.send(sender, "&cError&8: &7No player " + args[1] + "found");
                }
                return;
            }
            Optional<Player> player = CommandSenderUtils.getPlayer(sender);
            if(player.isPresent()){
                player.get().setHealth(health);
                ChatUtils.send(sender, "&9Notice&8: &7Set your health to " + health);
            } else {
                ChatUtils.send(sender, "cError&8: &7You are not a player and no target player was specified.");
            }
        });
    }

    @Command(permission = "dev.sethunger")
    public static CommandWrapper setHunger(){
        return CommandRegistry.createPlayerCommand("sethunger", (sender, command, alias, args) -> {
            int hunger = Integer.parseInt(args[0]);
            if(args.length > 1){
                Player player = sender.getServer().getPlayer(args[1]);
                if(player != null){
                    player.setFoodLevel(hunger);
                    ChatUtils.send(sender, "&9Notice&8: &7Set " + args[1] + "'s hunger to " + hunger);
                } else {
                    ChatUtils.send(sender, "&cError&8: &7Player does not exist.");
                }
            } else {
                sender.setFoodLevel(hunger);
                ChatUtils.send(sender, "&9Notice&8: &7Set your hunger to " + hunger);
            }
        });
    }

    @Command(permission = "dev.addlore")
    public static CommandWrapper addLore(){
        return CommandRegistry.createPlayerCommand("addlore", (player, command, label, args) -> {
            if(args.length > 0) {
                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                if(mainHandItem.getType().equals(Material.AIR)){
                    ChatUtils.send(player, "&cError&8: &7You must be holding an item to do that.");
                    return;
                }
                LoreUtils.addLoreLine(mainHandItem, StringUtils.join(args, ' '), player);
                ChatUtils.send(player, "Added line " + StringUtils.join(args, ' ') + " to your tool.");
            } else {
                ChatUtils.error(player, "Must specify a line of lore to add!");
            }
        });
    }

    @Command(permission = "dev.clearLore")
    public static CommandWrapper clearLore(){
        return CommandRegistry.createPlayerCommand("clearLore", (player, command, label, args) -> {
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            if(mainHandItem.getType().equals(Material.AIR)){
                ChatUtils.send(player, "&cError&8: &7You must be holding an item to do that.");
                return;
            }
            LoreUtils.clearLore(mainHandItem, player);
            ChatUtils.send(player, "Cleared the lore of your tool.");
        });
    }

    @Command(permission = "dev.removelore")
    public static CommandWrapper removeLore(){
        return CommandRegistry.createPlayerCommand("removelore", (player, command, label, args) -> {
            if(args.length > 0) {
                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                if(mainHandItem.getType().equals(Material.AIR)){
                    ChatUtils.send(player, "&cError&8: &7You must be holding an item to do that.");
                    return;
                }
                if(args[0].contains("+")){
                    int line = Integer.parseInt(args[0].replace("+", "")) - 1;
                    if(mainHandItem.getItemMeta() != null &&
                            mainHandItem.getItemMeta().getLore() != null
                            && mainHandItem.getItemMeta().getLore().size() < (line - 1)){
                        ChatUtils.send(player, "Lore line " + line + " does not exist");
                        return;
                    }
                    LoreUtils.truncateLoreLine(mainHandItem, line, player);
                } else {
                    int line = Integer.parseInt(args[0]) - 1;
                    if(mainHandItem.getItemMeta() != null &&
                            mainHandItem.getItemMeta().getLore() != null
                            && mainHandItem.getItemMeta().getLore().size() < (line - 1)){
                        ChatUtils.send(player, "Lore line " + line + " does not exist");
                        return;
                    }
                    LoreUtils.removeLoreLine(mainHandItem, line, player);
                }
                ChatUtils.send(player, "Removed the lore of your tool.");
            }
            else {
                ChatUtils.error(player, "Must specify a line to remove!");
            }

        });
    }

    @Command(permission = "dev.getTextureData")
    public static CommandWrapper getTextureData(){
        return CommandRegistry.createPlayerCommand("getTextureData", (player, command, label, args) -> {
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            if(!mainHandItem.getType().equals(Material.PLAYER_HEAD)) {
                ChatUtils.send(player, "&cError&8: &7You must be holding a head to do that.");
                return;
            }

            String textureUrl = MicroblockUtils.getTextureUrl(mainHandItem);

            ChatUtils.send(player, textureUrl);
            ChatUtils.info(textureUrl);
        });
    }

    @Command(permission = "dev.getRelic")
    public static CommandWrapper getRelic(){
        return CommandRegistry.createPlayerCommand("getRelic", (player, command, label, args)
                -> player.getInventory().addItem(Generators.RELIC.create()));
    }

    @Command(permission = "dev.getEssenceCollector")
    public static CommandWrapper getEssenceCollector(){
        return CommandRegistry.createPlayerCommand("getEssenceCollector", (player, command, label, args)
                -> player.getInventory().addItem(Generators.ESSENCE_COLLECTOR.create()));
    }

    @Command(permission = "dev.getCrate1")
    public static CommandWrapper getCrate1(){
        return CommandRegistry.createPlayerCommand("getCrate1", (player, command, label, args)
                -> player.getInventory().addItem(Generators.CRATE_I.create()));
    }

    @Command(permission = "dev.getCrate2")
    public static CommandWrapper getCrate2(){
        return CommandRegistry.createPlayerCommand("getCrate2", (player, command, label, args)
                -> player.getInventory().addItem(Generators.CRATE_II.create()));
    }

    @Command(permission = "dev.getGobbler")
    public static CommandWrapper getGobbler(){
        return CommandRegistry.createPlayerCommand("getGobbler", (player, command, label, args)
                -> player.getInventory().addItem(new GobblerGenerator().create()));
    }

    @Command(permission = "dev.getExperiencePack")
    public static CommandWrapper getExperiencePack(){
        return CommandRegistry.createPlayerCommand("getExperiencePack", (player, command, label, args)
                -> player.getInventory().addItem(PackGenerator.Packs.LARGE_EXP_HAUL.generate()));
    }

    @Command(permission = "dev.getGoldHaul")
    public static CommandWrapper getGoldHaul(){
        return CommandRegistry.createPlayerCommand("getGoldHaul", (player, command, label, args)
                -> player.getInventory().addItem(PackGenerator.Packs.LARGE_GOLD_HAUL.generate()));
    }

}
