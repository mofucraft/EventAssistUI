/*
 * Copyright 2022 NAFU_at
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.nafusoft.eventassistui;

import dev.nafusoft.eventassistui.command.CreateCommand;
import dev.nafusoft.eventassistui.command.EventCommand;
import dev.nafusoft.eventassistui.command.HelpCommand;
import dev.nafusoft.eventassistui.command.SubCommandExecutor;
import dev.nafusoft.eventassistui.editor.NoActionMenuHolder;
import dev.nafusoft.eventassistui.editor.actions.ActionOptionSetterRegistry;
import dev.nafusoft.eventassistui.editor.listener.EditorClickEventListener;
import dev.nafusoft.eventassistui.editor.listener.EditorInputEventListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EventAssistUI extends JavaPlugin implements Listener {
    private static EventAssistUI instance;
    private EditorInputEventListener inputListener;
    private Map<String, SubCommandExecutor> subCommands;

    private ActionOptionSetterRegistry optionSetterRegistry;

    public static EventAssistUI getInstance() {
        if (instance == null)
            instance = (EventAssistUI) Bukkit.getServer().getPluginManager().getPlugin("EventAssistUI");
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        inputListener = new EditorInputEventListener();
        getServer().getPluginManager().registerEvents(new EditorClickEventListener(), this);
        getServer().getPluginManager().registerEvents(inputListener, this);
        getServer().getPluginManager().registerEvents(this, this);

        optionSetterRegistry = new ActionOptionSetterRegistry();

        // Command Initialization
        subCommands = new HashMap<>();
        subCommands.put("register", new CreateCommand());
        subCommands.put("event", new EventCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private static final SubCommandExecutor HELP_COMMAND_EXECUTOR = new HelpCommand();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SubCommandExecutor executor = getSubCommand(args);

        if (executor != null)
            return executor.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
        else
            return HELP_COMMAND_EXECUTOR.onCommand(sender, command, label, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        SubCommandExecutor executor = getSubCommand(args);
        if (args.length == 1)
            return subCommands.keySet().stream().toList();
        else if (executor != null)
            return executor.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
        return null;
    }

    private SubCommandExecutor getSubCommand(@NotNull String[] args) {
        SubCommandExecutor executor = null;

        if (args.length != 0)
            executor = subCommands.get(args[0]);
        return executor;
    }


    @EventHandler(ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() != null)
            if (event.getClickedInventory().getHolder() instanceof NoActionMenuHolder)
                event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryDragEvent(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof NoActionMenuHolder)
            event.setCancelled(true);
    }

    public EditorInputEventListener getInputListener() {
        return inputListener;
    }

    public ActionOptionSetterRegistry getOptionSetterRegistry() {
        return optionSetterRegistry;
    }
}
