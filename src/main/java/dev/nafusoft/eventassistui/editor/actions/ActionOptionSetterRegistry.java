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

package dev.nafusoft.eventassistui.editor.actions;

import dev.nafusoft.eventassistcore.automation.ActionOptions;
import dev.nafusoft.eventassistui.editor.BaseEventEditorAction;
import dev.nafusoft.eventassistui.editor.component.InventoryItemSelectAction;
import dev.nafusoft.eventassistui.editor.component.PrimitiveInputWaitAction;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActionOptionSetterRegistry {
    private final Map<Class<? extends ActionOptions>, Map<String, Class<? extends BaseEventEditorAction>>> optionEditorMap;

    public ActionOptionSetterRegistry() {
        optionEditorMap = new HashMap<>();
    }

    public void register(@NotNull Class<? extends ActionOptions> options, @NotNull String optionName, @NotNull Class<? extends BaseEventEditorAction> editor) {
        Objects.requireNonNull(options);
        ObjectUtils.requireNonEmpty(optionName);
        Objects.requireNonNull(editor);

        getOptionSetters(options).put(optionName, editor);
    }

    public Map<String, Class<? extends BaseEventEditorAction>> getOptionSetters(@NotNull Class<? extends ActionOptions> options) {
        return optionEditorMap.computeIfAbsent(options, k -> new HashMap<>());
    }

    public Class<? extends BaseEventEditorAction> getSetter(@NotNull Class<? extends ActionOptions> options, @NotNull String optionName) {
        Objects.requireNonNull(options);
        ObjectUtils.requireNonEmpty(optionName);

        return getOptionSetters(options).getOrDefault(optionName, getDefaultSetter(options, optionName));
    }


    private Class<? extends BaseEventEditorAction> getDefaultSetter(Class<? extends ActionOptions> options, @NotNull String optionName) {
        Class<? extends BaseEventEditorAction> setter;
        val type = Arrays.stream(options.getRecordComponents()).filter(rc -> rc.getName().equals(optionName)).findFirst().get().getType();

        if (type.isPrimitive()) {
            setter = PrimitiveInputWaitAction.class;
        } else if (type == ItemStack.class) {
            setter = InventoryItemSelectAction.class;
        } else {
            setter = PrimitiveInputWaitAction.class;
        }

        return setter;
    }
}
