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

import dev.nafusoft.eventassistui.EventAssistUI;
import dev.nafusoft.eventassistui.editor.BaseEventEditorAction;
import dev.nafusoft.eventassistui.editor.CallbackAction;
import dev.nafusoft.eventassistui.editor.EditorMenuHolder;
import dev.nafusoft.eventassistui.editor.EventEditor;
import dev.nafusoft.eventassistui.utils.EditorMenuGenerator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EventNameInputWaitAction extends BaseEventEditorAction implements CallbackAction {
    private Player player;

    public EventNameInputWaitAction(EventEditor editor) {
        super(editor);
    }

    @Override
    public void execute(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            this.player = player;

            EventAssistUI.getInstance().getInputListener().registerWaitingAction(player, getEditor());
            getEditor().setWaitingAction(this);
            player.sendMessage(Component.text("[EventAssist] Enter your event name to complete the setup!").color(NamedTextColor.GREEN));
        }
    }

    @Override
    public void callback(Object value) {
        if (value instanceof String name) {
            getEditor().getBuilder().name(name);
            getEditor().setWaitingAction(null);
            Bukkit.getServer().getScheduler().runTask(EventAssistUI.getInstance(),
                    () -> player.openInventory(EditorMenuGenerator.getMainMenu(new EditorMenuHolder(getEditor()))));
        } else {
            player.sendMessage(Component.text("[EventAssist] Invalid input!").color(NamedTextColor.RED));
        }
    }
}
