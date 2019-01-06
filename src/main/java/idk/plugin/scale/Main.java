package idk.plugin.scale;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.util.ArrayList;
import java.util.List;

public class Main extends PluginBase implements Listener {

    Config config;
    List<String> interactMode = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("scale")) {
                if (args.length == 0) {
                    return false;
                }

                float scale = 1;
                float eid = 0;

                try {
                    scale = Float.parseFloat(args[0]);
                } catch (NumberFormatException e) {
                    p.sendMessage("\u00A7cScale value must be a float");
                    return true;
                }

                if (scale > config.getDouble("maxScale", 40.0)) {
                    p.sendMessage("\u00A7cScale can't be over " + config.getDouble("maxScale", 40.0));
                    return true;
                }

                if (scale < config.getDouble("minScale", 0.1)) {
                    p.sendMessage("\u00A7cScale can't be less than " + config.getDouble("minScale", 0.1));
                    return true;
                }

                if (args.length == 2) {
                    Player pl = getServer().getPlayer(args[1]);

                    if (pl != null) {
                        pl.setScale(scale);
                        p.sendMessage("\u00A7aPlayer's scale changed to " + scale);
                        return true;
                    } else {
                        try {
                            eid = Float.parseFloat(args[1]);
                        } catch (NumberFormatException e) {
                            p.sendMessage("\u00A7cEntity id must be a float");
                            return true;
                        }

                        for (Level l : getServer().getLevels().values()) {
                            for (Entity e : l.getEntities()) {
                                if (e.getId() == eid) {
                                    e.setScale(scale);
                                    p.sendMessage("\u00A7aEntity's scale changed to " + scale);
                                    return true;
                                }
                            }
                        }
                    }

                    p.sendMessage("\u00A7cEntity not found");
                    return true;
                } else {
                    p.setScale(scale);
                }

                p.sendMessage("\u00A7aYour scale changed to " + scale);
                return true;
            }

            if (cmd.getName().equalsIgnoreCase("geteid")) {
                interactMode.add(p.getName());
                p.sendMessage("\u00A7aNow tap any entity to get it's id");
            }
        }

        return true;
    }

    @EventHandler
    public void interact(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (interactMode.contains(p.getName())) {
            p.sendMessage("\u00A7aThis entity's id is \u00A76" + e.getEntity().getId() + " \u00A7aand scale is \u00A76" + e.getEntity().getScale());
            interactMode.remove(p.getName());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (interactMode.contains(p.getName())) {
            interactMode.remove(p.getName());
        }
    }
}
