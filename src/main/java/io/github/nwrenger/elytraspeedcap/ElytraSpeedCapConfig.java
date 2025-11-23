package io.github.nwrenger.elytraspeedcap;

import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;

public class ElytraSpeedCapConfig {

    public static final double TICKS_PER_SECOND = 20.0;

    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(),
            "elytra-speed-cap.json");

    public double max_speed = 60.0;

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_FILE.toPath())) {
            new GsonBuilder().setPrettyPrinting().create().toJson(this, writer);
        } catch (IOException e) {
            ElytraSpeedCap.LOGGER.error("[Elytra Speed Cap] IO error while trying to save config file", e);
        }
    }

    public static ElytraSpeedCapConfig load() {
        if (!CONFIG_FILE.exists()) {
            ElytraSpeedCapConfig newConfig = new ElytraSpeedCapConfig();
            newConfig.save();
            return newConfig;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_FILE.toPath())) {
            return new GsonBuilder().setPrettyPrinting().create().fromJson(reader, ElytraSpeedCapConfig.class);
        } catch (IOException e) {
            ElytraSpeedCap.LOGGER.error("[Elytra Speed Cap] Unable to read config file, using default config", e);
            return new ElytraSpeedCapConfig();
        }
    }

    public static double intoMaxSpeedPerTick(double max_speed) {
        return max_speed / TICKS_PER_SECOND;
    }
}
