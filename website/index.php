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

?>
<html>
    <head>
        <title>RManager</title>
        <script>

            function refreshStatus() {
                update('refresh', 'Server status: (Refreshing...)');
                updateContent('content', '/status.php');
                var timeout = parseInt(document.getElementById('refreshRate').value);
                Math.min(500, timeout);
                setTimeout(refreshStatus, timeout);
            }

            function updateContent(div, url) {
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function () {
                    if (xmlhttp.readyState === 4) {
                        update('refresh', 'Server status:');
                        update(div, xmlhttp.responseText);
                    }
                };
                xmlhttp.open("GET", url, true);
                xmlhttp.send();
            }

            function update(div, content) {
                document.getElementById(div).innerHTML = content;
            }

        </script>
    </head>
    <body>
        Welcome on the RManager website, not much to see here yet.
        <br>
        <label for='refreshRate'></label>
        <input id='refreshRate' type='number' value='1000'>

        <br>
        <div id='refresh'></div>
        <div id='content'></div>
        <script>
            refreshStatus();
        </script>
    </body>
</html>
