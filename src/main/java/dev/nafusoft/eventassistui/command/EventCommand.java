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

package dev.nafusoft.eventassistui.command;

import dev.nafusoft.eventassistcore.api.EventAssistAPI;
import dev.nafusoft.eventassistcore.gameevent.GameEvent;
import dev.nafusoft.eventassistcore.gameevent.GameEventStatus;
import dev.nafusoft.eventassistui.editor.EditorMenuHolder;
import dev.nafusoft.eventassistui.editor.NoActionMenuHolder;
import dev.nafusoft.eventassistui.editor.event.EntryEventAction;
import lombok.val;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EventCommand implements SubCommandExecutor {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {

            } else switch (args[0]) {
                case "show": {
                    // 登録済みのすべてのイベントを表示する
                    var events = EventAssistAPI.getInstance().getEvents(GameEventStatus.UPCOMING);
                    events = events.stream().sorted(Comparator.comparing(GameEvent::getEventStartTime)).toList(); // 開催日ソート

                    val inventory = new NoActionMenuHolder("Upcoming Events", 36).getInventory();
                    events.stream().forEach(event -> {
                        val eventItem = new ItemStack(Material.BOOK);
                        val eventItemMeta = eventItem.getItemMeta();
                        eventItemMeta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(event.getEventName()));

                        val startAutomation = EventAssistAPI.getInstance().getAutomationManager().getStartAutomation(event.getEventId());
                        val endAutomation = EventAssistAPI.getInstance().getAutomationManager().getEndAutomation(event.getEventId());

                        val loreList = new ArrayList<Component>();
                        loreList.add(LegacyComponentSerializer.legacyAmpersand().deserialize(event.getEventDescription()));
                        loreList.add(Component.text("StartDate: " + dateFormat.format(event.getEventStartTime())));
                        loreList.add(Component.text("EndDate: " + dateFormat.format(event.getEventEndTime())));
                        loreList.add(Component.text("Owner: " + Bukkit.getPlayer(event.getEventOwner()).getName()));
                        loreList.add(Component.text("Status: " + event.getEventStatus().name()));
                        loreList.add(Component.text("StartAutomation: " + (startAutomation != null ? startAutomation.getActions().stream().map(action -> action.getClass().getSimpleName()).collect(Collectors.joining(", ")) : "null")));
                        loreList.add(Component.text("EndAutomation: " + (endAutomation != null ? endAutomation.getActions().stream().map(action -> action.getClass().getSimpleName()).collect(Collectors.joining(", ")) : "null")));
                        eventItemMeta.lore(loreList);
                        eventItem.setItemMeta(eventItemMeta);
                        inventory.addItem(eventItem);
                    });

                    player.openInventory(inventory);
                }
                break;

                case "entry": {
                    // 登録済みのすべてのイベントを表示する
                    var events = EventAssistAPI.getInstance().getEvents(GameEventStatus.UPCOMING);
                    events = events.stream().sorted(Comparator.comparing(GameEvent::getEventStartTime)).toList(); // 開催日ソート

                    val holder = new EditorMenuHolder(null, "Upcoming Events");
                    holder.setSize(BigDecimal.valueOf((double) (events.size()) / 9).setScale(0, RoundingMode.UP).intValue() * 9);

                    for (int i = 0; i < events.size(); i++) {
                        val event = events.get(i);
                        val eventItem = new ItemStack(Material.BOOK);

                        val eventItemMeta = eventItem.getItemMeta();
                        eventItemMeta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(event.getEventName()));

                        val loreList = new ArrayList<Component>();
                        loreList.add(LegacyComponentSerializer.legacyAmpersand().deserialize(event.getEventDescription()));
                        loreList.add(Component.text("StartDate: " + dateFormat.format(event.getEventStartTime())));
                        loreList.add(Component.text("EndDate: " + dateFormat.format(event.getEventEndTime())));
                        loreList.add(Component.text("Owner: " + Bukkit.getPlayer(event.getEventOwner()).getName()));
                        loreList.add(Component.text(event.getEventId().toString()));
                        eventItemMeta.lore(loreList);
                        eventItem.setItemMeta(eventItemMeta);
                        holder.addMenu(i, eventItem, EntryEventAction.class);
                    }

                    player.openInventory(holder.getInventory());
                }
                break;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be executed in-game.");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Arrays.asList("show", "entry");
    }
}
