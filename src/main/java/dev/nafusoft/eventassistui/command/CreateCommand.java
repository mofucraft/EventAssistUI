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

package dev.nafusoft.eventassistui.command;

import dev.nafusoft.eventassistcore.gameevent.GameEventBuilder;
import dev.nafusoft.eventassistui.editor.EditorMenuHolder;
import dev.nafusoft.eventassistui.editor.EventEditor;
import dev.nafusoft.eventassistui.utils.EditorMenuGenerator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateCommand implements SubCommandExecutor {
    private final Map<Player, GameEventBuilder> workspace = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {

            } else switch (args[0]) {
                case "event" -> {
                    EventEditor editor = new EventEditor(player);
                    EditorMenuHolder holder = new EditorMenuHolder(editor);
                    player.openInventory(EditorMenuGenerator.getMainMenu(holder));
                }
                case "automation" -> {
                    EventEditor editor = new EventEditor(player);
                    EditorMenuHolder holder = new EditorMenuHolder(editor);
                    player.openInventory(EditorMenuGenerator.getEditEventAutomationMenu(holder));
                }
                default -> {
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be executed in-game.");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Arrays.asList("event", "automation");
    }
}
