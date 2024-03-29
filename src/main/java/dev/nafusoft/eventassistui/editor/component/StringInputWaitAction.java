/*
 * Copyright 2023 NAFU_at
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

package dev.nafusoft.eventassistui.editor.component;

import dev.nafusoft.eventassistui.EventAssistUI;
import dev.nafusoft.eventassistui.editor.BaseEventEditorAction;
import dev.nafusoft.eventassistui.editor.CallbackAction;
import dev.nafusoft.eventassistui.editor.EditorMenuHolder;
import dev.nafusoft.eventassistui.editor.EventEditor;
import dev.nafusoft.eventassistui.utils.EditorMenuGenerator;
import lombok.val;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StringInputWaitAction extends BaseEventEditorAction implements CallbackAction {

    public StringInputWaitAction(EventEditor editor) {
        super(editor);
    }

    private Player player;
    private String name;

    @Override
    public void execute(InventoryClickEvent event) {
        if (!event.getCurrentItem().getItemMeta().lore().isEmpty()
                && event.getWhoClicked() instanceof Player player) {
            this.player = player;

            EventAssistUI.getInstance().getInputListener().registerWaitingAction(player, getEditor());
            getEditor().setWaitingAction(this);

            val lore = event.getCurrentItem().getItemMeta().lore();
            name = PlainTextComponentSerializer.plainText().serialize(lore.get(0))
                    .substring(PlainTextComponentSerializer.plainText().serialize(lore.get(0)).indexOf(":") + 1)
                    .trim();
            val description = PlainTextComponentSerializer.plainText().serialize(lore.get(2))
                    .substring(PlainTextComponentSerializer.plainText().serialize(lore.get(1)).indexOf(":") + 1)
                    .trim();

            player.sendMessage(Component.text("[EventAssist] Enter option value to complete the setup!").color(NamedTextColor.GREEN));
            if (!StringUtils.isEmpty(description))
                player.sendMessage(Component.text("Hint!: " + description).color(NamedTextColor.YELLOW));
        }
    }

    @Override
    public void callback(Object value) {
        if (value instanceof String input)
            getEditor().getActionBuilder().setActionOption(name, input);

        getEditor().setWaitingAction(null);
        Bukkit.getServer().getScheduler().runTask(EventAssistUI.getInstance(),
                () -> player.openInventory(EditorMenuGenerator.getActionOptionMenu(new EditorMenuHolder(getEditor()))));
    }
}
