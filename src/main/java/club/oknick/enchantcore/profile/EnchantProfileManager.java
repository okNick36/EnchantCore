package club.oknick.enchantcore.profile;

import club.oknick.enchantcore.EnchantCore;
import com.google.common.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Nick
 */
public final class EnchantProfileManager implements Listener {
    private final Map<UUID, EnchantProfile> profiles = new HashMap<>();
    
    public EnchantProfileManager() {
        File profilesDir = new File(EnchantCore.getINSTANCE().getDataFolder(), "profiles");
        if (!profilesDir.exists()) {
            profilesDir.mkdirs();
        }
        Bukkit.getServer().getPluginManager().registerEvents(this, EnchantCore.getINSTANCE());
        Bukkit.getScheduler().runTaskTimerAsynchronously(EnchantCore.getINSTANCE(), () -> profiles.entrySet().removeIf(entry -> {
            EnchantProfile profile = entry.getValue();
            if (!profile.isDirty()) {
                return false;
            }
            try (FileWriter writer = new FileWriter(profile.getFile())) {
                writer.write(EnchantCore.getGSON().toJson(profile));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            profile.setDirty(false);
            return Bukkit.getPlayer(profile.getUuid()) == null;
        }), 20 * 60, 20 * 60);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onJoin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        if (profiles.containsKey(player.getUniqueId())) {
            return;
        }
        EnchantProfile profile;
        File file = new File(new File(EnchantCore.getINSTANCE().getDataFolder(), "profiles"), player.getUniqueId().toString() + ".json");
        if (!file.exists()) {
            file.createNewFile();
            profile = new EnchantProfile(player.getUniqueId());
            profile.setDirty(true);
        } else {
            profile = EnchantCore.getGSON().fromJson(Files.readAllLines(file.toPath()).get(0), new TypeToken<EnchantProfile>() {}.getType());
        }
        profile.setFile(file);
        profiles.put(player.getUniqueId(), profile);
    }
    
    public EnchantProfile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }
}