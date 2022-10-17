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

import dev.nafusoft.eventassistui.editor.BaseEventEditorAction;
import dev.nafusoft.eventassistui.editor.CallbackAction;
import dev.nafusoft.eventassistui.editor.EditorMenuHolder;
import dev.nafusoft.eventassistui.editor.EventEditor;
import dev.nafusoft.eventassistui.editor.component.CalendarMenu;
import dev.nafusoft.eventassistui.utils.EditorMenuGenerator;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Calendar;

public class SetEndDateAction extends BaseEventEditorAction implements CallbackAction {

    public SetEndDateAction(EventEditor editor) {
        super(editor);
    }

    private Player player;
    private int year = -1;
    private int month = -1;
    private int date = -1;
    private int hour = -1;
    private int pm = -1;
    private int minutes = -1;

    @Override
    public void execute(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof EditorMenuHolder) {
            if (event.getWhoClicked() instanceof Player player)
                this.player = player;

            getEditor().setWaitingAction(this);
            event.getWhoClicked().openInventory(CalendarMenu.getYearSelector(new EditorMenuHolder(getEditor()), this.getClass()));
        } else {
            throw new IllegalStateException("The EventEditorAction must hold an EditorMenuHolder.");
        }
    }

    @Override
    public void callback(Object value) {
        if (value instanceof ItemStack select) {
            var input =
                    switch (PlainTextComponentSerializer.plainText().serialize(select.getItemMeta().displayName())) {
                        case "AM" -> 0;

                        case "PM" -> 1;

                        default ->
                                Integer.parseInt(PlainTextComponentSerializer.plainText().serialize(select.getItemMeta().displayName()));
                    };

            Inventory nextInventory = null;
            if (year == -1) {
                year = input;
                nextInventory = CalendarMenu.getMonthSelector(new EditorMenuHolder(getEditor()), this.getClass());
            } else if (month == -1) {
                month = input;
                nextInventory = CalendarMenu.getDateSelector(new EditorMenuHolder(getEditor()), this.getClass(), year, month);
            } else if (date == -1) {
                date = input;
                nextInventory = CalendarMenu.getAMPMSelector(new EditorMenuHolder(getEditor()), this.getClass());
            } else if (pm == -1) {
                pm = input;
                nextInventory = CalendarMenu.getHourSelector(new EditorMenuHolder(getEditor()), this.getClass(), pm == 0);
            } else if (hour == -1) {
                hour = input;
                nextInventory = CalendarMenu.getMinutesSelector(new EditorMenuHolder(getEditor()), this.getClass());
            } else if (minutes == -1) {
                minutes = input;

                var calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DATE, date);
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.AM_PM, pm);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                getEditor().getBuilder().endTime(calendar.getTime().getTime());

                getEditor().setWaitingAction(null);
                nextInventory = EditorMenuGenerator.getMainMenu(new EditorMenuHolder(getEditor()));
            }

            if (nextInventory != null)
                player.openInventory(nextInventory);
        } else {
            throw new IllegalStateException("The value must be an ItemStack.");
        }
    }
}
