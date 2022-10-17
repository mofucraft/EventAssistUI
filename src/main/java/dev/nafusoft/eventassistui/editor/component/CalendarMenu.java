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
import dev.nafusoft.eventassistui.editor.EditorMenuHolder;
import dev.nafusoft.eventassistui.editor.event.SetStartDateAction;
import lombok.val;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class CalendarMenu {

    private CalendarMenu() {
        throw new UnsupportedOperationException();
    }

    public static Inventory getYearSelector(@NotNull EditorMenuHolder holder,
                                            @NotNull Class<? extends BaseEventEditorAction> editorActionClass) {
        holder.setMenuName("Year Selector");
        holder.setSize(9);

        var year = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 0; i < 9; i++) {
            var itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            val meta = itemStack.getItemMeta();
            meta.displayName(Component.text(String.valueOf(year)).asComponent());
            itemStack.setItemMeta(meta);
            holder.addMenu(i, itemStack, editorActionClass);
            year++;
        }

        return holder.getInventory();
    }

    public static Inventory getMonthSelector(@NotNull EditorMenuHolder holder,
                                             @NotNull Class<? extends BaseEventEditorAction> editorActionClass) {
        holder.setMenuName("Month Selector");
        holder.setSize(36);

        // - - -  1  2  3 - - -
        // - - -  4  5  6 - - -
        // - - -  7  8  9 - - -
        // - - - 10 11 12 - - -
        // 3 - 4 - 5 - 12 - 13 - 14 - 21 - 22 - 23 - 30 - 31 - 32
        int line = 0;
        int index = 2;
        for (int month = 1; month <= 12; month++) {
            if (index == 9 * line + 5) {
                index = index + 7;
                line++;
            } else {
                index++;
            }

            val itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, month);
            val meta = itemStack.getItemMeta();
            meta.displayName(Component.text(String.valueOf(month)).asComponent());
            itemStack.setItemMeta(meta);
            holder.addMenu(index, itemStack, editorActionClass);
        }

        return holder.getInventory();
    }

    public static Inventory getDateSelector(@NotNull EditorMenuHolder holder,
                                            @NotNull Class<? extends BaseEventEditorAction> editorActionClass,
                                            int year,
                                            int month) {
        holder.setMenuName("Date Selector");
        holder.setSize(45);

        // -  1  2  3  4  5  6  7 -
        // -  8  9 10 11 12 13 14 -
        // - 15 16 17 18 19 20 21 -
        // - 22 23 24 25 26 27 28 -
        // - 29 30 31  -  -  -  - -
        // 7 - 16 - 25 -
        val calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);

        int line = 0;
        int index = 0;
        int date = 1;
        while (date <= calendar.getActualMaximum(Calendar.DATE)) {
            if (index == line * 9 || index == (line + 1) * 9 - 1)
                index++;

            if (Math.floorMod(index, 9) == 0) {
                line++;
                continue;
            }

            val itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, date);
            val meta = itemStack.getItemMeta();
            meta.displayName(Component.text(String.valueOf(date)).asComponent());
            itemStack.setItemMeta(meta);
            holder.addMenu(index, itemStack, editorActionClass);

            date++;
            index++;
        }

        return holder.getInventory();
    }

    public static Inventory getAMPMSelector(@NotNull EditorMenuHolder holder,
                                            @NotNull Class<? extends BaseEventEditorAction> editorActionClass) {
        holder.setMenuName("AM/PM Selector");
        holder.setSize(9);

        val itemStackA = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        val metaA = itemStackA.getItemMeta();
        metaA.displayName(Component.text("AM").asComponent());
        itemStackA.setItemMeta(metaA);
        holder.addMenu(3, itemStackA, SetStartDateAction.class);

        val itemStackP = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        val metaP = itemStackP.getItemMeta();
        metaP.displayName(Component.text("PM").asComponent());
        itemStackP.setItemMeta(metaP);
        holder.addMenu(5, itemStackP, editorActionClass);

        return holder.getInventory();
    }

    public static Inventory getHourSelector(EditorMenuHolder holder, Class<? extends BaseEventEditorAction> editorActionClass, boolean startZero) {
        holder.setMenuName("Hour Selector");
        holder.setSize(36);

        // - - -  1  2  3 - - -
        // - - -  4  5  6 - - -
        // - - -  7  8  9 - - -
        // - - - 10 11 12 - - -
        int line = 0;
        int index = 2;
        for (int hour = startZero ? 0 : 1; hour <= 12 - (startZero ? 1 : 0); hour++) {
            if (index == 9 * line + 5) {
                index = index + 7;
                line++;
            } else {
                index++;
            }

            val itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, hour == 0 ? 1 : hour);
            val meta = itemStack.getItemMeta();
            meta.displayName(Component.text(String.valueOf(hour)).asComponent());
            itemStack.setItemMeta(meta);
            holder.addMenu(index, itemStack, editorActionClass);
        }

        return holder.getInventory();
    }

    public static Inventory getMinutesSelector(@NotNull EditorMenuHolder holder,
                                               @NotNull Class<? extends BaseEventEditorAction> editorActionClass) {
        holder.setMenuName("Minutes Selector");
        holder.setSize(9);

        for (int minutes = 0; minutes < 4; minutes++) {
            val itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, minutes == 0 ? 1 : minutes * 15);
            val meta = itemStack.getItemMeta();
            meta.displayName(Component.text(String.valueOf(minutes * 15)).asComponent());
            itemStack.setItemMeta(meta);
            holder.addMenu(minutes + 2, itemStack, editorActionClass);
        }

        return holder.getInventory();
    }
}
