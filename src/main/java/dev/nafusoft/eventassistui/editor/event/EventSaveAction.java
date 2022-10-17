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

import dev.nafusoft.eventassistcore.exception.EventRegisterException;
import dev.nafusoft.eventassistui.editor.BaseEventEditorAction;
import dev.nafusoft.eventassistui.editor.EventEditor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EventSaveAction extends BaseEventEditorAction {

    public EventSaveAction(EventEditor editor) {
        super(editor);
    }

    @Override
    public void execute(InventoryClickEvent event) {
        try {
            getEditor().getBuilder().build();
            event.getWhoClicked().sendMessage(Component.text("[EventAssist] Your event was saved correctly!").color(NamedTextColor.GREEN));
        } catch (EventRegisterException e) {
            event.getWhoClicked().sendMessage(Component.text("[EventAssist] Some error occurred while saving the event.").color(NamedTextColor.RED));
        }
    }
}
