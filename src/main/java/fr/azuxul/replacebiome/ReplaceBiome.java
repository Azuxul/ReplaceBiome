/**
 * Copyright (c) 2015-2015 Azuxul. All rights reserved.
 */

package fr.azuxul.replacebiome;

import net.minecraft.server.v1_8_R3.BiomeBase;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class of ReplaceBiome plugin
 *
 * @author Azuxul
 * @version 1.0
 */
public class ReplaceBiome extends JavaPlugin {

    private Map<Integer, BiomeBase> replacedBiomes = new HashMap<>();

    @Override
    public void onEnable() {

        saveDefaultConfig(); // Set default config

        for(String replaceBiome : getConfig().getStringList("replaceBiomes")) {

            int biomeID = Integer.parseInt(replaceBiome.substring(0, replaceBiome.lastIndexOf("->"))); // Get biome id
            String replacingBiomeName = replaceBiome.substring(replaceBiome.lastIndexOf("->") + 2); // Get biome name
            BiomeBase replacingBiome = getBiomeBaseWithName(replacingBiomeName); // Get biome with biome name

            if(replacingBiome == null) { // If biome is equals to null
                getLogger().warning(replacingBiomeName + " has not found !");
            } else if (replacedBiomes.containsKey(biomeID)) { // If biome has already replaced
                getLogger().warning("Biome " + biomeID + " has already replaced !");
            } else if(BiomeBase.getBiomes().length < biomeID || BiomeBase.getBiomes()[biomeID] == null) { // If biomeID > biomes size or biomeID biome is equals to null
                getLogger().warning("Biome " + biomeID + " does not exist");
            } else {

                replacedBiomes.put(biomeID, BiomeBase.getBiomes()[biomeID]); // Put biome id and biome of this biome in map
                BiomeBase.getBiomes()[biomeID] = replacingBiome; // Replace biome
                getLogger().info("Biome " + biomeID + " was replaced with " + replacingBiomeName + " biome");
            }
        }
    }

    @Override
    public void onDisable() {

        if(!replacedBiomes.isEmpty()) // If replaced biomes map is not empty
            for (Map.Entry<Integer, BiomeBase> replacedBiome : replacedBiomes.entrySet())
                BiomeBase.getBiomes()[replacedBiome.getKey()] = replacedBiome.getValue(); // Set id of biome in list to default biome
    }

    public BiomeBase getBiomeBaseWithName(String biomeName) {

        BiomeBase biomeBase;

        if(biomeName == null) {
            throw new NullPointerException("biomeName can't be null");
        } else {

            biomeName = biomeName.toUpperCase(); // Set biomeName to upper case

            try {

                Class biomeBaseClass = BiomeBase.class; // Get BiomeBase class

                Field field = biomeBaseClass.getDeclaredField(biomeName); // Get field of biome name

                biomeBase = (BiomeBase) field.get(null); // Get biomeBase of field

            } catch (NoSuchFieldException e) {

                return null;
            } catch (IllegalAccessException e) {

                return null;
            }
        }

        return biomeBase;
    }

}
