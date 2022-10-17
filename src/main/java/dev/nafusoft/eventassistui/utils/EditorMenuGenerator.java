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

package dev.nafusoft.eventassistui.utils;

import dev.nafusoft.eventassistcore.api.EventAssistAPI;
import dev.nafusoft.eventassistcore.gameevent.GameEvent;
import dev.nafusoft.eventassistcore.gameevent.GameEventStatus;
import dev.nafusoft.eventassistui.EventAssistUI;
import dev.nafusoft.eventassistui.editor.EditorMenuHolder;
import dev.nafusoft.eventassistui.editor.actions.select.ActionOptionSaveAction;
import dev.nafusoft.eventassistui.editor.actions.select.AutomationSaveAction;
import dev.nafusoft.eventassistui.editor.actions.select.SettingActionOptionAction;
import dev.nafusoft.eventassistui.editor.event.*;
import lombok.val;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EditorMenuGenerator {

    private EditorMenuGenerator() {
        throw new UnsupportedOperationException();
    }

    public static Inventory getMainMenu(EditorMenuHolder holder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

        holder.setMenuName("Create new event");
        holder.setSize(9);

        // 名前の設定
        val nameSetStack = new ItemStack(Material.NAME_TAG);
        val nameSetStackMeta = nameSetStack.getItemMeta();
        nameSetStackMeta.displayName(Component.text("Set event name"));
        if (holder.getEditor().getBuilder().getEventName() != null)
            nameSetStackMeta.lore(List.of(Component.text(holder.getEditor().getBuilder().getEventName())));
        nameSetStack.setItemMeta(nameSetStackMeta);
        holder.addMenu(0, nameSetStack, EventNameInputWaitAction.class);

        // 説明の設定
        val descriptionSetStack = new ItemStack(Material.BOOK);
        val descriptionSetStackMeta = descriptionSetStack.getItemMeta();
        descriptionSetStackMeta.displayName(Component.text("Set event description"));
        if (holder.getEditor().getBuilder().getEventDescription() != null)
            descriptionSetStackMeta.lore(List.of(Component.text(holder.getEditor().getBuilder().getEventDescription())));
        descriptionSetStack.setItemMeta(descriptionSetStackMeta);
        holder.addMenu(1, descriptionSetStack, EventDescriptionInputWaitAction.class);

        // 開催日時の設定
        val eventStartDateSetStack = new ItemStack(Material.CLOCK);
        val eventStartDateSetStackMeta = eventStartDateSetStack.getItemMeta();
        eventStartDateSetStackMeta.displayName(Component.text("Set event start date"));
        eventStartDateSetStackMeta.lore(List.of(Component.text(dateFormat.format(holder.getEditor().getBuilder().getEventStartTime()))));
        eventStartDateSetStack.setItemMeta(eventStartDateSetStackMeta);
        holder.addMenu(2, eventStartDateSetStack, SetStartDateAction.class);

        // 開催日時の設定
        val eventEndDateSetStack = new ItemStack(Material.CLOCK);
        val eventEndDateSetStackMeta = eventEndDateSetStack.getItemMeta();
        eventEndDateSetStackMeta.displayName(Component.text("Set event end date"));
        eventEndDateSetStackMeta.lore(List.of(Component.text(dateFormat.format(holder.getEditor().getBuilder().getEventEndTime()))));
        eventEndDateSetStack.setItemMeta(eventEndDateSetStackMeta);
        holder.addMenu(3, eventEndDateSetStack, SetEndDateAction.class);

        // 開催場所の設定
        val eventLocationSetStack = new ItemStack(Material.COMPASS);
        val eventLocationSetStackMeta = eventLocationSetStack.getItemMeta();
        eventLocationSetStackMeta.displayName(Component.text("Set event location"));
        if (holder.getEditor().getBuilder().getEventLocation() != null) {
            var locationString = holder.getEditor().getBuilder().getEventLocation().toString();
            locationString = locationString.substring(9, locationString.length() - 1);
            eventLocationSetStackMeta.lore(Arrays.stream(locationString.split(",")).map(content -> Component.text(content).compact()).toList());
        }
        eventLocationSetStack.setItemMeta(eventLocationSetStackMeta);
        holder.addMenu(4, eventLocationSetStack, SetLocationAction.class);

        // 設定の保存
        if (holder.getEditor().getBuilder().canBuild()) {
            val eventSaveStack = new ItemStack(Material.WRITABLE_BOOK);
            val eventSaveStackMeta = eventSaveStack.getItemMeta();
            eventSaveStackMeta.displayName(Component.text("Save event"));
            eventSaveStack.setItemMeta(eventSaveStackMeta);
            holder.addMenu(8, eventSaveStack, EventSaveAction.class);
        }

        return holder.getInventory();
    }


    public static Inventory getEditEventAutomationMenu(EditorMenuHolder holder) {
        var eventStacks = EventAssistAPI.getInstance().getEvents(GameEventStatus.UPCOMING).stream()
                .sorted(Comparator.comparing(GameEvent::getEventStartTime))
                .map(upcoming -> {
                    val eventStack = new ItemStack(Material.PAPER);
                    val eventStackMeta = eventStack.getItemMeta();
                    eventStackMeta.displayName(Component.text(upcoming.getEventName()));
                    eventStackMeta.lore(List.of(Component.text(upcoming.getEventId().toString())));
                    eventStack.setItemMeta(eventStackMeta);
                    return eventStack;
                })
                .toList();

        if (eventStacks.size() > 54)
            eventStacks = eventStacks.subList(0, 54);

        holder.setSize(BigDecimal.valueOf((double) eventStacks.size() / 9).setScale(0, RoundingMode.UP).intValue() * 9);
        for (int i = 0; i < eventStacks.size(); i++) {
            holder.addMenu(i, eventStacks.get(i), SettingActionOptionAction.class);
        }

        return holder.getInventory();
    }

    public static Inventory getStartEndSelectMenu(EditorMenuHolder holder) {
        holder.setMenuName("Start/End Selector");
        holder.setSize(9);

        val itemStackS = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        val metaS = itemStackS.getItemMeta();
        metaS.displayName(Component.text("Start").asComponent());
        itemStackS.setItemMeta(metaS);
        holder.addMenu(3, itemStackS, SettingActionOptionAction.class);
        val itemStackE = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        val metaE = itemStackE.getItemMeta();
        metaE.displayName(Component.text("End").asComponent());
        itemStackE.setItemMeta(metaE);
        holder.addMenu(5, itemStackE, SettingActionOptionAction.class);

        return holder.getInventory();
    }

    public static Inventory getAutomationSelectMenu(EditorMenuHolder holder) {
        val actionStacks = EventAssistAPI.getInstance().getAutomationManager().getActionRegistry().getActions().stream()
                .map(action -> {
                    val actionStack = new ItemStack(Material.COMMAND_BLOCK);
                    val actionStackMeta = actionStack.getItemMeta();
                    actionStackMeta.displayName(Component.text(action.getSimpleName()));
                    actionStack.setItemMeta(actionStackMeta);
                    return actionStack;
                }).toList();

        holder.setSize(BigDecimal.valueOf((double) actionStacks.size() / 9).setScale(0, RoundingMode.UP).intValue() * 9);
        for (int i = 0; i < actionStacks.size(); i++) {
            holder.addMenu(i, actionStacks.get(i), SettingActionOptionAction.class);
        }

        return holder.getInventory();
    }

    public static Inventory getActionOptionMenu(EditorMenuHolder holder) {
        val options = holder.getEditor().getActionBuilder().getOptions();
        val actionOptionStacks = options.entrySet().stream()
                .map(actionOption -> {
                    val actionOptionStack = new ItemStack(Material.COMMAND_BLOCK);
                    val actionOptionStackMeta = actionOptionStack.getItemMeta();
                    actionOptionStackMeta.displayName(Component.text(actionOption.getKey()));

                    val loreList = new ArrayList<Component>();
                    loreList.add(Component.text("OptionName: " + actionOption.getKey()));
                    loreList.add(Component.text("OptionType: " + actionOption.getValue()));
                    actionOptionStackMeta.lore(loreList);

                    actionOptionStack.setItemMeta(actionOptionStackMeta);
                    return actionOptionStack;
                }).toList();

        holder.setSize(BigDecimal.valueOf((double) (actionOptionStacks.size() + 1) / 9).setScale(0, RoundingMode.UP).intValue() * 9);
        for (int i = 0; i < actionOptionStacks.size(); i++) {
            holder.addMenu(i,
                    actionOptionStacks.get(i),
                    EventAssistUI.getInstance().getOptionSetterRegistry().getSetter(holder.getEditor().getActionBuilder().getOptionsClass(),
                            PlainTextComponentSerializer.plainText().serialize(actionOptionStacks.get(i).getItemMeta().displayName())));
        }

        if (holder.getEditor().getActionBuilder().canBuild()) {
            val optionSaveStack = new ItemStack(Material.WRITABLE_BOOK);
            val optionSaveStackMeta = optionSaveStack.getItemMeta();
            optionSaveStackMeta.displayName(Component.text("Save"));
            optionSaveStack.setItemMeta(optionSaveStackMeta);
            holder.addMenu(holder.getInventory().getSize() - 1, optionSaveStack, ActionOptionSaveAction.class);
        }

        return holder.getInventory();
    }

    public static Inventory getAutomationActionListMenu(EditorMenuHolder holder) {
        holder.setMenuName("Automation Editor");
        holder.setSize(27);

        if (holder.getEditor().getAutomationBuilder().getActions().size() < 18) {
            val actionAddStack = new ItemStack(Material.COMMAND_BLOCK);
            val actionAddStackMeta = actionAddStack.getItemMeta();
            actionAddStackMeta.displayName(Component.text("Add Action"));
            actionAddStack.setItemMeta(actionAddStackMeta);
            holder.addMenu(18, actionAddStack, SettingActionOptionAction.class);
        }

        val automationSaveStack = new ItemStack(Material.WRITABLE_BOOK);
        val automationSaveStackMeta = automationSaveStack.getItemMeta();
        automationSaveStackMeta.displayName(Component.text("Save"));
        automationSaveStack.setItemMeta(automationSaveStackMeta);
        holder.addMenu(holder.getInventory().getSize() - 1, automationSaveStack, AutomationSaveAction.class);

        val actionStacks = holder.getEditor().getAutomationBuilder().getActions().stream()
                .map(action -> {
                    val actionStack = new ItemStack(Material.COMMAND_BLOCK);
                    val actionStackMeta = actionStack.getItemMeta();
                    actionStackMeta.displayName(Component.text(action.getClass().getSimpleName()));
                    actionStack.setItemMeta(actionStackMeta);
                    return actionStack;
                }).toList();
        for (int i = 0; i < actionStacks.size(); i++) {
            // TODO: 2022/09/11 設定済みのアクションをクリックした場合はなにもしないようにする
            holder.addMenu(i, actionStacks.get(i), SettingActionOptionAction.class);
        }

        return holder.getInventory();
    }
}
