package club.oknick.enchantcore.common;

import lombok.Getter;
import org.bukkit.Material;

import java.util.Arrays;

/**
 * @author Nick
 */
@Getter
public enum ItemGroup {
    PICKAXE(Material.DIAMOND_PICKAXE, Material.GOLD_PICKAXE, Material.IRON_PICKAXE, Material.STONE_PICKAXE, Material.WOOD_PICKAXE),
    AXE(Material.DIAMOND_AXE, Material.GOLD_AXE, Material.IRON_AXE, Material.STONE_AXE, Material.WOOD_AXE),
    SHOVEL(Material.DIAMOND_SPADE, Material.GOLD_SPADE, Material.IRON_SPADE, Material.STONE_SPADE, Material.WOOD_SPADE),
    HOE(Material.DIAMOND_HOE, Material.GOLD_HOE, Material.IRON_HOE, Material.STONE_HOE, Material.WOOD_HOE),
    SWORD(Material.DIAMOND_SWORD, Material.GOLD_SWORD, Material.IRON_SWORD, Material.STONE_SWORD, Material.WOOD_SWORD),
    
    TOOLS(PICKAXE, AXE, SHOVEL, HOE);
    
    private final Material[] materials;
    
    ItemGroup(Material... materials) {
        this.materials = materials;
    }
    
    ItemGroup(ItemGroup... itemGroups) {
        this.materials = Arrays.stream(itemGroups)
                             .flatMap(group -> Arrays.stream(group.getMaterials()))
                             .toArray(Material[]::new);
    }
    
    public boolean includes(Material material) {
        for (Material material1 : materials) {
            if (material1 == material) {
                return true;
            }
        }
        return false;
    }
}