package club.oknick.enchantcore.enchant.impl;

import club.oknick.enchantcore.enchant.Enchant;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Nick
 */
public final class TestEnchant extends Enchant {
    public TestEnchant() {
        super("Test", Material.DIRT, 7);
    }
    
    @Override
    public String[] getDescription() {
        return new String[] {
            "test 1",
            "yes"
        };
    }
    
    @Override
    public void activate(Player player, Block block, long level) {
        player.sendMessage("the test enchant gods have spoken");
    }
    
    @Override
    public boolean canUse(ItemStack itemStack) {
        return itemStack.getType() == Material.DIAMOND_PICKAXE;
    }
    
    @Override
    public double getLevelUpCost(long currentLevel) {
        return 0;
    }
    
    @Override
    public long getBlocksBrokenUntilActivation(long level) {
        return 3;
    }
}