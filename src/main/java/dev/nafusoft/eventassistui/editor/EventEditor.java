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


import dev.nafusoft.eventassistcore.api.EventAssistAPI;
import dev.nafusoft.eventassistcore.automation.AutomationBuilder;
import dev.nafusoft.eventassistcore.automation.AutomationType;
import dev.nafusoft.eventassistcore.gameevent.GameEvent;
import dev.nafusoft.eventassistcore.gameevent.GameEventBuilder;
import org.bukkit.entity.Player;

public class EventEditor {
    private final GameEventBuilder builder;

    private CallbackAction waitingAction;

    public EventEditor(Player owner) {
        builder = EventAssistAPI.getInstance().getBuilder();
        builder.owner(owner.getUniqueId());
    }

    public GameEventBuilder getBuilder() {
        return builder;
    }

    public EditorAction getWaitingAction() {
        return waitingAction;
    }

    public void setWaitingAction(CallbackAction waitingAction) {
        this.waitingAction = waitingAction;
    }


    // ここから下はアクション設定用
    private GameEvent builtEvent;
    private AutomationType automationType = null;
    private AutomationBuilder automationBuilder;
    private AutomationBuilder.ActionBuilder actionBuilder;

    public GameEvent getBuiltEvent() {
        return builtEvent;
    }

    public void setBuiltEvent(GameEvent builtEvent) {
        this.builtEvent = builtEvent;
    }

    public AutomationType getAutomationType() {
        return automationType;
    }

    public void setAutomationType(AutomationType automationType) {
        this.automationType = automationType;
    }

    public AutomationBuilder getAutomationBuilder() {
        return automationBuilder;
    }

    public void setAutomationBuilder(AutomationBuilder automationBuilder) {
        this.automationBuilder = automationBuilder;
    }

    public AutomationBuilder.ActionBuilder getActionBuilder() {
        return actionBuilder;
    }

    public void setActionBuilder(AutomationBuilder.ActionBuilder actionBuilder) {
        this.actionBuilder = actionBuilder;
    }
}
