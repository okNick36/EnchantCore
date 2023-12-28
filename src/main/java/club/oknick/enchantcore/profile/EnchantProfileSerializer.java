package club.oknick.enchantcore.profile;

import club.oknick.enchantcore.EnchantCore;
import club.oknick.enchantcore.enchant.Enchant;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

/**
 * @author Nick
 */
public final class EnchantProfileSerializer implements JsonSerializer<EnchantProfile>, JsonDeserializer<EnchantProfile> {
    @Override
    public JsonElement serialize(EnchantProfile enchantProfile, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", enchantProfile.getUuid().toString());
        JsonArray enchantsArray = new JsonArray();
        for (Map.Entry<Enchant, Long> entry : enchantProfile.getEnchants().entrySet()) {
            JsonObject enchantObject = new JsonObject();
            enchantObject.addProperty("name", entry.getKey().getName());
            enchantObject.addProperty("level", entry.getValue());
            enchantsArray.add(enchantObject);
        }
        jsonObject.add("enchants", enchantsArray);
        jsonObject.addProperty("blocksBroken", enchantProfile.getBlocksBroken());
        return jsonObject;
    }
    
    @Override
    public EnchantProfile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
        EnchantProfile profile = new EnchantProfile(uuid);
        JsonArray enchantsArray = jsonObject.getAsJsonArray("enchants");
        for (JsonElement enchantElement : enchantsArray) {
            JsonObject enchantObject = enchantElement.getAsJsonObject();
            String name = enchantObject.get("name").getAsString();
            long level = enchantObject.get("level").getAsLong();
            Enchant enchant = EnchantCore.getINSTANCE().getEnchantManager().getEnchant(name);
            profile.getEnchants().put(enchant, level);
        }
        profile.setBlocksBroken(jsonObject.get("blocksBroken").getAsLong());
        return profile;
    }
}