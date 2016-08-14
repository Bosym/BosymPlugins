/* Copyright 2016 Acquized
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
package cc.acquized.ultimatereport.utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class Report {

    private UUID reporter;
    private UUID target;
    private String reason;
    private boolean proceeded = false;

    public Report(ProxiedPlayer reporter, ProxiedPlayer target, String reason) {
        this.reporter = ConverterCache.convertToUUID(reporter.getName());
        this.target = ConverterCache.convertToUUID(target.getName());
        this.reason = reason;
    }

    public Report(UUID reporter, UUID target, String reason) {
        this.reporter = reporter;
        this.target = target;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public UUID getTarget() {
        return target;
    }

    public UUID getReporter() {
        return reporter;
    }

    public boolean isProceeded() {
        return proceeded;
    }

    public void setProceeded(boolean proceeded) {
        this.proceeded = proceeded;
    }

}
