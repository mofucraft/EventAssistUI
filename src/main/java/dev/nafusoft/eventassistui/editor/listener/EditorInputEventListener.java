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

package dev.nafusoft.eventassistui.editor.listener;

import dev.nafusoft.eventassistcore.utils.PluginLogger;
import dev.nafusoft.eventassistui.editor.CallbackAction;
import dev.nafusoft.eventassistui.editor.EventEditor;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class EditorInputEventListener implements Listener {
    private final Map<Player, EventEditor> waitingInputs = new HashMap<>();

    @EventHandler
    public void onAsyncChatEvent(AsyncChatEvent event) {
        var editor = waitingInputs.remove(event.getPlayer());
        if (editor != null) {
            event.setCancelled(true);

            var waitingAction = editor.getWaitingAction();
            if (waitingAction instanceof CallbackAction action) {
                try {
                    action.callback(LegacyComponentSerializer.legacyAmpersand().serialize(event.message()));
                } catch (Exception e) {
                    PluginLogger.log(
                            Level.WARNING,
                            "An error occurred while executing the editor action.",
                            e
                    );

                    event.getPlayer().sendMessage(ChatColor.RED + "[EventAssistUI] An error occurred while executing the editor action.");
                    event.getPlayer().sendMessage(ChatColor.RED + e.getStackTrace()[0].toString());
                }
            }

            PluginLogger.log(Level.INFO, "[Debug] {0}", editor.getBuilder());
        }
    }

    public void registerWaitingAction(@NotNull Player player, @NotNull EventEditor editor) {
        waitingInputs.put(player, editor);
    }
}
