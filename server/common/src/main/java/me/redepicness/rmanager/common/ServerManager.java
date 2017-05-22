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

package me.redepicness.rmanager.common;

import me.redepicness.rmanager.common.Database.DBKey;
import me.redepicness.rmanager.common.util.LiveField;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Red_Epicness
 * @since 5/21/2017 @ 01:10 AM
 */
public class ServerManager {

    public static final DBKey SERVERS_KEY = Database.ROOT_KEY.cloneAdd("servers");
    public static final DBKey ONLINE_SERVERS_KEY = SERVERS_KEY.cloneAdd("online");
    public static final DBKey STATIC_SERVERS_KEY = SERVERS_KEY.cloneAdd("static");

    //private static final HashMap<String, ServerStatus> SERVER_STATUSES = new HashMap<>();
    private static final LiveField<HashMap<String, Integer>> ONLINE_SERVERS = new LiveField<>(Duration.ofMillis(40), () -> {
        HashMap<String, Integer> servers = new HashMap<>();
        Database.syncCommands()
                .keys(ONLINE_SERVERS_KEY.res("*"))
                .forEach(s -> {
                    String name = s.split(":")[2];
                    int port = Integer.valueOf(Database.syncCommands().get(s));
                    servers.put(name, port);
                });
        return servers;
    });
    private static final HashMap<String, StaticServer> STATIC_SERVERS = new HashMap<>();

    /*public static ServerStatus get(String name) {
        if (SERVER_STATUSES.containsKey(name)) {
            return SERVER_STATUSES.get(name);
        }
        Validate.isTrue(getStatic().containsKey(name) || getOnline().containsKey(name), "That server does not exist!");
        ServerStatus s = new ServerStatus(name);
        SERVER_STATUSES.put(name, s);
        return s;
    }

    public static HashMap<String, ServerStatus> getServerStatuses() {
        return SERVER_STATUSES;
    }*/

    public static HashMap<String, Integer> getOnline() {
        return ONLINE_SERVERS.get().orElseThrow(() -> new RuntimeException("Online Server Live Field returned null!"));
    }

    public static HashMap<String, StaticServer> getStatic() {
        if (STATIC_SERVERS.isEmpty()) {
            Database.syncCommands()
                    .keys(STATIC_SERVERS_KEY.res("*"))
                    .forEach(s -> {
                        String name = s.split(":")[2];
                        if (STATIC_SERVERS.containsKey(name)) return; //Just in case...
                        STATIC_SERVERS.put(name, new StaticServer(name));
                    });
        }
        return STATIC_SERVERS;
    }

    public static class StaticServer {

        private String id;

        private String name;
        private int port;
        private boolean reboot;

        private DBKey key;

        private StaticServer(String id) {
            this.id = id;

            key = STATIC_SERVERS_KEY.cloneAdd(id);

            Map<String, String> fields = Database.syncCommands().hgetall(key.get());
            name = fields.get("name");
            port = Integer.valueOf(fields.get("port"));
            reboot = Boolean.valueOf(fields.get("reboot"));
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPort() {
            return port;
        }

        public boolean shouldReboot() {
            return reboot;
        }

        public void setReboot(boolean reboot) {
            Database.syncCommands().hset(key.get(), "reboot", String.valueOf(reboot));
        }
    }

    /*public static class ServerStatus {

        private final String ID;
        private boolean online = false;
        private LiveField<RedisMessageData> statusReply = new LiveField<>(Duration.ofMillis(50), () -> {
            if(!isOnline()) return null; //Prevent too many requests being sent without need
            RedisReply reply = Database.getMessenger().sendMessageWithReply(
                    getID(),
                    new RedisMessageData("status").addData("detail", "all"),
                    Duration.ofMillis(20)
            );
            Optional<RedisMessage> message = reply.get();
            if(message == null) return null; // Fallback in the event the getOnlineList dun goofed
            else return message.map(RedisMessage::getData).orElse(null);
        });
        private int playerCount = -1;
        private int maxPlayers = -1;
        private HashSet<UUID> onlineUUIDs = null;
        private HashSet<String> onlineNames = null;
        private double tps = -1;

        ServerStatus(String id) {
            ID = id;
        }

        private void checkForUpdate() {
            Optional<RedisMessageData> optional = statusReply.get();

            online = optional.isPresent();

            if(!online) return;

            RedisMessageData data = optional.get();

            playerCount = data.get("playerCount");
            maxPlayers = data.get("maxPlayers");
            onlineUUIDs = data.get("onlineUUIDs");
            onlineNames = data.get("onlineNames");
            tps = data.get("tps");
        }

        public String getID() {
            return ID;
        }

        public boolean isOnline() {
            return getOnline().containsKey(getID());
        }

        public int getPlayerCount() {
            checkForUpdate();
            return playerCount;
        }

        public int getMaxPlayers() {
            checkForUpdate();
            return maxPlayers;
        }

        public HashSet<UUID> getOnlineUUIDs() {
            checkForUpdate();
            return onlineUUIDs;
        }

        public HashSet<String> getOnlineNames() {
            checkForUpdate();
            return onlineNames;
        }

        public double getTps() {
            checkForUpdate();
            return tps;
        }
    }*/

}
