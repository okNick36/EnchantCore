package club.oknick.enchantcore.enchant.menu;

import club.oknick.enchantcore.common.ItemGroup;
import club.oknick.enchantcore.enchant.Enchant;
import club.oknick.enchantcore.enchant.EnchantManager;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nick
 */
@AllArgsConstructor
public final class EnchantMenu implements Listener {
    private final EnchantManager enchantManager;
    
    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null ||
                !ItemGroup.TOOLS.includes(event.getItem().getType()) ||
                (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        openMenu(event.getPlayer());
    }
    
    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        InventoryAction action = event.getAction();
        // Validate the click
        if (event.getSlot() == -999 || action == InventoryAction.NOTHING || clickedInventory == null) {
            return;
        }
        if (!clickedInventory.getTitle().equals("Enchant Menu")) {
            return;
        }
        // Disallow shift-clicking items from the player inventory into the menu
        if ((event.isShiftClick() || action == InventoryAction.COLLECT_TO_CURSOR) && clickedInventory.getType() == InventoryType.PLAYER) {
            event.setCancelled(true); // Prevent shift-clicking items from the player inventory to the opened menu
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        player.updateInventory();
        ItemStack item = event.getCurrentItem();
        for (Enchant enchant : enchantManager.getEnchants()) {
            if (!item.getItemMeta().getDisplayName().equals("§b§l" + enchant.getName())) {
                continue;
            }
            player.sendMessage("enchant: " + enchant.getName());
        }
    }
    
    @EventHandler
    private void onClose(InventoryCloseEvent event) {
        if (!event.getInventory().getTitle().equals("Enchant Menu")) {
            return;
        }
        Player player = (Player) event.getPlayer();
        player.updateInventory();
    }
    
    private void openMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9 * 6, "Enchant Menu");
        ItemStack diamondItem = new ItemStack(Material.DIAMOND);
        ItemMeta diamondMeta = diamondItem.getItemMeta();
        diamondMeta.setDisplayName("§b0 Diamonds");
        diamondMeta.setLore(Arrays.asList(
            "§7",
            "§7Diamonds are used to level up your enchants.",
            "§7You can earn Diamonds by enchant rewards.",
            "§7",
            "§bYou have 0 Diamonds"));
        diamondItem.setItemMeta(diamondMeta);
        inventory.setItem(13, diamondItem);
        
        int slot = 28;
        for (Enchant enchant : enchantManager.getEnchants()) {
            ItemStack item = new ItemStack(enchant.getIcon());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§b§l" + enchant.getName());
            List<String> lore = new ArrayList<>();
            lore.add("§7");
            for (String line : enchant.getDescription()) {
                lore.add("§7" + line);
            }
            Long level = enchantManager.getProfileManager().getProfile(player.getUniqueId()).getEnchants().getOrDefault(enchant, 1L);
            lore.addAll(Arrays.asList(
                "§7",
                "§7Level: §b" + level + "§7/§b" + enchant.getMaxLevel(),
                "§7Cost: §b" + Math.round(enchant.getLevelUpCost(level)) + " Diamonds",
                "§7",
                "§bClick to level up!"));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(slot++, item);
        }
        player.openInventory(inventory);
    }
}