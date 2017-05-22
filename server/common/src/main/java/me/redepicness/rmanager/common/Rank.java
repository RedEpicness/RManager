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

import java.io.Serializable;

/**
 * @author Red_Epicness
 * @since 5/21/2017 @ 01:10 AM
 */
public enum Rank implements Serializable {

    //Make not enum... (so it's configurable through database)

    /*OWNER, ADMIN, SR_MOD, MOD, BUILDER, FAMOUS, DON_1, DON_2, DON_3;

    public static void initRanks(){
        for(Rank r : values()) r.init();
    }

    private String name;
    private char color;

    private void init(){
        Map<String, String> properties = getRankInfo(toString().toLowerCase());
        name = properties.get("name");
        color = properties.get("color").charAt(0);
    }

    public String getColor() {
        return "\u00A7"+color;
    }

    public int getPower() {
        return this.ordinal();
    }

    public String colored(){
        return colored(true);
    }

    public String colored(boolean reset){
        return getColor()+getName()+(reset?"\u00A7r":"");
    }

    public String formatted(boolean uppercase, boolean bold, boolean color, boolean reset){
        String result = (color?getColor():"")+(bold?"\u00A7l":"")+getName()+(reset?"\u00A7r":"");
        return uppercase?result.toUpperCase():result;
    }

    public String getName() {
        return name;
    }

    public static Optional<Rank> get(String name){
        for(Rank r : values()){
            if(r.getName().toLowerCase().equals(name.toLowerCase())) return Optional.of(r);
        }
        Rank rank;
        try{
            rank =  Rank.valueOf(name.toUpperCase().replace(' ', '_'));
        }
        catch (Exception e){
            return Optional.empty();
        }
        return Optional.of(rank);
    }

    public static Set<Rank> matches(String match){
        HashSet<Rank> options = new HashSet<>();
        for(Rank r : values()){
            if(r.getName().toLowerCase().startsWith(match.toLowerCase())) {
                options.add(r);
                continue;
            }
            if(r.toString().toUpperCase().startsWith(match.toUpperCase())){
                options.add(r);
            }
        }
        return options;
    }

    public static Map<String, String> getRankInfo(String name){
        return Database.getSyncCommands().hgetall(DBKey.resolve("rank", name, "options"));
    }*/

/**
 * @author Red_Epicness
 * @since 7/29/2016 at 3:36 AM @ 1:06 AM
 */
}
