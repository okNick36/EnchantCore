package club.oknick.enchantcore.enchant;

import club.oknick.enchantcore.EnchantCore;
import club.oknick.enchantcore.enchant.impl.TestEnchant;
import club.oknick.enchantcore.profile.EnchantProfile;
import club.oknick.enchantcore.profile.EnchantProfileManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick
 */
public final class EnchantManager implements Listener {
    private final List<Enchant> enchants = new ArrayList<>();
    
    private final EnchantProfileManager profileManager;

    public EnchantManager(EnchantCore plugin, EnchantProfileManager profileManager) {
        enchants.add(new TestEnchant());
        this.profileManager = profileManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        player.setItemInHand(new ItemStack(Material.DIAMOND_PICKAXE));
    }
    
    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        EnchantProfile profile = profileManager.getProfile(player.getUniqueId());
        long blocksBroken = profile.getBlocksBroken() + 1;
        profile.setBlocksBroken(blocksBroken);
        ItemStack item = player.getItemInHand();
        for (Enchant enchant : enchants) {
            if (!enchant.canUse(item)) {
                continue;
            }
            long level = profile.getEnchants().getOrDefault(enchant, 1L);
            if (blocksBroken % enchant.getBlocksBrokenUntilActivation(level) == 0 && blocksBroken != 1) {
                enchant.activate(player, event.getBlock(), level);
            }
        }
        profile.setDirty(true);
    }
    
    public Enchant getEnchant(String name) {
        for (Enchant enchant : enchants) {
            if (enchant.getName().equalsIgnoreCase(name)) {
                return enchant;
            }
        }
        return null;
    }
}