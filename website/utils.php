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

    include_once 'config.php';

    function getRedis() {
        $redis = new Redis();

        $host = constant('RMANAGER_REDIS_HOST');
        $port = constant('RMANAGER_REDIS_PORT');
        $password = constant('RMANAGER_REDIS_PASSWORD');
        $database_id = constant('RMANAGER_REDIS_DATABASE_ID');

        $redis -> pconnect($host, $port, 10);

        //Check if we are using a reused connection, or a new one, so we do not auth or select multiple times.
        try {
            $redis -> ping();

            return $redis;
        }
        catch (RedisException $ignored) {
        }

        $redis -> auth($password);
        $redis -> select($database_id);

        return $redis;
    }

