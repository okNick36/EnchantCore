package club.oknick.enchantcore.profile;

import club.oknick.enchantcore.enchant.Enchant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Nick
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public final class EnchantProfile {
    private final UUID uuid;
    private File file;
    private final Map<Enchant, Long> enchants = new HashMap<>();
    private long blocksBroken;
    private boolean dirty;
}