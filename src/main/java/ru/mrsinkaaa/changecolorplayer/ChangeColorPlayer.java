package ru.mrsinkaaa.changecolorplayer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public final class ChangeColorPlayer extends JavaPlugin {

    private static ChangeColorPlayer INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        Objects.requireNonNull(this.getCommand("setcolor")).setExecutor(new ColorChangeCommand());
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        mainScoreboard.getTeams().forEach(Team::unregister);
    }


    public FileConfiguration getMainConfig() {
        return this.getConfig();
    }

    public static ChangeColorPlayer getInstance() {
        return INSTANCE;
    }
}
