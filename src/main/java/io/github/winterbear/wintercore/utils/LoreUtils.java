package io.github.winterbear.wintercore.utils;

import com.google.common.base.Joiner;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import io.github.winterbear.wintercore.wonderhaul.equipment.Tier;
import io.github.winterbear.wintercore.wonderhaul.sockets.SocketType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by WinterBear on 20/05/2019.
 */
public class LoreUtils {

    private static final String WORD_SEPARATOR = "[\\s_]";

    public static String convertToTitleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        return Arrays
                .stream(text.split(WORD_SEPARATOR))
                .map(word -> word.isEmpty()
                        ? word
                        : Character.toTitleCase(word.charAt(0)) + word
                        .substring(1)
                        .toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public static void clearLore(ItemStack item, Player player){
        clearLore(item);
        player.updateInventory();
    }

    public static void clearLore(ItemStack item){
        setLore(item, null);
    }

    public static void addLoreLine(ItemStack item, String lore, Player player){
        addLoreLine(item, lore);
        player.updateInventory();
    }

    public static void addLore(ItemStack item, List<String> lore, Player player){
        lore.forEach(l -> addLoreLine(item, l));
        if(player != null) {
            player.updateInventory();
        }
    }

    public static void addLoreLine(ItemStack item, String lore){
        List<String> itemLore = getLore(item);
        itemLore.add(ChatUtils.format(lore));
        setLore(item, itemLore);
    }

    public static void addBlankLine(ItemStack item){
        List<String> itemLore = getLore(item);
        itemLore.add("");
        setLore(item, itemLore);
    }

    public static void addMultiLineLore(ItemStack item, String lore){
        List<String> itemLore = getLore(item);
        itemLore.addAll(getMultiline(lore, 26, 0));
        setLore(item, itemLore);
    }

    public static void addMultiLineLore(ItemStack item, net.md_5.bungee.api.ChatColor color, String lore){
        List<String> itemLore = getLore(item);
        List<String> split = getMultiline(lore, 26, 0);
        split.forEach(l -> itemLore.add(color + ChatUtils.uncolored(l)));
        setLore(item, itemLore);
    }

    public static List<String> getMultiline(String string, int lineLength, int indentCount){
        List<String> result = new ArrayList<>();
        String[] characters = string.split("");
        String color = "&7";
        String format = "";
        String indent = "";
        StringBuilder line = new StringBuilder();
        line.append(color);
        int maxLineLength = 0;
        for(int i = 0; i < indentCount; i++){
            indent = indent + " ";
        }
        for(int i = 0; i < characters.length; i++){
            String c = characters[i];
            if(c.equals("&") && i + 1 < characters.length){
                String modifier = characters[i + 1];
                if(ChatUtils.COLOR_CODES.contains(modifier)){
                    color = "&" + modifier;
                } else if (ChatUtils.FORMAT_CODES.contains(modifier)){
                    format = "&" + modifier;
                } else if (ChatUtils.RESET.equals(modifier)){
                    color = "&7";
                    format = "";
                }
            }
            line.append(c);
            if(line.length() > lineLength && c.equals(" ")){
                //Start new line
                String newLine = ChatUtils.format(line.toString());
                int currentLineLength = newLine.length();
                int remainingChars = characters.length - (i + 1);
                if((currentLineLength + remainingChars) > maxLineLength){
                    result.add(indent + newLine);
                    line = new StringBuilder();
                    line.append(color);
                    line.append(format);
                }
                if(currentLineLength > maxLineLength){
                    maxLineLength = currentLineLength;
                }
            }
        }
        result.add(indent + ChatUtils.format(line.toString()));

        return result;
    }


    public static void removeLoreLine(ItemStack item, int line, Player player){
        removeLoreLine(item, line);
        player.updateInventory();
    }

    public static void removeLoreLine(ItemStack item, int line){
        List<String> itemLore = getLore(item);
        itemLore.remove(line);
        setLore(item, itemLore);
    }

    public static void setLoreLine(ItemStack item, int line, String lore){
        List<String> itemLore = getLore(item);
        itemLore.set(line, lore);
        setLore(item, itemLore);
    }

    private static void setLore(ItemStack item, List<String> lore){
        ItemMeta meta = initialiseMeta(item);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private static ItemMeta initialiseMeta(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        return meta;
    }

    public static List<String> getLore(ItemStack item){
        if(item == null){
            return new ArrayList<>();
        }
        ItemMeta meta = initialiseMeta(item);
        if(meta == null){
            return Collections.EMPTY_LIST;
        }
        List<String> itemLore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        return itemLore;
    }

    public static String getItemsAsVerbal(List<Material> objects){
        if(objects.size() == 1){
            return WordUtils.capitalizeFully(objects.get(0).toString().replace("_", " "));
        }
        return getListAsVerbal(objects.stream()
                .map(o -> o.toString().replace("_", " "))
                .map(WordUtils::capitalizeFully)
                .collect(Collectors.toList()));
    }

    public static String getListAsVerbal(List<? extends Object> objects){
        return Joiner.on(", ")
                .join(objects.subList(0, objects.size() - 1))
                .concat(", and ")
                .concat(objects.get(objects.size() - 1).toString());

    }

    public static List<String> getLoreText(ItemStack item){
        return LoreUtils.getLore(item).stream()
                .filter(lore -> !ChatUtils.uncolored(lore).contains(":"))
                .filter(lore -> !StringUtils.isBlank(lore))
                .collect(Collectors.toList());
    }

    public static List<String> getTag(ItemStack item, String tagType) {
        return LoreUtils.getLore(item).stream()
                .filter(lore -> ChatUtils.uncolored(lore).contains(tagType + ":"))
                .map(line -> line.substring(line.indexOf(':') + 2))
                .map(type -> ChatUtils.uncolored(type.trim()))
                .collect(Collectors.toList());
    }

    public static String getType(ItemStack item) {
        return LoreUtils.getLore(item).stream()
                .filter(lore -> lore.contains("✦"))
                .map(line -> line.substring(line.indexOf(':') + 2))
                .map(type -> ChatUtils.uncolored(type.trim()))
                .findFirst().orElse("None");

    }

    public static Optional<Tier> getTier(ItemStack item) {
        String tier = LoreUtils.getLore(item).stream()
                .filter(lore -> lore.contains("✦"))
                .map(line -> line.substring(line.indexOf(':') + 2))
                .map(d -> d.split(" ")[0])
                .map(ChatUtils::uncolored)
                .findFirst().orElse(null);
        if(tier != null){
            try {
                return Optional.of(Tier.valueOf(tier.toUpperCase()));
            } catch (IllegalArgumentException ex){
                return Optional.empty();
            }
        }
        return Optional.empty();

    }

    public static void changeTier(ItemStack item, Tier newTier){
        ItemMeta meta = item.getItemMeta();
        List<String> lore = LoreUtils.getLore(item);
        for(String line : lore){
            if(line.contains("✦")){
                int index = lore.indexOf(line);
                lore.set(index, ChatColor.translateAlternateColorCodes('&' , newTier.getTierLore(item.getType())));
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void addEmptySocket(ItemStack item, SocketType socketType){
        ItemMeta meta = item.getItemMeta();
        List<String> lore = LoreUtils.getLore(item);
        int index = 0;
        for(String line : lore){
            if(line.contains("✦")){
                index = lore.indexOf(line) + 1;
            }
        }
        lore.add(index, socketType.getColor() + socketType.getSymbol() +
                ChatColor.translateAlternateColorCodes('&'," &7" + socketType.getName() + "&8: &7Empty Slot"));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }



    public static void truncateLoreLine(ItemStack item, int line, Player player){
        truncateLoreLine(item, line);
        player.updateInventory();
    }

    public static void truncateLoreLine(ItemStack item, int line){
        List<String> itemLore = getLore(item);
        while(itemLore.size() >= line){
            itemLore.remove(line);
        }
        setLore(item, itemLore);
    }


}
