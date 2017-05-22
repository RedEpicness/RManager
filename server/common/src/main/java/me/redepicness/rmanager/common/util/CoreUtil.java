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

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Red_Epicness
 * @since 5/21/2017 @ 01:10 AM
 */
public class CoreUtil {

    // Time

    public static class Time {

        public static final ZoneId ZONE = ZoneId.systemDefault();

        private static final DecimalFormat FORMAT = new DecimalFormat("####.#");

        public static String toMMSS(Duration time) {
            long seconds = time.getSeconds();
            long minutes = time.toMinutes();
            String s = (seconds - (minutes * 60)) + "";
            if (s.length() < 2) s = "0" + s;
            return (minutes < 10 ? "0" + minutes : minutes) + ":" + s; //MM:SS

        }

        public static String toTimer(Duration time) {
            long seconds = time.getSeconds();
            long minutes = time.toMinutes();
            long hours = time.toHours();

            if (minutes == 0) {
                long millis = time.toMillis();
                String ms = (millis - (seconds * 1000)) / 100 + "";
                return time.getSeconds() + "." + ms; //SS.s
            } else if (hours == 0) {
                String s = (seconds - (minutes * 60)) + "";
                if (s.length() < 2) s = "0" + s;
                return minutes + ":" + s; //MM:SS
            } else {
                String s = (seconds - (minutes * 60)) + "";
                if (s.length() < 2) s = "0" + s;
                String m = (minutes - (hours * 60)) + "";
                if (m.length() < 2) m = "0" + m;
                return hours + ":" + m + ":" + s; // HH:MM:SS
            }
        }

        //Timer has options D, H, M, S, s < s can be multiple times
        public static String toCustom(String format, Duration time) {
            long millis = time.toMillis();
            long seconds = time.getSeconds();
            long minutes = time.toMinutes();
            long hours = time.toHours();
            long days = time.toDays();

            String ms = String.valueOf(millis - (seconds * 1000));
            String s = String.valueOf(seconds - (minutes * 60));
            String m = String.valueOf(minutes - (hours * 60));
            String h = String.valueOf(hours - (days * 24));
            String d = String.valueOf(days);

            if (format.contains("s")) {
                int msCount = StringUtils.countMatches(format, 's');
                ms = ms.substring(0, msCount - 1);
            }

            return format.replace("%D", d).replace("%H", h).replace("%M", m).replace("%S", s).replaceAll("%+s*", ms);
        }

        @Deprecated
        public static String toText(Duration time, boolean isShort) {
            return toSingleText(time, isShort);
        }

        public static String toSingleText(Duration time, boolean isShort) {
            if (time.getSeconds() < 60) {
                String amount = FORMAT.format(time.toMillis() / 1000f);
                return amount + (isShort ? "s" : " Seconds");
            } else if (time.toMinutes() < 60) {
                String amount = FORMAT.format(time.getSeconds() / 60f);
                return amount + (isShort ? "m" : " Minutes");
            } else if (time.toHours() < 24) {
                String amount = FORMAT.format(time.toMinutes() / 60f);
                return amount + (isShort ? "h" : " Hours");
            } else {
                String amount = FORMAT.format(time.toHours() / 24f);
                return amount + (isShort ? "d" : " Days");
            }
        }

        public static String toMultiText(Duration time, boolean isShort) {
            long millis = time.toMillis();
            long seconds = time.getSeconds();
            long minutes = time.toMinutes();
            long hours = time.toHours();
            long days = time.toDays();

            millis = millis - (seconds * 1000);
            seconds = seconds - (minutes * 60);
            minutes = minutes - (hours * 60);
            hours = hours - (days * 24);

            StringBuilder builder = new StringBuilder();

            if (days != 0) {
                builder.append(days);
                if (isShort) builder.append("d");
                else builder.append(" Days");
            }
            if (hours != 0) {
                builder.append(" ");
                builder.append(hours);
                if (isShort) builder.append("h");
                else builder.append(" Hours");
            }
            if (minutes != 0) {
                builder.append(" ");
                builder.append(minutes);
                if (isShort) builder.append("m");
                else builder.append(" Minutes");
            }
            if (seconds != 0) {
                builder.append(" ");
                builder.append(seconds);
                if (isShort) builder.append("s");
                else builder.append(" Seconds");
            }
            if (millis != 0) {
                builder.append(" ");
                builder.append(millis);
                if (isShort) builder.append("ms");
                else builder.append(" Milliseconds");
            }

            return builder.toString().trim();
        }

        public static Duration fromString(String s) {
            String time = ("P" + s).toUpperCase();
            if (!time.endsWith("D")) {
                time = time.replace("D", "DT");
            }
            if (!time.contains("D")) {
                time = time.replace("P", "PT");
            }
            return Duration.parse(time);
        }

    }

    // List

    public static class List {

        public static String toString(Collection<?> objects) {
            return toString(", ", objects.stream());
        }

        public static String toString(String comma, Collection<?> objects) {
            return toString(comma, objects.stream());
        }

        public static String toString(Stream<?> objects) {
            return toString(", ", objects);
        }

        public static String toString(String comma, Stream<?> objects) {
            return objects.map(Object::toString).collect(Collectors.joining(comma));
        }

    }

    // Math

    public static class Math {

        public static double trim(double number, long degree) {
            String format = "###.#";

            for (int i = 1; i < degree; i++) {
                format = format + "#";
            }
            DecimalFormat twoDForm = new DecimalFormat(format);
            return Double.valueOf(twoDForm.format(number));
        }

    }

}
