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

package dev.nafusoft.eventassistui.editor.actions.select;

import dev.nafusoft.eventassistui.editor.BaseEventEditorAction;
import dev.nafusoft.eventassistui.editor.EditorMenuHolder;
import dev.nafusoft.eventassistui.editor.EventEditor;
import dev.nafusoft.eventassistui.utils.EditorMenuGenerator;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionOptionSaveAction extends BaseEventEditorAction {

    public ActionOptionSaveAction(EventEditor editor) {
        super(editor);
    }

    @Override
    public void execute(InventoryClickEvent event) {
        if (getEditor().getAutomationBuilder() != null) {
            var automationAction = getEditor().getActionBuilder().build();
            getEditor().getAutomationBuilder().automationAction(automationAction);
            getEditor().setActionBuilder(null);
            getEditor().setWaitingAction(null);
            event.getWhoClicked().openInventory(EditorMenuGenerator.getAutomationActionListMenu(new EditorMenuHolder(getEditor())));
        }
    }
}
