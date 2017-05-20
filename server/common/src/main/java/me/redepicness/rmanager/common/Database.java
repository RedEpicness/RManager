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

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.sync.RedisCommands;
import me.redepicness.redismsg.RedisMessenger;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Created by Red_Epicness on 7/28/2016 at 1:50 PM.
 */
public class Database {

    // static

    private static Database instance = null;

    public static void initDatabase(String serverID, String hostname, String password, int port, int dbnumber) {
        Validate.isTrue(instance == null, "Database has already been initialized!");
        instance = new Database(serverID, hostname, password, port, dbnumber);
        //Rank.initRanks();      Implements Permissions Manager Later
    }

    public static Database getInstance() {
        Validate.notNull(instance, "Database has not been initialized!");
        return instance;
    }

    public static String serverID() {
        return getInstance().getServerID();
    }

    public static RedisCommands<String, String> syncCommands() {
        return getInstance().getSyncCommands();
    }

    // instance

    private String serverID = null;
    private RedisCommands<String, String> syncCommands;
    private RedisClient redisClient;

    public Database(String serverID, String hostname, String password, int port, int dbnumber) {
        this.serverID = serverID;

        RedisURI redisURI = RedisURI.builder()
                                    .withHost(hostname)
                                    .withPassword(password)
                                    .withPort(port)
                                    .withDatabase(dbnumber)
                                    .withClientName("rmanager-" + serverID)
                                    .build();

        redisClient = RedisClient.create(redisURI);

        syncCommands = redisClient.connect().sync();

        new RedisMessenger(serverID, redisURI);
    }

    public String getServerID() {
        return serverID;
    }

    public RedisCommands<String, String> getSyncCommands() {
        return syncCommands;
    }

    public void disable() {
        RedisMessenger.getLocalInstance().disable();
        redisClient.shutdown();
    }

    //end instance

    public static class DBKey implements Cloneable {

        private static final String SEPARATOR = ":";

        public static String resolve(String... keys) {
            return new DBKey(keys).get();
        }

        public static DBKey get(String... keys) {
            return new DBKey(keys);
        }

        private LinkedList<String> key = new LinkedList<>();

        private DBKey(String... keys) {
            Arrays.stream(keys).forEach(this::add);
        }

        public DBKey add(String... keys) {
            for (String key : keys) {
                this.key.addLast(key);
            }
            return this;
        }

        public DBKey cloneAdd(String... keys) {
            DBKey clone = this.clone();
            clone.add(keys);
            return clone;
        }

        public String res(String... key) {
            String k = get();
            if (k.length() > 0) k += SEPARATOR;
            k += Arrays.stream(key).collect(Collectors.joining(SEPARATOR));
            return k;
        }

        public String get() {
            return key.stream().collect(Collectors.joining(SEPARATOR));
        }

        @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "MethodDoesntCallSuperMethod"})
        @Override
        protected DBKey clone() {
            return new DBKey(key.toArray(new String[key.size()]));
        }

        @Override
        public String toString() {
            return get();
        }
    }

}
