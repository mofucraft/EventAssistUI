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

package dev.nafusoft.eventassistui.editor;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class MenuHolder implements InventoryHolder {
    private String menuName = "Event Menu";
    private int size = 27;
    private Inventory inventory;

    protected MenuHolder() {
    }

    protected MenuHolder(String menuName) {
        this.menuName = menuName;
    }

    protected MenuHolder(String menuName, int size) {
        this.menuName = menuName;
        this.size = size;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public @NotNull Inventory getInventory() {
        if (inventory == null)
            inventory = Bukkit.getServer().createInventory(this, size, menuName);
        return inventory;
    }
}
