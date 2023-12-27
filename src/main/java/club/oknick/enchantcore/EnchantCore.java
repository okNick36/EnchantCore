package club.oknick.enchantcore;

import club.oknick.enchantcore.enchant.EnchantManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Nick
 */
public final class EnchantCore extends JavaPlugin {
    @Getter
    private static EnchantCore INSTANCE;
    
    @Override
    public void onEnable() {
        INSTANCE = this;
        new EnchantManager(this);
    }
}