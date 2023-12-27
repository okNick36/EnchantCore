package club.oknick.enchantcore.enchant;

import club.oknick.enchantcore.EnchantCore;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * @author Nick
 */
@Getter
public abstract class Enchant implements Listener {
    private final String name;
    private final Material icon;
    private final int maxLevel;

    public Enchant(String name, Material icon, int maxLevel) {
        this.name = name;
        this.icon = icon;
        this.maxLevel = maxLevel;
        Bukkit.getServer().getPluginManager().registerEvents(this, EnchantCore.getINSTANCE());
    }
    
    public abstract String[] getDescription();

    public abstract void activate(Player player, Block block, long level);

    public abstract boolean canUse(ItemStack itemStack);
    
    public abstract double getLevelUpCost(long currentLevel);

    public abstract long getBlocksBrokenUntilActivation(long level);
}