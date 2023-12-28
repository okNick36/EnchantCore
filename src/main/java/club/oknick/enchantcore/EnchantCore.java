package club.oknick.enchantcore;

import club.oknick.enchantcore.enchant.EnchantManager;
import club.oknick.enchantcore.profile.EnchantProfile;
import club.oknick.enchantcore.profile.EnchantProfileManager;
import club.oknick.enchantcore.profile.EnchantProfileSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Nick
 */
@Getter
public final class EnchantCore extends JavaPlugin {
    @Getter
    private static final Gson GSON = new GsonBuilder()
                                         .serializeNulls()
                                         .registerTypeAdapter(EnchantProfile.class, new EnchantProfileSerializer())
                                         .create();
    
    @Getter
    private static EnchantCore INSTANCE;
    
    private EnchantManager enchantManager;
    
    @Override
    public void onEnable() {
        INSTANCE = this;
        EnchantProfileManager enchantProfileManager = new EnchantProfileManager();
        enchantManager = new EnchantManager(this, enchantProfileManager);
    }
}