/*
 *    Copyright 2017 Miha Mitič
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.redepicness.rmanager.common.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Red_Epicness
 * @since 1/21/2017 @ 12:05 AM
 */
public class LiveField<T> {

    private T value = null;
    private Supplier<T> updater;
    private Instant lastUpdate = Instant.MIN;
    private Duration expiry;

    public LiveField(Duration expiry, Supplier<T> updater) {
        this.updater = updater;
        this.expiry = expiry;
    }

    public Optional<T> get() {
        if (value == null || lastUpdate.plus(expiry).isBefore(Instant.now())) {
            value = updater.get();
            lastUpdate = Instant.now();
        }
        if (value == null) return Optional.empty();
        else return Optional.of(value);
    }

}
