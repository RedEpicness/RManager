<?php
    /**
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
    include_once 'utils.php';

    $redis = getRedis();
    $status = $redis -> get('rmanager:servers:online:test123');
    if ($status) {
        $status = json_decode($status);

        echo 'ONLINE - ' . $status -> player_count . '/' . $status -> max_players;
        echo '<br>';
        echo 'Tps: ' . $status -> tps;
        echo "<br>";
        echo 'Online players: ' . implode(", ", $status -> players);
    } else echo "OFFLINE";