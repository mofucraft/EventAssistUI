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
import dev.nafusoft.eventassistui.editor.EditorMenuHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.logging.Level;

public class EditorClickEventListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getCurrentItem() != null) {
            var holder = event.getClickedInventory().getHolder();
            if (holder instanceof EditorMenuHolder editorHolder) {
                if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) // ほかインベントリの移動は問答無用でキャンセルする
                    event.setCancelled(true);

                event.getInventory().close();

                if (editorHolder.getEditor() != null
                        && editorHolder.getEditor().getWaitingAction() instanceof CallbackAction action) { // 操作待ちの場合は該当アクションを実行する
                    try {
                        action.callback(event.getCurrentItem());
                    } catch (Exception e) {
                        PluginLogger.log(
                                Level.WARNING,
                                "An error occurred while executing the editor action.",
                                e
                        );

                        event.getWhoClicked().sendMessage(Component.text("[EventAssist] An error occurred while executing the editor action.").color(NamedTextColor.RED));
                        event.getWhoClicked().sendMessage(Component.text(e.getStackTrace()[0].toString()).color(NamedTextColor.RED));
                    }
                } else {
                    var editorAction = editorHolder.getAction(event.getSlot());
                    if (editorAction == null) {
                        event.setCancelled(true);
                    } else {
                        try {
                            editorAction.execute(event);
                        } catch (Exception e) {
                            PluginLogger.log(
                                    Level.WARNING,
                                    "An error occurred while executing the editor action.",
                                    e
                            );

                            event.getWhoClicked().sendMessage(Component.text("[EventAssist] An error occurred while executing the editor action.").color(NamedTextColor.RED));
                            event.getWhoClicked().sendMessage(Component.text(e.getStackTrace()[0].toString()).color(NamedTextColor.RED));
                        }
                    }
                }
            }
        }
    }
}
