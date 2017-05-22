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

package me.redepicness.rmanager.spigot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.lettuce.core.SetArgs.Builder;
import me.redepicness.rmanager.common.Database;
import net.minecraft.server.v1_11_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;

import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;
import static me.redepicness.rmanager.common.Database.serverID;
import static me.redepicness.rmanager.common.ServerManager.ONLINE_SERVERS_KEY;

/**
 * @author Red_Epicness
 * @since 21/05/2017 @ 1:21 AM
 */
public class StatusUpdater implements Runnable {

    public void run() {
        /*RedisCommands<String, String> cmd = Database.syncCommands();

        String rootInfoKey = ONLINE_SERVERS_KEY.res(serverID());
        String playerListKey = ONLINE_SERVERS_KEY.res(serverID(), "players");

        Map<String, String> fields = new HashMap<>();
        fields.put("playerCount", valueOf(Bukkit.getOnlinePlayers().size()));
        fields.put("maxPlayers", valueOf(Bukkit.getMaxPlayers()));
        fields.put("tps", valueOf(getTPS()));
        cmd.hmset(rootInfoKey, fields);

        String[] names = getOnlinePlayerNames();
        if(names.length > 0){
            cmd.sadd(playerListKey, names);
        }

        // Minecraft tick is 50 ms, so we give it a bit of a buffer just in case it stutters...
        cmd.pexpire(rootInfoKey, 75);
        cmd.pexpire(playerListKey, 75);*/
        JsonObject data = new JsonObject();
        data.addProperty("player_count", valueOf(Bukkit.getOnlinePlayers().size()));
        data.addProperty("max_players", valueOf(Bukkit.getMaxPlayers()));
        data.addProperty("tps", getTPS());
        JsonArray playerNames = new JsonArray();
        for (String name : getOnlinePlayerNames()) {
            playerNames.add(new JsonPrimitive(name));
        }
        data.add("players", playerNames);

        //We give it a small buffer (4 ticks) as its better to be falsly online for 3 ticks than to have the status flash rapidly.
        Database.syncCommands().set(ONLINE_SERVERS_KEY.res(serverID()), data.toString(), Builder.px(200));
    }

    private Set<String> getOnlinePlayerNames() {
        return Bukkit.getOnlinePlayers()
                     .stream()
                     .map(HumanEntity::getName)
                     .collect(Collectors.toSet());
    }

    private double getTPS() {
        return Math.min(20.0, Math.round(MinecraftServer.getServer().recentTps[0] * 100.0D) / 100.0D);
    }

}
