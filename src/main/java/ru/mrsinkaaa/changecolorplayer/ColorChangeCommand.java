package ru.mrsinkaaa.changecolorplayer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class ColorChangeCommand implements CommandExecutor {

    private final ChangeColorPlayer plugin = ChangeColorPlayer.getInstance();
    private final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equals("help")) {
                    sendMessage(player, plugin.getMainConfig().getString("color-change-usage"), NamedTextColor.RED);
                    return true;
                }
                String colorCode = args[0];
                changePlayerNameColor(player, colorCode);
            } else if (args.length == 0) {
                resetPlayerNameColor(player);
            }

        } else {
            plugin.getLogger().warning(plugin.getMainConfig().getString("command-from-console"));

        }
        return true;
    }

    private void resetPlayerNameColor(Player player) {
        Component white = Component.text(player.getName(), NamedTextColor.WHITE);

        player.displayName(white);
        player.playerListName(white);

        Scoreboard board = scoreboardManager.getMainScoreboard();

        Team oldTeam = board.getPlayerTeam(player);
        if (oldTeam != null) {
            oldTeam.removeEntity(player);
            if (oldTeam.getEntries().isEmpty()) {
                oldTeam.unregister();
            }
        }

        sendMessage(player, plugin.getMainConfig().getString("color-change-success"), NamedTextColor.GREEN);
    }

    private void changePlayerNameColor(Player player, String colorCode) {
        NamedTextColor color = NamedTextColor.NAMES.value(colorCode);

        if (color != null) {
            Component coloredName = Component.text(player.getName(), color);
            player.displayName(coloredName);
            player.playerListName(coloredName);
            setTeamColor(player, color);

            sendMessage(player, plugin.getMainConfig().getString("color-change-success"), NamedTextColor.GREEN);
        } else {
            sendMessage(player, plugin.getMainConfig().getString("color-change-error"), NamedTextColor.RED);
        }
    }

    private void setTeamColor(Player player, NamedTextColor color) {
        Scoreboard board = scoreboardManager.getMainScoreboard();

        Team oldTeam = board.getPlayerTeam(player);
        Team team = board.getTeam(color.toString());

        if (team == null) {
            team = board.registerNewTeam(color.toString());
        }
        team.color(color);
        team.addEntity(player);

        if (oldTeam != null) {
            if (oldTeam.getEntries().isEmpty()) {
                oldTeam.unregister();
            }
        }

    }

    private void sendMessage(Player p, String message, NamedTextColor color) {
        p.sendMessage(Component.text(message, color));
    }
}
