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

package dev.nafusoft.eventassistui.editor.component;

import dev.nafusoft.eventassistui.editor.BaseEventEditorAction;
import dev.nafusoft.eventassistui.editor.CallbackAction;
import dev.nafusoft.eventassistui.editor.EditorMenuHolder;
import dev.nafusoft.eventassistui.editor.EventEditor;
import dev.nafusoft.eventassistui.utils.EditorMenuGenerator;
import lombok.val;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryItemSelectAction extends BaseEventEditorAction implements CallbackAction {

    public InventoryItemSelectAction(EventEditor editor) {
        super(editor);
    }

    private Player player;
    private String name;

    @Override
    public void execute(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            this.player = player;
            getEditor().setWaitingAction(this);

            val lore = event.getCurrentItem().getItemMeta().lore();
            name = PlainTextComponentSerializer.plainText().serialize(lore.get(0))
                    .substring(PlainTextComponentSerializer.plainText().serialize(lore.get(0)).indexOf(":") + 1)
                    .trim();

            val holder = new EditorMenuHolder(getEditor());
            holder.setMenuName("Item Selector");
            holder.setSize(36);

            for (int i = 0; i < 36; i++) {
                val item = player.getInventory().getItem(i);
                if (item != null)
                    holder.addMenu(i, item, this.getClass());
            }

            event.getWhoClicked().openInventory(holder.getInventory());
        }
    }

    @Override
    public void callback(Object value) {
        if (value instanceof ItemStack item)
            getEditor().getActionBuilder().setActionOption(name, item);

        getEditor().setWaitingAction(null);
        player.openInventory(EditorMenuGenerator.getActionOptionMenu(new EditorMenuHolder(getEditor())));
    }
}
