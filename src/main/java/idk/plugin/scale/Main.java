package idk.plugin.scale;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class Main extends PluginBase {

    Config config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("scale")) return true;
        if (!(sender instanceof Player) || args.length == 0) return false;
        float scale = 1;
        try {
            scale = Float.parseFloat(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("\u00A7cScale value must be a float");
            return true;
        }
        if (scale > config.getDouble("maxScale", 40)) {
            sender.sendMessage("\u00A7cScale can't be over " + config.getDouble("maxScale", 40));
            return true;
        }
        if (scale < config.getDouble("minScale", 0)) {
            sender.sendMessage("\u00A7cScale can't be less than " + config.getDouble("minScale", 0));
            return true;
        }
        ((Player) sender).setScale(scale);
        sender.sendMessage("\u00A7aScale changed to " + scale);
        return true;
    }
}
