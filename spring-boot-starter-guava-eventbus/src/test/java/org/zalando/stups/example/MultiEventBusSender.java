/**
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.stups.example;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

@Component
public class MultiEventBusSender {

    private final AsyncEventBus asyncEventBus;

    private final EventBus eventBus;

    @Autowired
    public MultiEventBusSender(final AsyncEventBus asyncEventBus, final EventBus eventBus) {
        this.asyncEventBus = asyncEventBus;
        this.eventBus = eventBus;
    }

    public void sendEvents() {
        this.asyncEventBus.post(new SimpleEvent());
        this.eventBus.post(new SimpleEvent());
    }
}
