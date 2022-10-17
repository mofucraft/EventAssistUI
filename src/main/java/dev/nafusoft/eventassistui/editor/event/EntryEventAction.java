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

package dev.nafusoft.eventassistui.editor.event;

import dev.nafusoft.eventassistcore.api.EventAssistAPI;
import dev.nafusoft.eventassistui.editor.BaseEventEditorAction;
import dev.nafusoft.eventassistui.editor.EventEditor;
import lombok.val;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class EntryEventAction extends BaseEventEditorAction {

    public EntryEventAction(EventEditor editor) {
        super(editor);
    }

    @Override
    public void execute(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!event.getCurrentItem().getItemMeta().lore().isEmpty()) {
                val eventId = event.getCurrentItem().getItemMeta().lore().stream()
                        .map(lore -> PlainTextComponentSerializer.plainText().serialize(lore))
                        .filter(this::checkUUID)
                        .map(UUID::fromString)
                        .findFirst().orElse(null);
                val gameEvent = EventAssistAPI.getInstance().getEvent(eventId);

                if (gameEvent == null) {
                    player.sendMessage(ChatColor.RED + "[EventAssist] Event not found.");
                } else if (gameEvent.entryEvent(player)) {
                    player.sendMessage(ChatColor.GREEN + "[EventAssist] You have entered the event!");
                } else {
                    player.sendMessage(ChatColor.RED + "[EventAssist] Failed to enter the event...");
                }
            }
        }
    }

    private boolean checkUUID(String uuid) {
        return uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }
}
