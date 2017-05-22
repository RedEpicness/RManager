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

import me.redepicness.rmanager.common.Database;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Red_Epicness
 * @since 21/05/2017 @ 1:10 AM
 */
public class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        try {
            Path data = getDataFolder().toPath();
            Path resolve = data.resolve("config.yml");
            if (!resolve.toFile().exists()) {
                getLogger().warning("Configuration file does not exist! Recreating!");
                List<String> lines = new ArrayList<>();
                lines.add("# RManager configuration file");
                lines.add("");
                lines.add("# Server ID, used for identification purposes");
                lines.add("server-id: 'foo_bar'");
                lines.add("");
                lines.add("# Database information, points to a valid Redis server");
                lines.add("# Database-id selects the database number provided, if unsure, leave as 0");
                lines.add("hostname: 'localhost'");
                lines.add("password: 'password'");
                lines.add("port: 6379");
                lines.add("database-id: 0");
                if (!data.toFile().exists())
                    Files.createDirectory(data);
                Files.write(resolve, lines, StandardOpenOption.CREATE);
            }
            getLogger().info("Loading configuration file...");
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(resolve.toFile());
            String serverID = configuration.getString("server-id");
            String hostname = configuration.getString("hostname");
            String password = configuration.getString("password");
            int port = configuration.getInt("port");
            int dbID = configuration.getInt("database-id");
            getLogger().info("Success! Initializing database as '" + serverID + "'! (" + hostname + ":" + port + "/" + dbID + ")");
            Database.initDatabase(serverID, hostname, password, port, dbID);
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration: ", e);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling database!");
        Database.getInstance().disable();
    }

    @Override
    public void onEnable() {
        getLogger().info("Started updating status for '" + Database.serverID() + "' to database!");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new StatusUpdater(), 1, 1);
    }
}
