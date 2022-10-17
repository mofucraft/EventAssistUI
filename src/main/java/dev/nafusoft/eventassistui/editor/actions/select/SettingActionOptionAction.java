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

import dev.nafusoft.eventassistcore.api.EventAssistAPI;
import dev.nafusoft.eventassistcore.automation.AutomationType;
import dev.nafusoft.eventassistui.editor.BaseEventEditorAction;
import dev.nafusoft.eventassistui.editor.EditorMenuHolder;
import dev.nafusoft.eventassistui.editor.EventEditor;
import dev.nafusoft.eventassistui.utils.EditorMenuGenerator;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class SettingActionOptionAction extends BaseEventEditorAction {

    public SettingActionOptionAction(EventEditor editor) {
        super(editor);
    }

    @Override
    public void execute(InventoryClickEvent event) {
        if (getEditor().getBuiltEvent() != null) {
            Inventory nextInventory = null;
            if (getEditor().getAutomationType() == null) {
                if (PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName()).equals("Start")) {
                    getEditor().setAutomationType(AutomationType.START_AUTOMATION);
                    nextInventory = EditorMenuGenerator.getAutomationSelectMenu(new EditorMenuHolder(getEditor()));
                } else if (PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName()).equals("End")) {
                    getEditor().setAutomationType(AutomationType.END_AUTOMATION);
                    nextInventory = EditorMenuGenerator.getAutomationSelectMenu(new EditorMenuHolder(getEditor()));
                } else {
                    nextInventory = EditorMenuGenerator.getStartEndSelectMenu(new EditorMenuHolder(getEditor()));
                }
            } else {
                if (getEditor().getAutomationBuilder() == null)
                    getEditor().setAutomationBuilder(EventAssistAPI.getInstance().getAutomationManager().getBuilder(getEditor().getBuiltEvent(), getEditor().getAutomationType()));

                if (getEditor().getActionBuilder() == null) {
                    var actionName = PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName());
                    var action = EventAssistAPI.getInstance().getAutomationManager().getActionRegistry().getActions().stream()
                            .filter(aClass -> aClass.getSimpleName().equals(actionName))
                            .findFirst().orElse(null);

                    if (action != null) {
                        getEditor().setActionBuilder(getEditor().getAutomationBuilder().getActionBuilder(action));
                        nextInventory = EditorMenuGenerator.getActionOptionMenu(new EditorMenuHolder(getEditor()));
                    } else {
                        nextInventory = EditorMenuGenerator.getAutomationSelectMenu(new EditorMenuHolder(getEditor()));
                    }
                }
            }

            if (nextInventory != null)
                event.getWhoClicked().openInventory(nextInventory);
        } else {
            if (!event.getCurrentItem().getItemMeta().lore().isEmpty()) {
                if (checkUUID(PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().lore().get(0)))) {
                    var selectedEventId = UUID.fromString(PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().lore().get(0)));
                    var editEvent = EventAssistAPI.getInstance().getEventManager().getEvent(selectedEventId);
                    if (editEvent != null) {
                        getEditor().setBuiltEvent(editEvent);
                        event.getWhoClicked().openInventory(EditorMenuGenerator.getStartEndSelectMenu(new EditorMenuHolder(getEditor())));
                    }
                }
            }
        }
    }

    private boolean checkUUID(String uuid) {
        return uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }
}
