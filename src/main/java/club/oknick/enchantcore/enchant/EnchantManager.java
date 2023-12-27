package club.oknick.enchantcore.enchant;

import club.oknick.enchantcore.EnchantCore;
import club.oknick.enchantcore.enchant.impl.TestEnchant;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Nick
 */
public final class EnchantManager implements Listener {
    private final List<Enchant> enchants = new ArrayList<>();
    private final Map<UUID, Long> blocksBroken = new HashMap<>();

    public EnchantManager(EnchantCore plugin) {
        enchants.add(new TestEnchant());
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(pickaxe);
        NBTTagCompound tag = nmsCopy.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setLong("Test", 1L);
        nmsCopy.setTag(tag);
        player.setItemInHand(CraftItemStack.asBukkitCopy(nmsCopy));
    }
    
    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        long blocksBroken = this.blocksBroken.computeIfAbsent(player.getUniqueId(), uuid -> 0L) + 1;
        ItemStack item = player.getItemInHand();
        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsCopy.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        for (Enchant enchant : enchants) {
            if (!enchant.canUse(item)) {
                continue;
            }
            long level = tag.getLong(enchant.getName());
            if (blocksBroken % enchant.getBlocksBrokenUntilActivation(level) == 0) {
                enchant.activate(player, event.getBlock(), level);
            }
        }
        player.setItemInHand(CraftItemStack.asBukkitCopy(nmsCopy));
        this.blocksBroken.put(player.getUniqueId(), blocksBroken);
    }
}